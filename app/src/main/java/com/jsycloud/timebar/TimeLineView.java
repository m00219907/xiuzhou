package com.jsycloud.timebar;

/** 
 * @Title CustomImageView.java 
 * @Package com.mcu.iVMS.component 
 * @Description  
 * @Copyright Hikvision Digital Technology Co., Ltd. All Right Reserved  
 * @author  
 * @date 2012-7-12 下午8:43:22 
 * @version 1.0 
 */

import java.util.ArrayList;
import java.util.Calendar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.jsycloud.ir.xiuzhou.R;

/**
 * @Class TimeLineView
 * @Description 回放时间轴具体内容绘制控件,和TimeBarHorizontalScrollView配合使用
 * @author mlianghua
 * @date 2012-7-12 下午8:43:22
 */

public class TimeLineView extends View {

    private final int            TIMESCALE_UNIT_VALUE         = 120;
    private final int            TIMESCALE_TEXT_SIZE          = 34;
    private final int            TIMESCALE_UNIT_TEXT_SIZE     = 26;
    private final int            PADDING_TO_MIDDLELINE        = 15;
    private final int            HOURS_OF_DATE                = 24;
    private int                  mLayoutWidth                 = 0;
    private int                  mLayoutHeight                = 0;
    private int                  mStartDrawX                  = 0;
    private int                  mTimeMiddleLinePaddingLeft   = 360;
    @SuppressWarnings("unused")
    private int                  mTimeMiddleLinePaddingRight  = 0;
    private int                  mTimeMiddleLinePaddingTop    = 0;
    private int                  mTimeMiddleLinePaddingBottom = 0;
    private String               mTimeLineDateString          = "2013-1-10";
    private String               mShowTime                    = "00:00:00";
    private ArrayList<TimeSlice> mTimeSlices                  = null;
    private Calendar             mTimeLineDate                = null;
    private int                  mTimeScaleTextSize           = 34;
    private int                  mTimeScaleTextColor          = Color.WHITE;
    private int                  mTimeScaleTextType           = 1;
    private int                  mTimeScaleLinePaddingLeft    = 0;
    private int                  mTimeScaleLinePaddingRight   = 0;
    private int                  mTimeScaleLinePaddingTop     = 0;
    private int                  mTimeScaleLinePaddingBottom  = 0;
    private int                  mTimeScaleLineColor          = Color.YELLOW;
    private int                  mTimeScaleLineWidth          = 3;
    private int                  mTimeScaleUnitValue          = TIMESCALE_UNIT_VALUE;
    private int                  mTimeScaleUnitTextSize       = 28;
    private int                  mTimeScaleUnitTextColor      = Color.rgb(204, 204, 204);
    private int                  mTimeScaleUnitTextStyle      = 0;

    @SuppressWarnings("unused")
    private int                  mTimeScaleUnitPaddingLeft    = 0;

    @SuppressWarnings("unused")
    private int                  mTimeScaleUnitPaddingRight   = 0;

    private int                  mTimeScaleUnitPaddingTop     = 0;
    private int                  mTimeScaleUnitPaddingBottom  = 2;
    private int                  mTimeScaleContentHeight      = 15;
    private int                  mTimeScaleUnitMarkHeight     = 4;
    private int                  mTimeScaleUnitMarkWidth      = 1;
    private int                  mTimeScaleUnitMarkColor      = Color.RED;
    private int                  mTimeScaleTextPaddingTop     = 0;

    /**
     * 构造函数
     * 
     * @param context 上下文
     */
    public TimeLineView(Context context) {
        super(context);
        mTimeScaleTextColor = Color.RED;
        init(context);
    }

