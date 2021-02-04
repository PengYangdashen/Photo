package com.make.camera3.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.make.camera3.ui.MyWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    /**
     * 自定义窗口
     */
    private MyWindow myWindow;
    /**
     * 窗口管理者
     */
    private WindowManager mWindowManager;
    /**
     * 窗口布局参数
     */
    private LayoutParams Params;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate");
        // 定时器类
        Timer timer = new Timer();
        timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行

        //对于6.0以上的设备
        if (Build.VERSION.SDK_INT >= 23) {
            //如果支持悬浮窗功能
            if (Settings.canDrawOverlays(getApplicationContext())) {
                showWindow();
            } else {
                //手动去开启悬浮窗
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        } else {
            //6.0以下的设备直接开启
            showWindow();
        }

    }

    private void showWindow() {
        //创建MyWindow的实例
        myWindow = new MyWindow(getApplicationContext());
        //窗口管理者
        mWindowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        //窗口布局参数
        Params = new WindowManager.LayoutParams();
        //布局坐标,以屏幕左上角为(0,0)
        Params.x = 0;
        Params.y = 0;

        //布局类型
        Params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY; // 系统提示类型,重要

        //布局flags
        Params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
        Params.flags = Params.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        Params.flags = Params.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
        Params.flags |= WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;

        //布局的gravity
        Params.gravity = Gravity.LEFT | Gravity.TOP;

        //布局的宽和高
        Params.width =  1;
        Params.height = 1;

        myWindow.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_MOVE:
                        Params.x = (int) event.getRawX() - myWindow.getWidth() / 2;
                        Params.y = (int) event.getRawY() - myWindow.getHeight() / 2;
                        //更新布局位置
                        mWindowManager.updateViewLayout(myWindow, Params);

                        break;
                }
                return false;
            }
        });

    }


    /**
     * @return 获取桌面(Launcher)的包名
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resolveInfo) {
            names.add(info.activityInfo.packageName);
        }
        return names;
    }

    /**
     * @return 判断当前是否是桌面
     */
    public boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        List<String> strs = getHomes();
        if (strs != null && strs.size() > 0) {
            return strs.contains(rti.get(0).topActivity.getPackageName());
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myWindow = null;
    }

}
