package com.example.deviceinfo;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.deviceinfo.bean.DeviceInfo;
import com.example.deviceinfo.util.DeviceInfoUtils;
import com.example.deviceinfo.util.SDCardUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceAndroidVersion(DeviceInfoUtils.getDeviceAndroidVersion());
        deviceInfo.setDeviceBoard(DeviceInfoUtils.getDeviceBoard());
        deviceInfo.setDeviceBrand(DeviceInfoUtils.getDeviceBrand());
        deviceInfo.setDeviceDefaultLanguage(DeviceInfoUtils.getDeviceDefaultLanguage());
        deviceInfo.setDeviceDisplay(DeviceInfoUtils.getDeviceDisplay());
        deviceInfo.setDeviceFubgerprint(DeviceInfoUtils.getDeviceFubgerprint());
        deviceInfo.setDeviceHardware(DeviceInfoUtils.getDeviceHardware());
        deviceInfo.setDeviceHeight(DeviceInfoUtils.getDeviceHeight(this));
        deviceInfo.setDeviceHost(DeviceInfoUtils.getDeviceHost());
        deviceInfo.setDeviceId(DeviceInfoUtils.getDeviceId());
        deviceInfo.setDeviceManufacturer(DeviceInfoUtils.getDeviceManufacturer());
        deviceInfo.setDeviceModel(DeviceInfoUtils.getDeviceModel());
        deviceInfo.setDeviceName(DeviceInfoUtils.getDeviceName());
        deviceInfo.setDeviceProduct(DeviceInfoUtils.getDeviceProduct());
        deviceInfo.setDeviceRAMInfo(SDCardUtils.getRAMInfo(this));
        deviceInfo.setDeviceSDK(DeviceInfoUtils.getDeviceSDK());
        deviceInfo.setDeviceSerial(DeviceInfoUtils.getDeviceSerial());
        deviceInfo.setDeviceSupportLanguage(DeviceInfoUtils.getDeviceSupportLanguage());
        deviceInfo.setDeviceUser(DeviceInfoUtils.getDeviceUser());
        deviceInfo.setDeviceWidth(DeviceInfoUtils.getDeviceWidth(this));
        deviceInfo.setIMEI(DeviceInfoUtils.getIMEI(this));
        deviceInfo.setIsSDCardMount(SDCardUtils.isSDCardMount()?"Y":"N");
        ((TextView)findViewById(R.id.tv)).setText(deviceInfo.toString());
    }
}
