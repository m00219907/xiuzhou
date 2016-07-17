/* 
 * @ProjectName VideoGoJar
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName CameraListActivity.java
 * @Description 这里对文件进行描述
 * 
 * @author xia xingsuo
 * @data 2015-11-5
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.jsycloud.ir.xiuzhou.videofragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 摄像头列表
 * @author xiaxingsuo
 * @data 2014-7-14
 */
public class EZCameraListActivity extends Activity implements View.OnClickListener  {
    protected static final String TAG = "CameraListActivity";
    /** 删除设备 */
    private final static int SHOW_DIALOG_DEL_DEVICE = 1;
    
    //private EzvizAPI mEzvizAPI = null;
    private BroadcastReceiver mReceiver = null;

    private ListView mListView = null;
    private View mNoMoreView;
    private EZCameraListAdapter mAdapter = null;
    
    private LinearLayout mNoCameraTipLy = null;
    private LinearLayout mGetCameraFailTipLy = null;
    private TextView mCameraFailTipTv = null;
    
    private EZOpenSDK mEZOpenSDK = null;
    private boolean bIsFromSetting = false;

    SwipeRefreshLayout myplayers_ptr_frame;

    List<EZCameraInfo> result = new ArrayList<EZCameraInfo>();

    private Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1003:
                    mAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.cameralist_page);

        myplayers_ptr_frame = (SwipeRefreshLayout)findViewById(R.id.myplayers_ptr_frame);
        myplayers_ptr_frame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCommentList();
            }
        });
        initView();
        initData();
        Utils.clearAllNotification(this);
    }

    private void initView() {
        
        mAdapter = new EZCameraListAdapter(this, result);
        mListView = (ListView) findViewById(R.id.camera_listview);
        mListView.setAdapter(mAdapter);
    }
    
    private void initData() {
        mEZOpenSDK = EZOpenSDK.getInstance();
        getCommentList();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if( bIsFromSetting || (mAdapter != null && mAdapter.getCount() == 0)) {
            //refreshButtonClicked();
            bIsFromSetting = false;
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if(mAdapter != null) {
            mAdapter.shutDownExecutorService();
            mAdapter.clearImageCache();
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void getCommentList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EZCameraInfo> curResult = null;
                    if(result.size()==0) {
                        curResult = mEZOpenSDK.getCameraList(0, 20);
                    } else {
                        curResult = mEZOpenSDK.getCameraList(result.size()/20, 20);
                    }

                    if(curResult != null) {
                        mHandler.sendEmptyMessage(1003);
                        result.addAll(curResult);
                    }

                } catch (BaseException e) {
                }
            }
        }).start();
    }
}
