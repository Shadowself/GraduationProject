package com.zgy.graduation.graduationproject.activity;

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

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Mr_zhang on 2015/4/19.
 * description: get information such as temperature_goods and so on from Equipment;
 */
public class getInfoFromTestActivity extends BaseActivity {
    private static final String TAG = getInfoFromTestActivity.class.getSimpleName();
    private EditText place;
    private EditText temperature_goods;
    private EditText dampness_goods;
    private EditText pestKind;
    private EditText pestNumber;

    private Button pastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_getinfor);
        comm_title.setText(getString(R.string.getStorehouseInfo));
        back_main.setVisibility(View.VISIBLE);

        place = (EditText) findViewById(R.id.place);
        temperature_goods = (EditText) findViewById(R.id.temperature_goods);
        dampness_goods = (EditText) findViewById(R.id.dampness_goods);
        pestKind = (EditText) findViewById(R.id.pestKind);
        pestNumber = (EditText) findViewById(R.id.pestNumber);

        pastButton = (Button)findViewById(R.id.pastButton);
        pastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeText = place.getText().toString().trim();
                String temperatureText = temperature_goods.getText().toString().trim();
                String dampnessText = dampness_goods.getText().toString().trim();
                String pestKindText = pestKind.getText().toString().trim();
                String pestNumberText = pestNumber.getText().toString().trim();

                if(checkText(placeText,temperatureText,dampnessText,pestKindText,pestNumberText)){
                    getTestInfo(placeText,temperatureText,dampnessText,pestKindText,pestNumberText);
                }else{
                    ViewUtil.showToast(getApplicationContext(),getString(R.string.testInfo_empty));
                }

            }
        });

    }

    /**
     * description: check Text not empty
     * @param placeText
     * @param temperatureText
     * @param dampnessText
     * @param pestKindText
     * @param pestNumberText
     * @return
     */
    public boolean checkText(String placeText,String temperatureText,String dampnessText,String pestKindText,String pestNumberText){
        boolean flag= false;
        if(StringUtils.isNotBlank(placeText) && StringUtils.isNotBlank(temperatureText) && StringUtils.isNotBlank(dampnessText)
                && StringUtils.isNotBlank(pestKindText) && StringUtils.isNotBlank(pestNumberText)){
            flag = true;
        }
        return flag;
    }

    public void getTestInfo(String placeText,String temperatureText,String dampnessText,String pestKindText,String pestNumberText){
        String url = String.format(getString(R.string.testInfo_url),getString(R.string.common_ip));
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.FLAG, ReqCmd.ADD_FLAG);
        jsonString.put(ReqCmd.STOREHOUSEID,preferencesUtil.getString(ReqCmd.STOREHOUSEID));
        jsonString.put(ReqCmd.PLACE,placeText);
        jsonString.put(ReqCmd.TEMPERATURE,temperatureText);
        jsonString.put(ReqCmd.DAMPNESS,dampnessText);
        jsonString.put(ReqCmd.PESTKIND,pestKindText);
        jsonString.put(ReqCmd.PESTNUMBER,pestNumberText);
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
                                    new SweetAlertDialog(mContext)
                                            .setTitleText(getString(R.string.getStorehouseInfo))
                                            .setContentText(resData.getMessage_())
                                            .show();
                                    break;
                                default:
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG,e.toString());
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
