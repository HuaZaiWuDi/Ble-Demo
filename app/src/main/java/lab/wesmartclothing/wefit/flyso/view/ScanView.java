package lab.wesmartclothing.wefit.flyso.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created icon_hide_password Jack on 2018/5/22.
 */
@SuppressLint("AppCompatCustomView")
public class ScanView extends ImageView {
    private Paint mPaintSector;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //绘画圆渐变色的画笔
        mPaintSector = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSector.setAntiAlias(true);
        mPaintSector.setStyle(Paint.Style.FILL);

    }

    private Matrix matrix;
    private int x;
    private int y;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        x = getWidth() / 2;
        y = getHeight() / 2;
        //背景
        setBackgroundResource(R.mipmap.img_bj33x);

        //绘制颜色渐变圆
        //初始化一个颜色渲染器
        float viewSize = (float) ((float) x * 0.88);

        SweepGradient shader = new SweepGradient(x, y, Color.TRANSPARENT, Color.WHITE);
        //mPaintSector设置颜色渐变渲染器
        mPaintSector.setShader(shader);
        //根据matrix中设定角度，不断绘制shader,呈现出一种扇形扫描效果
        canvas.concat(matrix);

        canvas.drawCircle(x, y, viewSize, mPaintSector);
        canvas.restore();
    }


    ValueAnimator animate;

    public void startAnimation() {
        if (animate == null) {
            animate = ValueAnimator.ofInt(0, 360);
            animate.setInterpolator(new LinearInterpolator());
            animate.setDuration(1000);
            animate.setRepeatCount(-1);
            animate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int angle = (int) animate.getAnimatedValue();
                    matrix = new Matrix();
                    //设定旋转角度,制定进行转转操作的圆心
                    matrix.preRotate(angle, x, y);
                    postInvalidate();
                }
            });
        }
        animate.start();
    }

    public boolean isAnim() {
        if (animate != null)
            return animate.isRunning();
        return false;
    }

    public void stopAnimation() {
        if (animate != null)
            animate.end();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (animate != null) {
            animate.cancel();
            animate = null;
        }

        super.onDetachedFromWindow();
    }

    //    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//        startAnimation(-1, 360);
//    }
}
