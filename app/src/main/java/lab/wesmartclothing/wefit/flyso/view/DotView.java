package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vondear.rxtools.utils.RxUtils;

/**
 * @Package lab.wesmartclothing.wefit.flyso.view
 * @FileName DotView
 * @Date 2018/10/24 15:22
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class DotView extends View {

    private int mWidth, mHeight;
    private Paint paintPoint;//下面4个点的画笔
    private int indexPoint = 2;//当前点的下标
    private int dotTotal = 3;//点的总数
    @ColorInt
    private int selectColor = Color.parseColor("#61D97F");
    @ColorInt
    private int defaultColor = Color.parseColor("#D9D8E1");


    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPoint.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头
        paintPoint.setStrokeWidth(RxUtils.dp2px(8));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLoad(canvas);
    }

    private void drawLoad(Canvas canvas) {
        for (int i = 0; i < dotTotal; i++) {
            if (i == indexPoint % dotTotal) {
                paintPoint.setColor(selectColor);
            } else {
                paintPoint.setColor(defaultColor);
            }
            canvas.drawPoint(RxUtils.dp2px(8) + RxUtils.dp2px(15) * i, mHeight / 2, paintPoint);
        }
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
            mWidth = RxUtils.dp2px(50);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = RxUtils.dp2px(16);
        }
        setMeasuredDimension(mWidth, mHeight);
    }


    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置点的颜色
     *
     * @param selectColor
     * @param defaultColor
     */
    public void setDotColor(@ColorInt int selectColor, @ColorInt int defaultColor) {
        this.defaultColor = defaultColor;
        this.selectColor = selectColor;

    }

    /**
     * 点的总数
     *
     * @param count
     */
    public void setDotTotal(int count) {
        this.dotTotal = count;
    }

    /**
     * 当前点
     *
     * @param index
     */
    public void setDotIndex(int index) {
        this.indexPoint = index;
        postInvalidate();
    }


}
