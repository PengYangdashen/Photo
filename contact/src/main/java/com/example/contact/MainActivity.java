package com.example.contact;

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
                ContactUtil.setCallback(new ContactUtil.ContactCallback() {
                    @Override
                    public void onToast(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onQueryContact(List<HashMap<String, String>> contactList) {
                        Log.i(TAG, "onQueryContact: contactList->" + contactList.size());
                        for (HashMap<String, String> map : contactList) {
                            for (String key : map.keySet()) {
                                tvContent.append(key + ":" + map.get(key) + "\n");
                            }
                            tvContent.append("---------------------\n");
                        }
                    }
                });
                ContactUtil.queryContactsShowData(context, new Addressbook());
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

    @Override
    public void onClick(View view) {
        etNameModify.setVisibility(View.GONE);
        etPhoneModify.setVisibility(View.GONE);
        etEmailModify.setVisibility(View.GONE);
        etAddressModify.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.btn_add:
                break;
            case R.id.btn_delete:
                break;
            case R.id.btn_modify:
                etNameModify.setVisibility(View.VISIBLE);
                etPhoneModify.setVisibility(View.VISIBLE);
                etEmailModify.setVisibility(View.VISIBLE);
                etAddressModify.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_search:
                break;
            default:
                break;
        }
    }
}
