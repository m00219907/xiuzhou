package com.jsycloud.playback.controlbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;



public abstract class PlayBackControlBar extends LinearLayout implements OnClickListener {

    /**
     * 抓图按钮ID
     */
    public static final int   ID_CAMERA = 0;
    /**
     * 声音按钮ID
     */
    public static final int   ID_SOUND  = 1;
    /**
     * 播放按钮ID
     */
    public static final int   ID_PLAY   = 2;
    /**
     * EPTZ按钮ID
     */
    public static final int   ID_EPTZ   = 3;
    /**
     * 关闭按钮ID
     */
    public static final int   ID_CLOSE  = 4;

    // 按钮
    /**
     * 抓图按钮
     */
    private ImageView         mCamera;

    /**
     * 声音按钮
     */
    private ImageView         mSound;

    /**
     * 播放按钮
     */
    private ImageView         mPlay;

    /**
     * 放大按钮
     */
    private ImageView         mEPTZ;

    /**
     * 关闭按钮
     */
    private ImageView         mClose;

    protected ControlListener mControlListener;

    // 数据
    protected boolean         isDraging;

    /**
     * 是否自动隐藏
     */
    private boolean           needAutoHide;

    /**
     * 自动隐藏的延时
     */
    private long              mDelay;

    private Handler           mHandler;

    private Runnable          mHideRunnable;

    public PlayBackControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout();

        initTimeBar();

        //initButtons();

