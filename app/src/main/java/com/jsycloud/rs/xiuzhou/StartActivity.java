package com.jsycloud.rs.xiuzhou;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsycloud.rs.xiuzhou.datafragment.TabDataFragment;
import com.jsycloud.rs.xiuzhou.mapfragment.TabMapFragment;
import com.jsycloud.rs.xiuzhou.riverfragment.TabRiverFragment;
import com.jsycloud.rs.xiuzhou.videofragment.TabVideoFragment;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StartActivity extends FragmentActivity implements View.OnClickListener{

    public ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    ImageView tab_map_image, tab_video_image, tab_river_image, tab_data_image;
    TextView tab_map_text, tab_video_text, tab_river_text, tab_data_text;

    private String version = "1.0.0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_activity);

        tab_map_image = (ImageView)findViewById(R.id.tab_map_image);
        tab_video_image = (ImageView)findViewById(R.id.tab_video_image);
        tab_river_image = (ImageView)findViewById(R.id.tab_river_image);
        tab_data_image = (ImageView)findViewById(R.id.tab_data_image);

        tab_map_text = (TextView)findViewById(R.id.tab_map_text);
        tab_video_text = (TextView)findViewById(R.id.tab_video_text);
        tab_river_text = (TextView)findViewById(R.id.tab_river_text);
        tab_data_text = (TextView)findViewById(R.id.tab_data_text);

        findViewById(R.id.tab_map_layout).setOnClickListener(this);
        findViewById(R.id.tab_video_layout).setOnClickListener(this);
        findViewById(R.id.tab_river_layout).setOnClickListener(this);
        findViewById(R.id.tab_data_layout).setOnClickListener(this);

        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        }
        catch(Exception e1) {
        }
        sendVersionReq();
        setChangeView();
        initFragmentData(0);
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
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_map_layout:
                initTopTab(0);
                break;
            case R.id.tab_video_layout:
                initTopTab(1);
                break;
            case R.id.tab_river_layout:
                initTopTab(2);
                break;
            case R.id.tab_data_layout:
                initTopTab(3);
                break;
            default:
                break;
        }
    }

    private void initTopTab(int selectIndex) {

        //tab_map_image.setImageResource(R.drawable.hifmnormal);
        //tab_video_image.setImageResource(R.drawable.eventsnormal);
        //tab_river_image.setImageResource(R.drawable.clubnormal);
        //tab_data_image.setImageResource(R.drawable.menormal);

        tab_map_text.setTextColor(0xff000000);
        tab_video_text.setTextColor(0xff000000);
        tab_river_text.setTextColor(0xff000000);
        tab_data_text.setTextColor(0xff000000);

        switch (selectIndex) {
            case 0:
                //tab_map_image.setImageResource(R.drawable.hifmpressed);
                tab_map_text.setTextColor(0xff4abe15);
                initFragmentData(0);
                break;
            case 1:
                //tab_event_image.setImageResource(R.drawable.eventspressed);
                tab_video_text.setTextColor(0xff4abe15);
                initFragmentData(1);
                break;
            case 2:
                //tab_club_image.setImageResource(R.drawable.clubpressed);
                tab_river_text.setTextColor(0xff4abe15);
                initFragmentData(2);
                break;
            case 3:
                //tab_me_image.setImageResource(R.drawable.mepressed);
                tab_data_text.setTextColor(0xff4abe15);
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
        String url = "http://websrv.jxtvtech.com/rs/update.php";
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
                if (CommonTools.newVersion(serverVersion, "2.0.0")) {
                    compareToVersion(updateInfo, bForce, downloadUrl);
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
