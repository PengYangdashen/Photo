package com.make.cameracopy;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class g {
    public static g f;

    public static ExecutorService g = Executors.newSingleThreadExecutor();

    public WindowManager a;

    public Context b;

    public CameraSurfaceView c;

    public View d;

    public volatile int e;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public g(Context paramContext) {
        char c;
        this.e = 0;
        this.b = paramContext;
        this.a = (WindowManager)paramContext.getSystemService(Context.WINDOW_SERVICE);
        this.c = new CameraSurfaceView(this.b);
        this.d = this.c;
        if (Build.VERSION.SDK_INT > 24) {
            c = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            c = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(1, 1, c, 0, -3);
        layoutParams.format = -3;
        layoutParams.flags = 56;
        layoutParams.gravity = 80;
        this.a.addView(this.c, layoutParams);
    }
}
