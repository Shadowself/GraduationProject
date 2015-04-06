package com.zgy.graduation.graduationproject.bean;

/**
 * Created by Mr_zhang on 2015/4/6.
 */

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 描述：产品回复数据封装类
 * 创建人：李满义
 * 创建时间：2014-6-6 下午6:03:18
 * 修改人：
 * 修改时间：
 */
public class ResData implements Serializable {
    private static final long serialVersionUID = -1894466547675744738L;
    private int code_;
    private String data;
    private String message_;
    private boolean result_;

    public int getCode_() {
        return code_;
    }

    public void setCode_(int code_) {
        this.code_ = code_;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage_() {
        return message_;
    }

    public void setMessage_(String message_) {
        this.message_ = message_;
    }

    public boolean getResult_() {
        return result_;
    }

    public void setResult_(boolean result_) {
        this.result_ = result_;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
