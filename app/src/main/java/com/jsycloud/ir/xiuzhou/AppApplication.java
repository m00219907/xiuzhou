package com.jsycloud.ir.xiuzhou;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.dh.DpsdkCore.Device_Info_Ex_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.InviteVtCallParam_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.RingInfo_t;
import com.dh.DpsdkCore.dpsdk_constant_value;
import com.dh.DpsdkCore.fDPSDKInviteVtCallParamCallBack;
import com.dh.DpsdkCore.fDPSDKRingInfoCallBack;
import com.dh.DpsdkCore.fDPSDKStatusCallback;
import com.jsycloud.activity.AutoVtActivity;
import com.jsycloud.groupTree.GroupListManager;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class AppApplication extends Application {

    private static final String TAG = "AppApplication";
    //private static final String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + "/DPSDKlog.txt";

    private static AppApplication _instance;
    private int m_loginHandle = 0;   //鏍囪鐧诲綍鏄惁鎴愬姛   1鐧诲綍鎴愬姛   0鐧诲綍澶辫触
    private int m_nLastError = 0;
    private Return_Value_Info_t m_ReValue = new Return_Value_Info_t();

    public static synchronized AppApplication get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        _instance = this;
        initApp();

        /*File appDirectory = new File( Environment.getExternalStorageDirectory() + "/秀洲智慧河道日志" );
        File logFile = new File( appDirectory, "logcat" + System.currentTimeMillis() + ".log" );

        if ( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }

        try {
            Process process = Runtime.getRuntime().exec( "logcat -c");
            process = Runtime.getRuntime().exec( "logcat -f " + logFile + " *:E");
        } catch ( IOException e ) {
            e.printStackTrace();
        }*/
        
    }

    /**
     * 鍏ㄥ眬鍒濆鍖栵紝鍦⊿plashActivity涓皟鐢�
     */
    public void initApp() {

        //Creat DPSDK
        Log.d("initApp:", m_nLastError + "");
        int nType = 1;
        m_nLastError = IDpsdkCore.DPSDK_Create(nType, m_ReValue);
        Log.d("DpsdkCreate:",m_nLastError+"");

        //set logPath
        //m_nLastError = IDpsdkCore.DPSDK_SetLog(m_ReValue.nReturnValue, LOG_PATH.getBytes());

        IDpsdkCore.DPSDK_SetDPSDKStatusCallback(m_ReValue.nReturnValue, new fDPSDKStatusCallback() {

            @Override
            public void invoke(int nPDLLHandle, int nStatus) {
                Log.v("fDPSDKStatusCallback", "nStatus = " + nStatus);
            }
        });

        //璁剧疆璁惧鎷ㄥ彿鐩戝惉鍣�
        IDpsdkCore.DPSDK_SetRingCallback(m_ReValue.nReturnValue, new fDPSDKRingInfoCallBack() {

            @Override
            public void invoke(int nPDLLHandle, RingInfo_t param) {
                //鑾峰彇鎷ㄥ彿淇℃伅
                Log.e(TAG, "fDPSDKRingInfoCallBack RingInfo_t info"
                        +"      callId : "+ param.callId);

                //鐣岄潰璺宠浆
                Intent intent = new Intent(AppApplication.this, AutoVtActivity.class);
                Bundle bundle = new Bundle();

                bundle.putByteArray("szUserId", param.szUserId);
                bundle.putInt("callId", param.callId);
                bundle.putInt("dlgId", param.dlgId);
                bundle.putInt("tid", param.tid);

                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);      //鑻ュ湪涓嶅悓鐨勮繘绋嬮渶瑕佹坊鍔爁lag,
                startActivity(intent);
            }
        });

        //璁剧疆鍙瀵硅鍛煎彨閭�璇峰弬鏁板洖璋�
        IDpsdkCore.DPSDK_SetVtCallInviteCallback(m_ReValue.nReturnValue, new fDPSDKInviteVtCallParamCallBack() {

            @Override
            public void invoke(int nPDLLHandle, InviteVtCallParam_t param) {

                //閫氳繃璁惧鍛煎彨鍙风爜锛屾煡鎵捐澶噄d锛屽啀鏌ユ壘璁惧鐨勭紪鐮侀�氶亾
                String strcallnum = new String(param.szUserId).trim();

                List<Device_Info_Ex_t> devlist = GroupListManager.getInstance().getDeviceExList();
                Device_Info_Ex_t deviceInfoEx;
                byte[] szId = new byte[dpsdk_constant_value.DPSDK_CORE_DEV_ID_LEN];
                String channelname = "";

                for(int i=0;i<devlist.size();i++){
                    deviceInfoEx = devlist.get(i);
                    String szCallNum = new String(deviceInfoEx.szCallNum).trim();
                    if(deviceInfoEx != null  && strcallnum.equals(szCallNum)){ //鍖归厤璁惧鍛煎彨鍙风爜
                        //channellist = GroupListManager.getInstance().getChannelsByDeviceId(deviceInfoEx.szId);  //閫氳繃璁惧id鏌ユ壘缂栫爜閫氶亾

//						if(channellist != null && channellist.size()>0){
//							encChannelInfoEx = channellist.get(0);   //鍙栫紪鐮侀�氶亾涓殑绗竴涓�
//						}
                        byte[] bt = (new String(deviceInfoEx.szId).trim()+"$1$0$0").getBytes();
                        System.arraycopy(bt, 0, szId, 0, bt.length);

                        channelname = new String(szId).trim();
                        Log.e(TAG, "****************channelid****************"+"           "+channelname );
                    }
                }

                //鐣岄潰璺宠浆
                Intent intent = new Intent(AppApplication.this, AutoVtActivity.class);
                Bundle bundle = new Bundle();
                bundle.putByteArray("szUserId", param.szUserId);
                bundle.putInt("audioType", param.audioType);
                bundle.putInt("audioBit", param.audioBit);
                bundle.putInt("sampleRate", param.sampleRate);
                bundle.putByteArray("rtpServIP", param.rtpServIP);
                bundle.putInt("rtpAPort", param.rtpAPort);
                bundle.putInt("rtpVPort", param.rtpVPort);
                bundle.putInt("nCallType", param.nCallType);
                bundle.putInt("tid", param.tid);
                bundle.putInt("callId", param.callId);
                bundle.putInt("dlgId", param.dlgId);

//				bundle.putByteArray("channelid", encChannelInfoEx.szId);
//				bundle.putByteArray("channelname", encChannelInfoEx.szName);
                bundle.putByteArray("channelid", szId);
                bundle.putByteArray("channelname", szId);

                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);      //鑻ュ湪涓嶅悓鐨勮繘绋嬮渶瑕佹坊鍔爁lag,
                startActivity(intent);

            }
        });
    }



    public int getDpsdkHandle(){
        if(m_loginHandle == 1)  //鐧诲綍鎴愬姛锛岃繑鍥濸DSDK_Creat鏃惰繑鍥炵殑 鏈夋晥鍙ユ焺
            return m_ReValue.nReturnValue;
        else
            return 0;
    }

    public int getDpsdkCreatHandle(){  //浠呯敤浜庤幏鍙朌PSDK_login鐨勫彞鏌�
        return m_ReValue.nReturnValue;
    }

    public void setLoginHandler(int loginhandler){
        this.m_loginHandle = loginhandler;
    }

    public int getLoginHandler(){
        return this.m_loginHandle;
    }

}
