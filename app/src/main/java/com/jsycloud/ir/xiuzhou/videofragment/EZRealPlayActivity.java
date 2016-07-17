/* 
 * @ProjectName VideoGo
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName RealPlayActivity.java
 * @Description 这里对文件进行描述
 * 
 * @author chenxingyf1
 * @data 2014-6-11
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.jsycloud.ir.xiuzhou.videofragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hik.streamconvert.StreamConvertCB;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.R;
import com.videogo.constant.IntentConsts;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZCameraInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 实时预览2.7
 *
 * @author xiaxingsuo
 * @data 2015-11-11
 */
public class EZRealPlayActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback{

    EZOpenSDK mEZOpenSDK = null;
    EZPlayer mEZPlayer;
    SurfaceHolder mRealPlaySh;
    EZCameraInfo mCameraInfo;
    SurfaceView video_activity_sfview;
    ImageView video_activity_capture, video_activity_record;
    ImageView video_activity_up, video_activity_left, video_activity_right, video_activity_down;
    View video_activity_level, video_activity_videolevel, video_activity_fullscreen, video_activity_layout1, video_activity_layout2;
    TextView video_activity_recordtext;
    FileOutputStream mOs;

    private Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1006:
                    Toast.makeText(EZRealPlayActivity.this, "截图已经保存到" + Environment.getExternalStorageDirectory(),Toast.LENGTH_SHORT).show();
                    break;
                case 1007:
                    Toast.makeText(EZRealPlayActivity.this, "正在录制，视频保存在" + Environment.getExternalStorageDirectory(),Toast.LENGTH_SHORT).show();
                    break;
                case 1008:
                    mEZPlayer.stopRealPlay();
                    SystemClock.sleep(500);
                    mEZPlayer = mEZOpenSDK.createPlayer(EZRealPlayActivity.this, mCameraInfo.getCameraId());
                    mEZPlayer.setHandler(mHandler);
                    mEZPlayer.setSurfaceHold(mRealPlaySh);
                    mEZPlayer.startRealPlay();
                    break;
                case 1009:
                    Toast.makeText(EZRealPlayActivity.this, "设置清晰度失败..",Toast.LENGTH_SHORT).show();
                    break;
                case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                    video_activity_level.setVisibility(View.VISIBLE);
                    video_activity_fullscreen.setVisibility(View.VISIBLE);
                    video_activity_layout1.setVisibility(View.VISIBLE);
                    if(Constant.isLogin && !Constant.isLogByCode && Constant.videorights.equals("1")) {
                        video_activity_layout2.setVisibility(View.VISIBLE);
                    }else{
                        video_activity_layout2.setVisibility(View.GONE);
                    }
                    break;
                case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
                    Toast.makeText(EZRealPlayActivity.this,"播放失败，请重试...", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.video_activity);
        video_activity_sfview = (SurfaceView)findViewById(R.id.video_activity_sfview);
        video_activity_sfview.getHolder().addCallback(this);

        video_activity_capture = (ImageView)findViewById(R.id.video_activity_capture);
        video_activity_capture.setTag("0");
        video_activity_capture.setOnClickListener(this);
        video_activity_record = (ImageView)findViewById(R.id.video_activity_record);
        video_activity_record.setTag("0");
        video_activity_record.setOnClickListener(this);

