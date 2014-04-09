package com.BrayterConn.AudioPill;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.parse.Parse;
import com.parse.ParseAnalytics;


import java.util.Locale;

/**
 * Created by Adam on 4/7/14.
 */
public class myActivity extends Activity implements ScanditSDKListener {
    /**
     * Called when the activity is first created.
     */
    private ScanditSDKAutoAdjustingBarcodePicker picker;
    private Resources res = getResources();
    public static TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String appKey = res.getString(R.string.ScaneditAppKey);
        picker = new
        ScanditSDKAutoAdjustingBarcodePicker(this, appKey,1);
        // Specify the object that will receive the callback events
        picker.getOverlayView().addListener(this);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    } else {
                        tts.speak("", TextToSpeech.QUEUE_FLUSH, null);
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
        setContentView(picker);
        Parse.initialize(this, "APP_ID", "CLIENT_ID");
        ParseAnalytics.trackAppOpened(getIntent());
    }

    @Override
    protected void onResume() {
        picker.startScanning();
        super.onResume();
    }

    @Override
    protected void onPause() {
        picker.stopScanning();
        super.onPause();
    }

    @Override
    public void didScanBarcode(String barcode, String symbology) {
        // this callback is only called whenever a barcode is decoded.
        StringBuffer buff = new StringBuffer();
        for (char c : barcode.toCharArray()) {
            if (c>30) {
                buff.append(c);
            }
        }
        Toast.makeText(this,symbology + ":" + buff.toString(),Toast.LENGTH_LONG).show();
        PostBarcodeAsync barsync = new PostBarcodeAsync();
        barsync.doInBackground(buff.toString(), res.getString(R.string.serverIPAddress));


    }
    @Override
    public void didManualSearch(String entry) {
        // this callback is only called when you use the Scandit SDK search bar.
    }
    @Override
    public void didCancel() {
        // this callback is deprecated since Scandit SDK 3.0
        picker.stopScanning();
        finish();
    }
}