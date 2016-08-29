package com.jsycloud.ir.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.SharePreferenceDataUtil;
import com.jsycloud.ir.xiuzhou.mefragment.ChangePasswordActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends Activity implements View.OnClickListener{

    EditText river_fragment_user, river_fragment_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        river_fragment_user = (EditText)findViewById(R.id.river_fragment_user);
        river_fragment_password = (EditText)findViewById(R.id.river_fragment_password);

        findViewById(R.id.login_activity_login).setOnClickListener(this);
        findViewById(R.id.login_activity_logwithcode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_activity_login:
                login();
                break;
            case R.id.login_activity_logwithcode:
                DialogShow.dialogShow2(this, "请输入8位委托码", "登录", new DialogShow.IloginClick() {
                    @Override
                    public void OnClick(String passcode) {
                        loginbycode(passcode);
                    }
                });
                break;
        }
    }

    public void login() {
        final String username = river_fragment_user.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String userpassword = river_fragment_password.getText().toString();
        if(userpassword.isEmpty()){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = HttpClentLinkNet.BaseAddr + "login.php";
        AjaxParams params = new AjaxParams();
        params.put("username", username);
        params.put("userpassword", userpassword);
        params.put("from", android.os.Build.MODEL);
        params.put("deviceid", CommonTools.getMacAddress(this));
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
                    if (success.equals("1")) {
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
                        boolean setAliasAndTags = SharePreferenceDataUtil.getSharedBooleanData(LoginActivity.this, "setAliasAndTags");
                        if (!setAliasAndTags) {
                            String alias = jsObj.getString("alias");
                            Set<String> tags = new HashSet<String>();
                            tags.add(jsObj.getString("tagcity"));
                            tags.add(jsObj.getString("tagdistrict"));
                            tags.add(jsObj.getString("tagtown"));
                            tags.add(jsObj.getString("taglevel"));
                            JPushInterface.setAliasAndTags(LoginActivity.this.getApplicationContext(), alias, JPushInterface.filterValidTags(tags), callback);
                        }
                        Constant.isLogin = true;
                        Constant.isLogByCode = false;
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        SharePreferenceDataUtil.setSharedStringData(LoginActivity.this, "username", username);
                        SharePreferenceDataUtil.setSharedStringData(LoginActivity.this, "userpassword", userpassword);
                        Constant.bReloadUrl = true;
                        if (jsObj.getString("firstlogin").equals("1")) {
                            Toast.makeText(LoginActivity.this, "你的密码为初始密码，请修改密码", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                            intent.putExtra("bForce", true);
                            LoginActivity.this.startActivity(intent);
                        }
                    } else if (success.equals("2")) {
                        Toast.makeText(LoginActivity.this, "手机号码不存在", Toast.LENGTH_LONG).show();
                    } else if (success.equals("3")) {
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                    } else if (success.equals("4")) {
                        Toast.makeText(LoginActivity.this, "请允许App获取设备识别码", Toast.LENGTH_LONG).show();
                    } else if (success.equals("5")) {
                        Toast.makeText(LoginActivity.this, "您的账号禁止在该设备登录", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                }

                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void loginbycode(String safecode) {
        if(safecode.isEmpty()){
            Toast.makeText(this, "委托码不能为空", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                }

                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    TagAliasCallback callback = new TagAliasCallback(){
        @Override
        public void gotResult(int responseCode, String alias, Set<String> tags) {
            if(responseCode == 0){
                SharePreferenceDataUtil.setSharedBooleanData(LoginActivity.this, "setAliasAndTags", true);
                //Toast.makeText(activity, "设置标签和别名成功！",Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(activity, "设置标签和别名失败！",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
