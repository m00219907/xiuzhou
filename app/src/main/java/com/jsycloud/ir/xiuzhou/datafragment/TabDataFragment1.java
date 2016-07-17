package com.jsycloud.ir.xiuzhou.datafragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;
import com.jsycloud.ir.xiuzhou.riverfragment.WebviewActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabDataFragment1 extends Fragment{

    Activity activity;
    WebView data_webview;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment1, null);

        data_webview = (WebView)view.findViewById(R.id.data_webview);

        WebSettings setting = data_webview.getSettings();
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

        data_webview.setWebViewClient(new GeoWebViewClient());

        data_webview.loadUrl(HttpClentLinkNet.BaseAddr + "page_bulletin.php");

        return view;
    }

    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = new Intent(activity, WebviewActivity.class);
            intent.putExtra("url", url);
            activity.startActivity(intent);
            return true;
        }
    }
}
