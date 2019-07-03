package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.view
 * @FileName NormalLineView
 * @Date 2019/6/29 16:29
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class NormalLineView extends View {

    private Paint linePaint;//曲线画笔
    private Paint textPaint;//曲线画笔
    private Path linePath;//曲线路径
    private int startPadding;//view四周padding默认dp
    private int endPadding;//view四周padding默认dp
    private int topPadding;//view四周padding默认dp
    private int bottomPadding;//view四周padding默认dp

    private int maxVisibility = 7;
    //数据集合
    private List<Integer> dataList = new ArrayList<>();
    private float lineWidth = 2;
    private int mWidth;
    private int mHeight;
    private boolean isInitialized = false;
    private int minValue = 100;
    private int maxValue = 0;
    private int stepSpace;
    private boolean isBezierLine;
    private DashPathEffect dashPath;
    private String[] texts = new String[]{"建议", "摄入"};

    private int color = Color.parseColor("#286DD4");

    public NormalLineView(Context context) {
        this(context, null);
    }

    public NormalLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setupView();
    }

    private void setupView() {
        lineWidth = dip2px(1);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);//抗锯齿
        linePaint.setStyle(Paint.Style.STROKE);//STROKE描边FILL填充
        linePaint.setStrokeWidth(lineWidth);//边框宽度
        linePaint.setColor(color);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);//抗锯齿
        textPaint.setStyle(Paint.Style.FILL);//STROKE描边FILL填充
        textPaint.setColor(color);
        textPaint.setTextSize(dip2px(8));

        linePath = new Path();

        startPadding = dip2px(35);
        endPadding = dip2px(5);
        topPadding = dip2px(2);
        bottomPadding = dip2px(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);//绘制背景颜色
        canvas.translate(0f, mHeight);//设置画布中心点垂直居中

        if (!isInitialized) {
            setupLine();
        }

        if (texts != null && texts.length == 2)
            drawYText(canvas);

        if (dashPath != null)
            linePaint.setPathEffect(dashPath);
        canvas.drawPath(linePath, linePaint);
    }

    private void drawYText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.top - fontMetrics.bottom;

        int y = -mHeight / 2;
        if (!dataList.isEmpty()) {
            y = -getValueHeight(dataList.get(0));
        }
        if (Math.abs(y) - textHeight > mHeight) {
            y -= textHeight;
        } else if (Math.abs(y) + textHeight < 0) {
            y += textHeight * 1.5f;
        }

        canvas.drawText(texts[0], (float) (startPadding * 0.2), y, textPaint);
        canvas.drawText(texts[1], (float) (startPadding * 0.2), y - fontMetrics.top + fontMetrics.bottom, textPaint);

    }

    private void setupLine() {
        if (dataList.isEmpty()) return;

        int stepTemp = startPadding;
        Point pre = new Point();
        pre.set(stepTemp, -getValueHeight(dataList.get(0)));//坐标系从0,0默认在第四象限绘制
        linePath.moveTo(pre.x, pre.y);

        if (dataList.size() == 1) {
            isInitialized = true;
            return;
        }

        for (int i = 1; i < dataList.size(); i++) {
            double data = dataList.get(i);
            Point next = new Point();
            next.set(stepTemp += stepSpace, -getValueHeight(data));

            if (isBezierLine) {
                int cW = pre.x + stepSpace / 2;

                Point p1 = new Point();//控制点1
                p1.set(cW, pre.y);

                Point p2 = new Point();//控制点2
                p2.set(cW, next.y);

                linePath.cubicTo(p1.x, p1.y, p2.x, p2.y, next.x, next.y);//创建三阶贝塞尔曲线
            } else {
                linePath.lineTo(next.x, next.y);
            }

            pre = next;
        }

        isInitialized = true;
    }

    /**
     * 获取value值所占的view高度
     *
     * @param value
     * @return
     */
    private int getValueHeight(double value) {
        float valuePercent = (float) (Math.abs(value - minValue) * 100f / (Math.abs(maxValue - minValue) * 100f));//计算value所占百分比
        return (int) ((mHeight - topPadding) * valuePercent + bottomPadding / 2);//底部加上间隔
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);//父类期望的高度
        if (MeasureSpec.EXACTLY == heightMode) {
            height = getPaddingTop() + getPaddingBottom() + height;
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);//父类期望的高度
        if (MeasureSpec.EXACTLY == MeasureSpec.getMode(widthMeasureSpec)) {
            width = getPaddingLeft() + getPaddingRight() + width;
        }
        setMeasuredDimension(width, height);//设置自己的宽度和高度

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        stepSpace = (mWidth - startPadding - endPadding) / (maxVisibility - 1);
    }


    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void refreshLayout() {
        resetParam();
        requestLayout();
        postInvalidate();
    }

    private void resetParam() {
        linePath.reset();
        isInitialized = false;
    }


    /**
     * 线条颜色的字体颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * 文字
     *
     * @param texts
     */
    public void setTexts(String[] texts) {
        this.texts = texts;
    }

    /**
     * 设置是否是贝塞尔曲线
     *
     * @param isBezier
     */
    public void setBezierLine(boolean isBezier) {
        isBezierLine = isBezier;
        refreshLayout();
    }


    /**
     * 设置曲线点的间距，标尺x轴间距
     *
     * @param dp
     */
    public void setStepSpace(int dp) {
        if (dp < 20) {
            dp = 20;
        }
        stepSpace = dip2px(dp);
    }

    public void setMaxVisibility(int maxVisibility) {
        this.maxVisibility = maxVisibility;
        stepSpace = (mWidth - startPadding - endPadding) / (maxVisibility - 1);
    }


    /**
     * 设置曲线宽度
     *
     * @param dp
     */
    public void setLineWidth(int dp) {
        if (dp < 1) {
            dp = 1;
        }
        lineWidth = dp;
        linePaint.setStrokeWidth(dip2px(lineWidth));//边框宽度
    }

    public void setMaxMinValue(int maxValue, int minValue) {
        this.maxValue = maxValue;
        this.minValue = minValue;
        refreshLayout();
    }


    /**
     * 返回画笔
     */
    public Paint getLinePaint() {
        return linePaint;
    }

    /**
     * 设置虚线
     */
    public void setDashPath(DashPathEffect dashPath) {
        this.dashPath = dashPath;
    }

    /**
     * 设置数据
     *
     * @param dataList
     */
    public void setData(List<Integer> dataList) {
        if (dataList == null) {
            throw new RuntimeException("dataList cannot is null!");
        }
        if (dataList.isEmpty()) return;
        this.dataList.clear();
        this.dataList.addAll(dataList);

        maxValue = Collections.max(this.dataList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int) (o1 - o2);
            }
        });

        minValue = Collections.min(this.dataList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int) (o1 - o2);
            }
        });

        refreshLayout();
    }


}
