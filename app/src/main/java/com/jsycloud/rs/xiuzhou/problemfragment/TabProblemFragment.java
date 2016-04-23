package com.jsycloud.rs.xiuzhou.problemfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.rs.xiuzhou.CommonTools;
import com.jsycloud.rs.xiuzhou.HttpClentLinkNet;
import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class TabProblemFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;
    TextView problem_fragment_coordinate, problem_fragment_postion, problem_fragment_chooseriver;
    EditText problem_fragment_name, problem_fragment_phone;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.problem_fragment, null);

        problem_fragment_coordinate = (TextView)view.findViewById(R.id.problem_fragment_coordinate);
        problem_fragment_postion = (TextView)view.findViewById(R.id.problem_fragment_postion);
        problem_fragment_chooseriver = (TextView)view.findViewById(R.id.problem_fragment_chooseriver);
        problem_fragment_chooseriver.setOnClickListener(this);
        problem_fragment_name = (EditText)view.findViewById(R.id.problem_fragment_name);
        problem_fragment_phone = (EditText)view.findViewById(R.id.problem_fragment_phone);

        view.findViewById(R.id.problem_fragment_uploadpic).setOnClickListener(this);
        view.findViewById(R.id.problem_fragment_commit).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.problem_fragment_chooseriver:
                Intent intent = new Intent(activity, RiverChooseActivity.class);
                activity.startActivityForResult(intent, 180);
                break;
            case R.id.problem_fragment_uploadpic:
                break;
            case R.id.problem_fragment_commit:
                reportProblem();
                break;
            default:
                break;
        }
    }

    public void setRiverName(String riverName) {
        problem_fragment_chooseriver.setText(riverName);
    }

    public void reportProblem() {
        if(problem_fragment_phone.getText().toString().isEmpty()){
            Toast.makeText(activity, "自动登录成功", Toast.LENGTH_SHORT).show();
        }
        String url = "http://websrv.jxtvtech.com/rs/report.php";
        AjaxParams params = new AjaxParams();
        params.put("mobile", "android");
        params.put("riverid", "riverid");
        params.put("describe", "describe");
        params.put("coordinate", "coordinate");
        params.put("address", "address");
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                String serverVersion = "1.0.0";
                String updateInfo = "";
                boolean bForce = false;
                String downloadUrl = "";
                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("version")) {
                        serverVersion = jsObj.getString("version");
                    }
                    if (jsObj.has("what")) {
                        updateInfo = jsObj.getString("what");
                    }
                    if (jsObj.has("force")) {
                        bForce = jsObj.getString("force").equals("1");
                    }
                    if (jsObj.has("url")) {
                        downloadUrl = jsObj.getString("url");
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
