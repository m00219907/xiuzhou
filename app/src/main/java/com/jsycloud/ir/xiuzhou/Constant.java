package com.jsycloud.ir.xiuzhou;


import android.os.Environment;

import com.amap.api.location.AMapLocation;
import com.jsycloud.ir.xiuzhou.mapfragment.riverInfo;

import java.util.ArrayList;

public class Constant {
    
    public static boolean isLogin = false;
    public static boolean isLogByCode = false;
    public static boolean bReloadUrl = false;

    public static String userid = "";
    public static String username = "";
    public static String userfullname = "";
    public static String usermobile = "";
    public static String usergroup = "";
    public static String videorights = "";
    public static  AMapLocation curLocation = null;
    public static String accessToken="";

    public static String selectRiverId ="";

    public static ArrayList<riverInfo> allriverList = new ArrayList<riverInfo>();

    public static String appFolder =  Environment.getExternalStorageDirectory().getPath() + "/jsycloud/iriverxz";
}
