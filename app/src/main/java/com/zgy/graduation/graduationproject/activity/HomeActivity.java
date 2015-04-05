package com.zgy.graduation.graduationproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.adapter.StorehouseAdapter;
import com.zgy.graduation.graduationproject.bean.Storehouse;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_zhang on 2015/3/31.
 */
public class HomeActivity extends BaseActivity {

    private GridView storehouse_gridView;
    private List<Storehouse> gridItems ;
    private StorehouseAdapter storehouseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_home);
        comm_title.setText(getString(R.string.home_title));

        storehouse_gridView = (GridView) findViewById(R.id.storehouse_gridView);
        gridItems = new ArrayList<Storehouse>();



        for(int i = 0; i < 10; i++){
            Storehouse gridMenu = new Storehouse();
    		gridMenu.setStorehouseTitleResId( 100 * (i+1) +i+1 + "");
    		gridMenu.setStorehouseImgResId(R.mipmap.storehouse);
    		gridItems.add(gridMenu);
    	}
        storehouseAdapter = new StorehouseAdapter(this);
        storehouseAdapter.setList(gridItems);
        storehouse_gridView.setAdapter(storehouseAdapter);
        storehouse_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent();
//                intent.setClass(HomeActivity.this,StorehouseActivity.class);
//                startActivity(intent);
                String url = "http://www.marschen.com/data1.html";

                HttpAsyncTaskManager httpAsyncTaskManager = new HttpAsyncTaskManager(HomeActivity.this);
                httpAsyncTaskManager.request(url,new StringTaskHandler() {
                    @Override
                    public void onNetError() {
                        ViewUtil.showToast(HomeActivity.this,"网络异常，请检查网络是否连接！");
                    }

                    @Override
                    public void onSuccess(String result) {

                        ViewUtil.showToast(HomeActivity.this,result);

                    }

                    @Override
                    public void onFail() {
                        ViewUtil.showToast(HomeActivity.this,"服务器端连接失败，请检查服务端设置！");
                    }

                    @Override
                    public void onFinish() {

                        Storehouse gridMenu = new Storehouse();
                        gridMenu.setStorehouseTitleResId( 100 * (10+1) +10+1 + "");
                        gridMenu.setStorehouseImgResId(R.mipmap.storehouse);
                        gridItems.add(gridMenu);

                        storehouseAdapter.setList(gridItems);
                        storehouse_gridView.setAdapter(storehouseAdapter);
                    }

                }
                );

            }
        });

    }

}
