package com.example.camera.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback, View.OnClickListener {

    private static final String TAG = "CameraSurfaceView";

    public static final int[] t = new int[] { 1280, 720 };

    public static final int[] u = new int[] { 1920, 1080 };

    public int a = 1;

    public boolean b = true;

    public SurfaceHolder surfaceHolder;

    public SurfaceTexture surfaceTexture;

    public boolean s = false;

//    public boolean e = false;

    public boolean f = false;

    public Camera camera;

    public Camera.Parameters parameters;

    public byte[] i;

    public int j;

    public int k = 17;

    public CameraSurfaceView l;

    public String m;

    public String n;

    public CameraState cameraState;

    public CameraStateCallback cameraStateCallback;

    public MediaRecorder mediaRecorder;

    public CameraSurfaceView(Context context) {
        super(context);
        CameraSurfaceView(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CameraSurfaceView(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CameraSurfaceView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        try {
            this.camera.enableShutterSound(false);
            camera.takePicture(null, null, new MyPictureCallback());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
        stopPreview();
        startPreview();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        stopPreview();
//        if (e) {
//            startPreview();
//        }
    }

    @Override
    public void onClick(View v) {

    }

    public final void CameraSurfaceView(Context paramContext) {
        this.l = this;
        cameraState = CameraState.START;
        if (cameraStateCallback != null) {
            cameraStateCallback.onCameraStateChanged(cameraState);
        }
        openCamera();
        if ((paramContext.getResources().getConfiguration()).orientation == 2) {
            this.a = 2;
        }
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceTexture = new SurfaceTexture(10);
        setOnClickListener(this);
//        post(new a(this));
    }

    private int getFacingFrontCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    return i;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public final void openCamera() {
        Log.i(TAG, "openCamera ... ");
        try {
            this.camera = Camera.open(getFacingFrontCamera());
        } catch (Exception exception) {
            exception.printStackTrace();
            this.camera = null;
            this.cameraState = CameraState.ERROR;
            if (cameraStateCallback != null)
                cameraStateCallback.onCameraStateChanged(cameraState);
        }
        if (this.camera == null) {
            Exception exception = new Exception("Open camera fail.");
            exception.printStackTrace();
        }
    }

    public void setOnCameraStateListener(CameraStateCallback cameraStateCallback) {
        this.cameraStateCallback = cameraStateCallback;
    }

//    public void setRunBack(boolean paramBoolean) {
//        if (this.camera == null)
//            return;
//        if (paramBoolean == this.e)
//            return;
//        if (!paramBoolean && !this.f) {
//            Log.i(TAG, "Vew not attach to Window");
//            return;
//        }
//        this.e = paramBoolean;
//        if (paramBoolean) {
//            setVisibility(GONE);
//        } else {
//            setVisibility(VISIBLE);
//        }
//    }


    public void closeCamera1() {
        Log.i(TAG, "closeCamera ... ");
        closeCamera();
        stopPreview();
        releaseCamera();
    }

    public final void stopPreview() {
        Log.i(TAG, "stopPreview ... ");
        if (camera == null)
            return;
        try {
//            if (this.e) {
                camera.setPreviewCallbackWithBuffer(null);
                this.camera.stopPreview();
//            } else {
                camera.setPreviewCallback(null);
//                this.camera.stopPreview();
//            }
            if (cameraState != CameraState.STOP) {
                cameraState = CameraState.STOP;
                if (cameraStateCallback != null)
                    cameraStateCallback.onCameraStateChanged(cameraState);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public final boolean startPreview() {
        Log.i(TAG, "startPreview ... ");
        Camera camera = this.camera;
        if (camera == null)
            return false;
        try {
            Camera.Parameters parameters = camera.getParameters();
            this.parameters = parameters;
            parameters.setPreviewFormat(this.k);
            this.parameters.setRotation(0);
            List<Camera.Size> list1 = parameters.getSupportedPictureSizes();
            int size = 0;
            for (int i =0 ;i < list1.size() - 1;i++){
                if (list1.get(i).width >= 480){
                    //完美匹配
                    size = i;
                    break;
                }
                else{
                    //找不到就找个最接近的吧
                    size = i;
                }
            }
            parameters.setPictureSize(list1.get(size).width,list1.get(size).height);
            List<Integer> list = this.parameters.getSupportedPictureFormats();
            int i = 0;
            while (true) {
                if (i < list.size()) {
                    if (256 == ((Integer)list.get(i)).intValue()) {
                        i = 1;
                        break;
                    }
                    i++;
                    continue;
                }
                i = 0;
                break;
            }
            if (i != 0) {
                this.parameters.setPictureFormat(256);
                this.parameters.setJpegQuality(100);
            }
            List<String> list22 = this.parameters.getSupportedFocusModes();
            i = 0;
            while (true) {
                int j = list22.size();
                if (i < j) {
                    if ("auto".equals(list22.get(i))) {
                        i = 1;
                        break;
                    }
                    i++;
                    continue;
                }
                i = 0;
                break;
            }
            if (i != 0)
                this.parameters.setFocusMode("auto");
            i = this.a;
            if (i != 2) {
                this.parameters.set("orientation", "portrait");
                this.camera.setDisplayOrientation(90);
            } else {
                this.parameters.set("orientation", "landscape");
                this.camera.setDisplayOrientation(0);
            }
//            if (this.e) {
                this.camera.setPreviewTexture(surfaceTexture);
                this.camera.addCallbackBuffer(this.i);
//            } else {
                this.camera.setPreviewDisplay(surfaceHolder);
//            }
            this.camera.setParameters(this.parameters);
            this.camera.startPreview();
            if (cameraState != CameraState.STOP) {
                cameraState = CameraState.STOP;
                if (cameraStateCallback != null)
                    cameraStateCallback.onCameraStateChanged(cameraState);
            }
            try {
                String str = this.camera.getParameters().getFocusMode();
                Log.i(TAG, "startPreview: camera: FocusMode: " + str);
//                if ("auto".equals(str) || "macro".equals(str)) {
                    Log.i(TAG, "startPreview: " + str);
                    this.camera.autoFocus(this);
//                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            releaseCamera();
            return false;
        }
    }

    public void closeCamera() {
        Log.i(TAG, "closeCamera ... ");
        if (!this.s)
            return;
        try {
            mediaRecorder.setPreviewDisplay(null);
            mediaRecorder.stop();
            this.s = false;
            stopPreview();
            releaseCamera();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("video path:");
            stringBuilder.append(this.n);
        } catch (IllegalStateException illegalStateException) {
            this.s = false;
        }
        this.mediaRecorder = null;
    }

    public final void releaseCamera() {
        try {
            Log.i(TAG, "releaseCamera ... ");
            Camera camera = this.camera;
            if (camera != null) {
                camera.setPreviewCallback(null);
                this.camera.setPreviewCallbackWithBuffer(null);
                this.camera.stopPreview();
                this.camera.release();
                this.camera = null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onAttachedToWindow() {
        Log.i(TAG, "onAttachedToWindow: ");
        super.onAttachedToWindow();
        this.f = true;
    }

    public enum CameraState {
        ERROR("ERROR", 3), PREVIEW("PREVIEW", 1), START("START", 0), STOP("STOP", 2);

        CameraState(String status, int code) {
        }
    }

    public static interface CameraStateCallback {
        void onCameraStateChanged(CameraSurfaceView.CameraState cameraState);
    }

    public class MyPictureCallback implements Camera.PictureCallback {

        public void onPictureTaken(byte[] param1ArrayOfbyte, Camera param1Camera) {
            Log.i(TAG, "onPictureTaken: ");
//            ExecutorFactory.g.execute(new a(this, param1ArrayOfbyte));
            closeCamera1();
        }

//        public class a implements Runnable {
//            public a(CameraSurfaceView.b this$0, byte[] param2ArrayOfbyte) {}
//
//            public void run() {
//                byte[] arrayOfByte = this.a;
//                Bitmap bitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
//                Matrix matrix = new Matrix();
//                if (this.b.a.b) {
//                    matrix.setRotate(90.0F);
//                } else {
//                    matrix.setRotate(270.0F);
//                    matrix.postScale(-1.0F, 1.0F);
//                }
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                CameraSurfaceView cameraSurfaceView2 = this.b.a;
//                StringBuilder stringBuilder2 = new StringBuilder();
//                stringBuilder2.append(j.d());
//                stringBuilder2.append(System.currentTimeMillis());
//                stringBuilder2.append(".jpg");
//                cameraSurfaceView2.m = stringBuilder2.toString();
//                String str = this.b.a.m;
//                try {
//                    FileOutputStream fileOutputStream = new FileOutputStream();
//                    this(str);
//                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
//                    this(fileOutputStream);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
//                    bufferedOutputStream.flush();
//                    bufferedOutputStream.close();
//                } catch (IOException iOException) {
//                    j0.b(iOException);
//                }
//                bitmap.recycle();
//                StringBuilder stringBuilder1 = new StringBuilder();
//                stringBuilder1.append("tack success, path:");
//                stringBuilder1.append(this.b.a.m);
//                j0.a(stringBuilder1.toString());
//                CameraSurfaceView cameraSurfaceView1 = this.b.a;
//                CameraSurfaceView.c c = cameraSurfaceView1.o;
//                if (c != null)
//                    c.b(cameraSurfaceView1.m);
//            }
//        }
    }

//    public class a implements Runnable {
//        public a(CameraSurfaceView this$0, byte[] param1ArrayOfbyte) {}
//
//        public void run() {
//            byte[] arrayOfByte = this.a;
//            Bitmap bitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
//            Matrix matrix = new Matrix();
//            if (this.b.a.b) {
//                matrix.setRotate(90.0F);
//            } else {
//                matrix.setRotate(270.0F);
//                matrix.postScale(-1.0F, 1.0F);
//            }
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            CameraSurfaceView cameraSurfaceView2 = this.b.a;
//            StringBuilder stringBuilder2 = new StringBuilder();
//            stringBuilder2.append(j.d());
//            stringBuilder2.append(System.currentTimeMillis());
//            stringBuilder2.append(".jpg");
//            cameraSurfaceView2.m = stringBuilder2.toString();
//            String str = this.b.a.m;
//            try {
//                FileOutputStream fileOutputStream = new FileOutputStream();
//                this(str);
//                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
//                this(fileOutputStream);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
//                bufferedOutputStream.flush();
//                bufferedOutputStream.close();
//            } catch (IOException iOException) {
//                j0.b(iOException);
//            }
//            bitmap.recycle();
//            StringBuilder stringBuilder1 = new StringBuilder();
//            stringBuilder1.append("tack success, path:");
//            stringBuilder1.append(this.b.a.m);
//            j0.a(stringBuilder1.toString());
//            CameraSurfaceView cameraSurfaceView1 = this.b.a;
//            CameraSurfaceView.c c = cameraSurfaceView1.o;
//            if (c != null)
//                c.b(cameraSurfaceView1.m);
//        }
//    }
}
