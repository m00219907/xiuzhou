package com.jsycloud.ir.xiuzhou;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dh.DpsdkCore.IDpsdkCore;
import com.jsycloud.ir.xiuzhou.mapfragment.TabMapFragment;
import com.jsycloud.ir.xiuzhou.problemfragment.TabProblemFragment;
import com.jsycloud.ir.xiuzhou.riverfragment.LoginActivity;
import com.jsycloud.ir.xiuzhou.riverfragment.TabRiverFragment2;
import com.jsycloud.ir.xiuzhou.riverfragment.WebviewActivity;
import com.jsycloud.ir.xiuzhou.videofragment.TabVideoFragment2;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class StartActivity extends FragmentActivity implements View.OnClickListener{

    public ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    ImageView tab_map_image, tab_video_image, tab_problem_image, tab_me_image;
    TextView tab_map_text, tab_video_text, tab_problem_text, tab_me_text;
    TextView tab_map_toplayout_text, tab_map_tipoff_list;
    ImageView tab_map_toplayout_search;
    long touchTime = 0;

    private String version = "1.0.0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_activity);

        tab_map_image = (ImageView)findViewById(R.id.tab_map_image);
        tab_video_image = (ImageView)findViewById(R.id.tab_video_image);
        tab_problem_image = (ImageView)findViewById(R.id.tab_problem_image);
        tab_me_image = (ImageView)findViewById(R.id.tab_me_image);

        tab_map_text = (TextView)findViewById(R.id.tab_map_text);
        tab_video_text = (TextView)findViewById(R.id.tab_video_text);
        tab_problem_text = (TextView)findViewById(R.id.tab_problem_text);
        tab_me_text = (TextView)findViewById(R.id.tab_me_text);

        tab_map_toplayout_text = (TextView)findViewById(R.id.tab_map_toplayout_text);

        tab_map_toplayout_search = (ImageView)findViewById(R.id.tab_map_toplayout_search);
        tab_map_toplayout_search.setOnClickListener(this);
        tab_map_tipoff_list = (TextView)findViewById(R.id.tab_map_tipoff_list);
        tab_map_tipoff_list.setOnClickListener(this);

        findViewById(R.id.tab_map_layout).setOnClickListener(this);
        findViewById(R.id.tab_video_layout).setOnClickListener(this);
        findViewById(R.id.tab_problem_layout).setOnClickListener(this);
        findViewById(R.id.tab_me_layout).setOnClickListener(this);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        }
        catch(Exception e1) {
        }
        sendVersionReq();
        setChangeView();
        initTopTab(0);
        this.getaccessToken();
        onLoginChange();
    }

    public void onLoginChange() {
    }

    public void setChangeView() {
        synchronized(this) {

            if (fragments.isEmpty()) {
                TabMapFragment mapFragment = new TabMapFragment();
                fragments.add(0, mapFragment);

                TabVideoFragment2 videoFragment = new TabVideoFragment2();
                fragments.add(1, videoFragment);

//                TabRiverFragment riverFragment = new TabRiverFragment();
//                fragments.add(2, riverFragment);

//                TabDataFragment1 dataFragment = new TabDataFragment1();
//                fragments.add(3, dataFragment);

                TabProblemFragment problemFragment = new TabProblemFragment();
                fragments.add(2, problemFragment);

                TabRiverFragment2 riverFragment2 = new TabRiverFragment2();
                fragments.add(3, riverFragment2);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_map_layout:
                initTopTab(0);
                tab_map_toplayout_text.setText("秀洲智慧河道");
                break;
            case R.id.tab_video_layout:
                initTopTab(1);
                tab_map_toplayout_text.setText("河道在线");
                break;
            case R.id.tab_problem_layout:
                initTopTab(2);
                tab_map_toplayout_text.setText("投诉举报");
                break;
            case R.id.tab_me_layout:
                if(Constant.isLogin) {
                    initTopTab(3);
                    tab_map_toplayout_text.setText("河长中心");
                }else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tab_map_toplayout_search:
                DialogShow.dialogShow2(this,"请输入投诉举报ID查询", "查询", new DialogShow.IloginClick() {
                    @Override
                    public void OnClick(String passcode) {
                        checkID(passcode);
                    }
                });
                break;
            case R.id.tab_map_tipoff_list:
                TabProblemFragment tabProblemFragment = (TabProblemFragment)fragments.get(2);
                tabProblemFragment.patrolupimg(0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TabProblemFragment problemFragment = (TabProblemFragment)fragments.get(2);
        problemFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= 2000) {
                Toast.makeText(this, "再按一次 退出程序", Toast.LENGTH_LONG).show();
                touchTime = currentTime;
            } else {
                IDpsdkCore.DPSDK_Logout(AppApplication.get().getDpsdkCreatHandle(), 30000);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void initTopTab(int selectIndex) {

        tab_map_image.setImageResource(R.drawable.ic_home_black_24dp);
        tab_video_image.setImageResource(R.drawable.ic_videocam_black_24dp);
        tab_problem_image.setImageResource(R.drawable.tab_problem_image_black);
        tab_me_image.setImageResource(R.drawable.ic_person_black_24dp);

        tab_map_text.setTextColor(0xff9a9a9a);
        tab_video_text.setTextColor(0xff9a9a9a);
        tab_problem_text.setTextColor(0xff9a9a9a);
        tab_me_text.setTextColor(0xff9a9a9a);

        switch (selectIndex) {
            case 0:
                tab_map_image.setImageResource(R.drawable.ic_home_blue_24dp);
                tab_map_text.setTextColor(0xff45c01a);
                tab_map_toplayout_search.setVisibility(View.GONE);
                tab_map_tipoff_list.setVisibility(View.GONE);
                initFragmentData(0);
                break;
            case 1:
                tab_video_image.setImageResource(R.drawable.ic_videocam_blue_24dp);
                tab_video_text.setTextColor(0xff45c01a);
                tab_map_toplayout_search.setVisibility(View.GONE);
                tab_map_tipoff_list.setVisibility(View.GONE);
                initFragmentData(1);
                break;
            case 2:
                tab_problem_image.setImageResource(R.drawable.tab_problem_image_blue);
                tab_problem_text.setTextColor(0xff45c01a);
                tab_map_toplayout_search.setVisibility(View.VISIBLE);
                tab_map_tipoff_list.setVisibility(View.VISIBLE);
                initFragmentData(2);
                break;
            case 3:
                tab_me_image.setImageResource(R.drawable.ic_person_blue_24dp);
                tab_me_text.setTextColor(0xff45c01a);
                tab_map_toplayout_search.setVisibility(View.GONE);
                tab_map_tipoff_list.setVisibility(View.GONE);
                initFragmentData(3);
                break;

            default:
                break;
        }
    }



    public void initFragmentData(int index) {

        while (getSupportFragmentManager().popBackStackImmediate()) {
            getSupportFragmentManager().popBackStack();
        }

        Fragment fragmentss = fragments.get(index);
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();

        if(!fragmentss.isAdded()){
            fts.add(R.id.start_mainFragment, fragmentss);
        }

        for(int i = 0; i < fragments.size(); i++){
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if(index == i && fragment.isAdded()){
                ft.show(fragment);
            }else if(fragment.isAdded()){
                ft.hide(fragment);
            }
            ft.commit();
        }
        fts.commit();

    }

    public void sendVersionReq() {
        File curFile = new File(Constant.appFolder + "//秀洲智慧河道.apk");
        if(curFile.exists()){
            curFile.delete();
        }
        String url = HttpClentLinkNet.BaseAddr + "update.php";
        AjaxParams params = new AjaxParams();
        params.put("from", "android");
        params.put("version", version);
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
                if (CommonTools.newVersion(serverVersion, version)) {
                    compareToVersion(updateInfo, serverVersion, bForce, downloadUrl);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void getaccessToken() {
        String url = HttpClentLinkNet.BaseAddr + "gettoken.php";
        AjaxParams params = new AjaxParams();
        params.put("accessToken", "");
        //Toast.makeText(activity, "1", Toast.LENGTH_SHORT).show();
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                //Toast.makeText(activity, "2", Toast.LENGTH_SHORT).show();
                String jsStr = "";
                String success = "";
                String jsaccessToken;

                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                //Toast.makeText(StartActivity.this, jsStr, Toast.LENGTH_SHORT).show();
                try {
                    //Toast.makeText(activity, "3", Toast.LENGTH_SHORT).show();
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("result")) {
                        //Toast.makeText(StartActivity.this, "3", Toast.LENGTH_SHORT).show();
                        jsStr = jsObj.getString("result");
                        jsObj = new JSONObject(jsStr);
                        if(jsObj.has("data")) {
                            jsStr = jsObj.getString("data");
                            jsObj = new JSONObject(jsStr);
                            if(jsObj.has("accessToken")) {
                                Constant.accessToken = jsObj.getString("accessToken");
                                //Toast.makeText(StartActivity.this, jsObj.getString("accessToken"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(StartActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void checkID(final String safecode) {
        if(safecode.isEmpty()){
            Toast.makeText(this, "投诉举报ID输入有误", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = HttpClentLinkNet.BaseAddr + "tipoffquery.php";
        AjaxParams params = new AjaxParams();
        params.put("tipid", safecode);
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                if (t != null) {
                    jsStr = String.valueOf(t);
                }
                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if(jsObj.getString("success").equals("1")) {
                        Intent intent = new Intent(StartActivity.this, WebviewActivity.class);
                        intent.putExtra("url", HttpClentLinkNet.BaseAddr + "page_tipofflogshow.php?tipid=" + safecode);
                        startActivity(intent);
                    }else{
                        Toast.makeText(StartActivity.this, "投诉举报ID输入有误", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(StartActivity.this, "投诉举报ID输入有误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void compareToVersion(String updateInfo, String serverVersion, boolean bForce, final String downloadUrl) {

        DialogShow.dialogShow1(this, bForce, updateInfo, serverVersion, new DialogShow.ICheckedCallBack() {

            @Override
            public void OnCheckedCallBackDispath(boolean bSucceed) {

                if (bSucceed) {
                    UpdateService upService = new UpdateService();
                    upService.startDownload(StartActivity.this, downloadUrl);
                }
            }
        });
    }

}
