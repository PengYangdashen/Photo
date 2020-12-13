package com.make.websocket.client;

import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class JWebSocketClient extends WebSocketClient {

    private final String TAG = getClass().getSimpleName();

    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i(TAG, "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Log.i(TAG, "onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i(TAG, "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.i(TAG, "onError()");
    }
}
