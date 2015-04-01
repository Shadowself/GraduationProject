package com.zgy.graduation.graduationproject.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgy.graduation.graduationproject.R;

/**
 * Created by Mr_zhang on 2015/3/31.
 */
public class BaseActivity extends Activity {

    // 标题栏以下部分内容布局类对象
    protected LinearLayout viewContent = null;
    protected TextView comm_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_base);
        viewContent = (LinearLayout) findViewById(R.id.viewBaseContent);
        viewContent.setVisibility(View.VISIBLE);
        comm_title = (TextView) findViewById(R.id.comm_title);
    }

    /**
     * 初始化中间部分内容布局方法
     * */
    public void setChildContentView(int layoutResId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        viewContent.addView(v,params);
        //设置中间布局可以长按才能触发手势
        viewContent.setLongClickable(true);
    }


}
