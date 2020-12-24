package com.example.camera.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.view.SurfaceView;
import androidx.annotation.Nullable;
import com.example.camera.PhotoHandler;

import java.io.IOException;

public class CameraService extends Service {

    private AlarmManager am = null;
    private Camera camera;

    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        camera = openFacingBackCamera();

        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vegetables_source.alarm");
        registerReceiver(alarmReceiver, filter);

        Intent intent = new Intent();
        intent.setAction("com.vegetables_source.alarm");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 10, pi);// 马上开始，每1分钟触发一次

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        public CameraService getService() {
            return CameraService.this;
        }

    }

    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.vegetables_source.alarm".equals(intent.getAction())) {

                if (camera != null) {
                    SurfaceView dummy = new SurfaceView(getBaseContext());
                    try {
                        camera.setPreviewDisplay(dummy.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();

                    camera.takePicture(null, null, new PhotoHandler(
                            getApplicationContext()));
                }

            }

        }
    };

    private Camera openFacingBackCamera() {
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        ;
        for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

        return cam;
    }
}
