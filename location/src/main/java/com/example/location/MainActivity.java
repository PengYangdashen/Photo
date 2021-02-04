package com.example.location;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.amap.api.location.AMapLocation;
import com.example.location.service.LocationService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "MainActivity";
    private TextView tv;
    private LocationService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<String>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1111);
            } else {
            }
        } else {
        }

        tv = findViewById(R.id.tv);
        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = service.getCurrentLocation().getAddress();
                Log.i(TAG, "onClick: address" + address);
                tv.append("\n");
                tv.append(address);
            }
        });
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        LocationService.Binder binder = (LocationService.Binder) iBinder;
        service = binder.getService();
        service.setCallback(new LocationService.Callback() {
            @Override
            public void onDataChange(AMapLocation aMapLocation) {
                Message message = Message.obtain();
                message.obj = aMapLocation;
                myHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            AMapLocation aMapLocation = (AMapLocation) msg.obj;
            tv.append("\n");
            tv.append(aMapLocation.getAddress());
        }
    };
}
