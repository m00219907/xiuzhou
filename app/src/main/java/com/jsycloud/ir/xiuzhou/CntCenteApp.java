package com.jsycloud.ir.xiuzhou;

import android.app.Application;

import com.videogo.constant.Config;
import com.videogo.openapi.EZOpenSDK;

import cn.jpush.android.api.JPushInterface;

public class CntCenteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Config.LOGGING = true;
        EZOpenSDK.initLib(this, "b381f351e99047b89e36676f80d52b0f", "");

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        /*File appDirectory = new File( Environment.getExternalStorageDirectory() + "/秀洲智慧河道日志" );
        File logFile = new File( appDirectory, "logcat" + System.currentTimeMillis() + ".log" );

        if ( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }

        try {
            Process process = Runtime.getRuntime().exec( "logcat -c");
            process = Runtime.getRuntime().exec( "logcat -f " + logFile + " *:E");
        } catch ( IOException e ) {
            e.printStackTrace();
        }*/
        
    }

}
