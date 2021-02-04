package com.example.camera.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.example.camera.ui.Window;

public class Camera4Service extends Service {

    private static final String TAG = "Camera4Service";

    private Context context;
    public Camera4Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        context = this;
        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vegetables_source.alarm");
        registerReceiver(alarmReceiver, filter);
    }

    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + intent.getAction());
            if ("com.vegetables_source.alarm".equals(intent.getAction())) {
                new Window(context);
            }
        }

    };
}
