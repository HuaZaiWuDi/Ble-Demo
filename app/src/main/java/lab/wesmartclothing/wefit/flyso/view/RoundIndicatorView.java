package lab.wesmartclothing.wefit.flyso.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import lab.wesmartclothing.wefit.flyso.R;


/**
 * 公司名称：DXY鼎芯
 * 项目名称：DXYBle_GM
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/6/7
 */

public class RoundIndicatorView extends View {

    private Paint paint;
    private Paint paint_2;
    private Paint paint_3;
    private Paint paint_4;
    private Context context;
    private int maxNum;
    private int startAngle;
    private int sweepAngle;
    private int radius;
    private int mWidth;
    private int mHeight;
    private int sweepInWidth;//内圆的宽度
    private int sweepOutWidth;//外圆的宽度
    private int currentNum = 0;//需设置setter、getter 供属性动画使用
    private String[] text = {"较差", "中等", "良好", "优秀", "极好"};
    private int[] indicatorColor = {0xffffffff, 0x00ffffff, 0x99ffffff, 0xffffffff};
    private int backGroundColor;

    private String content = 9537 + "";//中心文字
    private String text_top = "今天还能吃";
    private String text_bottom = "单位：";

    int runMi = 0;
    int ka = 0;


    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        postInvalidate();
    }


    public void setText(String content, String text_top, String text_bottom) {
        this.content = content;
        this.text_top = text_top;
        this.text_bottom = text_bottom;
        postInvalidate();
    }


    public RoundIndicatorView(Context context) {
        this(context, null);
    }

    public RoundIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundIndicatorView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setBackgroundColor(Color.parseColor("#25b4b4"));
        initAttr(attrs);
        initPaint();
    }

    public void setCurrentNumAnim(int num) {
        this.currentNum = num;
        invalidate();
    }


    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffffffff);
        paint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    private void initAttr(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundIndicatorView);
        maxNum = array.getInt(R.styleable.RoundIndicatorView_maxNum, 30000);
        startAngle = array.getInt(R.styleable.RoundIndicatorView_startAngle, -90);
        sweepAngle = array.getInt(R.styleable.RoundIndicatorView_sweepAngle, 360);
        //内外圆的宽度
        sweepInWidth = dp2px(8);
        sweepOutWidth = dp2px(3);
        array.recycle();
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
            mWidth = dp2px(300);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = dp2px(300);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        radius = getMeasuredWidth() / 2; //不要在构造方法里初始化，那时还没测量宽高
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        drawRound(canvas);  //画内外圆
//        drawScale(canvas);//画刻度
        drawIndicator(canvas); //画当前进度值
        drawCenterText(canvas);//画中间的文字
        canvas.restore();
    }

    private void drawCenterText(final Canvas canvas) {
        canvas.save();
        paint_4.setStyle(Paint.Style.FILL);
        paint_4.setTextSize(radius / 2);
        paint_4.setColor(0xffffffff);

        canvas.drawText(content, -paint_4.measureText(currentNum + "") / 2, 0, paint_4);

        float v = paint_4.measureText(currentNum + "");


        paint_4.setTextSize(radius / 8);
        Rect r = new Rect();
//        canvas.drawText(unit, v / 2, 0, paint_4);

//        paint_4.getTextBounds(text_bottom, 0, text_bottom.length(), r);
        canvas.drawText(text_top, -r.width() / 2, r.height() + 50, paint_4);
        canvas.drawText(text_bottom, -r.width() / 2, r.height() - 50, paint_4);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        paint_2.setStyle(Paint.Style.STROKE);
        int sweep;
        if (currentNum <= maxNum) {
            sweep = (int) ((float) currentNum / (float) maxNum * sweepAngle);
        } else {
            sweep = sweepAngle;
        }
        paint_2.setStrokeWidth(sweepOutWidth);
        paint_2.setColor(Color.parseColor("#FFFFFF"));
//        Shader shader = new SweepGradient(0, 0, indicatorColor, null);
//        paint_2.setShader(shader);
        int w = dp2px(10);
        RectF rectf = new RectF(-radius - w, -radius - w, radius + w, radius + w);
        canvas.drawArc(rectf, startAngle, sweep, false, paint_2);
        float x = (float) ((radius + dp2px(10)) * Math.cos(Math.toRadians(startAngle + sweep)));
        float y = (float) ((radius + dp2px(10)) * Math.sin(Math.toRadians(startAngle + sweep)));
        paint_3.setStyle(Paint.Style.FILL);
        paint_3.setColor(Color.parseColor("#DDDDDD"));
        //paint_3.setMaskFilter(new BlurMaskFilter(dp2px(3), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
        canvas.drawCircle(x, y, dp2px(5), paint_3);
        paint_3.setColor(0xffffffff);
        canvas.drawCircle(x, y, dp2px(3), paint_3);

        canvas.restore();
    }

    private void drawScale(Canvas canvas) {
        canvas.save();
        float angle = (float) sweepAngle / 30;//刻度间隔
        canvas.rotate(-270 + startAngle); //将起始刻度点旋转到正上方（270)
        for (int i = 0; i <= 30; i++) {
            if (i % 6 == 0) {   //画粗刻度和刻度值
                paint.setStrokeWidth(dp2px(2));
                paint.setAlpha(0x70);
                canvas.drawLine(0, -radius - sweepInWidth / 2, 0, -radius + sweepInWidth / 2 + dp2px(1), paint);
                drawText(canvas, i * maxNum / 30 + "", paint);
            } else {         //画细刻度
                paint.setStrokeWidth(dp2px(1));
                paint.setAlpha(0x50);
                canvas.drawLine(0, -radius - sweepInWidth / 2, 0, -radius + sweepInWidth / 2, paint);
            }
            if (i == 3 || i == 9 || i == 15 || i == 21 || i == 27) {  //画刻度区间文字
                paint.setStrokeWidth(dp2px(2));
                paint.setAlpha(0x50);
                drawText(canvas, text[(i - 3) / 6], paint);
            }
            canvas.rotate(angle); //逆时针
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas, String text, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(sp2px(8));
        float width = paint.measureText(text); //相比getTextBounds来说，这个方法获得的类型是float，更精确些
//        Rect rect = new Rect();
//        paint.getTextBounds(text,0,text.length(),rect);
        canvas.drawText(text, -width / 2, -radius + dp2px(15), paint);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void drawRound(Canvas canvas) {
        canvas.save();
//        //内圆
//        paint.setAlpha(0x40);
//        paint.setStrokeWidth(sweepInWidth);
//        RectF rectf = new RectF(-radius, -radius, radius, radius);
//        canvas.drawArc(rectf, startAngle, sweepAngle, false, paint);
        //外圆
        paint.setStrokeWidth(sweepOutWidth);
        paint.setColor(Color.parseColor("#DDDDDD"));
        int w = dp2px(10);
        RectF rectf2 = new RectF(-radius - w, -radius - w, radius + w, radius + w);
        canvas.drawArc(rectf2, startAngle, sweepAngle, false, paint);
        canvas.restore();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            doAnim(this.currentNum);
        }
    }

    private void doAnim(int i) {
        if (i == 0) return;
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(1, i);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator1) {
                int i1 = (int) valueAnimator.getAnimatedValue();
                RoundIndicatorView.this.setCurrentNumAnim(i1);
            }
        });
        valueAnimator.start();
    }


    //一些工具方法
    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    protected int sp2px(int sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics());
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
