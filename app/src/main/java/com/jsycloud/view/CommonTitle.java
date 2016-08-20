package com.jsycloud.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;



/**
 * 通用头部控件，包含： 左图标按钮，文字标题，右图标按钮
 * 
 * @author 25391
 */
public class CommonTitle extends RelativeLayout implements OnClickListener {

    /**
     * 左侧按钮ID
     */
    public static final int      ID_LEFT  = 0;
    /**
     * 右侧按钮ID
     */
    public static final int      ID_RIGHT = 1;

    /**
     * 左侧按钮
     */
    private ImageView            mLeft;

    /**
     * 右侧按钮
     */
    private ImageView            mRight;

    /**
     * 文字标题
     */
    private TextView             mTitle;

    /**
     * 点击监听
     */
    private OnTitleClickListener mListener;

    public CommonTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.widget_common_title, this);

        mLeft = (ImageView) findViewById(R.id.left);
        mRight = (ImageView) findViewById(R.id.right);
        mTitle = (TextView) findViewById(R.id.title_center);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonTitle);

        int leftResId = a.getResourceId(R.styleable.CommonTitle_drawableLeft, -1);
        if (leftResId != -1) {
            mLeft.setImageResource(leftResId);
        } else {
            mLeft.setVisibility(INVISIBLE);
        }

        int rightResId = a.getResourceId(R.styleable.CommonTitle_drawableRight, -1);
        if (rightResId != -1) {
            mRight.setImageResource(rightResId);
        } else {
            mRight.setVisibility(INVISIBLE);
        }

        String centerResId = a.getString(R.styleable.CommonTitle_textCenter);
        if (!TextUtils.isEmpty(centerResId)) {
            mTitle.setText(centerResId);
        } else {
            mTitle.setVisibility(INVISIBLE);
        }

        int color = a.getColor(R.styleable.CommonTitle_textCenterColor, 0x000);
        mTitle.setTextColor(color);

        int size = a.getDimensionPixelSize(R.styleable.CommonTitle_textCenterSize, 16);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        a.recycle();
        
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mListener = listener;
    }

    public void setLeftIcon(int resId) {
        if (mLeft != null) {
            mLeft.setVisibility(View.VISIBLE);
            mLeft.setImageResource(resId);
        }
    }

    public void setRightIcon(int resId) {
        if (mRight != null) {
            mRight.setVisibility(View.VISIBLE);
            mRight.setImageResource(resId);
        }
    }

    public void setText(int resId) {
        if (mTitle != null) {
            mTitle.setText(resId);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.left:
                mListener.onCommonTitleClick(ID_LEFT);
            break;
            case R.id.right:
                mListener.onCommonTitleClick(ID_RIGHT);
            break;
            default:
            break;
        }
    }

    public interface OnTitleClickListener {
        public void onCommonTitleClick(int id);
    }
}
