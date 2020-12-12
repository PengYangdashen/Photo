package com.example.contact.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import com.example.contact.bean.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactUtil {

    private static ContactCallback callback;
    private static List<HashMap<String, String>> mContactList = new ArrayList<>();

    /**
     * 写入手机联系人
     * @param context
     */
    private static void writeContact(Context context, Contact contact) {
        String name = contact.getName();
        String number = contact.getPhone();
        String email = contact.getEmail();

        //先查询要添加的号码是否已存在通讯录中, 不存在则添加. 存在则提示用户
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
        ContentResolver resolver = context.getContentResolver();
        //从raw_contact表中返回display_name
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        if (cursor == null)
            return;

        if (cursor.moveToFirst()) {
            Log.i("nn", "name=" + cursor.getString(0));
            callback.onToast("存在相同号码");
        } else {
            uri = Uri.parse("content://com.android.contacts/raw_contacts");
            ContentValues values = new ContentValues();
            long contact_id = ContentUris.parseId(resolver.insert(uri, values));
            //插入data表
            uri = Uri.parse("content://com.android.contacts/data");
            //add Name
            values.put("raw_contact_id", contact_id);
            values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/name");
            values.put("data1", name);
            resolver.insert(uri, values);
            values.clear();

            //add Phone
            values.put("raw_contact_id", contact_id);
            values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
            values.put("data1", number);
            resolver.insert(uri, values);
            values.clear();

            //add email
            values.put("raw_contact_id", contact_id);
            values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/email_v2");
            values.put("data1", email);
            resolver.insert(uri, values);
            values.clear();

            //add organization
            values.put("raw_contact_id", contact_id);
            values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/organization");
            values.put("data4", "产品经理");   //职务
            values.put("data1", "腾讯科技");   //公司
            resolver.insert(uri, values);
            values.clear();

            callback.onToast("插入号码成功");
        }
        cursor.close();
    }

    /**
     * 删除联系人
     * @param context
     */
    private static void deleteContact(Context context, Contact contact) {
        String name = "test";

        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, "display_name=?", new String[]{name}, null);
        if (cursor == null)
            return;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});

            callback.onToast("删除号码成功");
        }else{
            callback.onToast("没有找到号码");
        }
        cursor.close();
    }

    /**
     * 修改联系人
     * @param context
     * @param contact
     */
    private static void changeContact(Context context, Contact contact){
        String name = "test";
        String newPhone = "13644440000";
        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, "display_name=?", new String[]{name}, null);
        if (cursor == null)
            return;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("data1", newPhone);
            resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/phone_v2", id + ""});
        }else{
            callback.onToast("没有找到号码");
        }
        cursor.close();
    }

    /**
     * 查询联系人
     * @param context
     * @param contact
     */
    private static void queryContactsShowData(Context context, Contact contact) {
        mContactList.clear();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor == null)
            return;
        while (cursor.moveToNext()) {
            String phoneName;
            String phoneNumber;
            HashMap<String, String> listItem = new HashMap<>();
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            listItem.put("phoneName", phoneName);
            listItem.put("phoneNumber", phoneNumber);
            mContactList.add(listItem);
        }
        callback.onQueryContact(mContactList);
        cursor.close();
    }

    public interface ContactCallback {
        void onToast(String message);
        void onQueryContact(List<HashMap<String, String>> contactList);
    }
}
