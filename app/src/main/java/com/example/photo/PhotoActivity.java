package com.example.photo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.photo.util.PhotoUtil;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class PhotoActivity extends AppCompatActivity {
    private static final String TAG = "PhotoActivity";
    private TextView tv;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        context = this;
        tv = findViewById(R.id.tv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                this.requestPermissions(
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);
            } else {
                showText();
            }
        } else {
            showText();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            showText();
        }
    }

    private void showText() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> photos = PhotoUtil.getSystemPhotoList(context);
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "Camera";
                Log.i(TAG, "run: path->" + path);
                if (photos != null && photos.size() > 0) {
                    for (String photo : photos) {
                        Log.i(TAG, "run: photo->" + photo);
                        if (TextUtils.isEmpty(photo) || !photo.contains(path)) {
                            continue;
                        }
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                            Log.i(TAG, "有权限");
                        } else {
                            Log.i(TAG, "没权限");
                        }
                        String base = PhotoUtil.bitmapToBase64(PhotoUtil.compressImage(Objects.requireNonNull(PhotoUtil.getBitmap(photo))));
                        Log.i(TAG, "run: base:" + base);
                    }
                } else {
                    Log.i(TAG, "onCreate: 没有获取到相册！");
                }
            }
        }).start();
    }
}
