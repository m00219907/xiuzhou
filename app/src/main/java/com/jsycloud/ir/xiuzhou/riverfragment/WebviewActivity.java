package com.jsycloud.ir.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jsycloud.ir.xiuzhou.R;

public class WebviewActivity extends Activity{

    WebView webview_activity_webview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra("url");

        setContentView(R.layout.webview_activity);
        findViewById(R.id.webview_activity_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.this.finish();
            }
        });
        webview_activity_webview = (WebView)findViewById(R.id.webview_activity_webview);
        WebSettings setting = webview_activity_webview.getSettings();
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
        webview_activity_webview.setWebViewClient(new GeoWebViewClient());
        webview_activity_webview.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            if(webview_activity_webview.canGoBack()){
                webview_activity_webview.goBack();
            }else{
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}
