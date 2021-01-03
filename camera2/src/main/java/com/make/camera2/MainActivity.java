package com.make.camera2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private CameraManager mCameraManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<String>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1111);
            }
        }

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
