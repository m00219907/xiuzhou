package com.jsycloud.activity;


import com.company.PlaySDK.IPlaySDK;
import com.company.PlaySDK.IPlaySDKCallBack.pCallFunction;
import com.dh.DpsdkCore.Audio_Fun_Info_t;
import com.dh.DpsdkCore.Get_TalkStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.Send_Audio_Data_Info_t;
import com.dh.DpsdkCore.Talk_Sample_Rate_e;
import com.dh.DpsdkCore.dpsdk_audio_type_e;
import com.dh.DpsdkCore.dpsdk_talk_bits_e;
import com.dh.DpsdkCore.dpsdk_talk_type_e;
import com.dh.DpsdkCore.dpsdk_trans_type_e;
import com.dh.DpsdkCore.fDPSDKTalkParamCallback;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.ir.xiuzhou.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OperateSoundTalk extends Activity {
	public static final String TAG = "OperateSoundTalk";
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
    // playsdk端口
    private int port = 0;
    private Send_Audio_Data_Info_t sadInfo = null;
    private Boolean ISSOUNDON = false;
    private Boolean ISTALKOPEN = false;
    private ProgressDialog mProgressDialog;
    private Button btnSound;
    private Button btnTalk;
    private int m_pDLLHandle;
    private Return_Value_Info_t rvi;
    
    private int mAudioType = dpsdk_audio_type_e.Talk_Coding_PCM;
    private int mSampleRate = dpsdk_talk_bits_e.Talk_Audio_Bits_16;
    private int mTalkBits = Talk_Sample_Rate_e.Talk_Audio_Sam_8K;
    private int callBackTag = 0;
    private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soundtalk);
