package com.jsycloud.ir.xiuzhou.riverfragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.DialogUtils;
import com.jsycloud.ir.xiuzhou.R;

public class ManagementActivity extends Activity implements View.OnClickListener{

    WebView management_activity_webview;
    View management_activity_failed;

    ImageView management_activity_back, management_activity_forward;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1068:
                    if (!DialogUtils.isShowWaitDialog()) {
                        DialogUtils.showWaitDialog(ManagementActivity.this, "加载中...", -1);
                    }
                    break;
                case 1069:
                    if (DialogUtils.isShowWaitDialog()) {
                        DialogUtils.dismissDialog();
                    }
                    management_activity_back.setEnabled(management_activity_webview.canGoBack());
                    management_activity_forward.setEnabled(management_activity_webview.canGoForward());
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.management_activity);
        findViewById(R.id.management_activity_close).setOnClickListener(this);
        final TextView management_activity_tittle = (TextView)findViewById(R.id.management_activity_tittle);
        management_activity_failed = findViewById(R.id.management_activity_failed);
        management_activity_failed.setOnClickListener(this);
        management_activity_back = (ImageView)findViewById(R.id.management_activity_back);
        management_activity_back.setOnClickListener(this);
        management_activity_forward = (ImageView)findViewById(R.id.management_activity_forward);
        management_activity_forward.setOnClickListener(this);
        management_activity_webview = (WebView)findViewById(R.id.management_activity_webview);
        WebSettings setting = management_activity_webview.getSettings();
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
        management_activity_webview.setWebViewClient(new GeoWebViewClient());
        //设置UA
        String ua = management_activity_webview.getSettings().getUserAgentString();
        management_activity_webview.getSettings().setUserAgentString(ua+"; JsyCloud; InteligentRiver/"+ CommonTools.getVersionNum(this));

        management_activity_webview.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    handler.sendEmptyMessageDelayed(1069, 500);
                }
                super.onProgressChanged(view, progress);
            }

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                management_activity_tittle.setText(title);
            }
        });
        management_activity_webview.loadUrl("http://ir.jsycloud.com/pc");
        handler.sendEmptyMessage(1068);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.management_activity_close:
                finish();
                break;
            case R.id.management_activity_back:
                management_activity_webview.goBack();
                break;
            case R.id.management_activity_forward:
                management_activity_webview.goForward();
                break;
            case R.id.management_activity_failed:
                management_activity_webview.setVisibility(View.VISIBLE);
                management_activity_webview.loadUrl("http://ir.jsycloud.com/pc");
                handler.sendEmptyMessage(1068);
                management_activity_failed.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            if(management_activity_webview.canGoBack()){
                management_activity_webview.goBack();
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

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            management_activity_webview.setVisibility(View.INVISIBLE);
            management_activity_failed.setVisibility(View.VISIBLE);
        }
    }
}
