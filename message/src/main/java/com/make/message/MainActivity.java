package com.make.message;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.make.message.bean.Sms;
import com.make.message.observer.SMSObserver;
import com.make.message.util.SMSUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Activity activity;

    private EditText etPhone;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        activity = this;
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission_group.SMS);
        permissions.add(Manifest.permission.CALL_PHONE);
        requestRuntimePermission(permissions, 1);

        etPhone = findViewById(R.id.et_phone);
        etMessage = findViewById(R.id.et_message);
        Button btnSend = findViewById(R.id.btnSMS);
        Button btnAdd = findViewById(R.id.btnAddSMS);
        Button btnCall = findViewById(R.id.btnCall);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString();
                String message = etMessage.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> texts = smsManager.divideMessage(message);//拆分短信,短信字数太多了的时候要分几次发
                for(String text : texts){
                    smsManager.sendTextMessage(phone, null, text, null, null);//发送短信,mobile是对方手机号
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sms sms = new Sms();
                sms.setSendTimeStamp(System.currentTimeMillis());
                sms.setType("1");
                sms.setContent("团吧");
                sms.setSendNo("0336911");
                SMSUtil.addSms(MainActivity.this, sms);
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
            }
        });
    }

    /**
     * 申请权限
     *
     * @param permissions 所需要的权限
     */
    public void requestRuntimePermission(ArrayList<String> permissions, int code) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<String>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), code);
            } else {
                goon();
            }
        } else {
            goon();
        }
    }
    private void goon(){
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true,
                new SMSObserver(new Handler(), this));
    }
}
