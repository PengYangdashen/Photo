package com.example.audiorecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.audiorecorder.base.MyApp;
import com.example.audiorecorder.utils.Base64Util;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordDataListener;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnStart;
    private Button btnEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.RECORD_AUDIO);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
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

        RecordManager.getInstance().init(MyApp.getInstance(), false);
        RecordManager.getInstance().changeFormat(RecordConfig.RecordFormat.MP3);
        String recordDir = getCacheDir().getAbsolutePath();
        Log.i(TAG, "onCreate: recordDir->" + recordDir);
        RecordManager.getInstance().changeRecordDir(recordDir);
        RecordManager.getInstance().setRecordStateListener(new RecordStateListener() {
            @Override
            public void onStateChange(RecordHelper.RecordState state) {
                Log.i(TAG, "onStateChange: " + state.name());
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "onError: " + error);
            }
        });
        RecordManager.getInstance().setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                String base = Base64Util.fileToBase64(result);
                Log.i(TAG, "onResult: base->" + base);
            }
        });
        RecordManager.getInstance().setRecordDataListener(new RecordDataListener() {
            @Override
            public void onData(byte[] data) {
                Log.i(TAG, "onData: ");
            }
        });
        btnStart = findViewById(R.id.btn_start);
        btnEnd = findViewById(R.id.btn_end);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setClickable(false);
                btnEnd.setClickable(true);
                RecordManager.getInstance().start();
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setClickable(true);
                btnEnd.setClickable(false);
                RecordManager.getInstance().stop();
            }
        });
    }
}
