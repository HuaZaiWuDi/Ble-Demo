package com.vondear.rxtools.view.roundprogressbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vondear.rxtools.utils.RxUtils;

/**
 * @Package com.vondear.rxtools.view.roundprogressbar
 * @FileName VerticalProgress
 * @Date 2018/10/23 10:28
 * @Author JACK
 * @Describe TODO竖直的进度条
 * @Project Android_WeFit_2.0
 */
public class VerticalProgress extends View {

    private int mWidth, mHeight;
    private Paint bgProgressPaint, progressPaint;
    private float round;//圆角
    private float progressHeight = 0;//进度高度
    private ValueAnimator mAnimator;
    private boolean isAnimating;
    private boolean isClip = false;//是否剪裁只显示顶部圆角

    public VerticalProgress(Context context) {
        this(context, null);
    }

    public VerticalProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgProgressPaint.setDither(true);
        bgProgressPaint.setAntiAlias(true);
        bgProgressPaint.setStyle(Paint.Style.FILL);
        bgProgressPaint.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头
        bgProgressPaint.setColor(Color.parseColor("#ECEBF0"));

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setDither(true);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(Color.parseColor("#FF7200"));
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        round = mWidth / 2;
        // 必须先设置显示区域再绘制,剪裁掉底部的圆角
        if (isClip)
            canvas.clipRect(0, 0, mWidth, mHeight - round);
        drawBg(canvas);
        drawProgress(canvas);

    }


    //画进度
    private void drawProgress(Canvas canvas) {
        float progress = (progressHeight / 100f * mHeight);
        RectF rf = new RectF(0, (mHeight - progress), mWidth, mHeight);
        /*绘制圆角矩形，背景色为画笔颜色*/
        canvas.drawRoundRect(rf, round, round, progressPaint);
    }

    //画进度的背景
    private void drawBg(Canvas canvas) {
        RectF rf = new RectF(0, 0, mWidth, mHeight);

        /*绘制圆角矩形，背景色为画笔颜色*/
        canvas.drawRoundRect(rf, round, round, bgProgressPaint);

    }

    public void setClip(boolean clip) {
        isClip = clip;
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

    private void startAnimation(float start, float end) {
        mAnimator = ValueAnimator.ofFloat(start, end);
        int duration = (int) Math.abs(1000 * (end - start) / 100);
        mAnimator.setDuration(duration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressHeight = (float) animation.getAnimatedValue();
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
            mWidth = RxUtils.dp2px(8);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = RxUtils.dp2px(44);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

}
