package com.jsycloud.rs.xiuzhou.videofragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PLAYBACK_INFO;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.PlaybackControlCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;

import org.MediaPlayer.PlayM4.Player;

import java.io.File;


public class TabVideoFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {

    private StartActivity activity;
    EditText video_fragment_ip, video_fragment_port, video_fragment_username, video_fragment_password;
    Button video_fragment_login, video_fragment_preview,  video_fragment_play;
    SurfaceView video_fragment_sfview;

    private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
    private int                m_iLogID                = -1;                // return by NET_DVR_Login_v30
    private int                m_iPlayID                = -1;                // return by NET_DVR_RealPlay_V30
    private int                m_iPlaybackID            = -1;                // return by NET_DVR_PlayBackByTime

    private int                m_iPort                    = -1;                // play port
    private int                m_iStartChan             = 0;                // start channel no
    private int                m_iChanNum                = 0;                //channel number
    private boolean			m_bMultiPlay			= false;
    private static PlaySurfaceView [] playView = new PlaySurfaceView[4];

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, null);
        initeSdk();
        video_fragment_ip = (EditText)view.findViewById(R.id.video_fragment_ip);
        video_fragment_port = (EditText)view.findViewById(R.id.video_fragment_port);
        video_fragment_username = (EditText)view.findViewById(R.id.video_fragment_username);
        video_fragment_password = (EditText)view.findViewById(R.id.video_fragment_password);
        video_fragment_sfview = (SurfaceView)view.findViewById(R.id.video_fragment_sfview);
        video_fragment_sfview.getHolder().addCallback(this);
        video_fragment_login = (Button)view.findViewById(R.id.video_fragment_login);
        video_fragment_login.setOnClickListener(this);
        video_fragment_preview = (Button)view.findViewById(R.id.video_fragment_preview);
        video_fragment_preview.setOnClickListener(this);
        video_fragment_play = (Button)view.findViewById(R.id.video_fragment_play);
        video_fragment_play.setOnClickListener(this);

        return view;
    }

    private boolean initeSdk() {
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            Toast.makeText(activity,"视频组件初始化失败，请重试",Toast.LENGTH_SHORT).show();
            return false;
        }
        File commonFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if(commonFile.exists()){
            commonFile.mkdir();
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, commonFile.getAbsolutePath(), true);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_fragment_login:
                try {
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
                        video_fragment_login.setText("登出");
                        Toast.makeText(activity,"登陆成功！",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
                            return;
                        }
                        video_fragment_login.setText("登陆");
                        m_iLogID = -1;
                    }
                }
                catch (Exception err) {
                }
                break;
            case R.id.video_fragment_preview:
                try
                {
                    //((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                            //hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if(m_iLogID < 0)
                    {
                        Toast.makeText(activity,"请先登陆设备！",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    //if(m_bNeedDecode)
                    {
                        if(m_iChanNum > 1)//preview more than a channel
                        {
                            if(!m_bMultiPlay)
                            {
                                startMultiPreview();
                                m_bMultiPlay = true;
                                video_fragment_preview.setText("Stop");
                            }
                            else
                            {
                                stopMultiPreview();
                                m_bMultiPlay = false;
                                video_fragment_preview.setText("Preview");
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
                                video_fragment_preview.setText("Preview");
                            }
                        }
                    }
                }
                catch (Exception err)
                {
                }
                break;
            case R.id.video_fragment_play:
                try {
                    if(m_iLogID < 0) {
                        Toast.makeText(activity, "请先登陆一个设备", Toast.LENGTH_SHORT);
                        return ;
                    }
                    if(m_iPlaybackID < 0) {
                        if(m_iPlayID >= 0 ) {
                            return;
                        }
                        PlaybackCallBack fPlaybackCallBack = getPlayerbackPlayerCbf();
                        if (fPlaybackCallBack == null) {
                            return;
                        }
                        NET_DVR_TIME struBegin = new NET_DVR_TIME();
                        NET_DVR_TIME struEnd = new NET_DVR_TIME();

                        struBegin.dwYear = 2015;
                        struBegin.dwMonth = 12;
                        struBegin.dwDay = 23;

                        struEnd.dwYear = 2015;
                        struEnd.dwMonth = 12;
                        struEnd.dwDay = 24;

                        m_iPlaybackID = HCNetSDK.getInstance().NET_DVR_PlayBackByTime(m_iLogID, 1, struBegin, struEnd);
                        if(m_iPlaybackID >= 0)
                        {
                            if(!HCNetSDK.getInstance().NET_DVR_SetPlayDataCallBack(m_iPlaybackID, fPlaybackCallBack))
                            {
                                Toast.makeText(activity, "播放失败", Toast.LENGTH_SHORT);
                                return ;
                            }
                            NET_DVR_PLAYBACK_INFO struPlaybackInfo = null ;
                            if(!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(m_iPlaybackID, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, struPlaybackInfo))
                            {
                                return ;
                            }
                            video_fragment_play.setText("Stop");
                            int nProgress = -1;
						/*
						while(true)
					    {
					     	nProgress = HCNetSDK.getInstance().NET_DVR_GetPlayBackPos(m_iPlaybackID);
					       	System.out.println("NET_DVR_GetPlayBackPos:" + nProgress);
					       	if(nProgress < 0 || nProgress >= 100)
					       	{
					       		break;
					       	}
					      	try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					    }
					    */

                        }
                        else {
                        }
                    }
                    else
                    {
                        if(!HCNetSDK.getInstance().NET_DVR_StopPlayBack(m_iPlaybackID)) {
                        }
                        // player stop play
                        stopSinglePlayer();
                        video_fragment_play.setText("录制");
                        m_iPlaybackID = -1;
                    }
                }
                catch (Exception err) {
                }
                break;
            default:
                break;
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
        String strIP = video_fragment_ip.getText().toString();
        int    nPort = Integer.parseInt(video_fragment_port.getText().toString());
        String strUser = video_fragment_username.getText().toString();
        String strPsd = video_fragment_password.getText().toString();
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

    private PlaybackCallBack getPlayerbackPlayerCbf()
    {
        PlaybackCallBack cbf = new PlaybackCallBack()
        {
            @Override
            public void fPlayDataCallBack(int iPlaybackHandle, int iDataType, byte[] pDataBuffer, int iDataSize)
            {
                // player channel 1
                TabVideoFragment.this.processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_FILE);
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
                    Toast.makeText(activity,"getpot 失败了", Toast.LENGTH_SHORT).show();
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
                    if (!Player.getInstance().play(m_iPort, video_fragment_sfview.getHolder()))
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
//		    		Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort));
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
            Toast.makeText(activity,"请先停止录制", Toast.LENGTH_SHORT).show();
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

        video_fragment_preview.setText("Stop");
    }

    private void startMultiPreview()
    {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int i = 0;
        for(i = 0; i < 4; i++)
        {
            if(playView[i] == null)
            {
                playView[i] = new PlaySurfaceView(activity);
                playView[i].setParam(metric.widthPixels);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = playView[i].getCurHeight() - (i/2) * playView[i].getCurHeight();
                params.leftMargin = (i%2) * playView[i].getCurWidth();
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;
                activity.addContentView(playView[i], params);
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
            Toast.makeText(activity,"停止播放失败", Toast.LENGTH_SHORT).show();
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
                TabVideoFragment.this.processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        video_fragment_sfview.getHolder().setFormat(PixelFormat.TRANSLUCENT);
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
