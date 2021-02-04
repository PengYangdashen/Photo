package com.example.location.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;

public class LocationService extends Service {

    private final String TAG = getClass().getSimpleName();

    private boolean connecting = false;
    private Callback callback;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //声明AMapLocationClient类对象
    public AMapLocationClient locationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.i(TAG, "onLocationChanged: ");
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                    Log.i(TAG, "onLocationChanged: =====" + sdf.format(aMapLocation.getTime()));
                    Log.i(TAG, "定位结果来源: " + aMapLocation.getLocationType());
                    Log.i(TAG, "纬度: " + aMapLocation.getLatitude());
                    Log.i(TAG, "经度: " + aMapLocation.getLongitude());
                    Log.i(TAG, "精度: " + aMapLocation.getAccuracy());
                    Log.i(TAG, "速度: " + aMapLocation.getSpeed());
                    Log.i(TAG, "地址: " + aMapLocation.getAddress());
                    callback.onDataChange(aMapLocation);
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e(TAG,"location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public AMapLocationClientOption option = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public class Binder extends android.os.Binder {
        public LocationService getService() {
            return  LocationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        connecting = true;
        Log.i(TAG, "onStartCommand: ");
        //初始化定位
        locationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        locationClient.setLocationListener(mLocationListener);
        option = new AMapLocationClientOption();
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        option.setInterval(5000);
        option.setNeedAddress(true);
        if(null != locationClient){
            locationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            locationClient.stopLocation();
            locationClient.startLocation();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public AMapLocation getCurrentLocation() {
        Log.i(TAG, "getCurrentLocation: ");
        return locationClient.getLastKnownLocation();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onDataChange(AMapLocation aMapLocation);
    }
}
