package com.vondear.rxtools.view.chart.bar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vondear.rxtools.utils.RxUtils;

/**
 * @Package com.vondear.rxtools.view.chart.bar
 * @FileName BarGroupChart
 * @Date 2018/10/23 17:23
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class BarGroupChart extends View {

    private int mWidth, mHeight;
    private Paint bgProgressPaint, progressPaint;
    private float round;//圆角
    private float progressHeight = 0;//进度高度
    private ValueAnimator mAnimator;
    private boolean isAnimating;


    public BarGroupChart(Context context) {
        this(context, null);
    }

    public BarGroupChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarGroupChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
        } else {
            mWidth = RxUtils.dp2px(8);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = RxUtils.dp2px(46);
        }
        setMeasuredDimension(mWidth, mHeight);
    }


}
