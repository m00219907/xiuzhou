package com.jsycloud.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.company.PlaySDK.IPlaySDK;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.ir.xiuzhou.R;


public class RealPlayfullscreenActivity extends Activity{

    private byte[] m_szCameraId = null;
    private int m_pDLLHandle = 0;
    SurfaceView video_fullscreen_sfview;
    fMediaDataCallback fm;
    private int m_nPort = 0;
    private int m_nSeq = 0;
    private int mTimeOut = 30*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_fullscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        findViewById(R.id.video_fullscreen_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        m_pDLLHandle = AppApplication.get().getDpsdkHandle();
        m_szCameraId = getIntent().getByteArrayExtra("channelId");
        m_nPort = IPlaySDK.PLAYGetFreePort();

        video_fullscreen_sfview = (SurfaceView)findViewById(R.id.video_fullscreen_sfview);
        SurfaceHolder holder = video_fullscreen_sfview.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                IPlaySDK.InitSurface(m_nPort, video_fullscreen_sfview);
                openVideo();
            }
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        fm = new fMediaDataCallback() {

            @Override
            public void invoke(int nPDLLHandle, int nSeq, int nMediaType, byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {
                IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
            }
        };

    }

    public void openVideo() {
        if(!StartRealPlay()){
            return;
        }

        try{
            Return_Value_Info_t retVal = new Return_Value_Info_t();
            Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t();
            System.arraycopy(m_szCameraId, 0, getRealStreamInfo.szCameraId, 0, m_szCameraId.length);
            getRealStreamInfo.nMediaType = 1;
            getRealStreamInfo.nRight = 0;
            getRealStreamInfo.nStreamType = 1;
            getRealStreamInfo.nTransType = 1;
            Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
            IDpsdkCore.DPSDK_GetChannelInfoById(m_pDLLHandle, m_szCameraId, ChannelInfo);
            int ret = IDpsdkCore.DPSDK_GetRealStream(m_pDLLHandle, retVal, getRealStreamInfo, fm, mTimeOut);
            if(ret == 0){
                m_nSeq = retVal.nReturnValue;
            }else{
                StopRealPlay();
            }
        }catch(Exception e){
        }
    }

    public boolean StartRealPlay() {
        if(video_fullscreen_sfview == null) {
            return false;
        }

        boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort,null,0,1500*1024) != 0;
        if(bOpenRet) {
            boolean bPlayRet = IPlaySDK.PLAYPlay(m_nPort, video_fullscreen_sfview) != 0;
            if(bPlayRet) {
                boolean bSuccess = IPlaySDK.PLAYPlaySoundShare(m_nPort) != 0;
                if(!bSuccess) {
                    IPlaySDK.PLAYStop(m_nPort);
                    IPlaySDK.PLAYCloseStream(m_nPort);
                    return false;
                }
            } else {
                IPlaySDK.PLAYCloseStream(m_nPort);
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public void StopRealPlay() {
        try {
            IPlaySDK.PLAYStopSoundShare(m_nPort);
            IPlaySDK.PLAYStop(m_nPort);
            IPlaySDK.PLAYCloseStream(m_nPort);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

