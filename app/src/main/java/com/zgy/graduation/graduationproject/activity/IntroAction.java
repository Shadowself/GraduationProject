package com.zgy.graduation.graduationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.zgy.graduation.graduationproject.Fragment.FirstSlide;
import com.zgy.graduation.graduationproject.Fragment.SecondSlide;
import com.zgy.graduation.graduationproject.Fragment.ThirdSlide;
import com.zgy.graduation.graduationproject.R;

/**
 * Created by Mr_zhang on 2015/6/7.
 */
public class IntroAction extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(new FirstSlide(), getApplicationContext());
        addSlide(new SecondSlide(), getApplicationContext());
        addSlide(new ThirdSlide(), getApplicationContext());
//        addSlide(new FourthSlide(), getApplicationContext());
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
        Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}
