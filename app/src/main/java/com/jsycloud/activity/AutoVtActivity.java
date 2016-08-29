package com.jsycloud.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.PlaySDK.IPlaySDK;
import com.company.PlaySDK.IPlaySDKCallBack.pCallFunction;
import com.dh.DpsdkCore.Audio_Fun_Info_t;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.InviteVtCallParam_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.RingInfo_t;
import com.dh.DpsdkCore.Send_Audio_Data_Info_t;
import com.dh.DpsdkCore.SetDoorCmd_Request_t;
import com.dh.DpsdkCore.dpsdk_SetDoorCmd_e;
import com.dh.DpsdkCore.dpsdk_call_type_e;
import com.dh.DpsdkCore.fDPSDKGeneralJsonTransportCallback;
import com.dh.DpsdkCore.fDPSDKPecDoorStarusCallBack;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.entity.NotifyEntity;
import com.jsycloud.groupTree.GroupListActivity;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.util.MusicTool;
import com.jsycloud.util.Utils;
import com.google.gson.Gson;

public class AutoVtActivity extends Activity {
	private static final String TAG = "AutoVtActivity";

	private SurfaceView m_svPlayer = null;
	public static final int PLAY_BUFFER = 1024 * 1024 * 2;
	private RelativeLayout mAcceptLay;
	private RelativeLayout mOpenDoorLay;
	private TextView clockTime;
	private ImageView openDoorImg;
	private ImageView offlineImg;
	private ImageView confirmImg;
	private Handler mHandler;
	private static final int RUNTIME = 0;
	private static final int SETTITLE = 1;
	private static final int SETOFFSTATE = 2;
	private static final int PLAYVIEW = 3;
	private static final int TIMEPERIOD = 1000; 
	private static final int delayMillis = 2*1000;
	private int mCount = 0;
	private TimerTask timerTask;
	private Timer timer;
	private String channelName = "";
	//private int iPlayPort;
	private int mAudioSessionId; //音频会话
	private int mVideoSessionId;  //视频会话
	
	private MusicTool musicTool;
	private	long mDoublieClickTime;
	private Runnable mRunable;
	private boolean mbAnswerPhone = false; // false - 没有接听电话 true - 接听电话
	
	private fMediaDataCallback mfMediaDataCallback;   //接受可视对讲回调
	private fDPSDKPecDoorStarusCallBack mfDPSDKPecDoorStarusCallBack;  //设置门禁命令后接口回调
	private InviteVtCallParam_t mInviteVtCallParam;
	private int m_nPort = 0;  //video  视频
	private int mTalkPort = 0;  //audio 声音
	private int mTimeOut = 30*1000;
	public int mPDLLHandle = 0;
	private int m_nSeq = 0;
	private byte[] mChannelId;
	private byte[] mChannelName;
	private Send_Audio_Data_Info_t sadInfo = null;
	private Audio_Fun_Info_t audioFunInfo;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autovt);
		//MusicTool.get().playSound(0, 0);	
				
		findViews();
		initData();
		initSurfaceView();
		
		setDPSDKCallback();
		setListeners();
		
	    mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case RUNTIME:   //可视对讲时间更新
					mCount ++;
					int min = (mCount / 60);
					int sec = (mCount % 60);
					clockTime.setText(String.format("%1$02d:%2$02d", min, sec));
					break;
				case SETTITLE:
					TextView userName = (TextView)findViewById(R.id.vtc_name);
					userName.setText(channelName);
					TextView nUserName = (TextView)findViewById(R.id.vt_name);
					nUserName.setText(channelName);
					break;
				case SETOFFSTATE:   //结束通话  surfaceview如何处理？？
					mOpenDoorLay.setBackgroundColor(getResources().getColor(R.color.black));
//					mPlayWindow.setVisibility(View.GONE);
					openDoorImg.setBackgroundResource(R.drawable.video_intercom_close_door_dis);
					offlineImg.setBackgroundResource(R.drawable.video_intercom_refuse_dis);
					clockTime.setText(getString(R.string.call_offline));
					
					break;
				case PLAYVIEW:   //开始可视对讲
					
					playChannel();
					
					final Return_Value_Info_t retVal0 = new Return_Value_Info_t();
					final Return_Value_Info_t retVal1 = new Return_Value_Info_t();   //dpsdk_call_type_e.CALL_TYPE_VT_CALL  
					int ret = IDpsdkCore.DPSDK_InviteVtCall(mPDLLHandle, retVal0, retVal1, mInviteVtCallParam, mInviteVtCallParam.nCallType, new fMediaDataCallback() {

								@Override
								public void invoke(int nPDLLHandle, int nSeq,
										int nMediaType, byte[] szNodeId,
										int nParamVal, byte[] szData,
										int nDataLen) {
									// TODO Auto-generated method stub
									
								}}, mTimeOut);
					//开始语音信息采集和发送
					startTalk();
					mAudioSessionId = retVal0.nReturnValue;  //用于关闭对讲
					mVideoSessionId = retVal1.nReturnValue;  //用于关闭对讲
