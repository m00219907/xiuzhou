package com.jsycloud.baseclass;




import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.BuildConfig;
import com.jsycloud.ir.xiuzhou.R;





/**
 * 抽取一些基本操作，包含：
 * <p>
 * 通过handleFragmentMsg与 BaseFragment处理来自fragment的消息
 * <p>
 * 通过postMessage与handlerMessage处理内部的异步消息
 * 
 * @author 25391
 */
public class BaseActivity extends Activity implements IEventHandler, IMessageHandler {

	public static final String KEY_STR           = "str";
	public static final int EVENT_SHOWTOAST      = 2;
    /**
     * 子类消息的基数，注意由于不同的子类在内部定义的msg可能数值是相等的，因此这里的消息机制只适用于Fragment内部
     */
    protected final static int MSG_SUBCLASS_BASE = 10000;

    private Toast              mBaseToast;

    /**
     * 用于内部post消息
     */
    private BaseHandler        mHandler;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (mHandler == null) {
            mHandler = new BaseHandler(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void handleFragmentEvent(int msgId, Bundle data) {
        switch (msgId) {
            case EVENT_SHOWTOAST:
                showToast(data.getString(BaseActivity.KEY_STR));
            break;

            default:
            break;
        }
    }

    /**
     * <p>
     * 发送消息
     * </p>
     * 
     * @author fangzhihua 2014-5-12 上午10:05:30
     * @param mHandler
     * @param msg
     * @param param1
     * @param param2
     */
    protected void sendMessage(Handler mHandler, int msg, int param1, int param2) {
        if (mHandler == null)
            return;
        Message message = mHandler.obtainMessage();
        message.what = msg;
        message.arg1 = param1;
        message.arg2 = param2;
        mHandler.sendMessage(message);
    }

    // //////////////////////////////////////////////////////////
    @Override
    public void handlerMessage(Message msg) {
    }

    /**
     * 传递消息，在handlerMessage中处理这个消息
     * 
     * @param msg
     */
    protected void postMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    protected Message obtainMessage(int what) {
        return mHandler.obtainMessage(what);
    }

    // 弹出提示//////////////////////////////////////////////////
    /**
     * 最基本的toast样式
     * 
     * @param resId
     */
    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    /**
     * 最基本的toast样式
     * 
     * @param str
     */
    protected void showToast(String str) {
        if (mBaseToast == null) {
            mBaseToast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        } else {
            mBaseToast.setText(str);
        }
        mBaseToast.show();
    }

    protected void showToast(int id, int errCode) {

        if (isFinishing()) {
            return;
        }

        String text = getString(id);
        if (BuildConfig.DEBUG && errCode != 0) {
            text = text + " (errorCode：" + errCode + ")";
        }
        if (text != null && !text.equals("")) {
            showToast(text);
        }
    }

    // /网络Progress//////////////////////////////////////////////
    protected ProgressDialog mProgressDialog;

    private int              mLoadingCount = 0;

    /**
     * 显示加载框(累计)
     */
    protected void showLoadingProgress1() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mLoadingCount++;
        } else {
            mProgressDialog = ProgressDialog.show(this, null, getString(R.string.common_connect));
            mProgressDialog.setCancelable(false);
            mLoadingCount = 0;
        }
    }

    /**
     * 显示加载框(累计),显示指定文字
     */
    protected void showLoadingProgress(int resId) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mLoadingCount++;
        } else {
            mProgressDialog = ProgressDialog.show(this, null, getString(resId));
            mProgressDialog.setCancelable(false);
            mLoadingCount = 0;
        }
    }

    /**
     * 取消加载框(累计)
     */
    protected void dismissLoadingProgress1() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        if (mLoadingCount == 0) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        } else {
            mLoadingCount--;
        }
    }
}
