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

    private static final String TAG = "ContactUtil";
    private static ContactCallback callback;
    private static List<HashMap<String, String>> mContactList = new ArrayList<>();

    public static void setCallback(ContactCallback callback1) {
        callback = callback1;
    }
    /**
     * 写入手机联系人
     * @param context
     */
    public static void writeContact(Context context, Contact contact) {
        Log.i(TAG, "writeContact: ");
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
    public static void deleteContact(Context context, Contact contact) {
        Log.i(TAG, "deleteContact: ");
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
    public static void changeContact(Context context, Contact contact){
        Log.i(TAG, "changeContact: ");
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
    public static void queryContactsShowData(Context context, Contact contact) {
        Log.i(TAG, "queryContactsShowData: ");
        mContactList.clear();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor == null)
            return;
        while (cursor.moveToNext()) {
            String phoneName;
            String phoneNumber;
            HashMap<String, String> listItem = new HashMap<>();
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] orgWhereParams = new String[]{id,
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            Cursor orgCur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                    null, orgWhere, orgWhereParams, null);
            if (orgCur != null && orgCur.moveToFirst()) {
                do {
                    //组织名 (公司名字)
                    String company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                    //职位
                    String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    listItem.put("公司", company);
                    listItem.put("职务", title);
                } while (orgCur.moveToNext());
            }
            Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI//
                    , new String[] { ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.LABEL}// "data1"
                    , "raw_contact_id=?", new String[] { id }, null);
            if (emailCursor != null && emailCursor.moveToFirst()) {
                do {
                    Log.i(TAG, "queryContactsShowData: email");
                    String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    int typeindex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE);
                    int labelindex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL);
                    int email_type = emailCursor.getInt(typeindex);
                    String emailLabel = "";
                    if (email_type == ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM) {
                        emailLabel = emailCursor.getString(labelindex);
                    } else {
                        emailLabel = (String) ContactsContract.CommonDataKinds.Email.getTypeLabel(context.getResources(), email_type, "");
                    }
                    listItem.put(emailLabel, email);
                } while (emailCursor.moveToNext());
            }

            String[] phoneProjection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL};
            // arr[i] = id + " , 姓名：" + name;

            // 根据联系人的ID获取此人的电话号码
            Cursor phonesCusor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    phoneProjection,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + id, null, null);
            if (phonesCusor != null && phonesCusor.moveToFirst()) {
                Log.i(TAG, "queryContactsShowData: phone");
                do {
                    int typeindex = phonesCusor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int labelindex = phonesCusor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);
                    int phone_type = phonesCusor.getInt(typeindex);
                    String phoneLabel = "";
                    if (phone_type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
                        phoneLabel = phonesCusor.getString(labelindex);
                    } else {
                        phoneLabel = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), phone_type, "");
                    }
                    listItem.put(phoneLabel, phonesCusor.getString(0));
                } while (phonesCusor.moveToNext());
            }
            listItem.put("phoneName", phoneName);
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
