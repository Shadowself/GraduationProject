package com.zgy.graduation.graduationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.SweetAlertDialogUtils;
import com.zgy.graduation.graduationproject.util.ViewUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by zhangguoyu on 2015/4/2.
 */
public class StorehouseActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = StorehouseActivity.class.getSimpleName();
    private Button postPicture;
    private Button deleteButton;
    private Button changeButton;
    private Button getInfoFromTest;
    private Button getTestHistory;
    private JSONObject storehouseJson = new JSONObject();
    private SweetAlertDialog sweetAlertDialog;

    private TextView storehouse_name_text;
    private TextView storehouse_goods_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_storehouse);
        comm_title.setText(R.string.storehouse_info);
        back_main.setVisibility(View.VISIBLE);

        postPicture = (Button) findViewById(R.id.findPicture_from_server);
        postPicture.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteStore);
        deleteButton.setOnClickListener(this);

        changeButton = (Button) findViewById(R.id.changeStore);
        changeButton.setOnClickListener(this);

        getInfoFromTest = (Button) findViewById(R.id.getInfoFromTest);
        getInfoFromTest.setOnClickListener(this);

        getTestHistory = (Button) findViewById(R.id.getTestHistory);
        getTestHistory.setOnClickListener(this);

        storehouse_name_text = (TextView) findViewById(R.id.storehouse_name_text);
        storehouse_goods_text = (TextView) findViewById(R.id.storehouse_goods_text);

        Intent intent = getIntent();
        if (intent != null) {
            String jsonString = intent.getStringExtra("jsonStorehouse");
            storehouseJson = JSONObject.parseObject(jsonString);
            storehouse_name_text.setText(storehouseJson.getString("storehouseName"));
            storehouse_goods_text.setText(storehouseJson.getString("goods"));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findPicture_from_server:
                Intent intent = new Intent();
                intent.setClass(this, findPestKindActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_in_activity, R.anim.alpha_out_activity);
                break;

            case R.id.deleteStore:

                sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText(mContext.getString(R.string.delete_true))
//                .setContentText("Won't be able to recover this file!")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                deleteStore();
                            }
                        })
                        .show();

                break;

            case R.id.changeStore:

                Intent changeIntent = new Intent();
                changeIntent.setClass(this, ChangeStoreHouse.class);
                changeIntent.putExtra("jsonStorehouse",storehouseJson.toJSONString());
                startActivity(changeIntent);
                break;
            case R.id.getInfoFromTest:
                Intent intent1 = new Intent();
                intent1.setClass(this, getInfoFromTestActivity.class);
//                intent1.putExtra("jsonStorehouse",storehouseJson.toJSONString());
                startActivity(intent1);
                break;

            case R.id.getTestHistory:
                Intent intent2 = new Intent();
                intent2.setClass(this, TestHistoryActivity.class);
                startActivity(intent2);
                break;
        }
    }

    public void deleteStore() {
        String url = getString(R.string.storehouse_url);
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.FLAG, ReqCmd.DELETE_FLAG);
        jsonString.put(ReqCmd.STOREHOUSEID, storehouseJson.getString("storehouseName"));
//        showProgressDialog(getString(R.string.waiting), false);
//        SweetAlertDialogUtils.showProgressDialog(this,getString(R.string.waiting), false);

        HttpAsyncTaskManager httpAsyncTaskManager = new HttpAsyncTaskManager(mContext);
        httpAsyncTaskManager.requestStream(url, jsonString.toJSONString(), new StringTaskHandler() {
                    @Override
                    public void onNetError() {
//                        ViewUtil.showToast(mContext, getString(R.string.network_error));
                        sweetAlertDialog.dismiss();
                        SweetAlertDialogUtils.showErrorDialog(mContext,getString(R.string.network_error));
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
                                    sweetAlertDialog.dismiss();
                                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getString(R.string.delete_success))
//                                            .setContentText("Your imaginary file has been deleted!")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null).show();
                                    finish();

                                    break;
                                default:
                                    sweetAlertDialog.dismiss();
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                    }

                    @Override
                    public void onFail() {
//                        ViewUtil.showToast(mContext, getString(R.string.server_error));
                        sweetAlertDialog.dismiss();
                        SweetAlertDialogUtils.showErrorDialog(mContext,getString(R.string.server_error));
                    }

                    @Override
                    public void onFinish() {
//                        closeProgressDialog();
//                        SweetAlertDialogUtils.closeProgressDialog();
                    }

                }
        );
    }

}
