package com.example.deviceinfo.callback;

import java.util.List;

public interface PermissionCallback {

    void onGranted();

    void onDenied(List<String> deniedPermission);

}
