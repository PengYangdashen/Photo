package com.example.camera.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import com.example.camera.util.PhotoUtil;

public class PhotoHandler implements Camera.PictureCallback {
    private static final String TAG = "PhotoHandler";
    private final Context context;

    public PhotoHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        Log.i(TAG, "onPictureTaken: ");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, 0);
        Log.i(TAG, "onPictureTaken: " + bytes.toString());
        String base64 = PhotoUtil.bitmapToBase64(bitmap);
        Log.i(TAG, "onPictureTaken: " + base64);
        camera.startPreview();
    }
}
