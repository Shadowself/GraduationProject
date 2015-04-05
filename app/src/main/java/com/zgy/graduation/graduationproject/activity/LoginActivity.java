package com.zgy.graduation.graduationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ViewUtil;



public class LoginActivity extends ActionBarActivity {

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getString(R.string.login_url);

                JSONObject jsonString = new JSONObject();
                jsonString.put("username","admin");
                jsonString.put("password","123");

                HttpAsyncTaskManager httpAsyncTaskManager = new HttpAsyncTaskManager(LoginActivity.this);
                httpAsyncTaskManager.requestStream(url, jsonString.toJSONString(), new StringTaskHandler() {
                            @Override
                            public void onNetError() {
                                ViewUtil.showToast(LoginActivity.this, getString(R.string.network_error));
                            }

                            @Override
                            public void onSuccess(String result) {

                                String str = result;

                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,LoginActivity.class);
                                startActivity(intent);

                                ViewUtil.showToast(LoginActivity.this, result);
                            }

                            @Override
                            public void onFail() {
                                ViewUtil.showToast(LoginActivity.this, getString(R.string.server_error));
                            }

                            @Override
                            public void onFinish() {

                            }

                        }
                );



            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
