package com.jsycloud.timebar;
/** 
 * @Title TimeBarHorizontalScrollView.java 
 * @Package com.mcu.iVMS.component 
 * @Description  
 * @Copyright Hikvision Digital Technology Co., Ltd. All Right Reserved  
 * @author  
 * @date 2012-7-14 下午6:57:31 
 * @version 1.0 
 */


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * @Class TimeBarHorizontalScrollView
 * @Description 回放时间轴滑动控件
 * @author mlianghua
 * @date 2012-7-14 下午6:57:31
 */

public class TimeBarHorizontalScrollView extends HorizontalScrollView implements View.OnTouchListener {
    private boolean                     mIsEnable        = true;
    private TimeScrollBarScrollListener mTimeScrollBarScrollListener;
    private Handler                     mHandler;
    private final int                   CHECK_STOP_STATE = 0x0110;
    private int                         mLastLeftX       = 0;
    private int                         mTouchDownX      = 0;
    private int                         mTouchUpX        = 0;
    private boolean                     mIsScroll        = false;

    /**
     * 构造函数
     * 
     * @param context
     */
    public TimeBarHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    /**
     * 构造函数
     * 
     * @param context 上下文
     * @param attrs 属性
     */
    public TimeBarHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 构造函数
     * 
     * @param context 上下文
     * @param attrs 属性
     * @param defStyle 默认类型
     */
    public TimeBarHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化控件
     * 
     * @since V1.0
     */
    private void init() {
        this.setOnTouchListener(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CHECK_STOP_STATE:
                        checkScrollStop();
                    break;

                    default:
                    break;
                }
            }
        };

    }

    /**
     * 设置可用性
     * 
     * @param isEnable 可用性
     * @since V1.0
     */
    public void setEnable(boolean isEnable) {
        mIsEnable = isEnable;
    }

    /**
     * 设置滚动监控器
     * 
     * @param listener
     * @since V1.0
     */
    public void setTimeScrollBarScrollListener(TimeScrollBarScrollListener listener) {
        mTimeScrollBarScrollListener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!mIsEnable) {
            return true;
        }
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsScroll = false;
            break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsScroll) {
                    if (null != mTimeScrollBarScrollListener) {
                        mTimeScrollBarScrollListener.onScrollStart(this);
                    }
                    mIsScroll = true;
                }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsScroll) {
                    Message msg = new Message();
                    msg.what = CHECK_STOP_STATE;

                    mHandler.removeMessages(CHECK_STOP_STATE);
                    mHandler.sendMessage(msg);
                }
            break;
            default:
            break;
        }

        return false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mTimeScrollBarScrollListener != null) {
            mTimeScrollBarScrollListener.onScrollChanged(l, t, oldl, oldt, this);
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 检查滚动是否停止
     * 
     * @since V1.0
     */
    private void checkScrollStop() {
        int tempX = getScrollX();
        if (mLastLeftX == tempX) {
            if (null != mTimeScrollBarScrollListener) {
                mTimeScrollBarScrollListener.onScrollStop(TimeBarHorizontalScrollView.this);
            }
        } else {
            mLastLeftX = tempX;
            mHandler.removeMessages(CHECK_STOP_STATE);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(CHECK_STOP_STATE), 100);
        }
    }

    /**
     * 滚动事件监听器
     * 
     * @author mlianghua
     * @Data 2013-1-14
     */
    public interface TimeScrollBarScrollListener {
        public void onScrollChanged(int left, int top, int oldLeft, int oldTop, HorizontalScrollView scrollView);

        public void onScrollStart(HorizontalScrollView scrollView);

        public void onScrollStop(HorizontalScrollView scrollView);
    }
}
