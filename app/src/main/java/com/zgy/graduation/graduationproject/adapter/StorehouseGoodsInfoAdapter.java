package com.zgy.graduation.graduationproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.util.ReqCmd;

/**
 * Created by Mr_zhang on 2015/5/12.
 */
public class StorehouseGoodsInfoAdapter extends CommonAdapter {

    public StorehouseGoodsInfoAdapter(Context mContext){
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.goodsinfo_item,position);
        if(mDatas.size() > 0 && position < mDatas.size()){
            final JSONObject jsonObj = mDatas.getJSONObject(position);
//            if (null != jsonObj) {
//                ((TextView)viewHolder.getView(R.id.goods_temperature)).setText(jsonObj.getString(ReqCmd.TEMPERATURE));
//            }
            if(null != jsonObj){
                String temperature = jsonObj.getString(ReqCmd.TEMPERATURE);
                if(Integer.valueOf(temperature) < 25){
                    ((LinearLayout)viewHolder.getView(R.id.grid_itemLayout)).setBackgroundResource(R.drawable.grayredselector);
                }else if(Integer.valueOf(temperature) < 30 && Integer.valueOf(temperature) >= 25){
                    ((LinearLayout)viewHolder.getView(R.id.grid_itemLayout)).setBackgroundResource(R.drawable.red1selector);
                }else if(Integer.valueOf(temperature) >= 30 && Integer.valueOf(temperature) <40){
                    ((LinearLayout)viewHolder.getView(R.id.grid_itemLayout)).setBackgroundResource(R.drawable.red2selector);
                }else{
                    ((LinearLayout)viewHolder.getView(R.id.grid_itemLayout)).setBackgroundResource(R.drawable.redselector);
                }
            }
        }
        ((TextView)viewHolder.getView(R.id.goods_temperature)).setText("检测点" + String.valueOf(position+1));

        return viewHolder.getConvertView();
    }
}
