package com.jsycloud.playback.controlbar;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.timebar.ScreenInfoUtil;
import com.jsycloud.timebar.TimeBarHorizontalScrollView;
import com.jsycloud.timebar.TimeLineView;
import com.jsycloud.util.Utils;


public abstract class RemoteControlBar extends PlayBackControlBar implements TimeBarHorizontalScrollView.TimeScrollBarScrollListener {

    /**
     * 每小时像素值
     */
    public static final int             HOUR_LENGTH   = 180;

    /**
     * 时间栏总长度
     */
    public static final int             TOTAL_LENGTH  = 24 * HOUR_LENGTH;

    /**
     * 一天的总时间
     */
    public static final int             TOTAL_SECONDS = 24 * 3600;

    // 时间轴
    private TimeBarHorizontalScrollView mTimeBar;

    private TimeLineView                mTimeLine;

    /**
     * 当前时间轴的进度，供外部获取用，精度比从mTimeLine计算得到的高
     */
    private float                       mProgress;

    private Calendar                    mDate;

    private Calendar                    mLastTime;
    
    public static Calendar scrollStopCalendar;
    private static final int STATE_STOP = 1;
    //private OnScrollStopListener listener;
    private Context context;
    

    public RemoteControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void initTimeBar() {
        mTimeBar = (TimeBarHorizontalScrollView) findViewById(R.id.TimeBar);
        mTimeLine = (TimeLineView) findViewById(R.id.TimeLineBar);

        if (mTimeBar == null || mTimeLine == null) {
            throw new IllegalStateException("please set TimeBarHorizontalScrollView & TimeLineView in layout");
        }

        // 设置时间轴的控件参数
        setTimeLineWidth((int) ScreenInfoUtil.getScreenWidth(getContext()));

        // 设置到今天的时间
        Calendar currentTimeCalendar = Calendar.getInstance();
        mTimeLine.setTimeDate(currentTimeCalendar);

        mTimeBar.setTimeScrollBarScrollListener(this);
    }

    @Override
    public void onScrollChanged(int left, int top, int oldLeft, int oldTop, HorizontalScrollView scrollView) {
        if (mTimeLine != null) {
            mTimeLine.setStartDrawX(left);
        }
        resetHide();
        //isOnScrollChange = true;
        Log.i("remotecontrolbar onScrollChanged", "onScrollChanged");
    }

    @Override
    public void onScrollStart(HorizontalScrollView scrollView) {
//        if (null == mControlListener) {
//            return;
//        }
        isDraging = true;
        //mControlListener.onStartSeek();
    }

    @Override
    public void onScrollStop(HorizontalScrollView scrollView) {
//        if (null == mControlListener) {
//            return;
//        }
        isDraging = false;
        float progress = ((float) mTimeLine.getStartDrawX()) / TOTAL_LENGTH;
        mProgress = progress;
       // mControlListener.onSeekBarStop(progress);
        Log.i("RemoteControlBar", "progress:" + String.valueOf(progress));
		scrollStopCalendar = Utils.Progress2Calendar(progress);
		Log.i("remotecontrolbar --onscrollstop", "calendar:" + "時:" + scrollStopCalendar.get(Calendar.HOUR_OF_DAY) + "分:" + 
				scrollStopCalendar.get(Calendar.MINUTE) + "秒:" + scrollStopCalendar.get(Calendar.SECOND));
		//isOnScrollChange = false;
		//listener.ScrollStopListener(isDraging);
		
    }
    
	
	@Override
	protected void inflateLayout() {
		
	}

	@Override
    public void onOnrientationChanged() {
        setTimeLineWidth((int) ScreenInfoUtil.getScreenWidth(getContext()));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////

    
    
    /**
     * 设置时间片
     * 
     * @param timeArray
     */
    public void setTimeSlices(ArrayList<Pair<Calendar, Calendar>> timeArray) {
        mTimeLine.cleanTimeSlice();    //清除绿色时间片段， 重新绘制选择的时间片段
        mTimeLine.setTimeSlices(timeArray);

        if (timeArray != null && timeArray.size() > 0) { 
        	if (timeArray.get(0) != null) {
        		 setDate(timeArray.get(0).second);
                 // 记录最后的时间点，seek的时候需要用到
                 mLastTime = timeArray.get(timeArray.size() - 1).second;
                 Log.i("remotecontrolbar", mLastTime + String.valueOf(mLastTime.get(Calendar.HOUR_OF_DAY)));
        	}
        }

    }

    @Override
    public Calendar getLastTime() {
        return mLastTime;
    }

    /**
     * 按progress更新播放过程中时间轴
     */
    public void setTimeProgress(float progress) {
        mProgress = progress;
        int pos = (int) (progress * TOTAL_LENGTH);
        mTimeBar.scrollBy((int) pos - mTimeLine.getStartDrawX(), 0);
        mTimeLine.updateCurrentTime(Progress2Calendar(progress));
    }

    public float getTimeProgress() {
        return mProgress;
    }

    public void setTime(Calendar cal) {
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        int seconds = h * 3600 + m * 60 + s;
        float progress = ((float) seconds) / (TOTAL_SECONDS);
        setTimeProgress(progress);
    }

    @Override
    public Calendar getTime(float progress) {
        if (mDate != null) {
            int seconds = (int) (TOTAL_SECONDS * progress);
            int h = seconds / 3600;
            int m = (seconds / 60) % 60;
            int s = seconds % 60;
            Calendar time = Calendar.getInstance();
            time.setTime(mDate.getTime());
            time.set(Calendar.HOUR_OF_DAY, h);
            time.set(Calendar.MINUTE, m);
            time.set(Calendar.SECOND, s);
            return time;
        }
        return null;
    }

    public void clear() {
        super.clear();
        mTimeLine.cleanTimeSlice();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 设置时间轴上日期
     */
    private void setDate(Calendar time) {
        mDate = time;
        mTimeLine.setTimeDate(time);
    }

    protected abstract int getExtarWidth();

    private void setTimeLineWidth(int screenWidth) {
        int width = screenWidth - getExtarWidth();
        mTimeLine.setMiddleLinePadding((int) (width / 2.0), 2, 0, 2);
        mTimeLine.setTimeScaleUnitValue(HOUR_LENGTH);
        ViewGroup.LayoutParams param = mTimeLine.getLayoutParams();
        param.width = width + 24 * 180;
    }
    
    /**
     * 进度 -> 日历时间
     * 
     * @param progress
     * @return
     */
    public static Calendar Progress2Calendar(float progress) {
        int seconds = (int) (TOTAL_SECONDS * progress);
        int h = seconds / 3600;
        int m = (seconds / 60) % 60;
        int s = seconds % 60;
        Calendar time = Calendar.getInstance();
        time.set(0, 0, 0, h, m, s);
        return time;
    }
    
 /*   public interface OnScrollStopListener {
    	public void ScrollStopListener(Boolean isStop);
    }*/
}
