package com.zgy.graduation.graduationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zgy.graduation.graduationproject.R;

/**
 * Created by zhangguoyu on 2015/4/2.
 */
public class StorehouseActivity extends BaseActivity implements View.OnClickListener {

    private Button postPicture;
    private Button deleteButton;
    private Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_storehouse);
        comm_title.setText(R.string.storehouse_info);
        back_main.setVisibility(View.VISIBLE);

        postPicture = (Button) findViewById(R.id.postPicture);
        postPicture.setOnClickListener(this);

        deleteButton = (Button)findViewById(R.id.deleteStore);
        deleteButton.setOnClickListener(this);

        changeButton = (Button) findViewById(R.id.changeStore);
        changeButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.postPicture:
                Intent intent = new Intent();
                intent.setClass(this,findPestKindActivity.class);
                startActivity(intent);
                break;

            case R.id.deleteStore:

                break;

            case R.id.changeStore:

                break;
        }
    }
}
