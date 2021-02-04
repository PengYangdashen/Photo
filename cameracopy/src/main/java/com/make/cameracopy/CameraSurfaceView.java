package com.make.cameracopy;

import android.content.Context;
import android.graphics.ImageFormat;
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

    public SurfaceHolder c;

    public SurfaceTexture d;

    public boolean s = false;

    public boolean e = false;

    public boolean f = false;

    public Camera camera;

    public Camera.Parameters parameters;

    public byte[] i;

    public int j;

    public int k = 17;

    public CameraSurfaceView l;

    public String m;

    public String n;

    public c o;

    public CameraState p;

    public CameraStateCallback q;

    public MediaRecorder r;
    
    public CameraSurfaceView(Context context) {
        super(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        stopPreview();
        startPreview();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {

    }

    public enum CameraState {
        ERROR("ERROR", 3), PREVIEW("PREVIEW", 1), START("START", 0), STOP("STOP", 2);

        CameraState(String status, int code) {
        }
    }

    public void closeCamera1() {
        Log.i(TAG, "closeCamera ... ");
        closeCamera();
        stopPreview();
        releaseCamera();
    }


    public class CameraRunnable implements Runnable {
        private CameraSurfaceView cameraSurfaceView;
        public CameraRunnable(CameraSurfaceView cameraSurfaceView) {
        this.cameraSurfaceView = cameraSurfaceView;
        }
        public void run() {
            CameraSurfaceView cameraSurfaceView = this.cameraSurfaceView;
            if (!cameraSurfaceView.f) {
                cameraSurfaceView.e = true;
                cameraSurfaceView.startPreview();
            }
        }
    }

    public final void stopPreview() {
        Log.i(TAG, "stopPreview ... ");
        Camera camera = this.camera;
        if (camera == null)
            return;
        try {
            if (this.e) {
                camera.setPreviewCallbackWithBuffer(null);
                this.camera.stopPreview();
            } else {
                camera.setPreviewCallback(null);
                this.camera.stopPreview();
            }
            CameraState cameraState2 = this.p;
            CameraState cameraState1 = CameraState.STOP;
            if (cameraState2 != cameraState1) {
                this.p = cameraState1;
                CameraStateCallback d1 = this.q;
                if (d1 != null)
                    d1.onCameraStateChanged(cameraState1);
            }
        } catch (Exception exception) {
            c c1 = this.o;
            if (c1 != null)
                c1.a(exception);
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
                int j = list.size();
                if (i < j) {
                    if ("auto".equals(list.get(i))) {
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
            if (this.e) {
                this.camera.setPreviewTexture(this.d);
                this.camera.addCallbackBuffer(this.i);
            } else {
                this.camera.setPreviewDisplay(this.c);
            }
            this.camera.setParameters(this.parameters);
            this.camera.startPreview();
            CameraState cameraState2 = this.p;
            CameraState cameraState1 = CameraState.START;
            if (cameraState2 != cameraState1) {
                this.p = cameraState1;
                CameraStateCallback d1 = this.q;
                if (d1 != null)
                    d1.onCameraStateChanged(cameraState1);
            }
            try {
                String str = this.camera.getParameters().getFocusMode();
                if ("auto".equals(str) || "macro".equals(str))
                    this.camera.autoFocus(null);
            } catch (Exception exception) {
                c c1 = this.o;
                if (c1 != null)
                    c1.a(exception);
            }
            return true;
        } catch (Exception exception) {
            c c1 = this.o;
            if (c1 != null)
                c1.a(exception);
            releaseCamera();
            return false;
        }
    }

    public final void c(Context paramContext) {
        this.l = this;
        CameraState cameraState = CameraState.START;
        this.p = cameraState;
        CameraStateCallback d1 = this.q;
        if (d1 != null)
            d1.onCameraStateChanged(cameraState);
        openCamera();
        if ((paramContext.getResources().getConfiguration()).orientation == 2)
            this.a = 2;
        SurfaceHolder surfaceHolder = getHolder();
        this.c = surfaceHolder;
        surfaceHolder.addCallback(this);
        this.d = new SurfaceTexture(10);
        setOnClickListener(this);
        post(new a(this));
    }

    public final void openCamera() {
        Log.i(TAG, "openCamera ... ");
        if (this.b) {
            this.j = b(false);
        } else {
            this.j = b(true);
        }
        if (this.j == -1)
            this.j = 0;
        try {
            this.camera = Camera.open(this.j);
        } catch (Exception exception) {
            this.camera = null;
            c c1 = this.o;
            if (c1 != null)
                c1.a(exception);
            CameraState cameraState = CameraState.ERROR;
            this.p = cameraState;
            CameraStateCallback d1 = this.q;
            if (d1 != null)
                d1.onCameraStateChanged(cameraState);
        }
        if (this.camera == null) {
            Exception exception = new Exception("Open camera fail.");
            c c1 = this.o;
            if (c1 != null)
                c1.a(exception);
        }
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

    public void closeCamera() {
        Log.i(TAG, "closeCamera ... ");
        if (!this.s)
            return;
        try {
            c c1 = this.o;
            if (c1 != null)
                c1.b(this.n);
            this.r.setPreviewDisplay(null);
            this.r.stop();
            this.s = false;
            stopPreview();
            releaseCamera();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("video path:");
            stringBuilder.append(this.n);
        } catch (IllegalStateException illegalStateException) {
            c c1 = this.o;
            if (c1 != null)
                c1.a(illegalStateException);
            this.s = false;
        }
        this.r = null;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f = true;
    }

    public static interface CameraStateCallback {
        void onCameraStateChanged(CameraSurfaceView.CameraState param1CameraState);
    }

    public static interface c {
        void a(Exception param1Exception);

        void b(String param1String);
    }

    public class b implements Camera.PictureCallback {
        public b(CameraSurfaceView this$0) {}

        public void onPictureTaken(byte[] param1ArrayOfbyte, Camera param1Camera) {
            g.g.execute(new a(this, param1ArrayOfbyte));
            this.a.a();
        }

        public class a implements Runnable {
            public a(CameraSurfaceView.b this$0, byte[] param2ArrayOfbyte) {}

            public void run() {
                byte[] arrayOfByte = this.a;
                Bitmap bitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
                Matrix matrix = new Matrix();
                if (this.b.a.b) {
                    matrix.setRotate(90.0F);
                } else {
                    matrix.setRotate(270.0F);
                    matrix.postScale(-1.0F, 1.0F);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                CameraSurfaceView cameraSurfaceView2 = this.b.a;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(j.d());
                stringBuilder2.append(System.currentTimeMillis());
                stringBuilder2.append(".jpg");
                cameraSurfaceView2.m = stringBuilder2.toString();
                String str = this.b.a.m;
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream();
                    this(str);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
                    this(fileOutputStream);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                } catch (IOException iOException) {
                    j0.b(iOException);
                }
                bitmap.recycle();
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("tack success, path:");
                stringBuilder1.append(this.b.a.m);
                j0.a(stringBuilder1.toString());
                CameraSurfaceView cameraSurfaceView1 = this.b.a;
                CameraSurfaceView.c c = cameraSurfaceView1.o;
                if (c != null)
                    c.b(cameraSurfaceView1.m);
            }
        }
    }
}
