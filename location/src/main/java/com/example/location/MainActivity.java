package com.example.location;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.amap.api.location.AMapLocation;
import com.example.location.service.LocationService;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        LocationService.Binder binder = (LocationService.Binder) iBinder;
        LocationService service = binder.getService();
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
            tv.setText(aMapLocation.getAddress());
        }
    };
}
