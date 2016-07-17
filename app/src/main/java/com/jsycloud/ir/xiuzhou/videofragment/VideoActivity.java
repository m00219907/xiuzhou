package com.jsycloud.ir.xiuzhou.videofragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.jsycloud.ir.xiuzhou.R;

public class VideoActivity extends Activity{

    SurfaceView video_activity_sfview;

    private int                m_iLogID                = -1;                // return by NET_DVR_Login_v30
    private int                m_iPlayID                = -1;                // return by NET_DVR_RealPlay_V30
    private int                m_iPlaybackID            = -1;                // return by NET_DVR_PlayBackByTime

    private int                m_iPort                    = -1;                // play port
    private int                m_iStartChan             = 0;                // start channel no
    private int                m_iChanNum                = 0;                //channel number
    private boolean           m_bMultiPlay              = false;
    private static PlaySurfaceView [] playView = new PlaySurfaceView[4];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_activity);
    }

}
