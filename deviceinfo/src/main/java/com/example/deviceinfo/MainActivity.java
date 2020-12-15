package com.example.deviceinfo;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.deviceinfo.base.BaseActivity;
import com.example.deviceinfo.bean.DeviceInfo;
import com.example.deviceinfo.callback.PermissionCallback;
import com.example.deviceinfo.util.DeviceInfoUtils;
import com.example.deviceinfo.util.SDCardUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private TextView tv;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        tv = findViewById(R.id.tv);

        ArrayList<String> permissons = new ArrayList<>();
        permissons.add(android.Manifest.permission.READ_PHONE_STATE);
        requestRuntimePermission(permissons, 1, new PermissionCallback() {
            @Override
            public void onGranted() {
//                DeviceInfo deviceInfo = new DeviceInfo();
//                deviceInfo.setDeviceAndroidVersion(DeviceInfoUtils.getDeviceAndroidVersion());
//                deviceInfo.setDeviceBoard(DeviceInfoUtils.getDeviceBoard());
//                deviceInfo.setDeviceBrand(DeviceInfoUtils.getDeviceBrand());
//                deviceInfo.setDeviceDefaultLanguage(DeviceInfoUtils.getDeviceDefaultLanguage());
//                deviceInfo.setDeviceDisplay(DeviceInfoUtils.getDeviceDisplay());
//                deviceInfo.setDeviceFubgerprint(DeviceInfoUtils.getDeviceFubgerprint());
//                deviceInfo.setDeviceHardware(DeviceInfoUtils.getDeviceHardware());
//                deviceInfo.setDeviceHeight(DeviceInfoUtils.getDeviceHeight(mContext));
//                deviceInfo.setDeviceHost(DeviceInfoUtils.getDeviceHost());
//                deviceInfo.setDeviceId(DeviceInfoUtils.getDeviceId());
//                deviceInfo.setDeviceManufacturer(DeviceInfoUtils.getDeviceManufacturer());
//                deviceInfo.setDeviceModel(DeviceInfoUtils.getDeviceModel());
//                deviceInfo.setDeviceName(DeviceInfoUtils.getDeviceName());
//                deviceInfo.setDeviceProduct(DeviceInfoUtils.getDeviceProduct());
//                deviceInfo.setDeviceRAMInfo(SDCardUtils.getRAMInfo(mContext));
//                deviceInfo.setDeviceSDK(DeviceInfoUtils.getDeviceSDK());
//                deviceInfo.setDeviceSerial(DeviceInfoUtils.getDeviceSerial());
//                deviceInfo.setDeviceSupportLanguage(DeviceInfoUtils.getDeviceSupportLanguage());
//                deviceInfo.setDeviceUser(DeviceInfoUtils.getDeviceUser());
//                deviceInfo.setDeviceWidth(DeviceInfoUtils.getDeviceWidth(mContext));
//                deviceInfo.setIMEI(DeviceInfoUtils.getIMEI(mContext));
//                deviceInfo.setIsSDCardMount(SDCardUtils.isSDCardMount()?"Y":"N");
//                tv.setText(deviceInfo.toString());
                Log.i(TAG, "onGranted: ");
                tv.setText(DeviceInfoUtils.getDeviceAllInfo(mContext));
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                Log.i(TAG, "onDenied: ");
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
