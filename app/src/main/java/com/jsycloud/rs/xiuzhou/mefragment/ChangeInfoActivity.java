package com.jsycloud.rs.xiuzhou.mefragment;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jsycloud.rs.xiuzhou.Constant;
import com.jsycloud.rs.xiuzhou.HttpClentLinkNet;
import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.SharePreferenceDataUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeInfoActivity  extends Activity implements View.OnClickListener {

    EditText change_info_username, change_info_phone, change_info_nickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_info);

        change_info_username = (EditText)findViewById(R.id.change_info_username);
        change_info_username.setText(Constant.username);
        change_info_phone = (EditText)findViewById(R.id.change_info_phone);
        change_info_phone.setText(Constant.usermobile);
        change_info_nickname = (EditText)findViewById(R.id.change_info_nickname);
        change_info_nickname.setText(Constant.userfullname);
        findViewById(R.id.change_info_back).setOnClickListener(this);
        findViewById(R.id.change_info_commit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_info_back:
                finish();
                break;
            case R.id.change_info_commit:
                String username = change_info_username.getText().toString();
                String phone = change_info_phone.getText().toString();
                String nickname = change_info_nickname.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone.isEmpty()){
                    Toast.makeText(this, "手机号码不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                changinfo(username, phone, nickname);
                break;
            default:
                break;
        }
    }

    public void changinfo(final String username, final String phone, final String nickname) {
        String url = HttpClentLinkNet.BaseAddr + "userinfochg.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        String userpassword = SharePreferenceDataUtil.getSharedStringData(this, "userpassword");
        params.put("userpassword", userpassword);
        params.put("username", username);
        params.put("usermobile", phone);
        params.put("userfullname", nickname);
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
                        Toast.makeText(ChangeInfoActivity.this, "密码资料成功！", Toast.LENGTH_SHORT).show();
                        Constant.username = username;
                        Constant.usermobile = phone;
                        Constant.userfullname = nickname;
                        SharePreferenceDataUtil.setSharedStringData(ChangeInfoActivity.this, "username", username);
                        ChangeInfoActivity.this.finish();
                    }else{
                        Toast.makeText(ChangeInfoActivity.this, "密码资料成功！", Toast.LENGTH_SHORT).show();
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
}

