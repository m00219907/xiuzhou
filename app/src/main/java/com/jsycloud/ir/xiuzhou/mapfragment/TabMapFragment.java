package com.jsycloud.ir.xiuzhou.mapfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TabMapFragment extends Fragment implements AMapLocationListener{

    private View view;
    StartActivity activity;
    WebView map_webview;

    public AMapLocationClient mLocationClient = null;


    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        mLocationClient = new AMapLocationClient(activity.getApplicationContext());
        mLocationClient.setLocationListener(this);
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_webview, null);

        map_webview = (WebView)view.findViewById(R.id.map_webview);

        WebSettings setting = map_webview.getSettings();
        // 设置WebView属性，能够执行JavaScript脚本
        setting.setDefaultTextEncodingName("utf-8");
        setting.setJavaScriptEnabled(true);
        setting.setSupportZoom(false);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        setting.setAllowFileAccess(true);// 设置允许访问文件数据
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        // 设置支持缩放
        setting.setBuiltInZoomControls(false);
        setting.setDatabaseEnabled(true);
        // 使用localStorage则必须打开
        setting.setDomStorageEnabled(true);
        setting.setGeolocationEnabled(true);

        map_webview.setWebViewClient(new GeoWebViewClient());
        map_webview.setWebChromeClient(new GeoWebChromeClient());

        if(Constant.isLogin){
            map_webview.loadUrl(HttpClentLinkNet.BaseAddr + "pages/map.php" + "?uid=" + Constant.userid);
        }else{
            map_webview.loadUrl(HttpClentLinkNet.BaseAddr + "pages/map.php" + "?uid=0");
        }

        AMapLocationClientOption mLocationOption = null;
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
//设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            Constant.bReloadUrl = false;
        }else{
            if(Constant.bReloadUrl){
                if(Constant.isLogin){
                    map_webview.loadUrl(HttpClentLinkNet.BaseAddr + "page_map.php" + "?userid=" + Constant.userid);
                }else{
                    map_webview.loadUrl(HttpClentLinkNet.BaseAddr + "page_map.php");
                }
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                Constant.curLocation = aMapLocation;
                //Toast.makeText(activity, Constant.curLocation.getAddress(), Toast.LENGTH_SHORT).show();
            } else {
                Constant.curLocation = null;
                //String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                //Toast.makeText(activity, errText, Toast.LENGTH_SHORT).show();
            }
        }else{
            Constant.curLocation = null;
        }
    }

    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.contains("rid=")){
                Constant.selectRiverId = CommonTools.getRiverId(url);
                activity.initTopTab(4);
                return true;
            }
            return false;
        }
    }

    /**
     * WebChromeClient subclass handles UI-related calls
     * Note: think chrome as in decoration, not the Chrome browser
     */
    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }
    }
}
