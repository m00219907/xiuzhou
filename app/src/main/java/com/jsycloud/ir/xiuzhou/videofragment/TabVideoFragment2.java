package com.jsycloud.ir.xiuzhou.videofragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Login_Info_t;
import com.jsycloud.activity.RealPlayActivity;
import com.jsycloud.groupTree.GroupListAdapter2;
import com.jsycloud.groupTree.GroupListAdapter3;
import com.jsycloud.groupTree.GroupListGetTask;
import com.jsycloud.groupTree.GroupListManager;
import com.jsycloud.groupTree.bean.ChannelInfoExt;
import com.jsycloud.groupTree.bean.TreeNode;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.ir.xiuzhou.DialogUtils;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;


public class TabVideoFragment2 extends Fragment implements GroupListAdapter2.IOnItemClickListener, GroupListAdapter3.IOnItemClickListener, View.OnClickListener {

    private StartActivity activity;

    private AppApplication mAPP = AppApplication.get();

    TextView tab_guangdian, tab_yidong, tab_xiangzhen;

    private ListView mGroupsLv, group_list3;

    // 搜索框adapter
    private GroupListAdapter2 mGroupListAdapter = null;
    private GroupListAdapter3 mGroupListAdapter3 = null;

    // 获取实例
    private GroupListManager mGroupListManager = null;

    // 获取的树信息
    private TreeNode root = null;

