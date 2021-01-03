package com.example.camera.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CameraBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "CameraBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "onReceive: action->" + action);
        if ("".equalsIgnoreCase(action)) {

        }
    }
}