//
//					if(ret != 0){
//						Toast.makeText(AutoVtActivity.this, "InviteVtCall fail"+ret, 0).show();
//					}
					
//					if(ret == 0){
//						mAudioSessionId = retVal0.nReturnValue;  //用于关闭对讲
//						mVideoSessionId = retVal1.nReturnValue;  //用于关闭对讲					
//						//开始语音信息采集和发送
//						startTalk();
//					}else{
//						StopRealPlay();
//						Toast.makeText(AutoVtActivity.this, "接受可视对讲请求失败", 0).show();
//					}
						
					break;
				default:
					break;
				}
			}
			
		};	
		
		initMusicTool();
	}
		
    private void setListeners() {
    	
    	//接听按钮---> 接受可视对讲
		ImageView pickCallImg = (ImageView)findViewById(R.id.pickup_img);
		pickCallImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mbAnswerPhone = true;
				musicTool.stopSound(0);
				mHandler.removeCallbacks(mRunable);  //删除消息队列中的runnable，此runnable执行音乐循环播放----->结束设备拨号时的音乐播放
				String curTime = Utils.longToStrDate(System.currentTimeMillis());

				if(mInviteVtCallParam != null){
					
					TextView userName = (TextView)findViewById(R.id.vtc_name);
					userName.setText(new String(mInviteVtCallParam.szUserId));  //显示的是channledid
										
				    mAcceptLay.setVisibility(View.GONE);
				    mOpenDoorLay.setVisibility(View.VISIBLE);
				    
				    //设置计时器				    
				    timerTask = new TimerTask() {  //用于更新接听时间
						@Override
						public void run() {
							Message msg = mHandler.obtainMessage();
							msg.what = RUNTIME;
							msg.sendToTarget();
						}
					};
				    
				    timer = new Timer();
				    timer.schedule(timerTask, TIMEPERIOD, TIMEPERIOD);
				    
					Message msg = mHandler.obtainMessage();
					msg.what = PLAYVIEW;
					msg.sendToTarget();
		
					/*对方忙线中,主动挂断电话*/
	
				} else {
					Toast.makeText(AutoVtActivity.this, getString(R.string.visabletalk_error), Toast.LENGTH_SHORT).show();
				}
				saveIntoHistoryCalls(curTime);
			}

		});

		// 拒绝按钮 ---> 拒绝可视对讲
		ImageView refuseCallImg = (ImageView)findViewById(R.id.refuse_img);
		refuseCallImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mbAnswerPhone = false;
				musicTool.stopSound(0);
				mHandler.removeCallbacks(mRunable);
				String curTime = Utils.longToStrDate(System.currentTimeMillis());
				
				int ret = -1;
				if (mInviteVtCallParam != null) {
					//拒绝可视对讲邀请
					ret = IDpsdkCore.DPSDK_SendRejectVtCall(mPDLLHandle, mInviteVtCallParam.szUserId, mInviteVtCallParam.callId, mInviteVtCallParam.dlgId, mInviteVtCallParam.tid, mTimeOut);
				} else {
					Toast.makeText(AutoVtActivity.this, getString(R.string.visabletalk_error), Toast.LENGTH_SHORT).show();
				}
				startActivity(new Intent(AutoVtActivity.this, GroupListActivity.class));
				saveIntoHistoryCalls(curTime);
				finish();
			}
		});
		
		openDoorImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(Utils.isFastDoubleClick()) {
					return;
				}
				
			  	long start = 0;
		    	long end = 0;
		    	// 注意 channelId 是不是获取的门禁信息里面的codeChannel 还是编码信息里面的codeChannel
