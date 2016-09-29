package com.jsycloud.groupTree;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.Demo.TestDpsdkCoreActivity;
import com.dh.DpsdkCore.IDpsdkCore;
import com.jsycloud.activity.AlarmbuKongActivity;
import com.jsycloud.activity.BackPlayActivity;
import com.jsycloud.activity.OperateSoundTalk;
import com.jsycloud.activity.RealPlayActivity;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.baseclass.BaseActivity;
import com.jsycloud.groupTree.GroupListGetTask.IOnSuccessListener;
import com.jsycloud.groupTree.SearchChannelsAdapter.IOnSearchChannelsClick;
import com.jsycloud.groupTree.bean.ChannelInfoExt;
import com.jsycloud.groupTree.bean.TreeNode;
import com.jsycloud.util.AppDefine;
import com.jsycloud.view.PullDownListView;


public class GroupListActivity extends BaseActivity implements OnClickListener,
         PullDownListView.OnRefreshListioner,
        IOnSearchChannelsClick {

    // 打印标签
    private static final String TAG = "GroupListActivity";

    // 组织树控件
    private ListView mGroupsLv;

    // 搜索播放控件
    private Button mConfirmBtn = null;

    // 搜索框adapter
    private GroupListAdapter mGroupListAdapter = null;

    // 获取实例
    private GroupListManager mGroupListManager = null;

    // 选中的nodes
    private List<TreeNode> selectNnodes = null;

    // 下拉刷新对象
    private PullDownListView mPullDownView = null;

    // 获取的树信息
    private TreeNode root = null;

    // 消息对象
    private Handler mHandler = null;

    // 等待对话框
    private ProgressBar mWattingPb = null;

    // 从哪个页面过来 1： 从实时预览进入组织列表 2：从回放进入组织列:3: 从电子地图进入组织列表
    private int comeFrom = 0;
    
    /** 更新列表消息(const value:1000) */
    public static final int MSG_GROUPLIST_UPDATELIST = 1000;
    
    /** 点击进入回放消息 */
    public static final int MSG_GROUP_TO_PLAYBACK = 1005;

    private String deviceName;
    private String[] dialogList;
    private LinearLayout layLogout;
    private String mDeviceId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_fragment);

        // 查找控件
        findViews();

        // 设置监听器
        setListener();

        // 初始化数据
        initDate();
    }

    /**
     * <p>
     * 获取布局控件
     * </p>
     * 
     * @author fangzhihua 2014-5-6 下午2:27:25
     */
    private void findViews() {
        layLogout= (LinearLayout)findViewById(R.id.title_lay);
    }

    /**
     * <p>
     * 设置控件的监听事件
     * </p>
     * 
     * @author fangzhihua 2014-5-6 下午2:34:04
     */
    private void setListener() {
    	layLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int nPDLLHandle = AppApplication.get().getDpsdkHandle();
				int nRet = IDpsdkCore.DPSDK_Logout(nPDLLHandle, 30000);
				if(nRet == 0) {
					showToast(getResources().getString(R.string.logout));
				} else {
					showToast(getResources().getString(R.string.logout_fail));
				}
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), TestDpsdkCoreActivity.class);
				startActivity(intent);
				GroupListActivity.this.finish();
			}
		});
        // 开始播放控件点击
        mConfirmBtn.setOnClickListener(this);
        // 下拉刷新
        mPullDownView.setRefreshListioner(this);
    }

    /**
     * <p>
     * 初始化数据
     * </p>
     * 
     * @author fangzhihua 2014-5-7 上午10:05:54
     */
    private void initDate() {

        Display display = this.getWindowManager().getDefaultDisplay();

        mHandler = createHandler();
        mGroupsLv = mPullDownView.mListView;
        mGroupListManager = GroupListManager.getInstance();
        mGroupListAdapter = new GroupListAdapter(this);
        updateSelectChannels();
        
        mGroupsLv.setAdapter(mGroupListAdapter);
        getGroupList();

    }

    /**
     * <p>
     * 获取组织列表
     * </p>
     * 
     * @author fangzhihua 2014-5-12 上午9:56:14
     */
    private void getGroupList() {
        root = mGroupListManager.getRootNode();
        if (root == null) {
            mWattingPb.setVisibility(View.VISIBLE);
        }

        if (mGroupListManager.getTask() != null) {
            mGroupListManager.setGroupListGetListener(mIOnSuccessListener);
        }
        if (mGroupListManager.isFinish() && root != null) {
        	if (root.getChildren().size() == 0) {
        		 mGroupListManager.startGroupListGetTask();
        	}
        	Log.i(TAG, "getGroupList finished---" + root.getChildren().size());
            sendMessage(mHandler, MSG_GROUPLIST_UPDATELIST, 0, 0);
            return;
        } else if (root == null) {
            if (mGroupListManager.getTask() == null) {
                // 获取组织树任务
            	Log.i(TAG, "开始 执行GroupListGetTask");
                mGroupListManager.startGroupListGetTask();
                mGroupListManager.setGroupListGetListener(mIOnSuccessListener);
            }
        }

    }

    private Handler createHandler() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_GROUPLIST_UPDATELIST:
                        // 处理更新列表
                        handleUpdateList();
                        break;
                    case MSG_GROUP_TO_PLAYBACK:
                        //handleClickPlayback(msg.obj, msg.arg1);     //处理点击回放
                    default:
                        break;
                }
            }

            /**
             * <p>
             * 处理更新列表
             * </p>
             * 
             * @author fangzhihua 2014年7月10日 上午9:07:35
             */
            private void handleUpdateList() {
                root = mGroupListManager.getRootNode();

                mGroupListManager.setOnSuccessListener(mIOnSuccessListener);

                // 这里表示刷新处理完成后把上面的加载刷新界面隐藏
                mPullDownView.onRefreshComplete();

                if (mWattingPb != null) {
                    mWattingPb.setVisibility(View.GONE);
                }

                mGroupListAdapter.clearDate();
                mGroupListAdapter.addNode(root);
                // 设置默认展开级别
                mGroupListAdapter.notifyDataSetChanged();

            }


        };

        return handler;
    }

    IOnSuccessListener mIOnSuccessListener = new IOnSuccessListener() {
        @Override
        public void onSuccess(final boolean success, final int errCode) {
        	
        	mHandler.post(new Runnable() {
				
				@Override
				public void run() {
		            // 清空任务
		            mGroupListManager.setTask(null);

		            if (mWattingPb != null) {
		                mWattingPb.setVisibility(View.GONE);
		            }
		            if (success) {
		                root = mGroupListManager.getRootNode();
		                if (root != null) {
		                    mGroupListAdapter.clearDate();
		                    mGroupListAdapter.addNode(root);
		                    // 设置默认展开级别
		                    mGroupListAdapter.notifyDataSetChanged();
		                } else {
		                    mGroupListAdapter.clearDate();
		                    mGroupListAdapter.notifyDataSetChanged();
		                }
		                //updateSelectChannels();
		            } else {
		                showToast(R.string.grouplist_getgroup_fail, errCode);
		            }

				}
			});
        }
    };

	@Override
    public void onDestroy() {
        super.onDestroy();
      //  mGroupListHelper.closeSetTimePopupWindow();
        if(mGroupListManager.getRootNode() != null) {
        	mGroupListManager.setRootNode(null);
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void updateSelectChannels() {
        if (selectNnodes.size() > 32) {
            showToast(R.string.select_channel_limit_tv);
        }
    }

    @Override
    public void onRefresh() {
        getGroupList();
    	Log.i("GroupListActivity", "onRefresh..");
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mGroupListManager.getRootNode() != null) {
            	mGroupListManager.setRootNode(null);
            	Log.i("TAG", "onKeyDown");
            }
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onSearchChannelsClick(ChannelInfoExt channelInfoExt, boolean flag) {
        // 保存历史搜索
      /*  saveHistory("history", mSearchAutoEt);
        // 隐藏搜索框
        InputMethodManager inputManager = (InputMethodManager) mSearchAutoEt.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(mSearchAutoEt.getWindowToken(), 0);
        }*/
        // 如果是多选，则不处理
        if (!flag) {
            return;
        }
        switch (comeFrom) {
            case AppDefine.FROM_LIVE_TO_GROUPLIST:

                Intent mIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean(AppDefine.FROM_GROUPLIST, true);
                bundle.putBoolean(AppDefine.NEED_PLAY, true);
                bundle.putSerializable(AppDefine.SELECTED_CHANNEL, channelInfoExt);
                mIntent.putExtras(bundle);
                setResult(RESULT_FIRST_USER, mIntent);
                finish();
                break;
            case AppDefine.FROM_PLAYBACK_TO_GROUPLIST:
                break;
            case AppDefine.FROM_GIS_TO_GROUPLIST:
                Intent mIntent2 = new Intent();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(AppDefine.SELECTED_CHANNEL, channelInfoExt);
                mIntent2.putExtras(bundle2);
                setResult(RESULT_OK, mIntent2);
                finish();
                break;
            default:
                break;
        }
    }

	@Override
	public void onClick(View arg0) {
		
	}

}
