package com.zgy.graduation.graduationproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.util.PreferencesUtil;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.view.LoadingView;

/**
 * Created by Mr_zhang on 2015/5/6.
 * description:启动页、、、
 */
public class StartActivity extends Activity {
    private LoadingView loadingView;
    private PreferencesUtil preferencesUtil = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loadingView = (LoadingView) findViewById(R.id.main_imageview);
        preferencesUtil = new PreferencesUtil(this);


        initLoadingImages();
        new Thread(){
            @Override
            public void run(){
                loadingView.startAnim();
            }
        }.start();

        Thread T = new newthread();
        T.start();

    }

    class newthread extends Thread{

        @Override
        public void run() {
            try {
                Thread.sleep(2500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean rememberChecked = preferencesUtil.getBoolean(ReqCmd.SHOWINTRO, false);
            if(rememberChecked){
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, IntroAction.class);
                startActivity(intent);
                preferencesUtil.saveBoolean(ReqCmd.SHOWINTRO, true);
            }


            finish();
        }
    }

    private void initLoadingImages(){
        int[] imageIds = new int[6];
        imageIds[0] = R.drawable.loader_frame_1;
        imageIds[1] = R.drawable.loader_frame_2;
        imageIds[2] = R.drawable.loader_frame_3;
        imageIds[3] = R.drawable.loader_frame_4;
        imageIds[4] = R.drawable.loader_frame_5;
        imageIds[5] = R.drawable.loader_frame_6;

        loadingView.setImageIds(imageIds);
    }
}
