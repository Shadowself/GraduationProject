package com.zgy.graduation.graduationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.adapter.StorehouseAdapter;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.bean.Storehouse;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.SweetAlertDialogUtils;
import com.zgy.graduation.graduationproject.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_zhang on 2015/3/31.
 */
public class HomeActivity extends BaseActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private GridView storehouse_gridView;
    private List<Storehouse> gridItems;
    private StorehouseAdapter storehouseAdapter;
    private Button addStorehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_home);
        comm_title.setText(getString(R.string.home_title));

        storehouse_gridView = (GridView) findViewById(R.id.storehouse_gridView);
        addStorehouse = (Button) findViewById(R.id.addStorehouse);
        addStorehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,AddStorehouseActivity.class);
                startActivity(intent);
            }
        });

        gridItems = new ArrayList<Storehouse>();


        for (int i = 0; i < 10; i++) {
            Storehouse gridMenu = new Storehouse();
            gridMenu.setStorehouseTitleResId(100 * (i + 1) + i + 1 + "");
            gridMenu.setStorehouseImgResId(R.mipmap.storehouse);
            gridItems.add(gridMenu);
        }
        storehouseAdapter = new StorehouseAdapter(this);
        storehouseAdapter.setList(gridItems);
        storehouse_gridView.setAdapter(storehouseAdapter);
        storehouse_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Storehouse storehouse = gridItems.get(position);
                JSONObject json = new JSONObject();
                json.put("id",storehouse.getId());
                preferencesUtil.saveString(ReqCmd.STOREHOUSEID,Integer.toString(storehouse.getId()));
                json.put("storehouseName",storehouse.getStorehouseTitleResId());
                json.put("goods",storehouse.getGoods());
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, StorehouseActivity.class);
                intent.putExtra("jsonStorehouse",json.toJSONString());
                startActivity(intent);

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllStoreHouse();
    }

    public void getAllStoreHouse(){
        String url = getString(R.string.storehouse_url);
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.FLAG, ReqCmd.GET_STOREHOUSE_FLAG);
//        showProgressDialog(getString(R.string.waiting), false);
        SweetAlertDialogUtils.showProgressDialog(this,getString(R.string.waiting),false);

        HttpAsyncTaskManager httpAsyncTaskManager = new HttpAsyncTaskManager(mContext);
        httpAsyncTaskManager.requestStream(url, jsonString.toJSONString(), new StringTaskHandler() {
                    @Override
                    public void onNetError() {
                        ViewUtil.showToast(mContext, getString(R.string.network_error));
                    }

                    @Override
                    public void onSuccess(String result) {

                        try {
                            ResData resData = JSONObject.parseObject(result, ResData.class);
                            switch (resData.getCode_()) {
                                // resData.getcode_()=0;
                                case ReqCmd.RESULTCODE_SUCCESS:
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    JSONArray jsonArray = JSON.parseArray(resData.getData());
                                    gridItems.clear();
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        JSONObject json = (JSONObject) jsonArray.get(i);
                                        Storehouse gridMenu = new Storehouse();
                                        gridMenu.setId(Integer.valueOf(json.getString("storehouseId")));
                                        gridMenu.setStorehouseTitleResId(json.getString("storehouseName"));
                                        gridMenu.setGoods(json.getString("goods"));
                                        gridMenu.setStorehouseImgResId(R.mipmap.storehouse);
                                        gridItems.add(gridMenu);
                                    }
                                    storehouseAdapter.setList(gridItems);
                                    storehouse_gridView.setAdapter(storehouseAdapter);
//                                    Intent intent = new Intent();
//                                    intent.setClass(mContext, HomeActivity.class);
//                                    startActivity(intent);
//                                    finish();

                                    break;
                                default:
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                    }

                    @Override
                    public void onFail() {
                        ViewUtil.showToast(mContext, getString(R.string.server_error));
                    }

                    @Override
                    public void onFinish() {
//                        closeProgressDialog();
                        SweetAlertDialogUtils.closeProgressDialog();
                    }


                }
        );
    }

}
