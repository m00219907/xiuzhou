package com.jsycloud.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.PlaySDK.IPlaySDK;
import com.company.PlaySDK.IPlaySDKCallBack;
import com.dh.DpsdkCore.Audio_Fun_Info_t;
import com.dh.DpsdkCore.Get_TalkStream_Info_t;
import com.dh.DpsdkCore.Send_Audio_Data_Info_t;
import com.dh.DpsdkCore.Talk_Sample_Rate_e;
import com.dh.DpsdkCore.dpsdk_audio_type_e;
import com.dh.DpsdkCore.dpsdk_talk_bits_e;
import com.dh.DpsdkCore.dpsdk_talk_type_e;
import com.dh.DpsdkCore.dpsdk_trans_type_e;
import com.dh.DpsdkCore.fDPSDKTalkParamCallback;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.R;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Ptz_Direct_Info_t;
import com.dh.DpsdkCore.Ptz_Operation_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.jsycloud.ir.xiuzhou.AppApplication;

public class RealPlayActivity extends Activity implements View.OnClickListener{

    public final static String RECORD_PATH = Constant.appFolder + "/record/";
    public final static String IMAGE_PATH = Constant.appFolder + "/snapshot/";
    public final static String IMGSTR = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
    public final static String MP4STR = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".mp4";
    private static final int PicFormat_JPEG = 1;
    
