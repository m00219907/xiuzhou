/* 
 * @ProjectName iVMS-5060_V3.0
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName ScreenInfoUtil.java
 * @Description 这里对文件进行描述
 * 
 * @author mlianghua
 * @data Jul 13, 2012
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.jsycloud.timebar;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 在此对类做相应的描述
 * 
 * @author mlianghua
 * @Data Jul 13, 2012
 */
public class ScreenInfoUtil {
    private static String TAG            = "ScreenInfoUtil";
    private static float  mScreenDensity = 0;
    private static float  mScreenHight   = 0;
    private static float  mScreenWidth   = 0;

    private ScreenInfoUtil() {
    };

    private static void initilize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        mScreenDensity = dm.density;
        mScreenHight = dm.heightPixels;
        mScreenWidth = dm.widthPixels;
    }

    /**
     * 这里对方法做描述
     * 
     * @param context 上下文
     * @return screen density
     * @since V1.0
     */
    public static float getScreenDensity(Context context) {
        initilize(context);
        return mScreenDensity;
    }

    /**
     * 这里对方法做描述
     * 
     * @param context 上下文
     * @return screen hight
     * @since V1.0
     */
    public static float getScreenHight(Context context) {
        initilize(context);
        return mScreenHight;
    }

    /**
     * 这里对方法做描述
     * 
     * @param context 上下文
     * @return screen width
     * @since V1.0
     */
    public static float getScreenWidth(Context context) {
        initilize(context);
        return mScreenWidth;
    }
}
