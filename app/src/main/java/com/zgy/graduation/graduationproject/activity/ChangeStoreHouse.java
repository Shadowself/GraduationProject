package com.zgy.graduation.graduationproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.StringUtils;
import com.zgy.graduation.graduationproject.util.SweetAlertDialogUtils;
import com.zgy.graduation.graduationproject.util.ViewUtil;

/**
 * Created by zhangguoyu on 2015/4/10.
 * description: change storehouseInfo
 */
public class ChangeStoreHouse extends BaseActivity{
    private static final String TAG = ChangeStoreHouse.class.getSimpleName();

    private EditText storeHouseName;
    private EditText goodsName;
    private Button confirm_button;
    private JSONObject storehouseJson = new JSONObject();
    protected Context mContext = ChangeStoreHouse.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_changestorehouse);
        comm_title.setText(getString(R.string.changeStoreHouse));
        back_main.setVisibility(View.VISIBLE);

        storeHouseName = (EditText)findViewById(R.id.storeHouseName);
        goodsName = (EditText) findViewById(R.id.goodsName);
        confirm_button = (Button) findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String storeString = storeHouseName.getText().toString().trim();
                String goodsString = goodsName.getText().toString().trim();

                if(StringUtils.isNotBlank(storeString) && StringUtils.isNotBlank(goodsString)){
                    ChangeStore(storeString, goodsString);
                }else{
                    ViewUtil.showToast(mContext, "请先完善需要修改的信息。。。");
                }
            }
        });
        //get information from before action
        Intent intent = getIntent();
        if (intent != null) {
            String jsonString = intent.getStringExtra("jsonStorehouse");
            storehouseJson = JSONObject.parseObject(jsonString);
        }

    }

    public void ChangeStore(String storeString,String goodsString) {
        String url = String.format(getString(R.string.storehouse_url), getString(R.string.common_ip));
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.FLAG,ReqCmd.CHANGE_FLAG);
        jsonString.put(ReqCmd.STOREHOUSENAME, storeString);
        jsonString.put(ReqCmd.GOODS, goodsString);
        jsonString.put(ReqCmd.STOREHOUSEID,storehouseJson.getString("storehouseName"));
        SweetAlertDialogUtils.showProgressDialog(this,getString(R.string.waiting), false);

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
//                                    Intent intent = new Intent();
//                                    intent.setClass(mContext, HomeActivity.class);
//                                    startActivity(intent);
                                    finish();

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
