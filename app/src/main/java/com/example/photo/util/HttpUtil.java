package com.example.photo.util;

import android.util.Log;
import com.example.photo.callback.NetwordCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 作者    py
 * 时间    2018/11/21 13:27
 * 描述
 */
public class HttpUtil {

    private static final String TAG = "Contact-HttpUtil";

    public static void httpPostJSONAsync(final String url, final String params, final NetwordCallBack networdCallBack) {
        new Thread() {
            public void run() {
                httpPostJSON(url, params, networdCallBack);
            }
        }.start();
    }

    public static void httpPostJSON(String url, String params, NetwordCallBack networdCallBack) {
        Log.i(TAG, "httpPostJSON: params:" + params);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            if (params != null && !params.trim().equals("")) {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(params);
                // flush输出流的缓冲
                out.flush();
            }
            Log.i(TAG, "httpPostJSON: 1");
            if (conn.getResponseCode() == 200) {
                Log.i(TAG, "httpPostJSON: 2");
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                networdCallBack.onSuccess(result);
            }
        } catch (Exception e) {
            // 网络错误异常
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
//            Log.i(TAG, "httpPostJSON: " + result);
            try {
                Log.i(TAG, "httpPostJSON: code:" + conn.getResponseCode());
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
    }
}
