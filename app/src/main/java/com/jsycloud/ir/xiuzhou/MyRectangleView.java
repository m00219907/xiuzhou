package com.jsycloud.ir.xiuzhou;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyRectangleView extends View {
    
    int rectangleColor;
    String textStr;

    public MyRectangleView(Context context) {
        super(context);
    }
    
    public MyRectangleView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    
    public void setRectangleColor(int color){
        this.rectangleColor = color;
    }
    
    public void settextStr(String textStr){
        this.textStr = textStr;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paintRect = new Paint();

        paintRect.setColor(rectangleColor);
        paintRect.setStyle(Style.FILL);
        paintRect.setAntiAlias(true);
        paintRect.setStrokeWidth(3.0f);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                getResources().getDimensionPixelSize(R.dimen.four_dp),
                getResources().getDimensionPixelSize(R.dimen.four_dp), paintRect);

        Paint paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(getResources().getDimensionPixelSize(R.dimen.sixteen_text_size));
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paintText.descent() + paintText.ascent()) / 2)) ;
        canvas.drawText(textStr, xPos, yPos, paintText);
    }

}