        video_activity_up = (ImageView)findViewById(R.id.video_activity_up);
        video_activity_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    video_activity_up.setImageResource(R.drawable.top_normal);
                    mEZOpenSDK.controlPTZ(mCameraInfo.getCameraId(), EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP, 2);
                }else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    video_activity_up.setImageResource(R.drawable.top_highlighted);
                    mEZOpenSDK.controlPTZ(mCameraInfo.getCameraId(), EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTART, 2);
                }
                return true;
            }
        });
        video_activity_left = (ImageView)findViewById(R.id.video_activity_left);
        video_activity_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    video_activity_left.setImageResource(R.drawable.left_normal);
                    mEZOpenSDK.controlPTZ(mCameraInfo.getCameraId(), EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP, 2);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    video_activity_left.setImageResource(R.drawable.left_highlighted);
                    mEZOpenSDK.controlPTZ(mCameraInfo.getCameraId(), EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTART, 2);
                }
                return true;
            }
        });
        video_activity_right = (ImageView)findViewById(R.id.video_activity_right);
        video_activity_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    video_activity_right.setImageResource(R.drawable.right_normal);
                    mEZOpenSDK.controlPTZ(mCameraInfo.getCameraId(), EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP, 2);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    video_activity_right.setImageResource(R.drawable.right_highlighted);
                    mEZOpenSDK.controlPTZ(mCameraInfo.getCameraId(), EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTART, 2);
                }
                return true;
            }
        });
        video_activity_down = (ImageView)findViewById(R.id.video_activity_down);
        video_activity_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    video_activity_down.setImageResource(R.drawable.bottom_normal);
                    mEZOpenSDK.controlPTZ(mCameraInfo.getCameraId(), EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP, 2);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    video_activity_down.setImageResource(R.drawable.bottom_highlighted);
                    mEZOpenSDK.controlPTZ(mCameraInfo.getCameraId(), EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTART, 2);
                }
                return true;
            }
        });
        video_activity_level = findViewById(R.id.video_activity_level);
        video_activity_level.setOnClickListener(this);
        video_activity_videolevel = findViewById(R.id.video_activity_videolevel);
        video_activity_fullscreen = findViewById(R.id.video_activity_fullscreen);
        video_activity_fullscreen.setOnClickListener(this);
        video_activity_layout1 = findViewById(R.id.video_activity_layout1);
        video_activity_layout2 = findViewById(R.id.video_activity_layout2);

        findViewById(R.id.video_activity_back).setOnClickListener(this);
        findViewById(R.id.video_activity_hd).setOnClickListener(this);
        findViewById(R.id.video_activity_balanced).setOnClickListener(this);
        findViewById(R.id.video_activity_flunet).setOnClickListener(this);

        video_activity_recordtext = (TextView)findViewById(R.id.video_activity_recordtext);

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
        mEZPlayer.setSurfaceHold(mRealPlaySh);
        mEZPlayer.startRealPlay();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_activity_back:
                finish();
                break;
            case R.id.video_activity_capture:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bmp = mEZPlayer.capturePicture();
                        FileOutputStream fos = null;
                        if(bmp != null) {
                            try {
                                fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/video_snapshots.jpg");
                                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                bmp.recycle();
                                bmp = null;
                                mHandler.sendEmptyMessage(1006);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } finally {
                                if(fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }).start();
                break;
            case R.id.video_activity_record:
                if(video_activity_record.getTag().equals("0")){
                    mEZPlayer.startLocalRecord(mLocalRecordCb);
                    video_activity_recordtext.setText("停止录制");
                    video_activity_record.setTag("1");
                }else{
                    mEZPlayer.stopLocalRecord();
                    video_activity_recordtext.setText("录制");
                    video_activity_record.setTag("0");
                }
                break;
            case R.id.video_activity_hd:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mEZPlayer.setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_HD);
                            Message msg = Message.obtain();
                            msg.what = 1008;
                            mHandler.sendMessage(msg);
                        }catch (BaseException e){
                            e.printStackTrace();
                            Message msg = Message.obtain();
                            msg.what = 1009;
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();
                video_activity_videolevel.setVisibility(View.GONE);
                TextView textViewhd = (TextView)findViewById(R.id.video_activity_level);
                textViewhd.setText("高清");
                break;
            case R.id.video_activity_balanced:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mEZPlayer.setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED);
                            Message msg = Message.obtain();
                            msg.what = 1008;
                            mHandler.sendMessage(msg);
                        }catch (BaseException e){
                            e.printStackTrace();
                            Message msg = Message.obtain();
                            msg.what = 1009;
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();
                video_activity_videolevel.setVisibility(View.GONE);
                TextView textViewbalanced = (TextView)findViewById(R.id.video_activity_level);
                textViewbalanced.setText("平衡");
                break;
            case R.id.video_activity_flunet:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mEZPlayer.setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET);
                            Message msg = Message.obtain();
                            msg.what = 1008;
                            mHandler.sendMessage(msg);
                        }catch (BaseException e){
                            e.printStackTrace();
                            Message msg = Message.obtain();
                            msg.what = 1009;
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();
                video_activity_videolevel.setVisibility(View.GONE);
                TextView textViewflunet = (TextView)findViewById(R.id.video_activity_level);
                textViewflunet.setText("流畅");
                break;
            case R.id.video_activity_level:
                if(video_activity_videolevel.getVisibility() == View.VISIBLE){
                    video_activity_videolevel.setVisibility(View.GONE);
                }else{
                    video_activity_videolevel.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.video_activity_fullscreen:
                Intent intent = new Intent(this, EZRealPlayfullscreenActivity.class);
                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, mCameraInfo);
                startActivity(intent);
                break;
            default:
                break;
        }
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
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(null);
        }
        mRealPlaySh = null;
    }

    private StreamConvertCB.OutputDataCB mLocalRecordCb = new StreamConvertCB.OutputDataCB() {
        @Override
        public void onOutputData(byte[] bytes, int i, int i1, byte[] bytes1) {
            if (mOs == null) {
                File f = new File(Environment.getExternalStorageDirectory() + "/videogo.mp4");
                try {
                    mOs = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    mHandler.sendEmptyMessage(1007);
                    mOs.write(bytes, 0, i);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
    };

}
