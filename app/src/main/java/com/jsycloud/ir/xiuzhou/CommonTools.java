package com.jsycloud.ir.xiuzhou;


import android.content.Context;
import android.graphics.Bitmap;
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
}
