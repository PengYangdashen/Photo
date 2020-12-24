package com.make.message;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.make.message.observer.SMSObserver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        activity = this;
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.READ_CONTACTS);
        permissions.add(android.Manifest.permission.RECEIVE_SMS);
        permissions.add(android.Manifest.permission.READ_SMS);
        permissions.add(android.Manifest.permission.SEND_SMS);
        requestRuntimePermission(permissions, 1);


    }

    /**
     * 申请权限
     *
     * @param permissions 所需要的权限
     */
    public void requestRuntimePermission(ArrayList<String> permissions, int code) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<String>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), code);
            } else {
                goon();
            }
        } else {
            goon();
        }
    }
    private void goon(){
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true,
                new SMSObserver(new Handler(), this));
    }
}
