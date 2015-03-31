package com.zgy.graduation.graduationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.bean.Storehouse;

import java.util.List;

/**
 * Created by Mr_zhang on 2015/3/31.
 */
public class StorehouseAdapter extends BaseAdapter {

    private List<Storehouse> list;
    private LayoutInflater mInflater;

    private Context mContext;

    public StorehouseAdapter(Context context) {
        super();
        this.mContext = context;

    }

    public void setList(List<Storehouse> list) {
        this.list = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridHolder holder;
        if (convertView != null && convertView.getId() == R.id.grid_itemRL) {
            holder = (GridHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.store_grid_item, null);
            holder = new GridHolder();
            holder.tvAppName = (TextView) convertView.findViewById(R.id.tvMenuTitle);
            convertView.setTag(holder);
        }
        Storehouse info = list.get(position);
        if (info != null) {
            holder.tvAppName.setText(info.getStorehouseTitleResId());
            holder.tvAppName.setCompoundDrawablesWithIntrinsicBounds(null,
                    mContext.getResources().getDrawable(info.getStorehouseImgResId()), null, null);
            /*holder.tvAppName.setCompoundDrawablesWithIntrinsicBounds(null,
					ThumbnailUtils.zoomDrawable(context.getResources().getDrawable(info.getMenuImgResId()), menuWidth, menuWidth), null, null);*/

//			if(info.getMsgCount() > 0){
//				holder.tvMsgCount.setText(String.valueOf(info.getMsgCount()));
//				holder.tvMsgCount.setVisibility(View.VISIBLE);
//			}else{
//			}
        }
        return convertView;
    }

    private class GridHolder {
        TextView tvAppName;
    }

}
