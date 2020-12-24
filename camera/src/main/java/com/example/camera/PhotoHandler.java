package com.example.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;

public class PhotoHandler implements Camera.PictureCallback {
    private static final String TAG = "PhotoHandler";
    private final Context context;

    public PhotoHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, 0);
        Log.i(TAG, "onPictureTaken: " + bytes.toString());
    }
}
