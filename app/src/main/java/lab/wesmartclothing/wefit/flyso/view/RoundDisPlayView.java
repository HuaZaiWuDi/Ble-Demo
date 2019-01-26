package lab.wesmartclothing.wefit.flyso.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
*@date on 2018/12/28
*@author Jack
*@version 
*@describe TODO
*@org 智裳科技
*/
public class RoundDisPlayView extends View {

    private int mWidth;
    private int mHeight;
    private Paint paint1;
    private Paint paint2;
    private Paint paint_4;
    private Paint paintPoint;//下面4个点的画笔

    private int radius;//最近里面的圈的半径
    private int lineWidth;//线的宽度
    private int startAngle3 = 0;

    boolean isShowPoint = false;//是否显示点
    private int indexPoint = 0;//高亮的点
    private boolean showProgress = false;

    public RoundDisPlayView(Context context) {
        this(context, null);
    }

    public RoundDisPlayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundDisPlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void submit() {
        postInvalidate();
    }


    private void init() {

        lineWidth = dp2px(4);

        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setDither(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setColor(Color.parseColor("#61D97F"));
        paint1.setStrokeWidth(lineWidth);
        paint1.setAlpha((int) (255 * 0.1f));

        paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);


        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(lineWidth);
        paint2.setShader(new SweepGradient(0, 0, 0x0061D97F, 0xFF61D97F));


        paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPoint.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头
        paintPoint.setStrokeWidth(dp2px(8));
        paintPoint.setColor(Color.parseColor("#61D97F"));
        paintPoint.setAlpha((int) (255 * 0.1f));
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
            mWidth = dp2px(277);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = dp2px(280);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        radius = getMeasuredWidth() / 2; //不要在构造方法里初始化，那时还没测量宽高
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);

        drawRound3(canvas);
        //画中间的文字
//        drawCentreText(canvas);

        if (isShowPoint)
            drawLoad(canvas);
        canvas.restore();
    }


    private void drawLoad(Canvas canvas) {
        float startX = -dp2px(20);
        for (int i = 0; i < 4; i++) {
            if (i == indexPoint % 4) {
                paintPoint.setAlpha(255);
            } else {
                paintPoint.setAlpha((int) (255 * 0.1f));
            }
            canvas.drawPoint(startX + dp2px(15) * i, dp2px(70), paintPoint);
        }
    }


    public void showPoint(boolean show) {
        isShowPoint = show;
        invalidate();
    }

    public void startAnim() {
        indexPoint = 0;
        removeCallbacks(animRunnable);
        postDelayed(animRunnable, 500);
    }

    Runnable animRunnable = new Runnable() {
        @Override
        public void run() {
            indexPoint++;
            postInvalidate();
            postDelayed(this, 500);
        }
    };


    private void drawRound3(Canvas canvas) {
        canvas.save();
        //画最外层圈
        canvas.rotate(startAngle3, 0, 0);
        RectF rectf3 = new RectF(-radius + lineWidth, -radius + lineWidth, radius - lineWidth, radius - lineWidth);
        canvas.drawArc(rectf3, 0, 360, false, paint1);

        if (showProgress)
            canvas.drawArc(rectf3, 0, 360, false, paint2);
        canvas.restore();
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
//        startAnim();
//        startAnimation();
    }


    ValueAnimator cgAnima1;

    public void startAnimation() {
        if (cgAnima1 == null) {
            cgAnima1 = ValueAnimator.ofInt(0, 360);
            cgAnima1.setInterpolator(new LinearInterpolator());
            cgAnima1.setDuration(2000);
            cgAnima1.setRepeatCount(ValueAnimator.INFINITE);
            cgAnima1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    startAngle3 = (int) cgAnima1.getAnimatedValue();
                    invalidate();
                }
            });
        }
        cgAnima1.start();
        showProgress = true;
    }

    public void stopAnimation() {
        if (cgAnima1 != null) {
            cgAnima1.end();
        }
        showProgress = false;
        removeCallbacks(animRunnable);
        indexPoint = -1;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    public static int dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
