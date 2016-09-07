package com.jsycloud.ir.xiuzhou;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CommonTools {

    public static boolean newVersion(String first, String second) {
        String[] firsts = first.split("\\.");
        String[] seconds = second.split("\\.");
        int firstToNum = Integer.parseInt(firsts[0]) * 100 + Integer.parseInt(firsts[1]) * 10 + Integer.parseInt(firsts[2]);
        int secondToNum = Integer.parseInt(seconds[0]) * 100 + Integer.parseInt(seconds[1]) * 10 + Integer.parseInt(seconds[2]);
        if(firstToNum > secondToNum){
            return true;
        }
        return false;
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getRiverId(String url) {

        String result = "";
        boolean bStart = false;
        for(int i = 0;i<url.length();i++){
            if(url.charAt(i)=='&'){
                break;
            }
            if(bStart){
                result = result + url.charAt(i);
            }
            if(url.charAt(i)=='='){
                bStart = true;
            }
        }
        return result;
    }

    public static int getRiverIndexByRiverId(String riverId) {

        for(int i = 0;i<Constant.allriverList.size();i++){
            if(Constant.allriverList.get(i).getId().equals(riverId)){
                return i;
            }
        }
        return 0;
    }

    public static String getMacAddress(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }

    public static String getVersionNum(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (info != null) {
                return info.versionName;
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static Bitmap decodeBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap == null) {
            System.out.println("bitmap为空");
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        int inSampleSize = 1;
        if (realHeight > 500 || realWidth > 500) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round(realWidth / (float) 500);
            final int widthRatio = Math.round(realHeight / (float) 500);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }
}