//			  	int nRet = FModuleManager.SetDoorCmd(channelId, DoorCmd_e.DOOR_CMD_OPEN, start, end, fModuleListener);
//		    	Log.i(TAG, "channelId = " + channelId + ", nRet = " + nRet);
//		    	mListenerHandle = FModuleManager.AddListener(fModuleListener);
//		    	Log.i(TAG, "fModuleListener addListener~ mListenerHandle = " + String.valueOf(mListenerHandle));
		    	
		    	IDpsdkCore.DPSDK_SetPecDoorStatusCallback(mPDLLHandle, mfDPSDKPecDoorStarusCallBack);
		    	int nRet = IDpsdkCore.DPSDK_SetDoorCmd(mPDLLHandle, getSetDoorCmdRequest(mInviteVtCallParam, start, end), mTimeOut);
		   	
		    	if (nRet > 0) {
		    		long currentTime = System.currentTimeMillis();
		    		if (currentTime - mDoublieClickTime > 10000) {
						mDoublieClickTime = currentTime;
			    		confirmImg.setVisibility(View.VISIBLE);
					}
		    	}
		    	mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						confirmImg.setVisibility(View.GONE);
					}
				}, delayMillis);		    	
			}
		});
		
		//主动结束通话
		offlineImg.setOnClickListener(new OnClickListener() {  
						
			@Override
			public void onClick(View v) {
				
				if(Utils.isFastDoubleClick()) {
					return;
				}
		
				if (timer != null) {
					timerTask.cancel();
					timerTask = null;
					timer.cancel();
					timer.purge();
					timer = null;
					mHandler.removeMessages(RUNTIME);
				}
				
				//接受邀请后，挂断可视对讲
				stopPlay();  //stop video
				stopTalk();  //stop audio
				int nRet = IDpsdkCore.DPSDK_StopVtCall(mPDLLHandle, mInviteVtCallParam.szUserId, mAudioSessionId, mVideoSessionId, mInviteVtCallParam.callId,mInviteVtCallParam.dlgId, mTimeOut);
				Log.i(TAG, "VideoIntercomManager.CancelVtCall = " + nRet + ", mAudioSessionId = " + mAudioSessionId + ", mVideoSessionId =" + mVideoSessionId);
				Message msg = mHandler.obtainMessage();
				msg.what = SETOFFSTATE;
				msg.sendToTarget();
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setClass(AutoVtActivity.this, GroupListActivity.class); 
						startActivity(intent);
						finish();
					}
				}, delayMillis);
			}
		});		
		
		//设备主动挂断回调
	    int	ret = IDpsdkCore.DPSDK_SetGeneralJsonTransportCallback(mPDLLHandle,new fDPSDKGeneralJsonTransportCallback() {

			@Override
			public void invoke(int nPDLLHandle, byte[] szJson) {
				// TODO Auto-generated method stub
				
				NotifyEntity mNotify = fromJSON(new String(szJson));
				String mMethod = mNotify.getMethod();
				if (mMethod.equals("Scs.NotifyBye") || mMethod.equals("Server.notifyScsStatus")) {
					
					if (timer != null) {
						timerTask.cancel();
						timerTask = null;
						timer.cancel();
						timer.purge();
						timer = null;
						mHandler.removeMessages(RUNTIME);
					}
					
					Message msg = mHandler.obtainMessage();
					msg.what = SETOFFSTATE;
					msg.sendToTarget();
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							stopPlay();  //stop video
							stopTalk();  //stop audio
							Intent intent = new Intent();
							intent.setClass(AutoVtActivity.this, GroupListActivity.class); 
							startActivity(intent);
							finish();
						}
					}, delayMillis);
					
				}else if(mMethod.equals("Scs.NotifyCancel")) {//设备未接通时，主动取消拨号，或者拨号超时

					Log.e(TAG, "------------ get Scs.NotifyCancel");
					gotoGroupListActivity();
					
				} 
			}			
		});
	    		
	}
    
    private void gotoGroupListActivity() {
		Intent intent = new Intent();
		intent.setClass(AutoVtActivity.this, GroupListActivity.class); 
		startActivity(intent);
		finish();   	
    }
	
    public NotifyEntity fromJSON(String json) {
    	
		Gson gson = new Gson();
		NotifyEntity mNotify = gson.fromJson(json, NotifyEntity.class);
		return mNotify;

    }
    
	private void setDPSDKCallback() {
				
		//vttalk  请求vttalk对讲回调
		mfMediaDataCallback = new fMediaDataCallback() {

			@Override
			public void invoke(int nPDLLHandle,  int nSeq, int nMediaType,
					byte[] szNodeId, int nParamVal,  byte[] szData,  int nDataLen) {
				
				int ret = IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
				if(ret == 1){
					Log.e("xss","playing success=" + nSeq + " package size=" + nDataLen);
				}else{
					Log.e("xss","playing failed=" + nSeq + " package size=" + nDataLen);
				}
				
			}			
		};
		
    	//open door
		mfDPSDKPecDoorStarusCallBack = new fDPSDKPecDoorStarusCallBack() {
			
			@Override
			public void invoke(int nPDLLHandle, byte[] szCameraId, int nStatus,
					int nTime) {
				
				Toast.makeText(AutoVtActivity.this , new String(szCameraId)+" status :"+nStatus, 0).show();
				Log.e(TAG, new String(szCameraId)+" ======= status :"+nStatus);
			}
		};
	}
	
	private void playChannel(){
		if(!StartRealPlay()){
			Log.e(TAG, "StartRealPlay failed!");
			Toast.makeText(getApplicationContext(), "Open video failed!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		try{					
			Return_Value_Info_t retVal = new Return_Value_Info_t();

			Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t();
			//m_szCameraId = etCam.getText().toString().getBytes();
			
			System.arraycopy(mChannelId, 0, getRealStreamInfo.szCameraId, 0, mChannelId.length);
			//getRealStreamInfo.szCameraId = "1000096$1$0$0".getBytes();
			getRealStreamInfo.nMediaType = 1;
			getRealStreamInfo.nRight = 0;
			getRealStreamInfo.nStreamType = 1;
			getRealStreamInfo.nTransType = 1;
			Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
			IDpsdkCore.DPSDK_GetChannelInfoById(mPDLLHandle, mChannelId, ChannelInfo);
			int ret = IDpsdkCore.DPSDK_GetRealStream(mPDLLHandle, retVal, getRealStreamInfo, mfMediaDataCallback, mTimeOut);
			if(ret == 0){
				Log.e(TAG+"xss DPSDK_GetRealStream success!",ret+"");
				m_nSeq = retVal.nReturnValue;
				Toast.makeText(getApplicationContext(), "Open video success!", Toast.LENGTH_SHORT).show();
			}else{
				StopRealPlay();
				Log.e(TAG+"xss DPSDK_GetRealStream failed!",ret+""); 
				Toast.makeText(getApplicationContext(), "Open video failed!", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			Log.e("xss", e.toString());
		}
	}
	
	private void stopPlay(){
		int ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(mPDLLHandle, m_nSeq, mTimeOut);
		if(ret == 0){
			Log.e(TAG,"DPSDK_CloseRealStreamByCameraId success!");
			Toast.makeText(getApplicationContext(), "Close video success!", Toast.LENGTH_SHORT).show();
		}else{
			Log.e(TAG,"DPSDK_CloseRealStreamByCameraId failed! ret = " + ret);
			Toast.makeText(getApplicationContext(), "Close video failed!", Toast.LENGTH_SHORT).show();
		}
		StopRealPlay();
	}
	
	private void findViews() {
		mAcceptLay = (RelativeLayout)findViewById(R.id.pick_up_call_lay); 
		mOpenDoorLay = (RelativeLayout)findViewById(R.id.open_door_lay); 

	    openDoorImg = (ImageView)findViewById(R.id.open_door_img);	// 开门
	    offlineImg = (ImageView)findViewById(R.id.offline_img);     //挂断
	    confirmImg = (ImageView)findViewById(R.id.video_intercom_confirm);
	    clockTime = (TextView)findViewById(R.id.vt_time);
	    m_svPlayer = (SurfaceView)findViewById(R.id.sv_player);
	}
    
    private void initData(){
   	
    	mInviteVtCallParam = new InviteVtCallParam_t();
    	Bundle bundle = getIntent().getExtras();
    	mInviteVtCallParam.szUserId = bundle.getByteArray("szUserId");
    	mInviteVtCallParam.callId = bundle.getInt("callId");
    	mInviteVtCallParam.dlgId = bundle.getInt("dlgId");
    	mInviteVtCallParam.tid = bundle.getInt("tid");
    	mInviteVtCallParam.audioBit = bundle.getInt("audioBit");
    	mInviteVtCallParam.audioType = bundle.getInt("audioType");
    	mInviteVtCallParam.sampleRate = bundle.getInt("sampleRate");
    	mInviteVtCallParam.rtpAPort = bundle.getInt("rtpAPort");
    	mInviteVtCallParam.rtpVPort = bundle.getInt("rtpVPort");
    	mInviteVtCallParam.nCallType = bundle.getInt("nCallType");
    	mInviteVtCallParam.rtpServIP = bundle.getByteArray("rtpServIP");
    	
    	mChannelId = bundle.getByteArray("channelid");
    	mChannelName = bundle.getByteArray("channelname");
    	
    	//new DPSDK_CMD_VT_CALL_INVITE_NOTIFY:nAudioType[1]
    	//nAudioBits[16]nAudioSampleRate[16000]rtpServIP[172.3.4.107]nRtpAPort[20000]
    	//nRtpVPort[20001]szUserID[05018001]callId[1]dlgId[2]tid[7]
    	
    	InviteVtCallParam_t tt = mInviteVtCallParam;
    	String uid = new String(tt.szUserId);
    	String id = new String(tt.rtpServIP);
    	


    	  	
    	mPDLLHandle = AppApplication.get().getDpsdkHandle();
    	
		SharedPreferences sp = getSharedPreferences("HistoryCalls", 0);
		sp.edit().putString("UserName", channelName).commit();

		TextView userName = (TextView)findViewById(R.id.vtc_name);
		userName.setText(new String(mInviteVtCallParam.szUserId));  //userName.setText(channelName);
		TextView nUserName = (TextView)findViewById(R.id.vt_name);
		nUserName.setText(new String(mInviteVtCallParam.szUserId));  //channelname没有传输过来，这里显示的是channelid

	    TextView time = (TextView)findViewById(R.id.vtc_time);
		time.setText(getString(R.string.visabletalk_calling_txt));   	
    }
        
	private void initMusicTool(){
		musicTool = new MusicTool();
		musicTool.initSound();
		//musicTool.SetRes(0, R.raw.capture);
		mRunable = new Runnable() {
			@Override
			public void run() {
				musicTool.playSound(0, -1);
			}
		};
		mHandler.postDelayed(mRunable, 500);	
	}
	
	private void initSurfaceView(){
		m_nPort = IPlaySDK.PLAYGetFreePort();
		SurfaceHolder holder = m_svPlayer.getHolder();
        holder.addCallback(new Callback() {
     	   	public void surfaceCreated(SurfaceHolder holder){    		
     	   		Log.d("xss", "surfaceCreated");
     	   	    IPlaySDK.InitSurface(m_nPort, m_svPlayer);
     		}
     		
     		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height){   		
     			Log.d("xss", "surfaceChanged");
     		}

     		public void surfaceDestroyed(SurfaceHolder holder){
     			Log.d("xss", "surfaceDestroyed");
     		}
        });
	}
	
	private void StopRealPlay(){
    	try {
    		IPlaySDK.PLAYStopSoundShare(m_nPort);
    		IPlaySDK.PLAYStop(m_nPort);  		
    		IPlaySDK.PLAYCloseStream(m_nPort);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	private boolean StartRealPlay(){ 
        if(m_svPlayer == null){
        	return false;
        }
        	       
        boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort,null,0,1500*1024) == 0? false : true;
    	if(bOpenRet){
			boolean bPlayRet = IPlaySDK.PLAYPlay(m_nPort, m_svPlayer) == 0 ? false : true;
			Log.i("StartRealPlay", "StartRealPlay1");
			if(bPlayRet){
				boolean bSuccess = IPlaySDK.PLAYPlaySoundShare(m_nPort) == 0 ? false : true;
				Log.i("StartRealPlay", "StartRealPlay2");
				if(!bSuccess){
					IPlaySDK.PLAYStop(m_nPort);
					IPlaySDK.PLAYCloseStream(m_nPort);
					Log.i("StartRealPlay", "StartRealPlay3");
					return false;
				}
			}
			else{
				IPlaySDK.PLAYCloseStream(m_nPort);
				Log.i("StartRealPlay", "StartRealPlay4");
				return false;
			}
		}
    	else{
    		Log.i("StartRealPlay", "StartRealPlay5");
    		return false;
    	}      
        return true;
    }
	
	private int startTalk(){
		//vttalk
		mTalkPort = IPlaySDK.PLAYGetFreePort();
		int openStreamRet = IPlaySDK.PLAYOpenStream(mTalkPort, null, 0, PLAY_BUFFER / 2);
		int playRet = IPlaySDK.PLAYPlay(mTalkPort, null);
		int playSoundRet = IPlaySDK.PLAYPlaySound(mTalkPort);
		
		
		//获取语音采集回调信息
		audioFunInfo = new Audio_Fun_Info_t();
//		int ret = IDpsdkCore.DPSDK_GetSdkAudioCallbackInfo(mPDLLHandle, audioFunInfo);
//		if(ret == 0){
			//发送语音采集信息DPSDK_SetVtCallInviteCallbackDPSDK_SendAudioData
			int ret = IDpsdkCore.DPSDK_GetAudioSendFunCallBack(mPDLLHandle, audioFunInfo);
			Log.i("majinyangnn", audioFunInfo.pCallBackFun+"");
			Log.i("majinyangnn", audioFunInfo.pUserParam+"");
			Log.e(TAG, "+++++++++++++++++++++PLAYOpenAudioRecord call back run ++++++++++++++++++++++");
			Log.e(TAG, "=========================DPSDK_SendAudioData  ======================"+"ret ="+ret);
		int openaudiordRet = IPlaySDK.PLAYOpenAudioRecord(new pCallFunction() {
			@Override
			public void invoke(byte[] arg0, int arg1, long arg2) {
				
				sadInfo = new Send_Audio_Data_Info_t(arg1);
	            sadInfo.pData = arg0;
	            sadInfo.nLen = arg1;
	            sadInfo.nAudioType = mInviteVtCallParam.audioType;
	            sadInfo.nTalkBits = mInviteVtCallParam.sampleRate;
	            sadInfo.nSampleRate = mInviteVtCallParam.audioBit;
	            sadInfo.pCallBackFun = audioFunInfo.pCallBackFun;
	            sadInfo.pUserParam = audioFunInfo.pUserParam;
	            IDpsdkCore.DPSDK_SendVtCallAudioData(mPDLLHandle, sadInfo);
					
//				}	
			}
		}, mInviteVtCallParam.audioBit , mInviteVtCallParam.sampleRate, 1024, 0);
		return openaudiordRet;
	}
	
	private void stopTalk(){
		IPlaySDK.PLAYCloseAudioRecord();
		IPlaySDK.PLAYStop(mTalkPort);  		
		IPlaySDK.PLAYCloseStream(mTalkPort);
		IPlaySDK.PLAYStopSound();
		IPlaySDK.PLAYStopDataRecord(mTalkPort);
		
		mTalkPort = -1;
		
	}
	

	private void saveIntoHistoryCalls(String time) {
		
		SharedPreferences sp = getSharedPreferences("HistoryCalls", 0);
		StringBuilder sb = new StringBuilder();
		if (mbAnswerPhone) {
			sb.append(channelName).append(",").append(time).append("|"); 
		} else {
			sb.append(channelName).append(",").append(time).append(",").append("MissedCalls").append("|");
		}
		//sb.append(channelName).append(",").append(time).append("|");  //   "." "|" 都是转义字符 ， 需要加"//"
			String lastCalls = sp.getString("Calls", "");
			String historyCalls = new StringBuilder().append(sb.toString()).append(lastCalls).toString();
			Log.i(TAG, "historyPicUpCalls = " + historyCalls);
			sp.edit().putString("Calls", historyCalls).commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();

		IPlaySDK.PLAYCloseAudioRecord();
		//门口机自动挂断关闭声音
		musicTool.stopSound(0);
	}
	
	
	private Send_Audio_Data_Info_t getsendAudioDataInfo(InviteVtCallParam_t param,Audio_Fun_Info_t audioFunInfo,byte[] data){
		Send_Audio_Data_Info_t info = new Send_Audio_Data_Info_t(data.length);
		
		if(param != null && audioFunInfo != null){
			info.nAudioType = param.audioType;
			info.nSampleRate = param.sampleRate;
			info.nTalkBits = param.audioBit;
			info.pData = data;
			info.pCallBackFun = audioFunInfo.pCallBackFun;
			info.pUserParam = audioFunInfo.pUserParam;
		}
		
		return info;
	}
	
	private SetDoorCmd_Request_t getSetDoorCmdRequest(InviteVtCallParam_t param,long start,long end){
		SetDoorCmd_Request_t request = new SetDoorCmd_Request_t();
		
		if(param != null){
			request.szCameraId = param.szUserId;
			request.cmd = dpsdk_SetDoorCmd_e.DPSDK_CORE_DOOR_CMD_OPEN;
			request.start = start;
			request.end = end;
		}
		
		return request;
	}
	
}
