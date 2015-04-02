package com.zgy.graduation.graduationproject.activity;

import android.os.Bundle;

import com.zgy.graduation.graduationproject.R;

/**
 * Created by zhangguoyu on 2015/4/2.
 */
public class StorehouseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_storehouse);
        comm_title.setText(R.string.storehouse_info);

    }

}
