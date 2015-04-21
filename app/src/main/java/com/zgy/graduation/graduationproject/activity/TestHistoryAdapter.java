package com.zgy.graduation.graduationproject.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.util.ReqCmd;

/**
 * Created by Mr_zhang on 2015/4/21.
 */
public class TestHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private JSONArray mDatas;
    protected int totalCount = 0;

    public TestHistoryAdapter(Context mContext){
        this.mContext = mContext;
        mDatas = new JSONArray();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        if (mDatas.size() <= position)return null;
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem , null);
            holder = new ViewHolder();
            holder.place = (TextView) view.findViewById(R.id.place);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.temperature_goods = (TextView) view.findViewById(R.id.temperature_goods);
            holder.dampness_goods = (TextView) view.findViewById(R.id.dampness_goods);
            holder.pestKind = (TextView) view.findViewById(R.id.pestKind);
            holder.pestNumber = (TextView) view.findViewById(R.id.pestNumber);
            holder.testResult = (TextView) view.findViewById(R.id.testResult);
            view.setTag(holder);
        }

        if(mDatas.size() > 0 && position < mDatas.size()){
            final JSONObject jsonObj = mDatas.getJSONObject(position);
            if (null != jsonObj) {

                holder.place.setText("位置：" + jsonObj.getString(ReqCmd.PLACE));
                holder.time.setText(jsonObj.getString(ReqCmd.TIME));
                holder.temperature_goods.setText("温度：" + jsonObj.getString(ReqCmd.TEMPERATURE));
                holder.dampness_goods.setText("湿度：" + jsonObj.getString(ReqCmd.DAMPNESS));
                holder.pestKind.setText("害虫：" + jsonObj.getString(ReqCmd.PESTKIND));
                holder.pestNumber.setText("数量：" + jsonObj.getString(ReqCmd.PESTNUMBER));
                holder.testResult.setText(jsonObj.getString(ReqCmd.TESTRESULT));

            }
        }


        return view;
    }

    class ViewHolder {
        TextView place;
        TextView time;
        TextView temperature_goods;
        TextView dampness_goods;
        TextView pestKind;
        TextView pestNumber;
        TextView testResult;
    }

    public void addData(JSONObject mData){
        if(mData != null){
            mDatas.add(mData);
            notifyDataSetChanged();
        }
    }

    public void addDatas(JSONArray datas){
        if(datas.size() > 0){
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void clearDatas(){
        if(mDatas.size() > 0){
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
