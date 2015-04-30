package com.zgy.graduation.graduationproject.bean;

import java.io.Serializable;

/**
 * Created by Mr_zhang on 2015/3/31.
 * description:仓库
 */
public class Storehouse implements Serializable {

    private int storehouseImgResId;
    private String storehouseTitleResId;
    private int id;
    private String goods;

    public int getStorehouseImgResId() {
        return storehouseImgResId;
    }

    public void setStorehouseImgResId(int storehouseImgResId) {
        this.storehouseImgResId = storehouseImgResId;
    }

    public String getStorehouseTitleResId() {
        return storehouseTitleResId;
    }

    public void setStorehouseTitleResId(String storehouseTitleResId) {
        this.storehouseTitleResId = storehouseTitleResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }
}
