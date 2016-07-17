package com.jsycloud.ir.xiuzhou.mefragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.MyRectangleView;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.SharePreferenceDataUtil;
import com.jsycloud.ir.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class TabMeFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;
    View login_layout, aboutme_layout;
    EditText login_username, login_password;
    TextView aboutme_welcom, aboutme_username, aboutme_phonenum, aboutme_nickname;
    MyRectangleView aboutme_changepassword;
    ImageView login_pwd_miwen;
    TextView aboutme_version;


    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment, null);
        login_layout = view.findViewById(R.id.login_layout);
        aboutme_layout = view.findViewById(R.id.aboutme_layout);

        login_username = (EditText)view.findViewById(R.id.login_username);
        login_password = (EditText)view.findViewById(R.id.login_password);

        aboutme_welcom = (TextView)view.findViewById(R.id.aboutme_welcom);
        aboutme_username = (TextView)view.findViewById(R.id.aboutme_username);
        aboutme_phonenum = (TextView)view.findViewById(R.id.aboutme_phonenum);
        aboutme_nickname = (TextView)view.findViewById(R.id.aboutme_nickname);
        login_pwd_miwen = (ImageView)view.findViewById(R.id.login_pwd_miwen);
        login_pwd_miwen.setTag("1");
        login_pwd_miwen.setOnClickListener(this);
        aboutme_version = (TextView)view.findViewById(R.id.aboutme_version);
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            if (info != null) {
                aboutme_version.setText("秀洲智慧河道 v" + info.versionName);
            }
        } catch (Exception e) {
        }

        view.findViewById(R.id.login_use_code).setOnClickListener(this);
        //view.findViewById(R.id.aboutme_changecontent).setOnClickListener(this);
        view.findViewById(R.id.aboutme_changepassword).setOnClickListener(this);
        view.findViewById(R.id.aboutme_logout).setOnClickListener(this);

        MyRectangleView me_fragment_login = (MyRectangleView)view.findViewById(R.id.me_fragment_login);
        me_fragment_login.setRectangleColor(0xff2196f3);
        me_fragment_login.settextStr("登录");
        me_fragment_login.setOnClickListener(this);

        //aboutme_changecontent = (MyRectangleView)view.findViewById(R.id.aboutme_changecontent);
        //aboutme_changecontent.setRectangleColor(0xff2196f3);
        //aboutme_changecontent.settextStr("修改资料");
        //aboutme_changecontent.setOnClickListener(this);

        aboutme_changepassword = (MyRectangleView)view.findViewById(R.id.aboutme_changepassword);
        aboutme_changepassword.setRectangleColor(0xff4caf50);
        aboutme_changepassword.settextStr("修改密码");
        aboutme_changepassword.setOnClickListener(this);

        MyRectangleView aboutme_logout = (MyRectangleView)view.findViewById(R.id.aboutme_logout);
        aboutme_logout.setRectangleColor(0xffff0000);
        aboutme_logout.settextStr("注销");
        aboutme_logout.setOnClickListener(this);

        if(Constant.isLogin){
            login_layout.setVisibility(View.GONE);
            aboutme_layout.setVisibility(View.VISIBLE);
            setUserInfo();
        }else{
            login_layout.setVisibility(View.VISIBLE);
            aboutme_layout.setVisibility(View.GONE);
        }

        //getaccessToken();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            if(Constant.isLogin){
                login_layout.setVisibility(View.GONE);
                aboutme_layout.setVisibility(View.VISIBLE);
                setUserInfo();
            }else{
                login_layout.setVisibility(View.VISIBLE);
                aboutme_layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Constant.isLogin){
            login_layout.setVisibility(View.GONE);
            aboutme_layout.setVisibility(View.VISIBLE);
            setUserInfo();
        }else{
            login_layout.setVisibility(View.VISIBLE);
            aboutme_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_pwd_miwen:
                if(login_pwd_miwen.getTag().equals("1")) {
                    login_pwd_miwen.setImageResource(R.drawable.miwen);
                    login_pwd_miwen.setTag("0");
                    login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    login_pwd_miwen.setImageResource(R.drawable.mingwen);
                    login_pwd_miwen.setTag("1");
                    login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                login_password.setSelection(login_password.getText().length());
                break;
            case R.id.me_fragment_login:
                login();
                break;
            case R.id.login_use_code:
                DialogShow.dialogShow2(activity, "请输入8位委托码", "登录", new DialogShow.IloginClick() {
                    @Override
                    public void OnClick(String passcode) {
                        loginbycode(passcode);
                    }
                });
                break;
            //case R.id.aboutme_changecontent:
                //Intent intent1 = new Intent(activity, ChangeInfoActivity.class);
                //startActivity(intent1);
                //break;
            case R.id.aboutme_changepassword:
                Intent intent = new Intent(activity, ChangePasswordActivity.class);
                intent.putExtra("bForce", false);
                startActivity(intent);
                break;
            case R.id.aboutme_logout:
                Constant.isLogin = false;
                Constant.isLogByCode = false;
                Constant.username = "";
                Constant.usermobile = "";
                Constant.userfullname = "";
                login_layout.setVisibility(View.VISIBLE);
                aboutme_layout.setVisibility(View.GONE);
                setUserInfo();
                activity.onLoginChange();
                SharePreferenceDataUtil.setSharedStringData(activity, "username", "");
                SharePreferenceDataUtil.setSharedStringData(activity, "userpassword", "");
                Constant.bReloadUrl = true;
                break;
            default:
                break;
        }
    }

    public void setUserInfo() {
        if(Constant.isLogin) {
            aboutme_welcom.setText("欢迎您！" + Constant.userfullname);
        }else{
            aboutme_welcom.setText("未登录");
        }
        aboutme_username.setText(Constant.username);
        aboutme_phonenum.setText(Constant.usermobile);
        aboutme_nickname.setText(Constant.userfullname);
    }

    public void login() {
        final String username = login_username.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(activity, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String userpassword = login_password.getText().toString();
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
                        login_layout.setVisibility(View.GONE);
                        aboutme_layout.setVisibility(View.VISIBLE);
                        //aboutme_changecontent.setVisibility(View.VISIBLE);
                        aboutme_changepassword.setVisibility(View.VISIBLE);
                        setUserInfo();
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
                        login_layout.setVisibility(View.GONE);
                        aboutme_layout.setVisibility(View.VISIBLE);
                        //aboutme_changecontent.setVisibility(View.GONE);
                        aboutme_changepassword.setVisibility(View.GONE);
                        setUserInfo();
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
