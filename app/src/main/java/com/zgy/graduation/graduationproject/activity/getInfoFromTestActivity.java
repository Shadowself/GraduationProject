package com.zgy.graduation.graduationproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zgy.graduation.graduationproject.R;

/**
 * Created by Mr_zhang on 2015/4/19.
 */
public class getInfoFromTestActivity extends BaseActivity {

    private EditText place;
    private EditText temperature_goods;
    private EditText dampness_goods;
    private EditText pestKind;
    private EditText pestNumber;

    private Button pastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_getinfor);
        comm_title.setText(getString(R.string.getStorehouseInfo));
        back_main.setVisibility(View.VISIBLE);

        place = (EditText) findViewById(R.id.place);
        temperature_goods = (EditText) findViewById(R.id.temperature_goods);
        dampness_goods = (EditText) findViewById(R.id.dampness_goods);
        pestKind = (EditText) findViewById(R.id.pestKind);
        pestNumber = (EditText) findViewById(R.id.pestNumber);

        pastButton = (Button)findViewById(R.id.pastButton);
        pastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
