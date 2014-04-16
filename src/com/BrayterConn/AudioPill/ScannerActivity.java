package com.BrayterConn.AudioPill;


import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup;
import java.io.IOException;

/**
 * Created by Adam on 4/15/2014.
 */
public class ScannerActivity extends Activity {
    private Camera camera;
    private SurfaceView surface;
    private SurfaceHolder surfaceHolder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView();

    }

    private void initializeView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        surface = new SurfaceView(getApplicationContext());
        addContentView(surface, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        if (surfaceHolder == null) {
            surfaceHolder = surface.getHolder();
        }
        surfaceHolder.addCallback(myCallback());
    }

    SurfaceHolder.Callback myCallback() {
        SurfaceHolder.Callback cb1 = new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.stopPreview();
                camera.release();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                camera = Camera.open();

                try {
                    camera.setPreviewDisplay(holder);
                } catch (IOException exception) {
                    camera.release();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                camera.startPreview();
            }
        };
        return cb1;
    }

    @Override
    public void onResume () {
        super.onResume();
        camera.open();
    }

    @Override
    public void onPause () {
        super.onPause();
        camera.release();
    }
}