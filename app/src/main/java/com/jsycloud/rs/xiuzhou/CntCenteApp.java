package com.jsycloud.rs.xiuzhou;

import java.io.File;
import java.io.IOException;

import android.app.Application;
import android.os.Environment;

public class CntCenteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        File appDirectory = new File( Environment.getExternalStorageDirectory() + "/秀洲智慧河道日志" );
        File logFile = new File( appDirectory, "logcat" + System.currentTimeMillis() + ".log" );

        if ( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }

        try {
            Process process = Runtime.getRuntime().exec( "logcat -c");
            process = Runtime.getRuntime().exec( "logcat -f " + logFile + " *:E");
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        
    }

}
