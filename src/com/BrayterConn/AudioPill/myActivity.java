package com.BrayterConn.AudioPill;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.glass.app.Card;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;



/**
 * Created by Adam on 4/7/14.
 */
public class myActivity extends Activity implements ScanditSDKListener {
    /**
     * Called when the activity is first created.
     */
    private ScanditSDK mBarcodePicker;
    public static TextToSpeech tts;
    private  Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        initializeParse();
        //initializeTextToSpeech();
        initializeBarcodeScanner();
    }



    private void initializeParse() {

        InitializeParse parse = new InitializeParse(this);
        parse.setListener(new CallbackListener() {
            @Override
            public void callback() {
                ParseAnalytics.trackAppOpened(getIntent());
//                ParseObject testObject = new ParseObject("TestObject");
//                testObject.put("foo", "bar");
//                testObject.saveInBackground();
            }
        });
        parse.doInBackground(res.getString(R.string.Parse_CLIENT_ID),res.getString(R.string.Parse_APP_ID));

    }

    private void initializeBarcodeScanner() {
        // Switch to full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ScanditSDKAutoAdjustingBarcodePicker  picker = new
                ScanditSDKAutoAdjustingBarcodePicker(this, res.getString(R.string.ScaneditAppKey), ScanditSDK.CAMERA_FACING_FRONT);
        // Specify the object that will receive the callback events

//        picker.set2DScanningEnabled(false);
//        picker.set1DScanningEnabled(true);
        setContentView(picker);
        mBarcodePicker = picker;
        mBarcodePicker.getOverlayView().addListener(this);
//        mBarcodePicker.startScanning();
    }


    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //do nothing
            }
        });
    }

    @Override public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
//            // user tapped touchpad, do something
            mBarcodePicker.startScanning();
            return true;
        }
        else {
            return false;
        }
    }

//    @Override
//    public boolean onKeyDown(int keycode, KeyEvent event) {
//        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
//            // user tapped touchpad, do something
//            byte[] picture = mBarcodePicker.getMostRecentCameraPreviewImage();
//            final ParseFile file = new ParseFile("barcode.png",picture);
//            Toast.makeText(this,"Sending Picture for analysis",Toast.LENGTH_LONG);
//            file.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(com.parse.ParseException e) {
//                    if (e == null) {
//                        pictureSavedSuccessfully();
//                        Map<String, ParseFile> map = new HashMap<String, ParseFile>();
//                        map.put("picture",file);
//                        ParseCloud.callFunctionInBackground("decodePicture", map,  new FunctionCallback() {
//                            @Override
//                            public void done(Object o, com.parse.ParseException e) {
//                                if (e == null) {
//                                    //processResponse(object);
//                                } else {
//                                    // handleError();
//                                }
//                            }
//                        });
//                    }
//                    else {
//                        pictureSaveDidNotSucceed();
//                    }
//                }
//            });
//            return true;
//        }
//        else {
//            return false;
//        }
//    }


    private void pictureSavedSuccessfully(){
        Toast.makeText(this, "Picture sent to Server. Waiting for barcode to be analyzed", Toast.LENGTH_LONG);
    }

    private void pictureSaveDidNotSucceed() {
        Toast.makeText(this, "Error: Picture failed to be sent to Server. Retry Sending Picture", Toast.LENGTH_LONG);
    }

    @Override
    protected void onResume() {
        mBarcodePicker.startScanning();
        super.onResume();
    }

    @Override
    protected void onPause() {
        String text = "";
        mBarcodePicker.stopScanning();
        super.onPause();
    }

    @Override
    public void didScanBarcode(String barcode, String Sybolic) {
        // this callback is only called whenever a barcode is decoded.
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Package");
//        query.whereEqualTo("NDCPACKAGECODE",barcode);
//        query.setLimit(1);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
//                if (e == null) {
//                    String id = parseObjects.get(0).get("PRODUCTID").toString();
//                }
//            }
//        });
        Card card = new Card(this);
        card.setText(barcode.toString());
        setContentView(card.getView());
    }
    @Override
    public void didManualSearch(String entry) {
        // this callback is only called when you use the Scandit SDK search bar.
    }
    @Override
    public void didCancel() {
        // this callback is deprecated since Scandit SDK 3.0
        mBarcodePicker.stopScanning();
        finish();
    }
}
