package com.example.contact;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
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
