package com.jsycloud.ir.xiuzhou.riverfragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.SharePreferenceDataUtil;
import com.jsycloud.ir.xiuzhou.StartActivity;
import com.jsycloud.ir.xiuzhou.mefragment.ChangePasswordActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class TabRiverFragment2 extends Fragment implements View.OnClickListener{

    StartActivity activity;
    View river_fragment_login, river_fragment_unlogin;
    TextView river_fragment_username;
    EditText river_fragment_user, river_fragment_password;

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

        river_fragment_login = view.findViewById(R.id.river_fragment_login);
        river_fragment_unlogin = view.findViewById(R.id.river_fragment_unlogin);
        river_fragment_user = (EditText)view.findViewById(R.id.river_fragment_user);
        river_fragment_password = (EditText)view.findViewById(R.id.river_fragment_password);

        view.findViewById(R.id.river_fragment_logout).setOnClickListener(this);
        view.findViewById(R.id.change_password).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_remind).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_notice).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_assign).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_check_river).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_history).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_entrust).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_report).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_water_report).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_word).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_log).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_logwithcode).setOnClickListener(this);

        if(Constant.isLogin){
            river_fragment_login.setVisibility(View.VISIBLE);
            river_fragment_unlogin.setVisibility(View.GONE);
        }else{
            river_fragment_login.setVisibility(View.GONE);
            river_fragment_unlogin.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.river_fragment_logout:
                Constant.isLogin = false;
                Constant.isLogByCode = false;
                Constant.username = "";
                Constant.usermobile = "";
                Constant.userfullname = "";
                SharePreferenceDataUtil.setSharedStringData(activity, "username", "");
                SharePreferenceDataUtil.setSharedStringData(activity, "userpassword", "");
                Constant.bReloadUrl = true;
                river_fragment_login.setVisibility(View.GONE);
                river_fragment_unlogin.setVisibility(View.VISIBLE);
                break;
            case R.id.change_password:
                Intent intent1 = new Intent(activity, ChangePasswordActivity.class);
                intent1.putExtra("bForce", false);
                startActivity(intent1);
                break;
            case R.id.river_fragment_remind://提醒
                Intent intent2 = new Intent(activity, WebviewActivity.class);
                intent2.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/remind.php");
                startActivity(intent2);
                break;
            case R.id.river_fragment_notice://治水办通知
                Intent intent3 = new Intent(activity, WebviewActivity.class);
                intent3.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/notify.php");
                startActivity(intent3);
                break;
            case R.id.river_fragment_assign://交办
                Intent intent4 = new Intent(activity, WebviewActivity.class);
                intent4.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/assign.php");
                startActivity(intent4);
                break;
            case R.id.river_fragment_check_river://巡河
                Intent intent5 = new Intent(activity, CheckRiverActivity.class);
                startActivity(intent5);
                break;
            case R.id.river_fragment_history://巡河记录
                Intent intent6 = new Intent(activity, CheckRiverHistory.class);
                startActivity(intent6);
                break;
            case R.id.river_fragment_entrust://委托巡河
                getauthcode(false);
                break;
            case R.id.river_fragment_report://上报
                Intent intent7 = new Intent(activity, WebviewActivity.class);
                intent7.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/report.php");
                startActivity(intent7);
                break;
            case R.id.river_fragment_water_report://水质报告
                Intent intent8 = new Intent(activity, WebviewActivity.class);
                intent8.putExtra("url", HttpClentLinkNet.BaseAddr + "pages/waterquality.php");
                startActivity(intent8);
                break;
            case R.id.river_fragment_word://更多
                break;
            case R.id.river_fragment_log://登录
                login();
                break;
            case R.id.river_fragment_logwithcode://委托码登录
            case R.id.login_use_code:
                DialogShow.dialogShow2(activity, "请输入8位委托码", "登录", new DialogShow.IloginClick() {
                    @Override
                    public void OnClick(String passcode) {
                        loginbycode(passcode);
                    }
                });
                break;
        }
    }


    public void getauthcode(final boolean bNew) {
        String url = HttpClentLinkNet.BaseAddr + "getauthcode.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        if(bNew){
            params.put("isnew", "1");
        }
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                String success = "";

                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("success")) {
                        success = jsObj.getString("success");
                    }
                    if(success.equals("1")) {
                        if(bNew){
                            Toast.makeText(activity, "重新获取到的委托码是：" + jsObj.getString("authcode"), Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.putExtra("sms_body", "巡河委托码：" + jsObj.getString("authcode"));
                            intent.setType("vnd.android-dir/mms-sms");
                            activity.startActivity(intent);
                        }
                    }else if(success.equals("2")){
                        if(bNew){
                            Toast.makeText(activity, "重新获取委托码失败", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity, "当前无委托码", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


    public void login() {
        final String username = river_fragment_user.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(activity, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String userpassword = river_fragment_password.getText().toString();
        if(userpassword.isEmpty()){
            Toast.makeText(activity, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = HttpClentLinkNet.BaseAddr + "login.php";
        AjaxParams params = new AjaxParams();
        params.put("username", username);
        params.put("userpassword", userpassword);
        params.put("from", android.os.Build.MODEL);
        params.put("deviceid", CommonTools.getMacAddress(activity));
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                String success = "";

                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("success")) {
                        success = jsObj.getString("success");
                    }
                    if(success.equals("1")) {
                        if (jsObj.has("userid")) {
                            Constant.userid = jsObj.getString("userid");
                        }
                        if (jsObj.has("username")) {
                            Constant.username = jsObj.getString("username");
                        }
                        if (jsObj.has("userfullname")) {
                            Constant.userfullname = jsObj.getString("userfullname");
                        }
                        if (jsObj.has("usermobile")) {
                            Constant.usermobile = jsObj.getString("usermobile");
                        }
                        if (jsObj.has("usergroup")) {
                            Constant.usergroup = jsObj.getString("usergroup");
                        }
                        if (jsObj.has("videorights")) {
                            Constant.videorights = jsObj.getString("videorights");
                        }
                        boolean setAliasAndTags = SharePreferenceDataUtil.getSharedBooleanData(activity, "setAliasAndTags");
                        if(!setAliasAndTags) {
                            String alias = jsObj.getString("alias");
                            Set<String> tags = new HashSet<String>();
                            tags.add(jsObj.getString("tagcity"));
                            tags.add(jsObj.getString("tagdistrict"));
                            tags.add(jsObj.getString("tagtown"));
                            tags.add(jsObj.getString("taglevel"));
                            JPushInterface.setAliasAndTags(activity.getApplicationContext(), alias, JPushInterface.filterValidTags(tags), callback);
                        }
                        Constant.isLogin = true;
                        Constant.isLogByCode = false;
                        Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show();
                        river_fragment_login.setVisibility(View.VISIBLE);
                        river_fragment_unlogin.setVisibility(View.GONE);
                        activity.onLoginChange();
                        SharePreferenceDataUtil.setSharedStringData(activity, "username", username);
                        SharePreferenceDataUtil.setSharedStringData(activity, "userpassword", userpassword);
                        Constant.bReloadUrl = true;
                        if(jsObj.getString("firstlogin").equals("1")){
                            Toast.makeText(activity, "你的密码为初始密码，请修改密码", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity, ChangePasswordActivity.class);
                            intent.putExtra("bForce", true);
                            activity.startActivity(intent);
                        }
                    }else if(success.equals("2")){
                        Toast.makeText(activity, "手机号码不存在", Toast.LENGTH_LONG).show();
                    }else if(success.equals("3")){
                        Toast.makeText(activity, "密码错误", Toast.LENGTH_LONG).show();
                    }else if(success.equals("4")){
                        Toast.makeText(activity, "请允许App获取设备识别码", Toast.LENGTH_LONG).show();
                    }else if(success.equals("5")){
                        Toast.makeText(activity, "您的账号禁止在该设备登录", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(activity, "登录失败", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void loginbycode(String safecode) {
        if(safecode.isEmpty()){
            Toast.makeText(activity, "委托码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = HttpClentLinkNet.BaseAddr + "loginauth.php";
        AjaxParams params = new AjaxParams();
        params.put("authcode", safecode);
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                String success = "";

                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("success")) {
                        success = jsObj.getString("success");
                    }
                    if(success.equals("1")) {
                        if (jsObj.has("userid")) {
                            Constant.userid = jsObj.getString("userid");
                        }
                        if (jsObj.has("username")) {
                            Constant.username = jsObj.getString("username");
                        }
                        Constant.isLogin = true;
                        Constant.isLogByCode = true;
                        Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show();
                        river_fragment_login.setVisibility(View.VISIBLE);
                        river_fragment_unlogin.setVisibility(View.GONE);
                        activity.onLoginChange();
                    }else{
                        Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    TagAliasCallback callback = new TagAliasCallback(){
        @Override
        public void gotResult(int responseCode, String alias, Set<String> tags) {
            if(responseCode == 0){
                SharePreferenceDataUtil.setSharedBooleanData(activity, "setAliasAndTags", true);
                //Toast.makeText(activity, "设置标签和别名成功！",Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(activity, "设置标签和别名失败！",Toast.LENGTH_SHORT).show();
            }
        }
    };

}
