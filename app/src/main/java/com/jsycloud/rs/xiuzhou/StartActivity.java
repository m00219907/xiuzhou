package com.jsycloud.rs.xiuzhou;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.rs.xiuzhou.datafragment.TabDataFragment;
import com.jsycloud.rs.xiuzhou.mapfragment.TabMapFragment;
import com.jsycloud.rs.xiuzhou.mefragment.TabMeFragment;
import com.jsycloud.rs.xiuzhou.problemfragment.TabProblemFragment;
import com.jsycloud.rs.xiuzhou.riverfragment.TabRiverFragment;
import com.jsycloud.rs.xiuzhou.videofragment.TabVideoFragment;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class StartActivity extends FragmentActivity implements View.OnClickListener{

    public ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    ImageView tab_map_image, tab_video_image, tab_river_image, tab_data_image, tab_problem_image, tab_me_image;
    TextView tab_map_text, tab_video_text, tab_river_text, tab_data_text, tab_problem_text, tab_me_text;
    public TextView tab_map_toplayout_text;
    View tab_river_layout, tab_data_layout, tab_problem_layout;
    long touchTime = 0;

    private String version = "1.0.0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_activity);

        tab_map_image = (ImageView)findViewById(R.id.tab_map_image);
        tab_video_image = (ImageView)findViewById(R.id.tab_video_image);
        tab_river_image = (ImageView)findViewById(R.id.tab_river_image);
        tab_data_image = (ImageView)findViewById(R.id.tab_data_image);
        tab_problem_image = (ImageView)findViewById(R.id.tab_problem_image);
        tab_me_image = (ImageView)findViewById(R.id.tab_me_image);

        tab_map_text = (TextView)findViewById(R.id.tab_map_text);
        tab_video_text = (TextView)findViewById(R.id.tab_video_text);
        tab_river_text = (TextView)findViewById(R.id.tab_river_text);
        tab_data_text = (TextView)findViewById(R.id.tab_data_text);
        tab_problem_text = (TextView)findViewById(R.id.tab_problem_text);
        tab_me_text = (TextView)findViewById(R.id.tab_me_text);
        tab_map_toplayout_text = (TextView)findViewById(R.id.tab_map_toplayout_text);

        tab_river_layout = findViewById(R.id.tab_river_layout);
        tab_data_layout = findViewById(R.id.tab_data_layout);
        tab_problem_layout = findViewById(R.id.tab_problem_layout);

        findViewById(R.id.tab_map_layout).setOnClickListener(this);
        findViewById(R.id.tab_video_layout).setOnClickListener(this);
        tab_river_layout.setOnClickListener(this);
        tab_data_layout.setOnClickListener(this);
        tab_problem_layout.setOnClickListener(this);
        findViewById(R.id.tab_me_layout).setOnClickListener(this);

        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        }
        catch(Exception e1) {
        }
        sendVersionReq();
        setChangeView();
        initTopTab(0);

        String username = SharePreferenceDataUtil.getSharedStringData(this, "username");
        String userpassword = SharePreferenceDataUtil.getSharedStringData(this, "userpassword");
        login(username, userpassword);
    }

    public void onLoginChange() {
        if(Constant.isLogin) {
            tab_river_layout.setVisibility(View.VISIBLE);
            tab_data_layout.setVisibility(View.VISIBLE);
            tab_problem_layout.setVisibility(View.GONE);
        }else {
            tab_river_layout.setVisibility(View.GONE);
            tab_data_layout.setVisibility(View.GONE);
            tab_problem_layout.setVisibility(View.VISIBLE);
        }
    }

    public void setChangeView() {
        synchronized(this) {

            if (fragments.isEmpty()) {
                TabMapFragment mapFragment = new TabMapFragment();
                fragments.add(0, mapFragment);

                TabVideoFragment videoFragment = new TabVideoFragment();
                fragments.add(1, videoFragment);

                TabRiverFragment riverFragment = new TabRiverFragment();
                fragments.add(2, riverFragment);

                TabDataFragment dataFragment = new TabDataFragment();
                fragments.add(3, dataFragment);

                TabProblemFragment problemFragment = new TabProblemFragment();
                fragments.add(4, problemFragment);

                TabMeFragment meFragment = new TabMeFragment();
                fragments.add(5, meFragment);
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
                tab_map_toplayout_text.setText("视频监控");
                break;
            case R.id.tab_river_layout:
                initTopTab(2);
                tab_map_toplayout_text.setText("河长中心");
                break;
            case R.id.tab_data_layout:
                initTopTab(3);
                tab_map_toplayout_text.setText("数据通报");
                break;
            case R.id.tab_problem_layout:
                initTopTab(4);
                tab_map_toplayout_text.setText("问题上报");
                break;
            case R.id.tab_me_layout:
                initTopTab(5);
                tab_map_toplayout_text.setText("我的");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TabProblemFragment problemFragment = (TabProblemFragment)fragments.get(4);
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
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void initTopTab(int selectIndex) {

        //tab_map_image.setImageResource(R.drawable.hifmnormal);
        //tab_video_image.setImageResource(R.drawable.eventsnormal);
        //tab_river_image.setImageResource(R.drawable.clubnormal);
        //tab_data_image.setImageResource(R.drawable.menormal);

        tab_map_text.setTextColor(0xff9a9a9a);
        tab_video_text.setTextColor(0xff9a9a9a);
        tab_river_text.setTextColor(0xff9a9a9a);
        tab_data_text.setTextColor(0xff9a9a9a);
        tab_problem_text.setTextColor(0xff9a9a9a);
        tab_me_text.setTextColor(0xff9a9a9a);

        switch (selectIndex) {
            case 0:
                //tab_map_image.setImageResource(R.drawable.hifmpressed);
                tab_map_text.setTextColor(0xff45c01a);
                initFragmentData(0);
                break;
            case 1:
                //tab_event_image.setImageResource(R.drawable.eventspressed);
                tab_video_text.setTextColor(0xff45c01a);
                initFragmentData(1);
                break;
            case 2:
                //tab_club_image.setImageResource(R.drawable.clubpressed);
                tab_river_text.setTextColor(0xff45c01a);
                initFragmentData(2);
                break;
            case 3:
                //tab_me_image.setImageResource(R.drawable.mepressed);
                tab_data_text.setTextColor(0xff45c01a);
                initFragmentData(3);
                break;
            case 4:
                //tab_me_image.setImageResource(R.drawable.mepressed);
                tab_problem_text.setTextColor(0xff45c01a);
                initFragmentData(4);
                break;
            case 5:
                //tab_me_image.setImageResource(R.drawable.mepressed);
                tab_me_text.setTextColor(0xff45c01a);
                initFragmentData(5);
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
        File curFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "//秀洲智慧河道.apk");
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
                    compareToVersion(updateInfo, bForce, downloadUrl);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void login(String username, String userpassword) {
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
                        Toast.makeText(StartActivity.this, "自动登录成功", Toast.LENGTH_SHORT).show();
                        onLoginChange();
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

    private void compareToVersion(String updateInfo, boolean bForce, final String downloadUrl) {

        DialogShow.dialogShow1(this, bForce, updateInfo, new DialogShow.ICheckedCallBack() {

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
