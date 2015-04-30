package com.zgy.graduation.graduationproject.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.adapter.CommonAdapter;
import com.zgy.graduation.graduationproject.adapter.ViewHolder;
import com.zgy.graduation.graduationproject.util.ReqCmd;

/**
 * Created by Mr_zhang on 2015/4/21.
 * description: adapter for testHistory
 */
public class TestHistoryAdapter extends CommonAdapter {

    protected int totalCount = 0;

    public TestHistoryAdapter(Context mContext){
        super(mContext);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        //common viewHolder
        ViewHolder viewHolder = ViewHolder.get(mContext, view, parent, R.layout.listitem,position);

        if(mDatas.size() > 0 && position < mDatas.size()){
            final JSONObject jsonObj = mDatas.getJSONObject(position);
            if (null != jsonObj) {

                ((TextView)viewHolder.getView(R.id.place)).setText("位置：" + jsonObj.getString(ReqCmd.PLACE));
                ((TextView)viewHolder.getView(R.id.time)).setText(jsonObj.getString(ReqCmd.TIME));
                ((TextView)viewHolder.getView(R.id.temperature_goods)).setText("温度：" + jsonObj.getString(ReqCmd.TEMPERATURE));
                ((TextView)viewHolder.getView(R.id.dampness_goods)).setText("湿度：" + jsonObj.getString(ReqCmd.DAMPNESS));
                ((TextView)viewHolder.getView(R.id.pestKind)).setText("害虫：" + jsonObj.getString(ReqCmd.PESTKIND));
                ((TextView)viewHolder.getView(R.id.pestNumber)).setText("数量：" + jsonObj.getString(ReqCmd.PESTNUMBER));
                ((TextView)viewHolder.getView(R.id.testResult)).setText(jsonObj.getString(ReqCmd.TESTRESULT));

            }
        }
        return viewHolder.getConvertView();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
