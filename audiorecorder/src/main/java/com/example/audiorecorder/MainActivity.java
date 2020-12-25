package com.example.audiorecorder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.audiorecorder.base.MyApp;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnStart;
    private Button btnEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecordManager.getInstance().init(MyApp.getInstance(), false);
        RecordManager.getInstance().changeFormat(RecordConfig.RecordFormat.MP3);
        String recordDir = getExternalFilesDir("ppp").getAbsolutePath();
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
            }
        });
        btnStart = findViewById(R.id.btn_start);
        btnEnd = findViewById(R.id.btn_end);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setClickable(false);
                btnEnd.setClickable(true);
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setClickable(true);
                btnEnd.setClickable(false);
            }
        });
    }
}
