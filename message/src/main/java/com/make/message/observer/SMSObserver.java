package com.make.message.observer;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.make.message.bean.Sms;
import com.make.message.util.ContactUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SMSObserver extends ContentObserver {

    private Uri mUri;
    private Activity mActivity;
    private static final String TAG = "SMSObserver";

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SMSObserver(Handler handler, Activity activity) {
        super(handler);
        mActivity = activity;
        Log.i(TAG, "SMSObserver: create");
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.i(TAG, "onChange-uri: 数据变化了");
        if (uri == null) {
            mUri = Uri.parse("content://sms/inbox");
        } else {
            mUri = uri;
        }
        Log.i(TAG, "onChange: mUri->" + mUri.toString());
        if (mUri.toString().contains("content://sms/raw") || mUri.toString().equals("content://sms")) {
            return;
        }
        try {
            handleSMS();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void handleSMS() throws ParseException {

        /* 按照日期倒序排序 */
        Cursor cursor = mActivity.getContentResolver().query(mUri, null, null, null, "date desc");
        if (cursor != null) {
            if (cursor.moveToFirst()) {//游标移动到first位置
                String[] columnNames = cursor.getColumnNames();
                Log.i(TAG, "handleSMS: " + JSON.toJSONString(columnNames));
                /* 发件人的号码 */
                String address = cursor.getString(cursor.getColumnIndex("address"));
                /* 发件人的姓名 */
                String name = ContactUtil.GetContactsByNumber(mActivity, address);
                /* 短信内容 */
                String body = cursor.getString(cursor.getColumnIndex("body"));
                /* 短信编号 */
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                /* 短信时间 */
                String date = cursor.getString(cursor.getColumnIndex("date"));
                /* 短信类型 */
                String type = cursor.getString(cursor.getColumnIndex("type"));
                Sms sms = new Sms();
                sms.setSendName(name);
                sms.setSendNo(address);
                sms.setContent(body);
                sms.setId(id);
                sms.setType(type);
                sms.setSendTimeStamp(Long.valueOf(date));
                Log.i(TAG, "handleSMS: " + JSON.toJSONString(sms));
            }
            cursor.close();
        }
    }
}
