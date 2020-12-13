package com.make.websocket.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.make.websocket.client.JWebSocketClient;

import java.net.URI;

public class WebSocketService extends Service {

    private String TAG = getClass().getSimpleName();
    private WebSocketBinder mBinder = new WebSocketBinder();
    public JWebSocketClient client;
    private SocketCallback callback;
    private static final long HEART_BEAT_RATE = 10 * 1000;
    private Handler mHandler;
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "heartbeat: ");
            if (client != null) {
                if (client.isClosed()) {
                    reconnectWs();
                }
            } else {
                //如果client已为空，重新初始化websocket
                initSocketClient();
            }
            //定时对长连接进行心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSocketClient();
        mHandler = new Handler();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
    }

    private void initSocketClient() {
        URI uri = URI.create("ws://180.215.254.23:9903/JYSystem/ws/appLink?openId=eyJvd25lciI6IkpZR2V0IiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJpc3MiOiJKWSIsImV4cCI6MTYwNzc4NjQ0NSwiaWF0IjoxNjA3Nzg0NjQ1LCJKWV9DTEFJTSI6Ikw1dDBZd1IwTkdINFU0bWZsbzRPNmsxN0FybzVzc1poRG41ZSt6NVpGS1gvZmQralZBZVdOaDFBTTkvZE1Hbm5lSnlONzFFQXpkY1NpeXdZUG80UElnPT0ifQ.khaESaySaZyY9tEXichQNATxl-QJhJsIZUAIMCEgAGA");
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                Log.i(TAG, message);
                callback.onMessage(message);
            }
        };
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    //重连
                    client.reconnectBlocking();
                    Log.i(TAG, "run: reconnectWs");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void setCallback (SocketCallback callback) {
        this.callback = callback;
    }

    public class WebSocketBinder extends Binder {
        public WebSocketService getService() {
            Log.i(TAG, "getService: ");
            return WebSocketService.this;
        }
    }

    public interface SocketCallback {
        void onMessage(String message);
    }
}
