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
public class HealthyProgressView extends View {

    private int mWidth;
    private int mHeight;
    private Paint paint;
    private Paint paint_Text;
    private Paint paint_progress;
    private int round;//圆角
    //颜色列表
    private int[] colors = {Color.WHITE};

    //点阵集合  防止progress为0或者100时圆点被剪裁的问题，暂时这样处理
    private float max = 100f;
    private float min = 0f;
    private float progress = 0f;
    private float interval;//每段的间隔
    private String[] upText = {};
    private String[] downText = {};
    private float basePadding = dp2px(14);//文字和图形的间距
    private int index = 0;

    public HealthyProgressView(Context context) {
        this(context, null);
    }

    public HealthyProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HealthyProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setUpDownText(String[] up, String[] down) {
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
        paint.setColor(Color.parseColor("#FAFAFA"));


        paint_progress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_progress.setDither(true);
        paint_progress.setAntiAlias(true);
        paint_progress.setStyle(Paint.Style.FILL);
        paint_progress.setStrokeCap(Paint.Cap.ROUND);//设置为线条圆头

        paint_Text = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_Text.setDither(true);
        paint_Text.setAntiAlias(true);
        paint_Text.setStyle(Paint.Style.FILL);
        paint_Text.setTextAlign(Paint.Align.CENTER);
        paint_Text.setColor(Color.parseColor("#978E9D"));
        paint_Text.setTextSize(dp2px(11));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        round = mHeight / 2;

        float width = mWidth - getPaddingLeft() - getPaddingRight();
        interval = width / colors.length;

        drawBg(canvas);

        drawBar(canvas);

        drawUpText(canvas);
    }

    private void drawUpText(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < upText.length; i++) {
            canvas.drawText(upText[i], interval * (i + 1), mHeight / 2 - basePadding, paint_Text);
        }

        for (int i = 0; i < downText.length; i++) {
            canvas.drawText(downText[i], interval * (i + 1) - interval / 2f, mHeight / 2 + basePadding + getTextHeight(paint_Text), paint_Text);
        }
        canvas.restore();
    }

    //画进度
    private void drawBar(Canvas canvas) {
        canvas.save();
        float width = mWidth - getPaddingLeft() - getPaddingRight();
        progress = progress < min ? min : progress;
        progress = progress > max ? max : progress;

        float x = width * progress / (max - min);
        //所在的区间
        index = (int) (x / interval) + 1;

        if (progress == min) {
            canvas.translate(dp2px(6), mHeight / 2f);
        } else if (progress == max) {
            canvas.translate(width - dp2px(6), mHeight / 2f);
        } else {
            canvas.translate(x, mHeight / 2f);
        }
        paint_progress.setColor(colors[(index - 1) % colors.length]);
        canvas.drawCircle(0, 0, dp2px(6), paint_progress);
        paint_progress.setColor(Color.WHITE);
        canvas.drawCircle(0, 0, dp2px(2), paint_progress);
        canvas.restore();
    }

    //画进度的背景
    private void drawBg(Canvas canvas) {
        canvas.save();
        canvas.translate(0, mHeight / 2f);
        for (int i = 0; i < colors.length; i++) {
            RectF rf = new RectF(interval * i, -dp2px(2), interval * (i + 1), dp2px(2));
            /*绘制圆角矩形，背景色为画笔颜色*/
            paint.setColor(colors[i]);
            canvas.drawRect(rf, paint);
        }
        canvas.restore();
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
