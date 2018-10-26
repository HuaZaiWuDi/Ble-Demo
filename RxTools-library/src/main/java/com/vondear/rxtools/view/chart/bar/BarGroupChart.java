package com.vondear.rxtools.view.chart.bar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vondear.rxtools.utils.RxLogUtils;
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

    private int mWidth, mHeight, barWidth = RxUtils.dp2px(10), barHeight;
    private Paint textPaint, progressPaint;
    private float round;//圆角
    private float progressHeight = 0;//进度高度
    private ValueAnimator mAnimator;
    private boolean isAnimating;
    private RectF rf = new RectF();
    private int topValue = 0, oldValue;
    private int color = Color.parseColor("#FF7200");

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

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setDither(true);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(RxUtils.sp2px(8));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        progressPaint.setColor(color);
        textPaint.setColor(color);

        round = barWidth / 2;
        canvas.clipRect(0, 0, mWidth, mHeight - round);
        drawProgress(canvas);
    }


    //画进度
    private void drawProgress(Canvas canvas) {

        //进度最高高度
        barHeight = (int) (mHeight * 0.9f);

        float progress = (progressHeight / 100f * barHeight);

        float top = progressHeight != 0 ? mHeight - progress : mHeight - round * 2;

        rf.left = mWidth / 2 - round;
        rf.top = top;
        rf.right = mWidth / 2 + round;
        rf.bottom = mHeight;
        /*绘制圆角矩形，背景色为画笔颜色*/
        canvas.drawRoundRect(rf, round, round, progressPaint);

        RxLogUtils.d("字体底部：" + textPaint.getFontMetricsInt().bottom);

        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(oldValue), mWidth / 2, top - (mHeight - barHeight) / 2, textPaint);
    }


    public void setProgress(int progress, boolean anim) {
        float oldValue = this.progressHeight;

        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;

        if (anim) {
            if (isAnimating) {
                isAnimating = false;
                mAnimator.cancel();
            }

            startAnimation(oldValue, progress);
        }
        postInvalidate();
    }


    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    private void startAnimation(float start, final float end) {
        mAnimator = ValueAnimator.ofFloat(start, end);
        int duration = (int) Math.abs(1000 * (end - start) / 100);
        mAnimator.setDuration(duration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressHeight = (float) animation.getAnimatedValue();
                oldValue = (int) (topValue * ((float) animation.getAnimatedValue() + 100 - end) / 100);
                invalidate();
            }
        });


        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mAnimator.start();
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
            mWidth = RxUtils.dp2px(40);
        }


        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = RxUtils.dp2px(100);
        }
        setMeasuredDimension(mWidth, mHeight);
    }


    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////


    public void setTopValue(int topValue) {
        this.topValue = topValue;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }
}
