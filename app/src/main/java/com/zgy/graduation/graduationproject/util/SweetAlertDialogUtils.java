package com.zgy.graduation.graduationproject.util;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by zhangguoyu on 2015/4/14.
 */
public class SweetAlertDialogUtils {

    private static SweetAlertDialog pDialog;

    public static void showProgressDialog(Context mContext,String message, boolean canBack) {
        closeProgressDialog();
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(canBack);
        pDialog.show();
    }

    public static void closeProgressDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    public static void showErrorDialog(Context mContext,String message){
        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("")
                .setContentText(message)
                .setConfirmText("OK")
                .showCancelButton(false)
                .setCancelClickListener(null)
                .setConfirmClickListener(null).show();
    }


}
