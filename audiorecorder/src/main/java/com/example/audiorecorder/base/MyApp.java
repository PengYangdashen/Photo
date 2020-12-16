package com.example.audiorecorder.base;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getInstance() {
        return application;
    }
}
