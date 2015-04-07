package com.zgy.graduation.graduationproject.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhangguoyu on 2015/4/7.
 */
public class DeviceUtil {
    /**
     * 默认图片缓存地址
     */
    public final static String DEFAULTBASEPATH = "zgy" + File.separator + "nag" + File.separator;
    public final static String DEFAULTIMGBASEPATH = DEFAULTBASEPATH + "image" + File.separator + "caches" + File.separator;

    public static boolean checkSDCard(){
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static String getSDcardDir(){
        if(checkSDCard()){
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        }
        return "";
    }

}