    public static final int MSG_GROUPLIST_START = 1652;
    public static final int MSG_GROUPLIST_GETLIST = 1653;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list_fragment, null);
        mGroupsLv = (ListView) view.findViewById(R.id.group_list);
        group_list3 = (ListView) view.findViewById(R.id.group_list3);

        tab_guangdian = (TextView)view.findViewById(R.id.tab_guangdian);
        tab_guangdian.setTag("1");
        tab_guangdian.setOnClickListener(this);
        tab_yidong = (TextView)view.findViewById(R.id.tab_yidong);
        tab_yidong.setTag("0");
        tab_yidong.setOnClickListener(this);
        tab_xiangzhen = (TextView)view.findViewById(R.id.tab_xiangzhen);
        tab_xiangzhen.setTag("0");
        tab_xiangzhen.setOnClickListener(this);

        mGroupListAdapter = new GroupListAdapter2(activity);
        mGroupListAdapter.setListner(this);
        mGroupsLv.setAdapter(mGroupListAdapter);

        mGroupListAdapter3 = new GroupListAdapter3(activity);
        mGroupListAdapter3.setListner(this);
        group_list3.setAdapter(mGroupListAdapter3);

        mGroupListManager = GroupListManager.getInstance();
        getGroupList();
        mHandler.sendEmptyMessage(1038);

        return view;
    }

    private void getGroupList() {
        root = mGroupListManager.getRootNode();

        if (mGroupListManager.getTask() != null) {
            mGroupListManager.setGroupListGetListener(mIOnSuccessListener);
        }
        if (mGroupListManager.isFinish() && root != null) {
            if (root.getChildren().size() == 0) {
                mGroupListManager.startGroupListGetTask();
            }
        } else if (root == null) {
            if (mGroupListManager.getTask() == null) {
                mGroupListManager.startGroupListGetTask();
                mGroupListManager.setGroupListGetListener(mIOnSuccessListener);
            }
        }
    }

    Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GROUPLIST_START:
                    getGroupList();
                    break;
                case MSG_GROUPLIST_GETLIST:
                    root = mGroupListManager.getRootNode();
                    mGroupListManager.setOnSuccessListener(mIOnSuccessListener);

                    mGroupListAdapter.addNode(root);
                    mGroupListAdapter.notifyDataSetChanged();
                    break;

                case 1038:
                    if (!DialogUtils.isShowWaitDialog()) {
                        DialogUtils.showWaitDialog(activity, "加载中...", -1);
                    }
                    break;
                case 1039:
                    if (DialogUtils.isShowWaitDialog()) {
                        DialogUtils.dismissDialog();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    GroupListGetTask.IOnSuccessListener mIOnSuccessListener = new GroupListGetTask.IOnSuccessListener() {
        @Override
        public void onSuccess(final boolean success, final int errCode) {

            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // 清空任务
                    mGroupListManager.setTask(null);
                    mHandler.sendEmptyMessage(1039);

                    if (success) {
                        root = mGroupListManager.getRootNode();
                        if (root != null) {
                            if(tab_guangdian.getTag().equals("1")) {
                                mGroupListAdapter3.addNode(root);
                                mGroupListAdapter3.notifyDataSetChanged();
                            }else if(tab_yidong.getTag().equals("1")){
                                mGroupListAdapter.addNode(root);
                                mGroupListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_guangdian:
                if(tab_guangdian.getTag().equals("0")) {
                    mGroupListAdapter3.clearNode();
                    mGroupListManager.setRootNode(null);
                    tab_guangdian.setTextColor(0xff45c01a);
                    tab_guangdian.setTag("1");
                    tab_yidong.setTextColor(0xff9a9a9a);
                    tab_yidong.setTag("0");
                    tab_xiangzhen.setTextColor(0xff9a9a9a);
                    tab_xiangzhen.setTag("0");
                    group_list3.setVisibility(View.VISIBLE);
                    mGroupsLv.setVisibility(View.GONE);
                    IDpsdkCore.DPSDK_Logout(mAPP.getDpsdkCreatHandle(), 30000);
                    new LoginTask2().execute();
                    mHandler.sendEmptyMessage(1038);
                }
                break;
            case R.id.tab_yidong:
                if(tab_yidong.getTag().equals("0")) {
                    mGroupListAdapter.clearNode();
                    mGroupListManager.setRootNode(null);
                    tab_guangdian.setTextColor(0xff9a9a9a);
                    tab_guangdian.setTag("0");
                    tab_yidong.setTextColor(0xff45c01a);
                    tab_yidong.setTag("1");
                    tab_xiangzhen.setTextColor(0xff9a9a9a);
                    tab_xiangzhen.setTag("0");
                    mGroupsLv.setVisibility(View.VISIBLE);
                    group_list3.setVisibility(View.GONE);
                    IDpsdkCore.DPSDK_Logout(mAPP.getDpsdkCreatHandle(), 30000);
                    new LoginTask().execute();
                    mHandler.sendEmptyMessage(1038);
                }
                break;
            case R.id.tab_xiangzhen:
                if(tab_xiangzhen.getTag().equals("0")) {
                    tab_guangdian.setTextColor(0xff9a9a9a);
                    tab_guangdian.setTag("0");
                    tab_yidong.setTextColor(0xff9a9a9a);
                    tab_yidong.setTag("0");
                    tab_xiangzhen.setTextColor(0xff45c01a);
                    tab_xiangzhen.setTag("1");
                    mHandler.sendEmptyMessage(1038);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGroupListManager.getRootNode() != null) {
            mGroupListManager.setRootNode(null);
        }
    }

    @Override
    public void onItemClick(TreeNode treeNode, boolean isChecked, final int position) {
        if (treeNode.getType() == 3) {
            ChannelInfoExt chnlInfoExt = ((TreeNode)mGroupListAdapter.getItem(position)).getChannelInfo();
            String channelName =  chnlInfoExt.getSzName();
            String channelId = chnlInfoExt.getSzId();
            String deviceId = chnlInfoExt.getDeviceId();
            Intent intent = new Intent();
            intent.putExtra("channelName", channelName);
            intent.putExtra("channelId", channelId);
            intent.putExtra("deviceId", deviceId);
            intent.setClass(activity, RealPlayActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick3(TreeNode treeNode, boolean isChecked, final int position) {

        if (treeNode.getType() == 3) {
            ChannelInfoExt chnlInfoExt = ((TreeNode)mGroupListAdapter3.getItem(position)).getChannelInfo();
            String channelName =  chnlInfoExt.getSzName();
            String channelId = chnlInfoExt.getSzId();
            String deviceId = chnlInfoExt.getDeviceId();
            Intent intent = new Intent();
            intent.putExtra("channelName", channelName);
            intent.putExtra("channelId", channelId);
            intent.putExtra("deviceId", deviceId);
            intent.setClass(activity, RealPlayActivity.class);
            startActivity(intent);
        }
    }

    class LoginTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... arg0) {
            Login_Info_t loginInfo = new Login_Info_t();
            loginInfo.szIp = "111.1.31.147".getBytes();
            String strPort = "9000";
            loginInfo.nPort = Integer.parseInt(strPort);
            loginInfo.szUsername = "xzzsb".getBytes();
            loginInfo.szPassword = "abcd1234".getBytes();
            loginInfo.nProtocol = 2;
            return IDpsdkCore.DPSDK_Login(mAPP.getDpsdkCreatHandle(), loginInfo, 30000);
        }

        @Override
        protected void onPostExecute(Integer result) {

            super.onPostExecute(result);
            if (result == 0) {
                Log.d("DpsdkLogin success:", result + "");
                IDpsdkCore.DPSDK_SetCompressType(mAPP.getDpsdkCreatHandle(), 0);
                mAPP.setLoginHandler(1);
                mHandler.sendEmptyMessage(MSG_GROUPLIST_START);
            } else {
                Log.d("DpsdkLogin failed:",result+"");
                Toast.makeText(activity, "login failed" + result, Toast.LENGTH_SHORT).show();
                mAPP.setLoginHandler(0);
            }
        }

    }

    class LoginTask2 extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... arg0) {
            Login_Info_t loginInfo = new Login_Info_t();
            loginInfo.szIp = "122.225.61.100".getBytes();
            String strPort = "8001";
            loginInfo.nPort = Integer.parseInt(strPort);
            loginInfo.szUsername = "admin".getBytes();
            loginInfo.szPassword = "jsy2016.2".getBytes();
            loginInfo.nProtocol = 2;
            return IDpsdkCore.DPSDK_Login(mAPP.getDpsdkCreatHandle(), loginInfo, 30000);
        }

        @Override
        protected void onPostExecute(Integer result) {

            super.onPostExecute(result);
            if (result == 0) {
                Log.d("DpsdkLogin success:", result + "");
                IDpsdkCore.DPSDK_SetCompressType(mAPP.getDpsdkCreatHandle(), 0);
                mAPP.setLoginHandler(1);
                mHandler.sendEmptyMessage(MSG_GROUPLIST_START);
            } else {
                Log.d("DpsdkLogin failed:",result+"");
                Toast.makeText(activity, "login failed" + result, Toast.LENGTH_SHORT).show();
                mAPP.setLoginHandler(0);
            }
        }
    }
}
