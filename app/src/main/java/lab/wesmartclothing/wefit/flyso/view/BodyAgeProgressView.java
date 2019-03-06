package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created icon_hide_password jk on 2018/5/19.
 */
public class BodyAgeProgressView extends View {

    private int mWidth;
    private int mHeight;
    private Paint paint;
    private Paint paint_Text;
    private Paint paint_progress;

    //颜色列表
    private int[] colors = {Color.WHITE};

    //点阵集合  防止progress为0或者100时圆点被剪裁的问题，暂时这样处理
    private float max = 100f;
    private float min = 0f;
    private float progress = 0f;
    private float interval;//每段的间隔
    private String upText = "25";
    private String[] downText = {};
    private float basePadding = dp2px(10);//文字和图形的间距
    private float barHeight = dp2px(3);
    private float round;//圆角
    private int index = 0;

    public BodyAgeProgressView(Context context) {
        this(context, null);
    }

    public BodyAgeProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BodyAgeProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setUpDownText(String up, String[] down) {
        if (up != null) upText = up;
        if (down != null) downText = down;
    }

    public void setMaxMin(float max, float min) {
        if (min >= max) {
            Log.e("HealthyProgressView", "min 必须小于max");
        } else {
            this.max = max;
            this.min = min;
        }
    }


    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public String getStatus() {
        return downText[index];
    }


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头
        paint.setColor(Color.parseColor("#FFACF2BE"));


        paint_progress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_progress.setDither(true);
        paint_progress.setAntiAlias(true);
        paint_progress.setStyle(Paint.Style.FILL);
        paint_progress.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头
        paint_progress.setColor(Color.parseColor("#FF62D981"));

        paint_Text = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_Text.setDither(true);
        paint_Text.setAntiAlias(true);
        paint_Text.setStyle(Paint.Style.FILL);


        paint_Text.setTextSize(dp2px(11));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        round = barHeight / 2;

        canvas.translate(0, mHeight / 2f);

        mWidth = mWidth - getPaddingLeft() - getPaddingRight();
        interval = mWidth / (downText.length + 1);

        drawBg(canvas);

        drawProgress(canvas);

    }


    //画进度
    private void drawProgress(Canvas canvas) {
        progress = progress < min ? min : progress;
        progress = progress > max ? max : progress;

        float x = mWidth * progress / (max - min);

        //画进度
        canvas.drawRoundRect(new RectF(0, -round, x, round), round, round, paint_progress);

        if (x < dp2px(15)) {
            x = dp2px(15);
        } else if (x > mWidth - dp2px(15)) {
            x = mWidth - dp2px(15);
        }

        //画顶上的圆
        canvas.drawRoundRect(new RectF(x - dp2px(15), -(basePadding + dp2px(12)), x + dp2px(15), -basePadding), dp2px(6), dp2px(6), paint_progress);

        paint_Text.setColor(Color.WHITE);
        //画顶上的文字
        canvas.drawText(upText, x, -dp2px(12), paint_Text);

    }

    //画进度的背景
    private void drawBg(Canvas canvas) {

        //画背景
        canvas.drawRoundRect(new RectF(0, -round, mWidth, round), round, round, paint);

        for (int i = 0; i < downText.length; i++) {

            float startX = interval * (i + 1);
            //画节点
            paint_progress.setStrokeWidth(dp2px(2));
            canvas.drawLine(startX, -round/2, startX, round/2, paint_progress);

            //节点下面的文字
            paint_Text.setTextAlign(Paint.Align.CENTER);
            paint_Text.setColor(Color.parseColor("#978E9D"));
            canvas.drawText(downText[i], startX, basePadding + getTextHeight(paint_Text), paint_Text);
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
            mWidth = dp2px(360);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = dp2px(60);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    public static int dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    static float getTextHeight(Paint textPaint) {
        return -textPaint.ascent() - textPaint.descent();
    }
}
