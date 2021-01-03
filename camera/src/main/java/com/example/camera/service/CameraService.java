package com.example.camera.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.camera.callback.PhotoHandler;

import java.util.List;

public class CameraService extends Service {

    private static final String TAG = "CameraService";
    private AlarmManager am = null;
    private Camera camera;
    private int currentCameraIndex;
    private int currentCameraType;
    private static final int FRONT = 1;
    private static final int BACK = 2;

    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        Log.i(TAG, "init: ");
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vegetables_source.alarm");
        registerReceiver(alarmReceiver, filter);

        Intent intent = new Intent();
        intent.setAction("com.vegetables_source.alarm");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 10, pi);// 马上开始，每1分钟触发一次

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        public CameraService getService() {
            return CameraService.this;
        }

    }

    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + intent.getAction());
            if ("com.vegetables_source.alarm".equals(intent.getAction())) {
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

                    //使用takePicture()方法完成拍照
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        //自动聚焦完成后拍照
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (camera != null){
                                camera.takePicture(null, null, new PhotoHandler(context));
                                camera.release();
                            }
                        }
                    });

                }

            }

        }
    };

    /**
     * 按照type的类型打开相应的摄像头
     *
     * @param type 标志当前打开前还是后的摄像头
     * @return 返回当前打开摄像机的对象
     */
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
