package com.jsycloud.ir.xiuzhou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.mapfragment.riverInfo;
import com.jsycloud.ir.xiuzhou.mefragment.ChangePasswordActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class StartActivity01 extends Activity{

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 101:
                    Intent intent = new Intent(StartActivity01.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 102:
                    Toast.makeText(StartActivity01.this, "你的密码为初始密码，请修改密码", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(StartActivity01.this, ChangePasswordActivity.class);
                    intent2.putExtra("bForce", true);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                finish();
                return;
            }
        }

        setContentView(R.layout.start_activity01);
        mHandler.sendEmptyMessageDelayed(101, 3000);
        String username = SharePreferenceDataUtil.getSharedStringData(this, "username");
        String userpassword = SharePreferenceDataUtil.getSharedStringData(this, "userpassword");
        login(username, userpassword);
    }

    public void login(String username, String userpassword) {
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
                        Constant.isLogin = true;
                        if (jsObj.getString("firstlogin").equals("1")) {
                            mHandler.sendEmptyMessageDelayed(102, 3000);
                        }
                        boolean setAliasAndTags = SharePreferenceDataUtil.getSharedBooleanData(StartActivity01.this, "setAliasAndTags");
                        if(!setAliasAndTags) {
                            String alias = jsObj.getString("alias");
                            Set<String> tags = new HashSet<String>();
                            tags.add(jsObj.getString("tagcity"));
                            tags.add(jsObj.getString("tagdistrict"));
                            tags.add(jsObj.getString("tagtown"));
                            tags.add(jsObj.getString("taglevel"));
                            JPushInterface.setAliasAndTags(getApplicationContext(), alias, JPushInterface.filterValidTags(tags), callback);
                        }
                    }else{
                        getriver();
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

    TagAliasCallback callback = new TagAliasCallback(){
        @Override
        public void gotResult(int responseCode, String alias, Set<String> tags) {
            if(responseCode == 0){
                SharePreferenceDataUtil.setSharedBooleanData(StartActivity01.this, "setAliasAndTags", true);
                //Toast.makeText(StartActivity01.this, "设置标签和别名成功！",Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(StartActivity01.this, "设置标签和别名失败！",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void getriver() {
        String url = HttpClentLinkNet.BaseAddr + "getriver.php";
        AjaxParams params = new AjaxParams();
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
                        Constant.allriverList.clear();
                        JSONArray eventArray = jsObj.getJSONArray("river");
                        for (int i = 0; i < eventArray.length(); i++) {
                            JSONObject curRiverJSON = eventArray.getJSONObject(i);
                            riverInfo curRiverInfo = new riverInfo();
                            curRiverInfo.setId(curRiverJSON.getString("id"));
                            curRiverInfo.setName(curRiverJSON.getString("name"));

                            Constant.allriverList.add(curRiverInfo);
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
}
