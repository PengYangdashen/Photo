package com.example.camera;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.camera.callback.CameraPreview;
import com.example.camera.callback.PhotoHandler;
import com.example.camera.service.CameraService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private int currentCameraIndex;
    private int currentCameraType;
    private static final int FRONT = 1;
    private static final int BACK = 2;

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
            } else {
            }
        } else {
        }
//        Intent intent = new Intent(this, CameraService.class);
//        bindService(intent, new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//
//            }
//        }, BIND_AUTO_CREATE);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = openCamera(1);
                if (camera != null) {
                    Camera.Parameters parameters;
                    try{
                        parameters = camera.getParameters();
                    }catch(Exception e){
                        e.printStackTrace();
                        return;
                    }
                    //获取摄像头支持的各种分辨率,因为摄像头数组不确定是按降序还是升序，这里的逻辑有时不是很好找得到相应的尺寸
                    //可先确定是按升还是降序排列，再进对对比吧，我这里拢统地找了个，是个不精确的...
                    List<Camera.Size> list = parameters.getSupportedPictureSizes();
                    int size = 0;
                    for (int i =0 ;i < list.size() - 1;i++){
                        if (list.get(i).width >= 480){
                            //完美匹配
                            size = i;
                            break;
                        }
                        else{
                            //找不到就找个最接近的吧
                            size = i;
                        }
                    }
                    //设置照片分辨率，注意要在摄像头支持的范围内选择
                    parameters.setPictureSize(list.get(size).width,list.get(size).height);
                    //设置照相机参数
                    camera.setParameters(parameters);
                    camera.startPreview();
                    //使用takePicture()方法完成拍照
//                    camera.autoFocus(new Camera.AutoFocusCallback() {
//                        //自动聚焦完成后拍照
//                        @Override
//                        public void onAutoFocus(boolean success, Camera camera) {
//                            if (camera != null){
//                                camera.takePicture(null, null, new PhotoHandler(MainActivity.this));
//                                Toast.makeText(MainActivity.this, "拍照成功！", Toast.LENGTH_LONG).show();
//                                camera.release();
//                            }
//                        }
//                    });
                    if (camera != null){
                                camera.takePicture(null, null, new PhotoHandler(MainActivity.this));
                                Toast.makeText(MainActivity.this, "拍照成功！", Toast.LENGTH_LONG).show();
                                camera.release();
                            }
                }

            }
        });
    }

    private Camera openCamera(int type) {
        int frontIndex = -1;
        int backIndex = -1;
        int cameraCount = Camera.getNumberOfCameras();

        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < cameraCount; cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontIndex = cameraIndex;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backIndex = cameraIndex;
            }
        }

        currentCameraType = type;
        if (type == FRONT && frontIndex != -1) {
            currentCameraIndex = frontIndex;
            return Camera.open(frontIndex);
        } else if (type == BACK && backIndex != -1) {
            currentCameraIndex = backIndex;
            return Camera.open(backIndex);
        }
        return null;
    }
}
