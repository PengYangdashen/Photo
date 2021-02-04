package com.example.contact;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.alibaba.fastjson.JSON;
import com.example.contact.bean.Addressbook;
import com.example.contact.thread.ExecutorFactory;
import com.example.contact.util.ContactUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView tvContent;
    private LinearLayout ll;
    private LinearLayout llName;
    private LinearLayout llPhone;
    private EditText etName;
    private EditText etNameModify;
    private EditText etPhone;
    private EditText etPhoneModify;
    private EditText etEmail;
    private EditText etEmailModify;
    private EditText etAddress;
    private EditText etAddressModify;
    private Button btnAdd;
    private Button btnDelete;
    private Button btnModify;
    private Button btnSearch;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        List<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.READ_CONTACTS);
        permissions.add(android.Manifest.permission.WRITE_CONTACTS);
        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<String>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1111);
            } else {
            }
        } else {
        }
        initView();
        initData();
    }

    private void initData() {
        ExecutorFactory.getThreadInstance().execute(new Runnable() {
            @Override
            public void run() {
//                ContactUtil.setCallback(new ContactUtil.ContactCallback() {
//                    @Override
//                    public void onToast(String message) {
//                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onQueryContact(List<HashMap<String, String>> contactList) {
//                        Log.i(TAG, "onQueryContact: contactList->" + contactList.size());
//                        for (HashMap<String, String> map : contactList) {
//                            for (String key : map.keySet()) {
//                                tvContent.append(key + ":" + map.get(key) + "\n");
//                            }
//                            tvContent.append("---------------------\n");
//                        }
//                    }
//                });
                ContactUtil.queryContactsShowData(context, new Addressbook());
                ContactUtil.queryContacts(context);
            }
        });
    }

    private void initView() {
        tvContent = findViewById(R.id.tv_contact);
        ll = findViewById(R.id.ll);
        llName = findViewById(R.id.ll_name);
        llPhone = findViewById(R.id.ll_phone);
        etName = findViewById(R.id.et_name);
        etNameModify = findViewById(R.id.et_name_modify);
        etPhone = findViewById(R.id.et_phone);
        etPhoneModify = findViewById(R.id.et_phone_modify);
        etEmail = findViewById(R.id.et_email);
        etEmailModify = findViewById(R.id.et_email_modify);
        etAddress = findViewById(R.id.et_address);
        etAddressModify = findViewById(R.id.et_address_modify);
        btnAdd = findViewById(R.id.btn_add);
        btnDelete = findViewById(R.id.btn_delete);
        btnModify = findViewById(R.id.btn_modify);
        btnSearch = findViewById(R.id.btn_search);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    private boolean vis = false;

    @Override
    public void onClick(View view) {
        etNameModify.setVisibility(View.GONE);
        etPhoneModify.setVisibility(View.GONE);
        etEmailModify.setVisibility(View.GONE);
        etAddressModify.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.btn_add:
                vis = false;
                Addressbook addBook = new Addressbook();
                Map<String, String> phoneBook = new HashMap<>();
                Map<String, String> emailBook = new HashMap<>();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                // 1-住宅
                // 2-手机
                // 3-单位
                // 4-单位传真
                // 5-住宅传真
                // 6-寻呼机
                // 7-其他
                phoneBook.put("1", "1865212147");
                phoneBook.put("2", "0332163591");
                phoneBook.put("3", "0332163592");
                phoneBook.put("4", "0332163593");
                phoneBook.put("5", "0332163594");
                phoneBook.put("6", "0332163595");
                phoneBook.put("7", "0332163596");
                // 1-个人
                // 2-工作
                // 3-其他
                // 4-手机
                // 5-自定义
                emailBook.put("1", "10235@qq.com");
                emailBook.put("2", "1asdas3333@qq.com");
                emailBook.put("3", "3@qq.com");
                emailBook.put("4", "4@qq.com");
                emailBook.put("5", "5@qq.com");
                emailBook.put("6", "6@qq.com");
                addBook.setName(name);
                addBook.setPhoneBook(phoneBook);
                addBook.setEmailBook(emailBook);
                addBook.setCompany("天天向上");
                addBook.setPosition("天使投资人");
                ContactUtil.writeContact(context, addBook);
                ContactUtil.queryContactsShowData(context, new Addressbook());
                break;
            case R.id.btn_delete:
                vis = false;
                Addressbook deleteBook = new Addressbook();
                String id = etPhone.getText().toString();
                deleteBook.setId(id);
                ContactUtil.deleteContact(context, deleteBook);
                break;
            case R.id.btn_modify:
                if (vis) {
                    Addressbook modifyBook = new Addressbook();
                    modifyBook.setId("7");
                    Map<String, String> modifyPhoneBook = new HashMap<>();
                    modifyPhoneBook.put("2", etPhoneModify.getText().toString());
                    modifyBook.setPhoneBook(modifyPhoneBook);
                    ContactUtil.changeContact(context, modifyBook);
                }
                etNameModify.setVisibility(View.VISIBLE);
                etPhoneModify.setVisibility(View.VISIBLE);
                etEmailModify.setVisibility(View.VISIBLE);
                etAddressModify.setVisibility(View.VISIBLE);
                vis = true;
                break;
            case R.id.btn_search:
                vis = false;
                break;
            default:
                break;
        }
    }
}
