package com.jsycloud.rs.xiuzhou.mefragment;

import android.app.Activity;
import android.content.Intent;
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

import com.jsycloud.rs.xiuzhou.Constant;
import com.jsycloud.rs.xiuzhou.HttpClentLinkNet;
import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.SharePreferenceDataUtil;
import com.jsycloud.rs.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;


public class TabMeFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;
    View login_layout, aboutme_layout;
    EditText login_username, login_password, login_code_edit;
    TextView aboutme_welcom, aboutme_username, aboutme_phonenum, aboutme_nickname;
    ImageView login_pwd_miwen;

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
        login_code_edit = (EditText)view.findViewById(R.id.login_code_edit);

        aboutme_welcom = (TextView)view.findViewById(R.id.aboutme_welcom);
        aboutme_username = (TextView)view.findViewById(R.id.aboutme_username);
        aboutme_phonenum = (TextView)view.findViewById(R.id.aboutme_phonenum);
        aboutme_nickname = (TextView)view.findViewById(R.id.aboutme_nickname);
        login_pwd_miwen = (ImageView)view.findViewById(R.id.login_pwd_miwen);
        login_pwd_miwen.setTag("1");
        login_pwd_miwen.setOnClickListener(this);

        view.findViewById(R.id.login_login).setOnClickListener(this);
        view.findViewById(R.id.login_use_code).setOnClickListener(this);
        view.findViewById(R.id.aboutme_changecontent).setOnClickListener(this);
        view.findViewById(R.id.aboutme_changepassword).setOnClickListener(this);
        view.findViewById(R.id.aboutme_logout).setOnClickListener(this);

        if(Constant.isLogin){
            login_layout.setVisibility(View.GONE);
            aboutme_layout.setVisibility(View.VISIBLE);
            setUserInfo();
        }else{
            login_layout.setVisibility(View.VISIBLE);
            aboutme_layout.setVisibility(View.GONE);
        }

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
            case R.id.login_login:
                login();
                break;
            case R.id.login_use_code:
                loginbycode();
                break;
            case R.id.aboutme_changecontent:
                break;
            case R.id.aboutme_changepassword:
                Intent intent = new Intent(activity, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutme_logout:
                Constant.isLogin = false;
                login_layout.setVisibility(View.VISIBLE);
                aboutme_layout.setVisibility(View.GONE);
                setUserInfo();
                activity.onLoginChange();
                SharePreferenceDataUtil.setSharedStringData(activity, "username", "");
                SharePreferenceDataUtil.setSharedStringData(activity, "userpassword", "");
                break;
            default:
                break;
        }
    }

    public void setUserInfo() {
        if(Constant.isLogin) {
            aboutme_welcom.setText("欢迎您！" + Constant.userfullname);
            aboutme_username.setText(Constant.username);
            aboutme_phonenum.setText(Constant.usermobile);
            aboutme_nickname.setText(Constant.userfullname);
        }else{
            aboutme_welcom.setText("未登录");
            aboutme_username.setText("");
            aboutme_phonenum.setText("");
            aboutme_nickname.setText("");
        }
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
                        Constant.isLogin = true;
                        Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show();
                        login_layout.setVisibility(View.GONE);
                        aboutme_layout.setVisibility(View.VISIBLE);
                        setUserInfo();
                        activity.onLoginChange();
                        SharePreferenceDataUtil.setSharedStringData(activity, "username", username);
                        SharePreferenceDataUtil.setSharedStringData(activity, "userpassword", userpassword);
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

    public void loginbycode() {
        String safecode = login_code_edit.getText().toString();
        if(safecode.isEmpty()){
            Toast.makeText(activity, "委托码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = HttpClentLinkNet.BaseAddr + "loginsc.php";
        AjaxParams params = new AjaxParams();
        params.put("username", safecode);
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
                        Constant.isLogin = true;
                        Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show();
                        login_layout.setVisibility(View.GONE);
                        aboutme_layout.setVisibility(View.VISIBLE);
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
}
