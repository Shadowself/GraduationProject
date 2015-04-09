package com.zgy.graduation.graduationproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.ViewUtil;

/**
 * Created by zhangguoyu on 2015/4/9.
 */
public class AddStorehouseActivity extends BaseActivity {

    private EditText storeHouseName;
    private EditText goodsName;
    private Button confirm_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_addstorehouse);
        comm_title.setText(getString(R.string.add_Storehouse));

        storeHouseName = (EditText)findViewById(R.id.storeHouseName);
        goodsName = (EditText) findViewById(R.id.goodsName);
        confirm_button = (Button) findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStoreHouse();
            }
        });
    }

    public void AddStoreHouse() {
        String url = getString(R.string.storehouse_url);
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.STOREHOUSENAME, "1号仓库");
        jsonString.put(ReqCmd.GOODS, "水稻");
        showProgressDialog(getString(R.string.waiting), false);

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
//                                    finish();

                                    break;
                                default:
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    break;
                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFail() {
                        ViewUtil.showToast(mContext, getString(R.string.server_error));
                    }

                    @Override
                    public void onFinish() {
                        closeProgressDialog();
                    }

                }
        );
    }


}