//		HandlerThread handlerThread = new HandlerThread("handlerThread");
//		handlerThread.start();
		Callback callback = new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_TOAST_OPEN_SOUND:
					Toast.makeText(OperateSoundTalk.this, "open sound", Toast.LENGTH_SHORT).show();
					break;
				case MSG_TOAST_CLOSE_SOUND:
					Toast.makeText(OperateSoundTalk.this, "stop sound", Toast.LENGTH_SHORT).show();
					break;
				case MSG_LIVE_OPEN_SOUND:
					//若对讲是打开的，关闭对讲，打开声音
					//closeTalk();
					playSound(true);
					btnSound.setText(getbtnString(R.string.operate_sound_down));
					break;
				case MSG_LIVE_CLOSE_SOUND:	
					btnSound.setText(getbtnString(R.string.operate_sound_on));
					playSound(false);
					break;
				case MSG_LIVE_OPEN_TALK:
					//关闭声音，开始对讲，打开语音对讲等待框
					//playSound(false);
					//showProgressDialog();
					startTalk();
					break;
				case MSG_LIVE_CLOSE_TALK:
					btnSound.setEnabled(false);
					btnTalk.setText(getbtnString(R.string.operate_talk_open));
					closeTalk();
				case MSG_LIVE_TALK_SUCCUSS:
					mProgressDialog.dismiss();
					Toast.makeText(OperateSoundTalk.this, getResources().getString(R.string.operate_sound_success), Toast.LENGTH_SHORT).show();
					break;
				case MSG_LIVE_TALK_FAIL:
					 mProgressDialog.dismiss();
		             Toast.makeText(OperateSoundTalk.this, getResources().getString(R.string.live_talk_open_fail) + ",errCode:" + msg.arg1, Toast.LENGTH_SHORT).show();
					break;
				case MSG_LIVE_STOP_TALK_SUCCUSS:
					Toast.makeText(OperateSoundTalk.this, getResources().getString(R.string.operate_sound_stop_success), Toast.LENGTH_SHORT).show();
					break;
				case MSG_LIVE_STOP_TALK_FAIL:
					Toast.makeText(OperateSoundTalk.this, getResources().getString(R.string.live_talk_fail) + ",errCode:" + msg.arg1, Toast.LENGTH_SHORT).show();
					break;
					
				
					}
				
				return false;
			}
		};
		mHandler = new Handler(getMainLooper(), callback);
		
		m_pDLLHandle = AppApplication.get().getDpsdkHandle();
		IDpsdkCore.DPSDK_SetDPSDKTalkParamCallback(m_pDLLHandle, fdpsdkCallback);
		mProgressDialog = new ProgressDialog(this);
		TextView txtChannelId = (TextView)findViewById(R.id.channel_id);
		TextView txtChanlName = (TextView)findViewById(R.id.channel_name);
		txtChannelId.setText(getIntent().getStringExtra("channelId"));
		txtChanlName.setText(getIntent().getStringExtra("channelName"));
		//打开对讲将导致声音关闭
		btnSound = (Button)findViewById(R.id.btn_sound);
		btnTalk = (Button)findViewById(R.id.btn_talk);
		btnSound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if (!ISSOUNDON) {
					ISSOUNDON = true;
					sendMessage(mHandler, MSG_LIVE_OPEN_SOUND, 0, 0);
				} else {
					ISSOUNDON = false;
				    sendMessage(mHandler, MSG_LIVE_CLOSE_SOUND, 0, 0);
				}
			}
			
		});
		btnTalk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
		
				if (!ISTALKOPEN) {
					callBackTag = 0;
					ISTALKOPEN = true;
					btnSound.setEnabled(true);
					btnTalk.setText(getbtnString(R.string.operate_talk_close));
					showProgressDialog();
		 		    new Thread() {
						public void run()
						{
							startTalk();
							Log.i("thread", "run thread");
						}
					}.start();
					//sendMessage(mHandler, MSG_LIVE_OPEN_TALK, 0, 0);
				} else {
					ISTALKOPEN = false;
					sendMessage(mHandler, MSG_LIVE_CLOSE_TALK, 0, 0);
				}
				
			}
			
		});

	}
	private String getbtnString(int id) {
		return getResources().getString(id);
	}
	private void showProgressDialog() {
		mProgressDialog = ProgressDialog.show(this, null, getString(R.string.common_wait));
		mProgressDialog.setCancelable(false);
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
        Log.e(TAG, "stopTalk ret:" + ret);
        btnTalk.setText(getbtnString(R.string.operate_talk_open));
		ISTALKOPEN = false;
	}

    
	private void startTalk() {
		
		// 码流请求信息
        Get_TalkStream_Info_t gti = new Get_TalkStream_Info_t();
        gti.nAudioType = mAudioType;
        gti.nBitsType = mTalkBits;
        gti.nSampleType = mSampleRate;

        gti.nTalkType = dpsdk_talk_type_e.Talk_Type_Device;
        gti.nTransType = dpsdk_trans_type_e.DPSDK_CORE_TRANSTYPE_TCP;
        
        gti.szCameraId = getIntent().getStringExtra("deviceId").getBytes();  //设备ID
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
	        	btnSound.setText(getbtnString(R.string.operate_sound_on));
	        	ISSOUNDON = false;
	        }
	        
		}
	  private void sendMessage(Handler mHandler, int msg, int org1,
			int org2) {
		    Message Msg = mHandler.obtainMessage();
            Msg.what = msg;
            Msg.arg1 = org1;
            Msg.arg2 = org2;
            Msg.sendToTarget();
	}

	fMediaDataCallback fmdCallback = new fMediaDataCallback() {
	        @Override
	        public void invoke(int nPDLLHandle, int nSeq, int nMediaType, byte[] szNodeId, int nParamVal, byte[] szData,
	                int nDataLen) {
	            // TODO 接收对讲的语音数据
	            IPlaySDK.PLAYInputData(port, szData, nDataLen);
	        }
	    };

	    pCallFunction fun = new pCallFunction() {
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
    
	
    /*public Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		
    };*/
	
	    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
