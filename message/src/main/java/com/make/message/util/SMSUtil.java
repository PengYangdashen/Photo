package com.make.message.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.make.message.bean.Sms;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SMSUtil {

    private static final String TAG = "SMSUtil";

    public static List<Sms> getSmsInPhone(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
        int lastid = sharedPreferences.getInt("lastid", 0);
        String[] projection = new String[]{"_id", "thread_id", "address", "person", "date", "type", "body"};
        String where = "_id > " + lastid;
        Cursor cur = contentResolver.query(Uri.parse("content://sms/"), projection, where, null, "date desc");
        if (null == cur || cur.getColumnCount() == 0) {
            return null;
        }
        ArrayList<Sms> list = new ArrayList<>();
        while (cur.moveToNext()) {
            int id = cur.getInt(cur.getColumnIndex("_id"));//id
            long date = cur.getLong(cur.getColumnIndex("date"));//date
            String address = cur.getString(cur.getColumnIndex("address"));//手机号
//            String person = cur.getString(cur.getColumnIndex("person"));
            String person = ContactUtil.GetContactsByNumber(context, address);
            String content = cur.getString(cur.getColumnIndex("body"));//短信内容
            String type = cur.getString(cur.getColumnIndex("type"));//短信类型
            Sms sms = new Sms();
            sms.setId(id+"");
            sms.setContent(content);
            sms.setSendName(person);
            sms.setSendNo(address);
            sms.setType(type);
            sms.setSendTimeStamp(date);
            list.add(sms);
            if (id > lastid) {
                lastid = id;
                sharedPreferences.edit().putInt("lastid", lastid).apply();
            }
        }
        cur.close();
        return list;
    }
    public static List<Sms> getSmsInPhone(Context context, Uri smsUri) {
        ContentResolver contentResolver = context.getContentResolver();
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", MODE_PRIVATE);
        int lastid = sharedPreferences.getInt("lastid", 0);
        String[] projection = new String[]{"_id", "thread_id", "address", "person", "date", "type", "body"};
        String where = "_id > " + lastid;
        Cursor cur = contentResolver.query(smsUri, projection, where, null, "date desc");
        if (null == cur || cur.getColumnCount() == 0) {
            return null;
        }
        ArrayList<Sms> list = new ArrayList<>();
        while (cur.moveToNext()) {
            int id = cur.getInt(cur.getColumnIndex("_id"));//id
            long date = cur.getLong(cur.getColumnIndex("date"));//date
            String address = cur.getString(cur.getColumnIndex("address"));//手机号
//            String person = cur.getString(cur.getColumnIndex("person"));
            String person = ContactUtil.GetContactsByNumber(context, address);
            String content = cur.getString(cur.getColumnIndex("body"));//短信内容
            String type = cur.getString(cur.getColumnIndex("type"));//短信类型
            Sms sms = new Sms();
            sms.setId(id+"");
            sms.setContent(content);
            sms.setSendName(person);
            sms.setSendNo(address);
            sms.setType(type);
            sms.setSendTimeStamp(date);
            list.add(sms);
            if (id > lastid) {
                lastid = id;
                sharedPreferences.edit().putInt("lastid", lastid).apply();
            }
        }
        cur.close();
        return list;
    }

    public static void addSms(Context context, Sms sms){
        ContentValues values = new ContentValues();
        values.put("address", sms.getSendNo());
        values.put("type", sms.getType());
        values.put("read", "0");
        values.put("body", sms.getContent());
        values.put("date", sms.getSendTimeStamp());
        values.put("person", ContactUtil.GetContactsByNumber(context, sms.getSendNo()));
        Uri insert = context.getContentResolver().insert(Uri.parse("content://sms/"), values);
        Log.i(TAG, "addSms: " + insert.toString());
    }

    public static int deleteSms(Context context, Sms sms){
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(Uri.parse("content://sms/"),"_id=?",new String[]{sms.getId()});
    }

}
