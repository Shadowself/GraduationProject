package com.zgy.graduation.graduationproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.adapter.StorehouseAdapter;
import com.zgy.graduation.graduationproject.bean.Storehouse;

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

                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,StorehouseActivity.class);
                startActivity(intent);

            }
        });

    }


}
