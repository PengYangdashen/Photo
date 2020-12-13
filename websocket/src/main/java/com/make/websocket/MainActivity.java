package com.make.websocket;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.make.websocket.client.JWebSocketClient;
import com.make.websocket.service.WebSocketService;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private JWebSocketClient client;
    private WebSocketService.WebSocketBinder binder;
    private WebSocketService webSocketService;

    private TextView tv;
    private Button btn;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //服务与活动成功绑定
            Log.i(TAG, "服务与活动成功绑定");
            binder = (WebSocketService.WebSocketBinder) iBinder;
            webSocketService = binder.getService();
            webSocketService.setCallback(new WebSocketService.SocketCallback() {
                @Override
                public void onMessage(String message) {
                    Log.i(TAG, "onMessage: 收到socket回复：" + message);
                    tv.append("\n" + message);
                }
            });
            client = webSocketService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //服务与活动断开
            Log.i(TAG, "服务与活动成功断开");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                client.send("wakaka");
            }
        });
        Intent intent = new Intent(this, WebSocketService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }
}
