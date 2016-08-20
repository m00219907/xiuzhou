package com.jsycloud.timebar;

public class DensityUtil {
    public static int dip2px(float density, float dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    public static int px2dp(float density, float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }
}
