package com.example.deviceinfo.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.deviceinfo.callback.PermissionCallback;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    public Context mContext;
    public Activity activity;

    /**
     * 申请权限的回调接口
     */
    private PermissionCallback permissionCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        mContext = this;
        activity = this;
        initView();
    }

    protected abstract int setLayoutId();

    public abstract void initView();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                List<String> deniedPermission = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermission.add(permissions[i]);
                    }
                }
                if (permissionCallback != null) {
                    if (deniedPermission.isEmpty()) {
                        permissionCallback.onGranted();
                    } else {
                        permissionCallback.onDenied(deniedPermission);
                    }
                }
            }
        }
    }

    /**
     * 申请权限
     *
     * @param permissions 所需要的权限
     * @param permissionCallback    回调接口，需要实现申请成功和失败的接口
     */
    public void requestRuntimePermission(ArrayList<String> permissions, int code, PermissionCallback permissionCallback) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<String>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), code);
            } else {
                permissionCallback.onGranted();
            }
        } else {
            permissionCallback.onGranted();
        }
    }

}
