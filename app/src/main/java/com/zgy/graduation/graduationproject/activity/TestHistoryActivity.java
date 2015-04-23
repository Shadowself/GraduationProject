package com.zgy.graduation.graduationproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.SweetAlertDialogUtils;
import com.zgy.graduation.graduationproject.util.ViewUtil;

/**
 * Created by Mr_zhang on 2015/4/21.
 */
public class TestHistoryActivity extends BaseActivity {
    private static final String TAG = TestHistoryActivity.class.getSimpleName();
    private ListView historyList;
    private TestHistoryAdapter testHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_testhistory);
        comm_title.setText(R.string.getTestInfoHistory);
        back_main.setVisibility(View.VISIBLE);

        historyList = (ListView) findViewById(R.id.historyList);
        testHistoryAdapter = new TestHistoryAdapter(this);
        historyList.setAdapter(testHistoryAdapter);
        getJsonData();
    }

    public void getJsonData(){
        String url = getString(R.string.testInfo_url);
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.FLAG, ReqCmd.CHANGE_FLAG);
        jsonString.put(ReqCmd.STOREHOUSEID,preferencesUtil.getString(ReqCmd.STOREHOUSEID));
//        showProgressDialog(getString(R.string.waiting), false);
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
                                    JSONArray jsonArray = JSON.parseArray(resData.getData());
                                    testHistoryAdapter.addDatas(jsonArray);
                                    historyList.setAdapter(testHistoryAdapter);

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
