package com.make.message.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.make.message.bean.Sms;
import com.make.message.util.ContactUtil;

public class SMSReciever extends BroadcastReceiver {

    private static final String TAG = "SMSReciever";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //提取短信消息
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        //获取发送方号码
        String address = messages[0].getOriginatingAddress();
        String sendName = ContactUtil.GetContactsByNumber(context, address);

        String fullMessage = "";
        for (SmsMessage message : messages) {
            //获取短信内容
            fullMessage += message.getMessageBody();
        }
        Sms sms = new Sms();
        sms.setContent(fullMessage);
        sms.setType("recieve");
        sms.setSendTimeStamp(System.currentTimeMillis());
        sms.setSendNo(address);
        sms.setSendName(sendName);
        Log.i(TAG, "onReceive: " + JSON.toJSONString(sms));
        //截断广播,阻止其继续被Android自带的短信程序接收到
        abortBroadcast();

    }
}
