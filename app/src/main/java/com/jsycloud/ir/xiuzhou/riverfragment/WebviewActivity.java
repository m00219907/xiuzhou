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
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogUtils;
import com.jsycloud.ir.xiuzhou.R;

public class WebviewActivity extends Activity implements View.OnClickListener{

    String url;
    TextView webview_activity_tittle;
    WebView webview_activity_webview;
    View webview_activity_failed;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1068:
                    if (!DialogUtils.isShowWaitDialog()) {
                        DialogUtils.showWaitDialog(WebviewActivity.this, "加载中...", -1);
                    }
                    break;
                case 1069:
                    if (DialogUtils.isShowWaitDialog()) {
                        DialogUtils.dismissDialog();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra("url");
        setContentView(R.layout.webview_activity);
        findViewById(R.id.webview_activity_back).setOnClickListener(this);
        webview_activity_tittle = (TextView)findViewById(R.id.webview_activity_tittle);
        webview_activity_failed = findViewById(R.id.webview_activity_failed);
        webview_activity_failed.setOnClickListener(this);
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
        //设置UA
        String ua = webview_activity_webview.getSettings().getUserAgentString();
        webview_activity_webview.getSettings().setUserAgentString(ua+"; JsyCloud; InteligentRiver/"+ CommonTools.getVersionNum(this));

        if(Constant.isLogin) {
            if(url.contains("?")) {
                url = url + "&uid=" + Constant.userid;
            }else{
                url = url + "?uid=" + Constant.userid;
            }
        }

        if(url.contains("about.php")){
            url = url + "&ver=" + CommonTools.getVersionNum(this);
        }

        webview_activity_webview.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    handler.sendEmptyMessageDelayed(1069, 500);
                }
                super.onProgressChanged(view, progress);
            }

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                webview_activity_tittle.setText(title);
            }
        });
        webview_activity_webview.loadUrl(url);
        handler.sendEmptyMessage(1068);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.webview_activity_back:
                back();
                break;
            case R.id.webview_activity_failed:
                webview_activity_webview.setVisibility(View.VISIBLE);
                webview_activity_webview.loadUrl(url);
                handler.sendEmptyMessage(1068);
                webview_activity_failed.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            back();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void back(){
        String curUrl = webview_activity_webview.getUrl();
        if(curUrl.contains("_show.php")){
            curUrl = curUrl.replace("_show.php", ".php");
            if(curUrl.contains("?")) {
                curUrl = curUrl + "&uid=" + Constant.userid;
            }else{
                curUrl = curUrl + "?uid=" + Constant.userid;
            }
            webview_activity_webview.loadUrl(curUrl);
        }else if(curUrl.contains("_deal.php")){
            curUrl = curUrl.replace("_deal.php", ".php");
            if(curUrl.contains("?")) {
                curUrl = curUrl + "&uid=" + Constant.userid;
            }else{
                curUrl = curUrl + "?uid=" + Constant.userid;
            }
            webview_activity_webview.loadUrl(curUrl);
        }else{
            finish();
        }
    }

    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webview_activity_webview.setVisibility(View.INVISIBLE);
            webview_activity_failed.setVisibility(View.VISIBLE);
            webview_activity_tittle.setText("");
        }
    }

}
