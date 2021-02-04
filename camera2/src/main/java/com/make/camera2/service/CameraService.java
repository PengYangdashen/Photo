package com.make.camera2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import androidx.annotation.Nullable;

import java.io.IOException;

public class CameraService extends Service implements Camera.PictureCallback{

    private static final String TAG = "CameraService";
    private Camera camera;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void onTakePhotoClicked() {
        final SurfaceView preview = new SurfaceView(this);
        SurfaceHolder holder = preview.getHolder();
        // deprecated setting, but required on Android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            //The preview must happen at or after this point or takePicture fails
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "Surface created");
                camera = null;
                try {
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    Log.d(TAG, "Opened camera");
                    try {
                        camera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    camera.startPreview();
                    Log.d(TAG, "Started preview");
                    //延时拍照
//                    ThreadUtils.postOnUiThreadDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e(TAG,"开始拍照");
                            camera.takePicture(null, null, CameraService.this);
//                        }
//                    },5000);
                } catch (Exception e) {
                    if (camera != null)
                        camera.release();
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {}
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
        });
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                1, 1, //Must be at least 1x1
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                0,
                //Don't know if this is a safe default
                PixelFormat.UNKNOWN);
        //Don't set the preview visibility to GONE or INVISIBLE
        wm.addView(preview, params);
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        Log.e(TAG, "拍照结束");
    }
}
