package com.zgy.graduation.graduationproject.util;

/**
 * Created by Mr_zhang on 2015/4/6.
 */
public class ReqCmd {

    /** login result success **/
    public static final int RESULTCODE_SUCCESS = 0;

    //login
    public static final String USERNAME = "username",
                                PASSWORD = "password";

    //仓库
    public static final String STOREHOUSENAME = "storeHouseName",
                               GOODS ="goods",
                               FLAG = "flag";

    public static final String ADD_FLAG = "1",
                               CHANGE_FLAG = "2",
                                DELETE_FLAG = "3",
                                GET_STOREHOUSE_FLAG = "4";

    //
    public static final String REMEMBERCHECKED = "rememberChecked",
           AUTOLOGINCHECKED = "autologinChecked";


}
