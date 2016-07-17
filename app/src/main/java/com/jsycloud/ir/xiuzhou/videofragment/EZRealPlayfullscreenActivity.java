package com.jsycloud.ir.xiuzhou.videofragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;
import com.videogo.constant.IntentConsts;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZCameraInfo;

import java.io.FileOutputStream;


public class EZRealPlayfullscreenActivity  extends Activity implements SurfaceHolder.Callback{

    EZOpenSDK mEZOpenSDK = null;
    EZPlayer mEZPlayer;
    SurfaceHolder mRealPlaySh;
    EZCameraInfo mCameraInfo;
    SurfaceView video_fullscreen_sfview;
    TextView video_activity_record;
    FileOutputStream mOs;
    EZConstants.EZPTZCommand curCommand;

    private Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1005:
                    if(mEZPlayer!=null) {
                        mEZPlayer.setSurfaceHold(mRealPlaySh);
                        mEZPlayer.startRealPlay();
                    }
                    break;

                default:
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_fullscreen);
        video_fullscreen_sfview = (SurfaceView)findViewById(R.id.video_fullscreen_sfview);
        video_fullscreen_sfview.getHolder().addCallback(this);

        findViewById(R.id.video_fullscreen_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EZRealPlayfullscreenActivity.this, EZRealPlayActivity.class);
                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, mCameraInfo);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            mCameraInfo = (EZCameraInfo) intent.getParcelableExtra(IntentConsts.EXTRA_CAMERA_INFO);
            /*mRtspUrl = intent.getStringExtra(IntentConsts.EXTRA_RTSP_URL);
            mCurrentQulityMode = (mCameraInfo.getVideoLevel() == 0 ? EZVideoLevel.VIDEO_LEVEL_FLUNET :
                    (mCameraInfo.getVideoLevel() == 1) ? EZVideoLevel.VIDEO_LEVEL_BALANCED :
                            EZVideoLevel.VIDEO_LEVEL_HD);*/
        }

        mEZOpenSDK = EZOpenSDK.getInstance();
        mEZPlayer = mEZOpenSDK.createPlayer(this, mCameraInfo.getCameraId());
        mEZPlayer.setHandler(mHandler);
        mHandler.sendEmptyMessageDelayed(1005, 500);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            Intent intent = new Intent(this, EZRealPlayActivity.class);
            intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, mCameraInfo);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(holder);
        }
        mRealPlaySh = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

}

