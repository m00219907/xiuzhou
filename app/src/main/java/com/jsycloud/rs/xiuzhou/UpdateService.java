package com.jsycloud.rs.xiuzhou;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class UpdateService implements Runnable {

    private long curdwnId;
    Activity activity;
    private String downloadUrl;

    private DownloadComplete onComplete;

    public UpdateService(){}

    public void startDownload(Activity activity, String downloadUrl){
        this.activity = activity;
        this.downloadUrl = downloadUrl;
        onComplete = new DownloadComplete();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        activity.registerReceiver(onComplete, filter);

        new Thread(UpdateService.this).start();
    }

    @Override
    public void run() {
        Uri nowuri = Uri.parse(downloadUrl);

        DownloadManager.Request req = new DownloadManager.Request(nowuri);

        req.setAllowedOverRoaming(false)
           .setDescription("秀洲智慧河道.apk")
           .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
           .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "秀洲智慧河道.apk");

        DownloadManager mgr = (DownloadManager)activity.getSystemService(Context.DOWNLOAD_SERVICE);
        curdwnId = mgr.enqueue(req);
    }

    private class DownloadComplete extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Long dwnId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            if(dwnId == curdwnId) {

                File installFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "//秀洲智慧河道.apk");
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installIntent.setAction(android.content.Intent.ACTION_VIEW);
                installIntent.setDataAndType(Uri.fromFile(installFile), "application/vnd.android.package-archive");
                context.startActivity(installIntent);
            }
        }
    }
}
