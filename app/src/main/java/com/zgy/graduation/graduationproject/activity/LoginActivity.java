package com.zgy.graduation.graduationproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.PreferencesUtil;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.SweetAlertDialogUtils;
import com.zgy.graduation.graduationproject.util.ViewUtil;


public class LoginActivity extends ActionBarActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    protected Context mContext = LoginActivity.this;
    private Button loginButton;
    private EditText account;
    private EditText password;
    private CheckBox rememberPswd;
    private CheckBox autoLogin;
    private PreferencesUtil preferencesUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        preferencesUtil = new PreferencesUtil(this);
        boolean rememberChecked = preferencesUtil.getBoolean(ReqCmd.REMEMBERCHECKED, false);
        final boolean autologinChecked = preferencesUtil.getBoolean(ReqCmd.AUTOLOGINCHECKED, false);
        if (rememberChecked) {
            String user = preferencesUtil.getString(ReqCmd.USERNAME, "");
            String pswd = preferencesUtil.getString(ReqCmd.PASSWORD, "");
            account.setText(user);
            password.setText(pswd);
            rememberPswd.setChecked(true);
            if (autologinChecked) {
                autoLogin.setChecked(true);
                Login(user, pswd);
            }
        } else {
            rememberPswd.setChecked(false);
            autoLogin.setChecked(false);

        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = account.getText().toString().trim();
                String pswd = password.getText().toString().trim();

                //not empty then login
                if (!username.isEmpty() && !pswd.isEmpty()) {
                    if (rememberPswd.isChecked()) {
                        preferencesUtil.saveBoolean(ReqCmd.REMEMBERCHECKED, true);
                        preferencesUtil.saveString(ReqCmd.USERNAME, username);
                        preferencesUtil.saveString(ReqCmd.PASSWORD, pswd);
                    } else {
                        preferencesUtil.saveBoolean(ReqCmd.REMEMBERCHECKED, false);
                        preferencesUtil.saveString(ReqCmd.USERNAME, "");
                        preferencesUtil.saveString(ReqCmd.PASSWORD, "");
                    }

                    if (autoLogin.isChecked()) {
                        preferencesUtil.saveBoolean(ReqCmd.AUTOLOGINCHECKED, true);
                    } else {
                        preferencesUtil.saveBoolean(ReqCmd.AUTOLOGINCHECKED, false);
                    }

                    Login(username, pswd);

//                    Intent intent = new Intent();
//                    intent.setClass(mContext, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
                } else {
                    ViewUtil.showToast(mContext, getString(R.string.text_empty));
                }

            }
        });


    }

    public void initView() {
        loginButton = (Button) findViewById(R.id.loginButton);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        rememberPswd = (CheckBox) findViewById(R.id.rememberPswd);
        autoLogin = (CheckBox) findViewById(R.id.autologin);

        //if auto login then must remember password
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    rememberPswd.setChecked(true);
                }

            }
        });

        // if not remember password then not auto login
        rememberPswd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (!isChecked) {
                    autoLogin.setChecked(false);
                }

            }
        });

    }

    public void Login(String username, String password) {
        String url = getString(R.string.login_url);
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.USERNAME, username);
        jsonString.put(ReqCmd.PASSWORD, password);
//        showProgressDialog(getString(R.string.logining), false);
        SweetAlertDialogUtils.showProgressDialog(this,getString(R.string.logining),false);
        HttpAsyncTaskManager httpAsyncTaskManager = new HttpAsyncTaskManager(mContext);
        httpAsyncTaskManager.requestStream(url, jsonString.toJSONString(), new StringTaskHandler() {
                    @Override
                    public void onNetError() {
                        ViewUtil.showToast(mContext, getString(R.string.network_error));
                    }

                    @Override
                    public void onSuccess(String result) {

                        try {
                            ResData resData = JSONObject.parseObject(result, ResData.class);
                            switch (resData.getCode_()) {
                                // resData.getcode_()=0;
                                case ReqCmd.RESULTCODE_SUCCESS:
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                    break;
                                default:
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    break;
                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFail() {
                        ViewUtil.showToast(mContext, getString(R.string.server_error));
                    }

                    @Override
                    public void onFinish() {
//                        closeProgressDialog();
                        SweetAlertDialogUtils.closeProgressDialog();
                    }

                }
        );

    }

}
