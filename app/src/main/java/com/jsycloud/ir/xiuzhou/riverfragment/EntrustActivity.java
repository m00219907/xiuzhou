package com.jsycloud.ir.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;


public class EntrustActivity extends Activity implements View.OnClickListener{

    TextView entrust_activity_code;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.entrust_activity);
        entrust_activity_code = (TextView)findViewById(R.id.entrust_activity_code);

        findViewById(R.id.entrust_activity_back).setOnClickListener(this);
        findViewById(R.id.entrust_activity_sendcode).setOnClickListener(this);
        findViewById(R.id.entrust_activity_newcode).setOnClickListener(this);
        getauthcode(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.entrust_activity_back:
                finish();
                break;
            case R.id.entrust_activity_sendcode:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra("sms_body", "巡河委托码：" + entrust_activity_code.getText().toString());
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
                break;
            case R.id.entrust_activity_newcode:
                getauthcode(true);
                break;
            default:
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
                        entrust_activity_code.setText(jsObj.getString("authcode"));
                    }else if(success.equals("2")){
                        if(bNew){
                            Toast.makeText(EntrustActivity.this, "重新获取委托码失败", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(EntrustActivity.this, "当前无委托码", Toast.LENGTH_SHORT).show();
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
