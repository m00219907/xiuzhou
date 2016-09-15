package com.jsycloud.ir.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.SharePreferenceDataUtil;
import com.jsycloud.ir.xiuzhou.StartActivity;
import com.jsycloud.ir.xiuzhou.mefragment.ChangePasswordActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class TabRiverFragment2 extends Fragment implements View.OnClickListener{

    StartActivity activity;
    TextView river_fragment_username;
    View river_fragment_logout, change_password;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.river_fragment2, null);
        river_fragment_username = (TextView)view.findViewById(R.id.river_fragment_username);
        if(Constant.userfullname != null){
            river_fragment_username.setText(Constant.userfullname);
        }
        river_fragment_logout = view.findViewById(R.id.river_fragment_logout);
        change_password = view.findViewById(R.id.change_password);
        if(!Constant.isLogin){
            river_fragment_logout.setVisibility(View.GONE);
            change_password.setVisibility(View.GONE);
            startLoginActivity();
        }else {
            river_fragment_logout.setOnClickListener(this);
            change_password.setOnClickListener(this);
        }

        view.findViewById(R.id.river_fragment_about).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_remind).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_notice).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_assign).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_check_river).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_history).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_entrust).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_report).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_water_report).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_word).setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        reloadData();
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            reloadData();
        }
    }

    public void reloadData(){
        if(!Constant.isLogin){
            river_fragment_logout.setVisibility(View.GONE);
            change_password.setVisibility(View.GONE);
        }else {
            river_fragment_logout.setVisibility(View.VISIBLE);
            change_password.setVisibility(View.VISIBLE);
            if(Constant.userfullname != null){
                river_fragment_username.setText(Constant.userfullname);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.river_fragment_about:
                Intent intent0 = new Intent(activity, WebviewActivity.class);
                intent0.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/about.php");
                startActivity(intent0);
                break;
            case R.id.river_fragment_logout:
                Constant.isLogin = false;
                Constant.isLogByCode = false;
                Constant.username = "";
                Constant.usermobile = "";
                Constant.userfullname = "";
                SharePreferenceDataUtil.setSharedStringData(activity, "username", "");
                SharePreferenceDataUtil.setSharedStringData(activity, "userpassword", "");
                Constant.bReloadUrl = true;
                startLoginActivity();
                break;
            case R.id.change_password:
                Intent intent1 = new Intent(activity, ChangePasswordActivity.class);
                intent1.putExtra("bForce", false);
                startActivity(intent1);
                break;
            case R.id.river_fragment_remind://提醒
                if(Constant.isLogin) {
                    Intent intent2 = new Intent(activity, WebviewActivity.class);
                    intent2.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/remind.php");
                    startActivity(intent2);
                }else{
                    startLoginActivity();
                }
                break;
            case R.id.river_fragment_notice://治水办通知
                Intent intent3 = new Intent(activity, WebviewActivity.class);
                intent3.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/notify.php");
                startActivity(intent3);
                break;
            case R.id.river_fragment_assign://交办
                if(Constant.isLogin) {
                    Intent intent4 = new Intent(activity, WebviewActivity.class);
                    intent4.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/assign.php");
                    startActivity(intent4);
                }else{
                    startLoginActivity();
                }
                break;
            case R.id.river_fragment_check_river://巡河
                if(Constant.isLogin) {
                    Intent intent5 = new Intent(activity, CheckRiverActivity.class);
                    startActivity(intent5);
                }else{
                    startLoginActivity();
                }
                break;
            case R.id.river_fragment_history://巡河记录
                if(Constant.isLogin) {
                    //Intent intent6 = new Intent(activity, CheckRiverHistory.class);
                    //startActivity(intent6);

                    Intent intent6 = new Intent(activity, WebviewActivity.class);
                    intent6.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/patrol.php");
                    startActivity(intent6);
                }else{
                    startLoginActivity();
                }
                break;
            case R.id.river_fragment_entrust://委托巡河
                if(Constant.isLogin) {
                    Intent intent6 = new Intent(activity, EntrustActivity.class);
                    startActivity(intent6);
                }else{
                    startLoginActivity();
                }
                break;
            case R.id.river_fragment_report://上报
                if(Constant.isLogin) {
                    Intent intent7 = new Intent(activity, WebviewActivity.class);
                    intent7.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/report.php");
                    startActivity(intent7);
                }else{
                    startLoginActivity();
                }
                break;
            case R.id.river_fragment_water_report://水质报告
                if(Constant.isLogin) {
                    Intent intent8 = new Intent(activity, WebviewActivity.class);
                    intent8.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/waterquality.php");
                    startActivity(intent8);
                }else{
                    startLoginActivity();
                }
                break;
            case R.id.river_fragment_word://更多
                Intent intent9 = new Intent(activity, MoreActivity.class);
                startActivity(intent9);
                break;
            default:
                break;
        }
    }

    public void startLoginActivity(){
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivity(intent);
    }

}