    /**
     * 构造函数
     * 
     * @param context 上下文
     * @param attrs 属性
     */
    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context);
        }

        try {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.TimeLineView);
            if (null != typeArray) {
                mTimeLineDateString = typeArray.getText(R.styleable.TimeLineView_TimeScaleDateText).toString();
                if (null == mTimeLineDateString) {
                    mTimeLineDateString = "";
                }

                mShowTime = typeArray.getNonResourceString(R.styleable.TimeLineView_TimeScaleTimeText);
                if (null == mShowTime) {
                    mShowTime = "00:00:00";
                }

                // 显示日期时间
                mTimeScaleTextPaddingTop = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleTextPaddingTop, 0);
                mTimeScaleTextSize = typeArray.getDimensionPixelSize(R.styleable.TimeLineView_TimeScaleTextSize,
                        TIMESCALE_TEXT_SIZE);
                mTimeScaleTextColor = typeArray.getColor(R.styleable.TimeLineView_TimeScaleTextColor, Color.WHITE);
                mTimeScaleTextType = typeArray.getInt(R.styleable.TimeLineView_TimeScaleTextStyle, 0);

                // 时间轴中心线参数设置
                mTimeScaleLinePaddingLeft = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleLinePaddingLeft, 1);
                mTimeMiddleLinePaddingLeft = mTimeScaleLinePaddingLeft;

                mTimeScaleLinePaddingRight = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleLinePaddingRight, 1);
                mTimeMiddleLinePaddingRight = mTimeScaleLinePaddingRight;

                mTimeScaleLinePaddingTop = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleLinePaddingTop, 1);
                mTimeMiddleLinePaddingTop = mTimeScaleLinePaddingTop;

                mTimeScaleLinePaddingBottom = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleLinePaddingBottom, 1);
                mTimeMiddleLinePaddingBottom = mTimeScaleLinePaddingBottom;

                mTimeScaleLineColor = typeArray.getColor(R.styleable.TimeLineView_TimeScaleLineColor, Color.YELLOW);
                mTimeScaleLineWidth = typeArray.getDimensionPixelSize(R.styleable.TimeLineView_TimeScaleLineWidth, 1);

                // 时间轴刻度参数设置
                mTimeScaleUnitValue = typeArray.getDimensionPixelSize(R.styleable.TimeLineView_TimeScaleUnitValue,
                        TIMESCALE_UNIT_VALUE);
                mTimeScaleUnitTextSize = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleUnitTextSize, TIMESCALE_UNIT_TEXT_SIZE);
                mTimeScaleUnitTextColor = typeArray.getColor(R.styleable.TimeLineView_TimeScaleUnitTextColor,
                        Color.WHITE);
                mTimeScaleUnitTextStyle = typeArray.getInt(R.styleable.TimeLineView_TimeScaleUnitTextStyle, 0);

                // 时间轴刻度位置分布
                mTimeScaleUnitPaddingLeft = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleUnitPaddingLeft, 0);
                mTimeScaleUnitPaddingRight = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleUnitPaddingRight, 0);
                mTimeScaleUnitPaddingTop = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleUnitPaddingTop, 0);
                mTimeScaleUnitPaddingBottom = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleUnitPaddingBottom, 0);

                mTimeScaleUnitMarkHeight = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleUnitMarkHeight, 1);
                mTimeScaleUnitMarkWidth = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleUnitMarkWidth, 1);
                mTimeScaleUnitMarkColor = typeArray
                        .getColor(R.styleable.TimeLineView_TimeScaleUnitMarkColor, Color.RED);

                // 时间轴内容填充
                mTimeScaleContentHeight = typeArray.getDimensionPixelSize(
                        R.styleable.TimeLineView_TimeScaleContentWidth, 1);

                typeArray.recycle();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造函数
     * 
     * @param context 上下文
     * @param attrs 属性
     * @param defStyle 默认类型
     */
    public TimeLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化控件
     * 
     * @param context 上下文
     */
    private void init(Context context) {
        int screenWidth = (int) ScreenInfoUtil.getScreenWidth(context);
        setMiddleLinePadding(screenWidth / 2, 0, 0, 0);

        mTimeSlices = new ArrayList<TimeSlice>();
    }

    public void setTimeScaleUnitValue(int timeScaleUnitValue) {
        mTimeScaleUnitValue = timeScaleUnitValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLayoutWidth = getMeasuredWidth();
        mLayoutHeight = getMeasuredHeight();
        onDrawTimeSlices(canvas, 0);
        onDrawTime(canvas);
        int dateTextBottom = onDrawDate(canvas);

        dateTextBottom = onDrawGraduation(canvas, dateTextBottom);
        onDrawMiddleLine(canvas);
    }

    /**
     * 绘制时间轴刻度
     * 
     * @param canvas 画布
     * @param top 开始绘图X轴的位置
     * @return 绘图后X轴的位置
     * @since V1.0
     */
    protected int onDrawGraduation(Canvas canvas, int top) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int startShowHour = 0;
        float x = 0;
        float y = 0;
        for (int i = 0; i <= HOURS_OF_DATE; i++) {
            String graducation = String.format("%02d:%02d", startShowHour, 0);
            paint.setColor(mTimeScaleUnitTextColor);
            paint.setTextSize(mTimeScaleUnitTextSize);
            paint.setTypeface(Typeface.defaultFromStyle(mTimeScaleUnitTextStyle));
            Rect rect = new Rect();
            paint.getTextBounds(graducation, 0, graducation.length(), rect);

            x = (float) (i * mTimeScaleUnitValue) + mTimeMiddleLinePaddingLeft - (rect.width() / 2);
            // y = top + rect.height() + mTimeScaleUnitPaddingTop;
            y = mLayoutHeight - mTimeScaleUnitPaddingBottom;

            // 刻度
            canvas.drawText(graducation, x, y, paint);

            if (startShowHour != HOURS_OF_DATE) {
                startShowHour = (startShowHour % HOURS_OF_DATE) + 1;
            }

            // 刻度底标
            paint.setColor(mTimeScaleUnitMarkColor);
            canvas.drawRect(i * mTimeScaleUnitValue + mTimeMiddleLinePaddingLeft, y + mTimeScaleUnitPaddingBottom - 5,
                    i * mTimeScaleUnitValue + mTimeScaleUnitMarkWidth + mTimeMiddleLinePaddingLeft, y
                            + mTimeScaleUnitPaddingBottom + mTimeScaleUnitMarkHeight - 5, paint);
        }

        int lineTop = (int) (y + mTimeScaleUnitPaddingBottom + mTimeScaleUnitMarkHeight);

        // 刻度底线
        paint.setColor(Color.rgb(10, 10, 10));
        canvas.drawRect(0, lineTop, mLayoutWidth, lineTop + 2, paint);

        paint.setColor(Color.argb(0x10, 255, 255, 255));
        canvas.drawRect(0, lineTop + 2, mLayoutWidth, lineTop + 4, paint);

        return (int) lineTop + 4;
    }

    /**
     * 绘制时间轴中间线
     * 
     * @param canvas 画布
     * @param top 开始绘图X轴的位置
     * @return 绘图后X轴的位置
     * @since V1.0
     */
    protected void onDrawMiddleLine(Canvas canvas) {
        // Paint paint = new Paint();
        // paint.setAntiAlias(true);
        // paint.setColor(mTimeScaleLineColor);
        //
        // canvas.drawRect(mStartDrawX + mTimeMiddleLinePaddingLeft, mTimeMiddleLinePaddingTop, mStartDrawX
        // + mTimeMiddleLinePaddingLeft + mTimeScaleLineWidth, mLayoutHeight - mTimeMiddleLinePaddingBottom, paint);

        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
        // R.drawable.playback_body_cursor_n);
        //
        // Rect rect = new Rect(mStartDrawX + mTimeMiddleLinePaddingLeft, 0, mStartDrawX + mTimeMiddleLinePaddingLeft
        // + bitmap.getWidth(), mLayoutHeight - 0);
        // rect.left = (int) (rect.left - bitmap.getWidth());
        // canvas.drawBitmap(bitmap, null, rect, paint);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.playback_body_cursor_n);
        NinePatch np = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);

        Rect rect = new Rect(mStartDrawX + mTimeMiddleLinePaddingLeft - bitmap.getWidth() / 2, 0, mStartDrawX
                + mTimeMiddleLinePaddingLeft + bitmap.getWidth() / 2, mLayoutHeight - 0);
        np.draw(canvas, rect);
    }

    @SuppressWarnings("unused")
    private void drawNinePatch(Canvas canvas, Bitmap bitmap, Rect rect) {
        NinePatch ninePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
        ninePatch.draw(canvas, rect);
    }

    /**
     * 绘制时间轴日期
     * 
     * @param canvas 画布
     * @return 绘图后X轴的位置
     * @since V1.0
     */
    protected int onDrawDate(Canvas canvas) {
        mLayoutHeight = getMeasuredHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mTimeScaleTextColor);
        paint.setTypeface(Typeface.defaultFromStyle(mTimeScaleTextType));
        paint.setTextSize(mTimeScaleTextSize);
        Rect rect = new Rect();
        paint.getTextBounds(mTimeLineDateString, 0, mTimeLineDateString.length(), rect);

        int dataTextX = mStartDrawX + mTimeMiddleLinePaddingLeft - rect.width() - PADDING_TO_MIDDLELINE;
        int dataTextY = rect.height() + mTimeMiddleLinePaddingTop + mTimeScaleTextPaddingTop;

        canvas.drawText(mTimeLineDateString, dataTextX, dataTextY, paint);

        return dataTextY;
    }

    /**
     * 绘制时间轴具体时间
     * 
     * @param canvas 画布
     * @return 无
     * @since V1.0
     */
    protected void onDrawTime(Canvas canvas) {
        mLayoutWidth = getMeasuredWidth();
        mLayoutHeight = getMeasuredHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mTimeScaleTextColor);
        paint.setTextSize(mTimeScaleTextSize);
        paint.setTypeface(Typeface.defaultFromStyle(mTimeScaleTextType));
        Rect rect = new Rect();
        paint.getTextBounds(mShowTime, 0, mShowTime.length(), rect);
        canvas.drawText(mShowTime, mStartDrawX + mTimeMiddleLinePaddingLeft + PADDING_TO_MIDDLELINE, rect.height()
                + mTimeMiddleLinePaddingTop + mTimeScaleTextPaddingTop, paint);
    }

    /**
     * 绘制时间轴片段
     * 
     * @param canvas 画布
     * @param top 开始绘图X轴的位置
     * @return 无
     * @since V1.0
     */
    protected void onDrawTimeSlices(Canvas canvas, int top) {
        if ((mLayoutWidth <= 0) || (mLayoutHeight <= 0)) {
            return;
        }

        // int timeSliceTop = (int) ((mLayoutHeight - top - mTimeScaleContentHeight) / 2.0) + top;
        int timeSliceTop = 1;
        int timeSliceBottom = mLayoutHeight - 1;
        // int timeSliceBottom = timeSliceTop + mTimeScaleContentHeight;

        Calendar startTime;
        Calendar endTime;
        float drawBeginX = 0;
        float drawEndX = 0;

        Paint paint = new Paint();

        if (null == mTimeSlices || 0 == mTimeSlices.size()) {
            return;
        }

        Calendar zeroTime = mTimeLineDate;
        for (TimeSlice timeSlice : mTimeSlices) {
            if (null == timeSlice) {
                continue;
            }

            if (timeSlice.getTimeLength() <= 0) {
                continue;
            }

            endTime = timeSlice.getEndTime();
            if (null == endTime || endTime.before(zeroTime)) {
                continue;
            }

            startTime = timeSlice.getBeginTime();
            if (null == startTime) {
                continue;
            } else if (startTime.before(zeroTime)) {
                startTime = zeroTime;
            }

            paint.setAntiAlias(true);
            paint.setColor(getPaintColor(timeSlice.getType()));

            drawBeginX = (float) (((startTime.getTimeInMillis() - zeroTime.getTimeInMillis()) / 3600000.0) * mTimeScaleUnitValue);
            drawEndX = (float) (((endTime.getTimeInMillis() - zeroTime.getTimeInMillis()) / 3600000.0) * mTimeScaleUnitValue);

            canvas.drawRect(mTimeMiddleLinePaddingLeft + drawBeginX, timeSliceTop, mTimeMiddleLinePaddingLeft
                    + drawEndX, timeSliceBottom, paint);
            // canvas.drawRect(10, 10, 10, 10, paint);
        }
    }

    /**
     * 根据不同的类型，返回不同的画笔颜色
     * 
     * @param type 类型
     * @return 画笔颜色
     * @since V1.0
     */
    private int getPaintColor(int type) {
        int color = Color.BLUE;
        switch (type) {
            case TimeSlice.TIMESLICE_BULE_TYPE:
                color = Color.rgb(0, 89, 178);
            break;
            case TimeSlice.TIMESLICE_GREEN_TYPE:
                color = Color.rgb(0, 128, 42);
            break;
            case TimeSlice.TIMESLICE_RED_TYPE:
                color = Color.rgb(127, 0, 0);
            break;
            case TimeSlice.TIMESLICE_YELLOW_TYPE:
                color = Color.rgb(153, 127, 0);
            break;
            default:
            break;
        }

        return color;
    }

    /**
     * setStartDrawX
     * 
     * @param 起始绘制点
     */
    public void setStartDrawX(int positionX) {
        mStartDrawX = positionX;
        invalidate();

        int seconds = (int) (mStartDrawX * 3600.0 / mTimeScaleUnitValue);
        int minute = (int) (seconds / 60 % 60);
        int hour = (int) (seconds / 3600);
        int second = (int) (seconds % 60);

        if (hour == 24 && (minute > 0 || second > 0)) {
            minute = 0;
            second = 0;
        }
        mShowTime = String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * 获取绘图的起始绘制点
     * 
     * @return 起始绘制点
     * @since V1.0
     */
    public int getStartDrawX() {
        return mStartDrawX;
    }

    /**
     * 更新具体时间
     * 
     * @return 具体时间
     * @since V1.0
     */
    public void updateCurrentTime(Calendar time) {
        if (null == time) {
            return;
        }

        mShowTime = String.format("%02d:%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE),
                time.get(Calendar.SECOND));

        invalidate();
    }

    /**
     * setMiddleLinePadding 设置中心线的偏移量
     * 
     * @param left 中心线左偏移
     * @param top 中心线上偏移
     * @param right 中心线右偏移
     * @param bottom 中心线下偏移
     */
    public void setMiddleLinePadding(int left, int top, int right, int bottom) {
        mTimeMiddleLinePaddingLeft = left;
        mTimeMiddleLinePaddingRight = right;
        mTimeMiddleLinePaddingTop = top;
        mTimeMiddleLinePaddingBottom = bottom;
    }

    /**
     * 设置时间轴日期
     * 
     * @param time 时间轴日期
     * @since V1.0
     */
    public void setTimeDate(Calendar time) {
        if (null == time) {
            return;
        }

        mTimeLineDate = Calendar.getInstance();
        mTimeLineDate.set(Calendar.YEAR, time.get(Calendar.YEAR));
        mTimeLineDate.set(Calendar.MONTH, time.get(Calendar.MONTH));
        mTimeLineDate.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH));
        mTimeLineDate.set(Calendar.HOUR_OF_DAY, 0);
        mTimeLineDate.set(Calendar.MINUTE, 0);
        mTimeLineDate.set(Calendar.SECOND, 0);

        mTimeLineDateString = String.format("%4d-%d-%d", time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1,
                time.get(Calendar.DAY_OF_MONTH));

        // if (null != mTimeSlices) {
        // mTimeSlices.clear();
        // }

        invalidate();
    }

    /**
     * 设置时间片段
     * 
     * @param timeSlices 时间片段
     * @since V1.0
     */
    public void setTimeSlices(ArrayList<Pair<Calendar, Calendar>> timeSlices) {
        if (null == mTimeSlices || null == timeSlices || timeSlices.size() == 0) {
            return;
        }

        int index = 0;
        for (Pair<Calendar, Calendar> tsPair : timeSlices) {
            TimeSlice timeSlice = new TimeSlice();
            timeSlice.setBeginTime(tsPair.first);
            timeSlice.setEndTime(tsPair.second);
            timeSlice.setType(1);
            mTimeSlices.add(index, timeSlice);
            index++;
        }

        invalidate();
    }

    /**
     * 增加时间轴片段
     * 
     * @param index 索引
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @return true-增加成功 or false -增加失败
     * @since V1.0
     */
    public boolean addTimeSlice(int index, Calendar beginTime, Calendar endTime, int type) {
        if (null == mTimeSlices || null == beginTime || null == endTime || index < 0) {
            return false;
        }

        TimeSlice timeSlice = new TimeSlice();
        timeSlice.setBeginTime(beginTime);
        timeSlice.setEndTime(endTime);
        timeSlice.setType(type);

        mTimeSlices.add(index, timeSlice);
        invalidate();
        return true;
    }

    public void cleanTimeSlice() {
        if (null != mTimeSlices) {
            mTimeSlices.clear();
        }
        Log.e(getClass().getSimpleName(), "cleanTimeSlice!!!!");
        invalidate();
    }

    /**
     * 时间片段类
     * 
     * @author mlianghua
     * @Data 2013-1-14
     */
    public class TimeSlice {
        public static final int TIMESLICE_BULE_TYPE   = 0;
        public static final int TIMESLICE_GREEN_TYPE  = 1;
        public static final int TIMESLICE_YELLOW_TYPE = 2;
        public static final int TIMESLICE_RED_TYPE    = 3;
        private int             mType                 = 0;
        private Calendar        mBeginTime            = null;
        private Calendar        mEngTime              = null;

        /**
         * 设置开始时间
         * 
         * @param beginTime 开始时间
         * @since V1.0
         */
        public void setBeginTime(Calendar beginTime) {
            mBeginTime = beginTime;
        }

        /**
         * 设置结束时间
         * 
         * @param endTime 结束时间
         * @since V1.0
         */
        public void setEndTime(Calendar endTime) {
            mEngTime = endTime;
        }

        /**
         * 获取开始时间
         * 
         * @return 开始时间
         * @since V1.0
         */
        public Calendar getBeginTime() {
            return mBeginTime;
        }

        /**
         * 获取结束时间
         * 
         * @return 结束时间
         * @since V1.0
         */
        public Calendar getEndTime() {
            return mEngTime;
        }

        /**
         * 设置类型
         * 
         * @param type 类型
         * @since V1.0
         */
        public void setType(int type) {
            mType = type;
        }

        /**
         * 获取类型
         * 
         * @return 时间片段类型
         * @since V1.0
         */
        public int getType() {
            return mType;
        }

        /**
         * 获取时间长度
         * 
         * @return 时间长度
         * @since V1.0
         */
        public long getTimeLength() {
            if (null == mBeginTime || null == mEngTime) {
                return -1;
            }

            if (mEngTime.compareTo(mBeginTime) == -1) {
                return -1;
            }

            return mEngTime.getTimeInMillis() - mBeginTime.getTimeInMillis();
        }
    }
}
