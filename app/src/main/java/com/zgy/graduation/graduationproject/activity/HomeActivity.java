package com.zgy.graduation.graduationproject.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.adapter.StorehouseAdapter;
import com.zgy.graduation.graduationproject.bean.Storehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_zhang on 2015/3/31.
 */
public class HomeActivity extends Activity {

    private GridView storehouse_gridView;
    private List<Storehouse> gridItems ;
    private StorehouseAdapter storehouseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

    }


}
