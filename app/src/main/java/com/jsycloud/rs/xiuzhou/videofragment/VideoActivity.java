package com.jsycloud.rs.xiuzhou.videofragment;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.RealPlayCallBack;
import com.jsycloud.rs.xiuzhou.R;

import org.MediaPlayer.PlayM4.Player;

import java.io.File;

public class VideoActivity extends Activity implements SurfaceHolder.Callback{

    SurfaceView video_activity_sfview;

    private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
    private int                m_iLogID                = -1;                // return by NET_DVR_Login_v30
    private int                m_iPlayID                = -1;                // return by NET_DVR_RealPlay_V30
    private int                m_iPlaybackID            = -1;                // return by NET_DVR_PlayBackByTime

    private int                m_iPort                    = -1;                // play port
    private int                m_iStartChan             = 0;                // start channel no
    private int                m_iChanNum                = 0;                //channel number
    private boolean           m_bMultiPlay              = false;
    private static PlaySurfaceView [] playView = new PlaySurfaceView[4];

    private Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1006:
                    login();
                    handler.sendEmptyMessageDelayed(1007, 500);
                    break;
                case 1007:
                    preview();
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_activity);

        video_activity_sfview = (SurfaceView)findViewById(R.id.video_activity_sfview);
        findViewById(R.id.video_activity_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.this.finish();
            }
        });
        initeSdk();
        handler.sendEmptyMessageDelayed(1006, 500);
    }

    private boolean initeSdk() {
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            Toast.makeText(this,"视频组件初始化失败，请重试", Toast.LENGTH_SHORT).show();
            return false;
        }
        File commonFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if(commonFile.exists()){
            commonFile.mkdir();
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, commonFile.getAbsolutePath(), true);
        return true;
    }

    private void login() {
        if(m_iLogID < 0) {
            m_iLogID = loginDevice();
            if (m_iLogID < 0) {
                return;
            }
            // get instance of exception callback and set
            ExceptionCallBack oexceptionCbf = getExceptiongCbf();
            if (oexceptionCbf == null) {
                return ;
            }
            if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf)) {
                return;
            }
        }
    }

    private int loginDevice()
    {
        // get instance
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30)
        {
            return -1;
        }
        String strIP = "122.225.61.100";
        int    nPort = 8000;
        String strUser = "admin";
        String strPsd = "jsy2016.";
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort, strUser, strPsd, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0)
        {
            return -1;
        }
        if(m_oNetDvrDeviceInfoV30.byChanNum > 0)
        {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        }
        else if(m_oNetDvrDeviceInfoV30.byIPChanNum > 0)
        {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }

        return iLogID;
    }

    private ExceptionCallBack getExceptiongCbf(){
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack()
        {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                System.out.println("recv exception, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    private void preview() {
        if(m_iChanNum > 1)//preview more than a channel
        {
            if(!m_bMultiPlay)
            {
                startMultiPreview();
                m_bMultiPlay = true;
            }
            else
            {
                stopMultiPreview();
                m_bMultiPlay = false;
            }
        }
        else    //preivew a channel
        {
            if(m_iPlayID < 0)
            {
                startSinglePreview();
            }
            else
            {
                stopSinglePreview();
            }
        }
    }

    private PlaybackCallBack getPlayerbackPlayerCbf()
    {
        PlaybackCallBack cbf = new PlaybackCallBack()
        {
            @Override
            public void fPlayDataCallBack(int iPlaybackHandle, int iDataType, byte[] pDataBuffer, int iDataSize)
            {
                // player channel 1
                VideoActivity.this.processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_FILE);
            }
        };
        return cbf;
    }

    public void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode)
    {
        //if(!m_bNeedDecode)
        //{
        //   Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" + iDataType + ",iDataSize:" + iDataSize);
        //}
        //else
        {
            if(HCNetSDK.NET_DVR_SYSHEAD == iDataType)
            {
                if(m_iPort >= 0)
                {
                    return;
                }
                m_iPort = Player.getInstance().getPort();
                if(m_iPort == -1)
                {
                    Toast.makeText(this,"getpot 失败了", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (iDataSize > 0)
                {
                    if (!Player.getInstance().setStreamOpenMode(m_iPort, iStreamMode))  //set stream mode
                    {
                        return;
                    }
                    if (!Player.getInstance().openStream(m_iPort, pDataBuffer, iDataSize, 2*1024*1024)) //open stream
                    {
                        return;
                    }
                    if (!Player.getInstance().play(m_iPort, video_activity_sfview.getHolder()))
                    {
                        return;
                    }
                    if(!Player.getInstance().playSound(m_iPort))
                    {
                        return;
                    }
                }
            }
            else
            {
                if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize))
                {
//                    Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort));
                    for(int i = 0; i < 4000 && m_iPlaybackID >=0 ; i++)
                    {
                        if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize))
                            Log.e("mahezhen", "inputData failed with: " + Player.getInstance().getLastError(m_iPort));
                        else
                            break;
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }
                    }
                }

            }
        }

    }

    private void startSinglePreview()
    {
        if(m_iPlaybackID >= 0)
        {
            Toast.makeText(this,"请先停止录制", Toast.LENGTH_SHORT).show();
            return ;
        }
        RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
        if (fRealDataCallBack == null)
        {
            return ;
        }

        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
        previewInfo.lChannel = m_iStartChan;
        previewInfo.dwStreamType = 1; //substream
        previewInfo.bBlocked = 1;
        // HCNetSDK start preview
        m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(m_iLogID, previewInfo, fRealDataCallBack);
        if (m_iPlayID < 0)
        {
            return ;
        }
    }

    private void startMultiPreview()
    {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int i = 0;
        for(i = 0; i < 4; i++)
        {
            if(playView[i] == null)
            {
                playView[i] = new PlaySurfaceView(this);
                playView[i].setParam(metric.widthPixels);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = playView[i].getCurHeight() - (i/2) * playView[i].getCurHeight();
                params.leftMargin = (i%2) * playView[i].getCurWidth();
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;
                addContentView(playView[i], params);
            }
            playView[i].startPreview(m_iLogID, m_iStartChan + i);
        }
        m_iPlayID = playView[0].m_iPreviewHandle;
    }

    private void stopMultiPreview()
    {
        int i = 0;
        for(i = 0; i < 4;i++)
        {
            playView[i].stopPreview();
        }
        m_iPlayID = -1;
    }

    private void stopSinglePlayer()
    {
        Player.getInstance().stopSound();
        // player stop play
        if (!Player.getInstance().stop(m_iPort))
        {
            return;
        }

        if(!Player.getInstance().closeStream(m_iPort))
        {
            return;
        }
        if(!Player.getInstance().freePort(m_iPort))
        {
            return;
        }
        m_iPort = -1;
    }

    private void stopSinglePreview()
    {
        if ( m_iPlayID < 0)
        {
            return;
        }

        //  net sdk stop preview
        if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID))
        {
            Toast.makeText(this,"停止播放失败", Toast.LENGTH_SHORT).show();
            return;
        }

        m_iPlayID = -1;
        stopSinglePlayer();
    }

    private RealPlayCallBack getRealPlayerCbf()
    {
        RealPlayCallBack cbf = new RealPlayCallBack()
        {
            public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize)
            {
                // player channel 1
                VideoActivity.this.processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        video_activity_sfview.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        if (-1 == m_iPort) {
            return;
        }
        Surface surface = holder.getSurface();
        if (surface.isValid()) {
            Player.getInstance().setVideoWindow(m_iPort, 0, holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (-1 == m_iPort) {
            return;
        }
        if (holder.getSurface().isValid()) {
            Player.getInstance().setVideoWindow(m_iPort, 0, null);
        }
    }
}
