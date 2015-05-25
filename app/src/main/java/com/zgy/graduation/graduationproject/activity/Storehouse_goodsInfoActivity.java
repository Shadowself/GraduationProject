package com.zgy.graduation.graduationproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.adapter.StorehouseGoodsInfoAdapter;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.SweetAlertDialogUtils;
import com.zgy.graduation.graduationproject.util.ViewUtil;

/**
 * Created by Mr_zhang on 2015/5/12.
 */
public class Storehouse_goodsInfoActivity extends BaseActivity {
    private static final String TAG = Storehouse_goodsInfoActivity.class.getSimpleName();

    private StorehouseGoodsInfoAdapter storehouseGoodsInfoAdapter;
    private GridView goods_gridview;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_goods_info);
        comm_title.setText(R.string.storehouse_goodInfo);
        back_main.setVisibility(View.VISIBLE);

        goods_gridview = (GridView) findViewById(R.id.goods_gridview);
        storehouseGoodsInfoAdapter = new StorehouseGoodsInfoAdapter(this);
        jsonArray = new JSONArray();
        JSONArray jsonArrayTemp = new JSONArray();
        for (int i = 0; i < 9; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ReqCmd.TEMPERATURE, "20");
            jsonArrayTemp.add(jsonObject);
        }

        storehouseGoodsInfoAdapter.addDatas(jsonArrayTemp);
        goods_gridview.setAdapter(storehouseGoodsInfoAdapter);

        goods_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (jsonArray.size() > position) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(position);

                    ViewUtil.showToast(mContext, "温度：" + jsonObject.getString(ReqCmd.TEMPERATURE) + "℃ ，湿度：" + jsonObject.getString(ReqCmd.DAMPNESS) + " %RH ，害虫：" + jsonObject.getString(ReqCmd.PESTKIND) + ", 数量：" + jsonObject.getString(ReqCmd.PESTNUMBER) + "。");
                }
            }
        });
        getJsonData();
    }


    public void getJsonData() {
        String url = String.format(getString(R.string.goodInfo_url), getString(R.string.common_ip));
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.STOREHOUSEID, preferencesUtil.getString(ReqCmd.STOREHOUSEID));
        SweetAlertDialogUtils.showProgressDialog(this, getString(R.string.waiting), false);

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

                                    jsonArray = JSON.parseArray(resData.getData());
                                    Log.i(TAG, jsonArray.toJSONString());
                                    storehouseGoodsInfoAdapter.clearAndAddDatas(jsonArray);
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
                        SweetAlertDialogUtils.closeProgressDialog();
                    }
                }
        );
    }
}

