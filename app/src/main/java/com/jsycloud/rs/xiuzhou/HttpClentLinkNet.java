package com.jsycloud.rs.xiuzhou;


import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class HttpClentLinkNet {

    private static HttpClentLinkNet instance = null;

    private HttpClentLinkNet() {
    }

    public static HttpClentLinkNet getInstance() {
        if (instance == null) {
            instance = new HttpClentLinkNet();
        }
        return instance;
    }

    public void sendReqFinalHttp_Get(String url, AjaxCallBack callBack) {
        FinalHttp fh = new FinalHttp();
        fh.configTimeout(30000);// 超时时间
        fh.get(url, callBack);
    }

    public void sendReqFinalHttp_Post(String url, AjaxParams params, AjaxCallBack callBack) {
        FinalHttp fh = new FinalHttp();
        fh.configTimeout(30000);// 超时时间
        fh.post(url, params, callBack);
    }

    public void sendReqFinalHttp_Get1(String url, AjaxCallBack callBack, int timeout) {
        FinalHttp fh = new FinalHttp();
        fh.configTimeout(timeout);
        fh.get(url, callBack);
    }

    public void sendReqFinalHttp_Post1(String url, AjaxParams params, AjaxCallBack callBack, int timeout) {
        FinalHttp fh = new FinalHttp();
        fh.configTimeout(timeout);
        fh.post(url, params, callBack);
    }
}
