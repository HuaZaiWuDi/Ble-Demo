package com.smartclothing.module_wefit.widget.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.smartclothing.module_wefit.R;

public class UpdateProgressBar extends ProgressBar {

    /**进度条高度*/
    private float progressHeight;

    /**浮动框宽度*/
    private float floatRectWidth;

    /**浮动框高度*/
    private float floatRectHeight;

    /**浮动框左右边距*/
    private float margin;

    /**浮动框颜色*/
    private int rectColor;

    /**进度条填充颜色*/
    protected int fillColor;

    protected int rectInt=6;//浮动矩形的圆角度

    public UpdateProgressBar(Context context) {
        super(context);
    }

    public UpdateProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UpdateProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void getDimension() {
        super.getDimension();
        progressHeight = height / 5*2;
        floatRectWidth = height / 1;
        floatRectHeight = height / 5 * 2;
        margin = 0;
    }

    private void init(AttributeSet attrs){
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.floatTextProgressBar);
        fillColor = a.getColor(R.styleable.floatTextProgressBar_fillColorProgress, 0xffff0000);
        rectColor = a.getColor(R.styleable.floatTextProgressBar_rectColor, 0xffff0000);
        a.recycle();
    }

    @Override
    public void drawProgress(Canvas canvas) {
        //绘制未填充进度条
        paint.setColor(backgroundColor);
        RectF backgroundRectF = new RectF(0, height - progressHeight, width, height);
        canvas.drawRoundRect(backgroundRectF, progressHeight / 2, progressHeight / 2, paint);

        //绘制填充条
        paint.setColor(fillColor);
        //避免1%-3%时，矩形在内矩形外面
        RectF fillRectF;
        if(progress<5 && progress>0){
            fillRectF = new RectF(0, height - progressHeight, (float) (4 / 100.0 * width), height);
        } else {
            fillRectF = new RectF(0, height - progressHeight, progressWidth, height);
        }
        canvas.drawRoundRect(fillRectF, progressHeight / 2, progressHeight / 2, paint);
    }

    /**
     * 绘制浮动框
     * @param canvas
     */
    private void drawFloatRect(Canvas canvas){
        if (progressWidth < floatRectWidth/2){
            //绘制浮动框
            paint.setColor(rectColor);
            RectF floatRectF = new RectF(margin, 0, margin + floatRectWidth, floatRectHeight);//floatRectWidth
            canvas.drawRoundRect(floatRectF, dip2px(rectInt), dip2px(rectInt), paint);
        } else if (progressWidth > (width - floatRectWidth/2)){
            //绘制浮动框
            paint.setColor(rectColor);
            RectF floatRectF = new RectF(width - floatRectWidth - margin ,0, width - margin, floatRectHeight);
            canvas.drawRoundRect(floatRectF, dip2px(rectInt), dip2px(rectInt), paint);
        } else {
            //绘制浮动框
            paint.setColor(rectColor);
            RectF floatRectF = new RectF(progressWidth - floatRectWidth/2 ,0, progressWidth + floatRectWidth/2, floatRectHeight);
            canvas.drawRoundRect(floatRectF, dip2px(rectInt), dip2px(rectInt), paint);
        }
    }

    @Override
    public void drawText(Canvas canvas) {

    }

    /**
     * 设置填充色
     * @param fillColor
     */
    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * 设置浮动框颜色
     * @param rectColor
     */
    public void setRectColor(int rectColor) {
        this.rectColor = rectColor;
    }

}
