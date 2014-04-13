package com.BrayterConn.AudioPill;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.parse.Parse;
import com.parse.ParseAnalytics;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        initializeParse();
        initializeBarcodeScanner();
        initializeTextToSpeech();
    }

    private void initializeParse() {
        Parse.initialize(this, res.getString(R.string.Parse_APP_ID), res.getString(R.string.Parse_CLIENT_ID));
        ParseAnalytics.trackAppOpened(getIntent());
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
    }

    private void initializeBarcodeScanner() {
        // Switch to full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ScanditSDKAutoAdjustingBarcodePicker  picker = new
                ScanditSDKAutoAdjustingBarcodePicker(this, res.getString(R.string.ScaneditAppKey), ScanditSDK.CAMERA_FACING_FRONT);
        // Specify the object that will receive the callback events
        setContentView(picker);
        mBarcodePicker = picker;
        mBarcodePicker.getOverlayView().addListener(this);
    }

    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //do nothing
            }
        });
    }

    @Override
    protected void onResume() {
        mBarcodePicker.startScanning();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mBarcodePicker.stopScanning();
        super.onPause();
    }

    @Override
    public void didScanBarcode(String barcode, String symbology) {
        // this callback is only called whenever a barcode is decoded.
        Log.d("debug","scanded barcode");
        StringBuffer buff = new StringBuffer();
        for (char c : barcode.toCharArray()) {
            if (c>30) {
                buff.append(c);
            }
        }
        Toast.makeText(this,symbology + ":" + buff.toString(),Toast.LENGTH_LONG).show();


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