        initAutoHide();

    }

    /**
     * 决定加载那个xml，样式可以不一样，但是控件的id必须是固定的
     */
    protected abstract void inflateLayout();

    /**
     * 初始化时间轴，不同的控件时间轴可能不一样，但是对外提供一致的接口
     */
    protected abstract void initTimeBar();

    /**
     * 初始化按钮
     */
    /*protected void initButtons() {
        mCamera = (ImageView) findViewById(R.id.camera);
        if (mCamera != null) {
            mCamera.setOnClickListener(this);
        }

        mSound = (ImageView) findViewById(R.id.sound);
        if (mSound != null) {
            mSound.setOnClickListener(this);
        }

        mPlay = (ImageView) findViewById(R.id.play);
        if (mPlay != null) {
            mPlay.setOnClickListener(this);
        }

        mEPTZ = (ImageView) findViewById(R.id.eptz);
        if (mEPTZ != null) {
            mEPTZ.setOnClickListener(this);
        }

        mClose = (ImageView) findViewById(R.id.close);
        if (mClose != null) {
            mClose.setOnClickListener(this);
        }
    }*/

    private void initAutoHide() {
        needAutoHide = false;
        mDelay = 5000;
        mHandler = new Handler();
        mHideRunnable = new AutoHideRunnable(this);
    }

    /**
     * 开始自动隐藏功能
     * 
     * @param autoHide
     * @param delay
     */
    public void setAutoHide(boolean autoHide, long delay) {
        needAutoHide = autoHide;
        mDelay = delay;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            resetHide();
        }
    }

    public void resetHide() {
        if (needAutoHide) {
            mHandler.removeCallbacks(mHideRunnable);
            mHandler.postDelayed(mHideRunnable, mDelay);
        }
    }

    @Override
    public void onClick(View v) {
        if (null == mControlListener) {
            return;
        }

        resetHide();

      /*  switch (v.getId()) {
            case R.id.camera:
                mControlListener.onButtonClick(ID_CAMERA);
            break;
            case R.id.sound:
                mControlListener.onButtonClick(ID_SOUND);
            break;
            case R.id.play:
                mControlListener.onButtonClick(ID_PLAY);
            break;
            case R.id.eptz:
                mControlListener.onButtonClick(ID_EPTZ);
            break;
            case R.id.close:
                mControlListener.onButtonClick(ID_CLOSE);
            break;
            default:
            break;
        }*/
    }

    /**
     * 绑定控制器监听
     * 
     * @param listener
     */
    public void setControlListener(ControlListener listener) {
        mControlListener = listener;
    }

    /**
     * 根据按钮ID得到对应的按钮，返回結果可能为空
     * 
     * @param id
     * @return
     */
    protected View findViewByID(int id) {
        switch (id) {
            case ID_CAMERA:
                return mCamera;
            case ID_SOUND:
                return mSound;
            case ID_PLAY:
                return mPlay;
            case ID_EPTZ:
                return mEPTZ;
            case ID_CLOSE:
                return mClose;
            default:
                return null;
        }
    }

    // 状态
    /**
     * 同步状态
     * 
     * @param ctrl
     */
    public void syncState(PlayBackControlBar other) {
        setSelected(ID_CAMERA, other.getSelected(ID_CAMERA));
        setSelected(ID_CLOSE, other.getSelected(ID_CLOSE));
        setSelected(ID_EPTZ, other.getSelected(ID_EPTZ));
        setSelected(ID_PLAY, other.getSelected(ID_PLAY));
        setSelected(ID_SOUND, other.getSelected(ID_SOUND));
        setTimeProgress(other.getTimeProgress());
    }

    /**
     * 横竖屏切换时的回调，用于动态更新一些参数
     */
    public abstract void onOnrientationChanged();

    /**
     * 清空状态
     */
    public void clear() {
        setTimeProgress(0);
        setSelected(RemoteControlBar.ID_PLAY, false);
        setSelected(RemoteControlBar.ID_SOUND, false);
    }

    // 时间轴
    /**
     * 按progress更新播放过程中时间轴(0.0 - 1.0)
     */
    public abstract void setTimeProgress(float progress);

    /**
     * 获取时间轴进度(0.0 - 1.0)
     * 
     * @return
     */
    public abstract float getTimeProgress();

    /**
     * 根据calendar设置时间轴
     * 
     * @param cal
     */
    public abstract void setTime(Calendar cal);

    /**
     * 获取当前时间轴时间
     * 
     * @return
     */
    public Calendar getTime() {
        return getTime(getTimeProgress());
    }

    /**
     * 根据progress获得calendar
     * 
     * @param progress
     * @return
     */
    public abstract Calendar getTime(float progress);

    /**
     * 是否正在拖拽
     * 
     * @return
     */
    public boolean isDragging() {
        return isDraging;
    }

    /**
     * 远程回放：标记时间轴上有录像的时间段,并且设置日期
     * <p>
     * 文件回放：标记时间轴的开始时间与结束时间
     * 
     * @param timeArray
     */
    public abstract void setTimeSlices(ArrayList<Pair<Calendar, Calendar>> timeArray);

    /**
     * 时间轴上时间片的最后一个时间点
     * 
     * @return
     */
    public abstract Calendar getLastTime();

    // 按钮
    /**
     * 设置按钮的选中状态
     * 
     * @param index
     * @param isSelected
     */
    public void setSelected(int index, boolean isSelected) {
        View v = findViewByID(index);
        if (v != null) {
            v.setSelected(isSelected);
        }
    }

    /**
     * 设置按钮的选中状态
     * 
     * @param index
     * @param visibility
     */
    public void setVisibility(int index, int visibility) {
        View v = findViewByID(index);
        if (v != null) {
            v.setVisibility(visibility);
        }
    }

    /**
     * 计算当前控件有几个button
     * 
     * @return
     */
    protected int getBtnCount() {
        int count = 0;
        if (mCamera != null && mCamera.getVisibility() != View.GONE) {
            count++;
        }
        if (mClose != null && mClose.getVisibility() != View.GONE) {
            count++;
        }
        if (mPlay != null && mPlay.getVisibility() != View.GONE) {
            count++;
        }
        if (mSound != null && mSound.getVisibility() != View.GONE) {
            count++;
        }
        if (mEPTZ != null && mEPTZ.getVisibility() != View.GONE) {
            count++;
        }
        return count;
    }

    /**
     * 获取按钮的选中状态
     * 
     * @param index
     * @return
     */
    public boolean getSelected(int index) {
        View v = findViewByID(index);
        if (v != null) {
            return v.isSelected();
        } else {
            return false;
        }
    }

    // 接口//////////////////////////////////////////////

    public interface ControlListener {
        /**
         * 按钮按下，可以分开写
         * 
         * @param id
         */
        public void onButtonClick(int id);

        /**
         * 进度条停止,返回进度 （0.0-1.0）
         * 
         * @param progress
         */
        public void onSeekBarStop(float progress);

        /**
         * 开始拖动进度条
         */
        public void onStartSeek();
    }

    public static class AutoHideRunnable implements Runnable {
        WeakReference<View> ref;

        public AutoHideRunnable(View view) {
            ref = new WeakReference<View>(view);
        }

        @Override
        public void run() {
            if (ref == null || ref.get() == null) {
                return;
            }
            animateToHide(ref.get());
        }
    }
    
    public static void  animateToHide(final View view) {
    	if (view.getVisibility()!=VISIBLE) {
			return ;
		}
    	AnimatorSet set = new AnimatorSet();
    	set.playTogether(getHideAnimator(view),ObjectAnimator.ofFloat(view, "alpha",1f, 0f));
    	set.setDuration(200);
    	set.start();
//         Animator animator = getHideAnimator(view);
//        animator.start();
	}
    
    
    public static   Animator getHideAnimator(final View view) {
 		final ViewGroup.LayoutParams lp = view.getLayoutParams();
         final int originalHeight = view.getHeight();
         
         ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 0);
         if (view.getVisibility()==VISIBLE) {
 			animator.setDuration(200);
 		} else {
 			animator.setDuration(1);
 		}
         animator.addListener(new AnimatorListenerAdapter() {
             @Override
             public void onAnimationEnd(Animator animation) {
                  // Reset view presentation
                /* setAlpha(view, 1f);
                 setTranslationX(view, 0);*/
                 lp.height = originalHeight;
                 view.setLayoutParams(lp);
                 setGone(view, true);
             }
         });

         animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
             @Override
             public void onAnimationUpdate(ValueAnimator valueAnimator) {
                 lp.height = (Integer) valueAnimator.getAnimatedValue();
                 view.setLayoutParams(lp);
             }
         });
 		return animator;
 	}
    
    public static void setGone(final View view, final boolean gone) {
        if (view == null)
            return;

        final int current = view.getVisibility();
        if (gone && current != GONE)
            view.setVisibility(GONE);
        else if (!gone && current != VISIBLE)
            view.setVisibility(VISIBLE);
    }
}
