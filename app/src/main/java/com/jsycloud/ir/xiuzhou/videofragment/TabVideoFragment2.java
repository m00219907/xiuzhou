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
import com.jsycloud.groupTree.GroupListAdapter;
import com.jsycloud.groupTree.GroupListAdapter2;
import com.jsycloud.groupTree.GroupListAdapter3;
import com.jsycloud.groupTree.GroupListGetTask;
import com.jsycloud.groupTree.GroupListManager;
import com.jsycloud.groupTree.bean.ChannelInfoExt;
import com.jsycloud.groupTree.bean.TreeNode;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogUtils;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;

import java.io.File;


public class TabVideoFragment2 extends Fragment implements View.OnClickListener {

    private StartActivity activity;

    private AppApplication mAPP = AppApplication.get();

    TextView tab_guangdian, tab_yidong, tab_xiangzhen;

    private ListView list_guangdian, list_yidong, list_xiangzhen;

    // 搜索框adapter
    private GroupListAdapter adapter_guangdian;
    private GroupListAdapter2 adapter_yidong;
    private GroupListAdapter3 adapter_xiangzhen;

    // 获取实例
    private GroupListManager mGroupListManager = null;

    // 获取的树信息
    private TreeNode root = null;

    public static final int MSG_GROUPLIST_START = 1652;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list_fragment, null);
        list_guangdian = (ListView) view.findViewById(R.id.list_guangdian);
        list_yidong = (ListView) view.findViewById(R.id.list_yidong);
        list_xiangzhen = (ListView) view.findViewById(R.id.list_xiangzhen);

        tab_guangdian = (TextView)view.findViewById(R.id.tab_guangdian);
        tab_guangdian.setTag("1");
        tab_guangdian.setOnClickListener(this);
        tab_yidong = (TextView)view.findViewById(R.id.tab_yidong);
        tab_yidong.setTag("0");
        tab_yidong.setOnClickListener(this);
        tab_xiangzhen = (TextView)view.findViewById(R.id.tab_xiangzhen);
        tab_xiangzhen.setTag("0");
        tab_xiangzhen.setOnClickListener(this);

        adapter_guangdian = new GroupListAdapter(activity);
        list_guangdian.setAdapter(adapter_guangdian);

        adapter_yidong = new GroupListAdapter2(activity);
        list_yidong.setAdapter(adapter_yidong);

        adapter_xiangzhen = new GroupListAdapter3(activity);
        list_xiangzhen.setAdapter(adapter_xiangzhen);

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
                                adapter_guangdian.addNode(root);
                                adapter_guangdian.notifyDataSetChanged();
                            }else if(tab_yidong.getTag().equals("1")){
                                adapter_yidong.addNode(root);
                                adapter_yidong.notifyDataSetChanged();
                            }else {
                                adapter_xiangzhen.addNode(root);
                                adapter_xiangzhen.notifyDataSetChanged();
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
                    adapter_guangdian.clearNode();
                    if(tab_yidong.getTag().equals("1")) {
                        CommonTools.deleteDir(new File(Constant.appFolder+ "/dhsdk"));
                        new File(Constant.appFolder+ "/dhsdk").mkdirs();
                        IDpsdkCore.DPSDK_Logout(mAPP.getDpsdkCreatHandle(), 30000);
                        new LoginTask_guangdian().execute();
                        mGroupListManager.setRootNode(null);
                        mHandler.sendEmptyMessage(1038);
                    }else{
                        adapter_guangdian.addNode(root);
                        adapter_guangdian.notifyDataSetChanged();
                    }
                    tab_guangdian.setTextColor(0xff45c01a);
                    tab_guangdian.setTag("1");
                    tab_yidong.setTextColor(0xff9a9a9a);
                    tab_yidong.setTag("0");
                    tab_xiangzhen.setTextColor(0xff9a9a9a);
                    tab_xiangzhen.setTag("0");
                    list_guangdian.setVisibility(View.VISIBLE);
                    list_yidong.setVisibility(View.GONE);
                    list_xiangzhen.setVisibility(View.GONE);
                }
                break;
            case R.id.tab_yidong:
                if(tab_yidong.getTag().equals("0")) {
                    CommonTools.deleteDir(new File(Constant.appFolder+ "/dhsdk"));
                    new File(Constant.appFolder+ "/dhsdk").mkdirs();
                    IDpsdkCore.DPSDK_Logout(mAPP.getDpsdkCreatHandle(), 30000);
                    new LoginTask_yidong().execute();
                    adapter_yidong.clearNode();
                    mGroupListManager.setRootNode(null);
                    tab_guangdian.setTextColor(0xff9a9a9a);
                    tab_guangdian.setTag("0");
                    tab_yidong.setTextColor(0xff45c01a);
                    tab_yidong.setTag("1");
                    tab_xiangzhen.setTextColor(0xff9a9a9a);
                    tab_xiangzhen.setTag("0");
                    list_guangdian.setVisibility(View.GONE);
                    list_yidong.setVisibility(View.VISIBLE);
                    list_xiangzhen.setVisibility(View.GONE);
                    mHandler.sendEmptyMessage(1038);
                }
                break;
            case R.id.tab_xiangzhen:
                if(tab_xiangzhen.getTag().equals("0")) {
                    adapter_xiangzhen.clearNode();
                    if(tab_yidong.getTag().equals("1")) {
                        CommonTools.deleteDir(new File(Constant.appFolder+ "/dhsdk"));
                        new File(Constant.appFolder+ "/dhsdk").mkdirs();
                        IDpsdkCore.DPSDK_Logout(mAPP.getDpsdkCreatHandle(), 30000);
                        new LoginTask_guangdian().execute();
                        mGroupListManager.setRootNode(null);
                        mHandler.sendEmptyMessage(1038);
                    }else{
                        adapter_xiangzhen.addNode(root);
                        adapter_xiangzhen.notifyDataSetChanged();
                    }
                    tab_guangdian.setTextColor(0xff9a9a9a);
                    tab_guangdian.setTag("0");
                    tab_yidong.setTextColor(0xff9a9a9a);
                    tab_yidong.setTag("0");
                    tab_xiangzhen.setTextColor(0xff45c01a);
                    tab_xiangzhen.setTag("1");
                    list_guangdian.setVisibility(View.GONE);
                    list_yidong.setVisibility(View.GONE);
                    list_xiangzhen.setVisibility(View.VISIBLE);
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

    class LoginTask_yidong extends AsyncTask<Void, Integer, Integer> {

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

    class LoginTask_guangdian extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... arg0) {
            Login_Info_t loginInfo = new Login_Info_t();
            loginInfo.szIp = "122.225.61.100".getBytes();
            String strPort = "8001";
            loginInfo.nPort = Integer.parseInt(strPort);
            loginInfo.szUsername = "iriver".getBytes();
            loginInfo.szPassword = "ir123456".getBytes();
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