    private byte[] m_szCameraId = null;
    String deviceId;
    private int m_pDLLHandle = 0;
    ImageView real_play_play, real_play_video, real_play_up, real_play_down, real_play_left, real_play_right,
              real_play_record, real_play_decrease, real_play_increase;
    TextView real_play_resolution;
    SurfaceView real_play_surfaceview;
    fMediaDataCallback fm;
    private int m_nPort = 0;
    private int m_nSeq = 0;
    private int mTimeOut = 30*1000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_real_play);
        m_pDLLHandle = AppApplication.get().getDpsdkHandle();
        m_szCameraId = getIntent().getStringExtra("channelId").getBytes();
        deviceId = getIntent().getStringExtra("channelId");
        m_nPort = IPlaySDK.PLAYGetFreePort();

        findViewById(R.id.real_play_back).setOnClickListener(this);
        real_play_play = (ImageView)findViewById(R.id.real_play_play);
        real_play_play.setOnClickListener(this);

        real_play_video = (ImageView)findViewById(R.id.real_play_video);
        real_play_video.setOnClickListener(this);
        real_play_video.setTag("0");
        real_play_up = (ImageView)findViewById(R.id.real_play_up);
        real_play_down = (ImageView)findViewById(R.id.real_play_down);
        real_play_left = (ImageView)findViewById(R.id.real_play_left);
        real_play_right = (ImageView)findViewById(R.id.real_play_right);
        real_play_record = (ImageView)findViewById(R.id.real_play_record);
        real_play_decrease = (ImageView)findViewById(R.id.real_play_decrease);
        real_play_increase = (ImageView)findViewById(R.id.real_play_increase);

        real_play_resolution = (TextView)findViewById(R.id.real_play_resolution);
        real_play_resolution.setTag("1");

        findViewById(R.id.real_play_fullscreen).setOnClickListener(this);
        findViewById(R.id.real_play_capture).setOnClickListener(this);

        real_play_surfaceview = (SurfaceView)findViewById(R.id.real_play_surfaceview);
        SurfaceHolder holder = real_play_surfaceview.getHolder();
        holder.addCallback(new Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
            IPlaySDK.InitSurface(m_nPort, real_play_surfaceview);
                openVideo();
            }
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
             }
             public void surfaceDestroyed(SurfaceHolder holder) {
             }
        });
    
        fm = new fMediaDataCallback() {
            
            @Override
            public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
                    byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {

                int ret = IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
                if(ret == 1){
                    Log.e("xss","playing success=" + nSeq + " package size=" + nDataLen);
                }else{
                    Log.e("xss","playing failed=" + nSeq + " package size=" + nDataLen);
                }
            }
        };

        setListener();
        new File(RECORD_PATH).mkdirs();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.real_play_back:
                finish();
                break;
            case R.id.real_play_play:
                if(real_play_play.getTag().equals("0")){
                    openVideo();
                }else{
                    closeVideo();
                }
                break;
            case R.id.real_play_fullscreen:
                Intent intent = new Intent(this, RealPlayfullscreenActivity.class);
                intent.putExtra("channelId", m_szCameraId);
                startActivity(intent);
                break;
            case R.id.real_play_video:
                if(real_play_video.getTag().equals("0")){
                    real_play_video.setTag("1");
                    real_play_video.setImageResource(R.drawable.real_play_recording);
                    Toast.makeText(this, "正在录制，再次点击停止录制", Toast.LENGTH_LONG).show();
                    IPlaySDK.PLAYStartDataRecord(m_nPort, RECORD_PATH + MP4STR, 0, null, 0);
                }else{
                    real_play_video.setTag("0");
                    real_play_video.setImageResource(R.drawable.real_play_video);
                    Toast.makeText(this, "录制完毕，视频保存在"+ RECORD_PATH, Toast.LENGTH_LONG).show();
                    IPlaySDK.PLAYStopDataRecord(m_nPort);
                }
                break;
            case R.id.real_play_capture:
                captureBitmap();
                break;
            default:
                break;
        }
    }

    /**
     * 创建文件夹  保存截图 图片
     */
    private void captureBitmap() {
        
        String path = IMAGE_PATH + IMGSTR;
        //先创建一个文件夹
        File dir = new File(IMAGE_PATH);
        File file = new File(IMAGE_PATH, IMGSTR);
        if(!dir.exists()) {
            dir.mkdir();
        } else {
            if(file.exists()) {
                file.delete();
            }
        }
        
        int result = IPlaySDK.PLAYCatchPicEx(m_nPort, path, PicFormat_JPEG);
        if (result > 0) {
            Toast.makeText(this, "截图已为你保存在:"+IMAGE_PATH, Toast.LENGTH_LONG).show();
            saveIntoMediaCore();
        } else {
            Toast.makeText(this, "截图失败", Toast.LENGTH_LONG).show();
        }
    }
    
    private void saveIntoMediaCore(){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        //intent.setAction(MEDIA_ROUTER_SERVICE);
        Uri uri = Uri.parse(IMAGE_PATH + IMGSTR);
        intent.setData(uri);
        RealPlayActivity.this.setIntent(intent);
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

    public void openVideo() {
        if(!StartRealPlay()){
            Toast.makeText(getApplicationContext(), "视频打开失败!", Toast.LENGTH_SHORT).show();
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
                real_play_play.setTag("1");
                real_play_play.setImageResource(R.drawable.real_play_play);
                m_nSeq = retVal.nReturnValue;
                Toast.makeText(getApplicationContext(), "视频打开成功!", Toast.LENGTH_SHORT).show();
            }else{
                real_play_play.setTag("0");
                real_play_play.setImageResource(R.drawable.real_play_stop);
                StopRealPlay();
                Toast.makeText(getApplicationContext(), "视频打开失败!", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Log.e("xss", e.toString());
        }
    }
    
    public boolean StartRealPlay() {
        if(real_play_surfaceview == null) {
            return false;
        }
        
        boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort,null,0,1500*1024) != 0;
        if(bOpenRet) {
            boolean bPlayRet = IPlaySDK.PLAYPlay(m_nPort, real_play_surfaceview) != 0;
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

    public void closeVideo() {

        int ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq, mTimeOut);
        if(ret == 0){
            real_play_play.setTag("0");
            real_play_play.setImageResource(R.drawable.real_play_stop);
            Toast.makeText(getApplicationContext(), "关闭视频成功!", Toast.LENGTH_SHORT).show();
        }else{
            real_play_play.setTag("1");
            real_play_play.setImageResource(R.drawable.real_play_play);
            Toast.makeText(getApplicationContext(), "关闭视频失败!", Toast.LENGTH_SHORT).show();
        }
        StopRealPlay();
    }

    private void setListener(){
        real_play_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
                    ptzDirectInfo.bStop = false;
                    ptzDirectInfo.nDirect = 3;
                    ptzDirectInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
                    ptzDirectInfo.bStop = true;
                    ptzDirectInfo.nDirect = 3;
                    ptzDirectInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
                }
                return true;
            }
        });

        real_play_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
                    ptzDirectInfo.bStop = false;
                    ptzDirectInfo.nDirect = 4;
                    ptzDirectInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
                    ptzDirectInfo.bStop = true;
                    ptzDirectInfo.nDirect = 4;
                    ptzDirectInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
                }
                return true;
            }
        });

        real_play_up.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
                    ptzDirectInfo.bStop = false;
                    ptzDirectInfo.nDirect = 1;
                    ptzDirectInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
                    ptzDirectInfo.bStop = true;
                    ptzDirectInfo.nDirect = 1;
                    ptzDirectInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
                }

                return true;
            }
        });

        real_play_down.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
                    ptzDirectInfo.bStop = false;
                    ptzDirectInfo.nDirect = 2;
                    ptzDirectInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
                    ptzDirectInfo.bStop = true;
                    ptzDirectInfo.nDirect = 2;
                    ptzDirectInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
                }

                return true;
            }
        });

        real_play_increase.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
                    ptzOperationInfo.bStop = false;
                    ptzOperationInfo.nOperation = 0;
                    ptzOperationInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
                    ptzOperationInfo.bStop = true;
                    ptzOperationInfo.nOperation = 0;
                    ptzOperationInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
                }

                return true;
            }
        });

        real_play_decrease.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
                    ptzOperationInfo.bStop = false;
                    ptzOperationInfo.nOperation = 3;
                    ptzOperationInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
                    System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
                    ptzOperationInfo.bStop = true;
                    ptzOperationInfo.nOperation = 3;
                    ptzOperationInfo.nStep = 4;

                    IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
                }
                return true;
            }
        });

        real_play_record.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    new Thread() {
                        public void run() {
                            startTalk();
                        }
                    }.start();
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    sendMessage(mHandler, MSG_LIVE_CLOSE_TALK, 0, 0);
                }
                return true;
            }
        });
    }


    public static final int MSG_SHOW_DIALOG = 1;
    /** 打开声音提醒 */
    public static final int MSG_LIVE_OPEN_SOUND = 2;
    public static final int MSG_LIVE_CLOSE_SOUND = 3;
    /** 打开对讲提醒 */
    public static final int MSG_LIVE_OPEN_TALK = 5;
    public static final int MSG_LIVE_CLOSE_TALK = 6;
    /** 对讲成功消息 */
    public static final int MSG_LIVE_TALK_SUCCUSS = 7;
    /** 对讲失败消息 */
    public static final int MSG_LIVE_TALK_FAIL = 8;
    /** 对讲状态 */
    public static final int MSG_LIVE_TALK_STATUS = 9;
    /** 关闭对讲成功消息 */
    public static final int MSG_LIVE_STOP_TALK_SUCCUSS = 10;
    /** 关闭对讲失败消息 */
    public static final int MSG_LIVE_STOP_TALK_FAIL = 11;
    public static final int MSG_TOAST_OPEN_SOUND = 12;
    public static final int MSG_TOAST_CLOSE_SOUND = 13;
    // 超时时间
    private static final int DPSDK_CORE_DEFAULT_TIMEOUT = 30000;
    private int ret;
    private Audio_Fun_Info_t afInfo = null;
    private Return_Value_Info_t rvi;
    private int callBackTag = 0;
    private Send_Audio_Data_Info_t sadInfo = null;
    private int port = 0;

    private int mAudioType = dpsdk_audio_type_e.Talk_Coding_PCM;
    private int mSampleRate = dpsdk_talk_bits_e.Talk_Audio_Bits_16;
    private int mTalkBits = Talk_Sample_Rate_e.Talk_Audio_Sam_8K;

    Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOAST_OPEN_SOUND:
                    Toast.makeText(RealPlayActivity.this, "open sound", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_TOAST_CLOSE_SOUND:
                    Toast.makeText(RealPlayActivity.this, "stop sound", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_LIVE_OPEN_SOUND:
                    //若对讲是打开的，关闭对讲，打开声音
                    //closeTalk();
                    playSound(true);
                    break;
                case MSG_LIVE_CLOSE_SOUND:
                    playSound(false);
                    break;
                case MSG_LIVE_OPEN_TALK:
                    //关闭声音，开始对讲，打开语音对讲等待框
                    //playSound(false);
                    //showProgressDialog();
                    startTalk();
                    break;
                case MSG_LIVE_CLOSE_TALK:
                    closeTalk();
                case MSG_LIVE_TALK_SUCCUSS:
                    Toast.makeText(RealPlayActivity.this, getResources().getString(R.string.operate_sound_success), Toast.LENGTH_SHORT).show();
                    break;
                case MSG_LIVE_TALK_FAIL:
                    Toast.makeText(RealPlayActivity.this, getResources().getString(R.string.live_talk_open_fail) + ",errCode:" + msg.arg1, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_LIVE_STOP_TALK_SUCCUSS:
                    Toast.makeText(RealPlayActivity.this, getResources().getString(R.string.operate_sound_stop_success), Toast.LENGTH_SHORT).show();
                    break;
                case MSG_LIVE_STOP_TALK_FAIL:
                    Toast.makeText(RealPlayActivity.this, getResources().getString(R.string.live_talk_fail) + ",errCode:" + msg.arg1, Toast.LENGTH_SHORT).show();
                    break;
            }

            return false;
        }
    });

    private void startTalk() {

        // 码流请求信息
        Get_TalkStream_Info_t gti = new Get_TalkStream_Info_t();
        gti.nAudioType = mAudioType;
        gti.nBitsType = mTalkBits;
        gti.nSampleType = mSampleRate;

        gti.nTalkType = dpsdk_talk_type_e.Talk_Type_Device;
        gti.nTransType = dpsdk_trans_type_e.DPSDK_CORE_TRANSTYPE_TCP;

        gti.szCameraId = deviceId.getBytes();  //设备ID
        // 码流请求序号,可作为后续操作标识
        rvi = new Return_Value_Info_t();
        int ret = IDpsdkCore.DPSDK_GetTalkStream(m_pDLLHandle, rvi, gti, fmdCallback,
                DPSDK_CORE_DEFAULT_TIMEOUT);
        Log.e("startTalk", "rvi.nReturnValue=" + rvi.nReturnValue);

        if (ret != 0 ) {
            return;
        } else if (ret == 0) {
            sendMessage(mHandler, MSG_LIVE_TALK_SUCCUSS, 0, 0);
            playSound(true);
        }
        /************** 手机采集数据发送给设备 ****************/
        // 获取语音采集回调信息
        afInfo = new Audio_Fun_Info_t();
        ret = IDpsdkCore.DPSDK_GetSdkAudioCallbackInfo(m_pDLLHandle, afInfo);
        if (ret != 0) {

        }

        // 打开音频采集功能
        ret = IPlaySDK.PLAYOpenAudioRecord(fun, dpsdk_talk_bits_e.Talk_Audio_Bits_16, Talk_Sample_Rate_e.Talk_Audio_Sam_8K, 1024, 0);
        if (ret != 1) {
            Log.i("IPlaySDK", "PLAYOpenAudioRecord failed");
        }
    }

    fDPSDKTalkParamCallback fdpsdkCallback = new fDPSDKTalkParamCallback() {
        @Override
        public void invoke(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
            mAudioType = arg1;
            mSampleRate = arg4;
            mTalkBits = arg3;
            if (callBackTag < 1) {
                new Thread() {
                    public void run()
                    {
                        startTalk();
                        Log.i("thread", "run thread");
                    }
                }.start();
            } else {
                sendMessage(mHandler, MSG_LIVE_TALK_FAIL, 0, 0);
                Log.i("fdpsdkCallback", "start talk failed");
            }
            ++callBackTag;
            Log.i("fDPSDKTalkParamCallback", "callBackTag = " + callBackTag);
        }
    };

    fMediaDataCallback fmdCallback = new fMediaDataCallback() {
        @Override
        public void invoke(int nPDLLHandle, int nSeq, int nMediaType, byte[] szNodeId, int nParamVal, byte[] szData,
                           int nDataLen) {
            // TODO 接收对讲的语音数据
            IPlaySDK.PLAYInputData(port, szData, nDataLen);
        }
    };

    IPlaySDKCallBack.pCallFunction fun = new IPlaySDKCallBack.pCallFunction() {
        @Override
        public void invoke(byte[] arg0, int arg1, long arg2) {

            sadInfo = new Send_Audio_Data_Info_t(arg1);
            sadInfo.pData = arg0;
            sadInfo.nLen = arg1;
            sadInfo.nAudioType = mAudioType;
            sadInfo.nTalkBits = mSampleRate;
            sadInfo.nSampleRate = mTalkBits;
            sadInfo.pCallBackFun = afInfo.pCallBackFun;
            sadInfo.pUserParam = afInfo.pUserParam;
            IDpsdkCore.DPSDK_SendAudioData(m_pDLLHandle, sadInfo);

        }
    };

    private void sendMessage(Handler mHandler, int msg, int org1, int org2) {
        Message Msg = mHandler.obtainMessage();
        Msg.what = msg;
        Msg.arg1 = org1;
        Msg.arg2 = org2;
        Msg.sendToTarget();
    }

    private void playSound (boolean isPlaySound) {
        if (isPlaySound) {
            port = IPlaySDK.PLAYGetFreePort();
            if (port == -1) {
                return;
            }
            // open stream
            IPlaySDK.PLAYOpenStream(port, null, 0, 1024 * 1024);
            int ret = IPlaySDK.PLAYPlay(port, null);
            if (ret == 0) {
                // release port
                IPlaySDK.PLAYReleasePort(port);
                port = -1;
                return;
            }
            IPlaySDK.PLAYPlaySound(port);
            sendMessage(mHandler, MSG_TOAST_OPEN_SOUND, 0, 0);
        } else {
            IPlaySDK.PLAYStopSound();
            sendMessage(mHandler, MSG_TOAST_CLOSE_SOUND, 0, 0);
        }
    }

    private void closeTalk() {
        // 停止数据采集
        playSound(false);
        IPlaySDK.PLAYCloseAudioRecord();
        // 根据通道号关闭对讲
        if ( rvi != null ) {
            ret = IDpsdkCore.DPSDK_CloseTalkStreamBySeq(m_pDLLHandle, rvi.nReturnValue,
                    DPSDK_CORE_DEFAULT_TIMEOUT);
            Log.e("stopTalk", "rvi.nReturnValue=" + rvi.nReturnValue);
        }

        if (ret != 0) {
            Log.e("stopTalk", "ret=" + ret);
            sendMessage(mHandler, MSG_LIVE_STOP_TALK_FAIL, ret, 0);
            return;
        }
        sendMessage(mHandler, MSG_LIVE_STOP_TALK_SUCCUSS, 0, 0);
    }

}
