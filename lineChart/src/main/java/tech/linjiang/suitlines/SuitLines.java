/*
 * Copyright 2017 linjiang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.linjiang.suitlines;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EdgeEffect;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://github.com/whataa
 */
public class SuitLines extends View {

    public static final String TAG = SuitLines.class.getSimpleName();

    public SuitLines(Context context) {
        this(context, null);
    }

    public SuitLines(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuitLines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initOptionalState(context, attrs);

        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        basePadding = Util.dip2px(basePadding);

        scroller = new Scroller(context);
        edgeEffectLeft = new EdgeEffect(context);
        edgeEffectRight = new EdgeEffect(context);
        setEdgeEffectColor(edgeEffectColor);

        basePaint.setColor(defaultLineColor[0]);
        basePaint.setStyle(Paint.Style.STROKE);
        basePaint.setStrokeWidth(4);
        coverLinePaint.setStyle(Paint.Style.STROKE);
        coverLinePaint.setStrokeWidth(Util.dip2px(5));
        setLineStyle(0, SOLID);

        xyPaint.setTextSize(Util.size2sp(defaultXySize, getContext()));
        xyPaint.setColor(defaultXyColor);

        hintPaint.setTextSize(Util.size2sp(12, getContext()));
        hintPaint.setColor(defaultXyColor);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setStrokeWidth(2);
        hintPaint.setTextAlign(Paint.Align.CENTER);
        hintPaint.setAlpha((int) (255 * 0.6f));

        selectPaint.setTextSize(Util.size2sp(12, getContext()));
        selectPaint.setColor(defaultXyColor);
        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setStrokeWidth(2);


        //每个点上面的线条
//        pointLinePaint.setStyle(Paint.Style.STROKE);
        pointLinePaint.setStrokeWidth(0.3f);
        pointLinePaint.setColor(defaultXyColor);
        pointLinePaint.setAlpha((int) (255 * 0.6f));


        //辅助线
        LimitLinePaint.setStyle(Paint.Style.FILL);
        LimitLinePaint.setPathEffect(new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0));
        LimitLinePaint.setStrokeWidth(1f);
        LimitLinePaint.setColor(defaultXyColor);
        LimitLinePaint.setAlpha((int) (255 * 0.6f));
    }

    private void initOptionalState(Context ctx, AttributeSet attrs) {
        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.suitlines);
        defaultXySize = ta.getFloat(R.styleable.suitlines_xySize, defaultXySize);
        defaultXyColor = ta.getColor(R.styleable.suitlines_xyColor, defaultXyColor);
        lineType = ta.getInt(R.styleable.suitlines_lineType, CURVE);
        lineStyle = ta.getInt(R.styleable.suitlines_lineStyle, SOLID);
        needEdgeEffect = ta.getBoolean(R.styleable.suitlines_needEdgeEffect, needEdgeEffect);
        edgeEffectColor = ta.getColor(R.styleable.suitlines_colorEdgeEffect, edgeEffectColor);
        needShowHint = ta.getBoolean(R.styleable.suitlines_needClickHint, needShowHint);
        hintColor = ta.getColor(R.styleable.suitlines_colorHint, hintColor);
        maxOfVisible = ta.getInt(R.styleable.suitlines_maxOfVisible, maxOfVisible);
        countOfY = ta.getInt(R.styleable.suitlines_countOfY, countOfY);
        ta.recycle();
    }

    protected int mHeight;
    protected float mWidth;

    // 创建自己的Handler，与ViewRootImpl的Handler隔离，方便detach时remove。
    private Handler handler = new Handler(Looper.getMainLooper());
    private RectF linesArea, xArea, yArea;
    /**
     * 默认画笔
     */
    private Paint basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * x，y轴对应的画笔
     */
    private Paint xyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 点击提示的画笔
     */
    private Paint hintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 点击提示的画笔
     */
    private Paint selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 默认画笔的颜色，索引0位置为画笔颜色，整个数组为shader颜色
     */
    private int[] defaultLineColor = {Color.RED, Color.YELLOW, Color.WHITE};
    private int hintColor = Color.RED;
    /**
     * xy轴文字颜色和大小
     */
    private int defaultXyColor = Color.parseColor("#FFFFFF");
    private float defaultXySize = 8;
    /**
     * 每根画笔对应一条线
     */
    private List<Paint> paints = new ArrayList<>();
    private List<Path> paths = new ArrayList<>();
    private Path tmpPath = new Path();
    /**
     * fill形态下时，边缘线画笔
     */
    Paint coverLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 约定：如果需要实现多组数据，那么每组数据的长度必须相同！
     * 多组数据的数据池；
     * Key：一组数据的唯一标识,注意：要求连续且从0开始
     * value：一组数据
     */
    private Map<Integer, List<Unit>> datas = new HashMap<>();

    /**
     * 一组数据在可见区域中的最大可见点数，至少>=2
     */
    private int maxOfVisible = 7;
    /**
     * 文本之间/图表之间的间距
     */
    private int basePadding = 4;
    /**
     * y轴刻度数，至少>=1
     */
    private int countOfY = 5;

    /**
     * y轴的缓存，提高移动效率
     */
    private Bitmap yAreaBuffer;

    /**
     * y轴的最大刻度值，保留一位小数
     */
    private float maxValueOfY = 100f;
    /**
     * y轴的最小刻度值，保留一位小数
     */
    private float minValueY = 0f;

    /**
     * 根据可见点数计算出的两点之间的距离
     */
    private float realBetween;
    /**
     * 手指/fling的上次位置
     */
    private float lastX;
    /**
     * 滚动当前偏移量
     */
    private float offset;
    /**
     * 滚动上一次的偏移量
     */
    private float lastOffset;
    /**
     * 滚动偏移量的边界
     */
    private float maxOffset;

    /**
     * 判断左/右方向，当在边缘就不触发fling，以优化性能
     */
    float orientationX;
    private VelocityTracker velocityTracker;
    private Scroller scroller;

    private EdgeEffect edgeEffectLeft, edgeEffectRight;
    // 对于fling，仅吸收到达边缘时的速度
    private boolean hasAbsorbLeft, hasAbsorbRight;
    /**
     * 是否需要边缘反馈效果
     */
    private boolean needEdgeEffect = false;
    private int edgeEffectColor = Color.GRAY;
    /**
     * fill形态下，是否绘制边缘线
     * 若开启该特性，闭合路径的操作将延迟到绘制时
     */
    private boolean needCoverLine;
    /**
     * 点击是否弹出额外信息
     */
    private boolean needShowHint = true;
    /**
     * 实际的点击位置，0为x索引，1为某条line
     */
    private int[] clickIndexs = new int[2];
    private float firstX, firstY;
    /**
     * 控制是否强制重新生成path，当改变lineType/paint时需要
     */
    private boolean forceToDraw;

    /**
     * lines在当前可见区域的边缘点
     */
    private int[] suitEdge;

    /**
     * 每个点后面的线
     */
    private Paint pointLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 辅助线
     */
    private Paint LimitLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 图标上下间距
     */
    private float spaceMax = 0;
    private float spaceMin = 0;

    /**
     * 画辅助线
     */
    private Map<Float, String> limits;

    public interface LineChartSelectItemListener {
        void selectItem(int valueX);
    }

    public interface LineChartScrollEdgeListener {
        void leftEdge();

        void rightEdge();
    }

    private LineChartSelectItemListener mLineChartSelectItemListener;
    private LineChartScrollEdgeListener mLineChartScrollEdgeListener;

    public void setLineChartSelectItemListener(LineChartSelectItemListener lineChartSelectItemListener) {
        mLineChartSelectItemListener = lineChartSelectItemListener;
    }

    public void setLineChartScrollEdgeListener(LineChartScrollEdgeListener lineChartScrollEdgeListener) {
        mLineChartScrollEdgeListener = lineChartScrollEdgeListener;
    }

    // 曲线、线段
    public static final int CURVE = 0;
    public static final int SEGMENT = 1;
    private int lineType = CURVE;
    public static final int SOLID = 0;
    public static final int DASHED = 1;
    private int lineStyle = SOLID;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calcAreas();
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        if (!datas.isEmpty()) {
            calcUnitXY();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private int mLastX, mMove, mMinVelocity;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                scroller.forceFinished(true);
                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker();
                return false;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void changeMoveAndValue() {
        offset -= mMove;
        if (offset <= maxOffset) {
            offset = maxOffset;
            mMove = 0;
            scroller.forceFinished(true);
        } else if (offset >= 0) {
            offset = 0;
            mMove = 0;
            scroller.forceFinished(true);
        }

        clickIndexs[0] = (Math.round(Math.abs(offset) * 1.0f / realBetween));
        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (mLineChartSelectItemListener != null && !datas.isEmpty()) {
            mLineChartSelectItemListener.selectItem(clickIndexs[0]);
        }
    }

    private void countMoveEnd() {
        offset -= mMove;
        if (offset <= maxOffset) {
            offset = maxOffset;
            Log.d("测试：", "滑动到右边");
            if (mLineChartScrollEdgeListener != null)
                mLineChartScrollEdgeListener.rightEdge();
        } else if (offset >= 0) {
            offset = 0;
            Log.d("测试：", "滑动到左边");
            if (mLineChartScrollEdgeListener != null)
                mLineChartScrollEdgeListener.leftEdge();
        }

        mLastX = 0;
        mMove = 0;

        clickIndexs[0] = (Math.round(Math.abs(offset) * 1.0f / realBetween));
        offset = -clickIndexs[0] * realBetween; // 矫正位置,保证不会停留在两个相邻刻度之间
        notifyValueChange();
        postInvalidate();
    }

    private void countVelocityTracker() {
        velocityTracker.computeCurrentVelocity(1000);
        float xVelocity = velocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > mMinVelocity) {
            scroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (scroller.getCurrX() == scroller.getFinalX()) { // over
                countMoveEnd();
            } else {
                int xPosition = scroller.getCurrX();
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }


    //绘制想级提示
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (datas.isEmpty()) return;
        // lines

        canvas.save();
        canvas.clipRect(linesArea.left, linesArea.top, linesArea.right, linesArea.bottom + xArea.height());
        canvas.translate(offset, 0);

        // 当滑动到边缘 或 上次与本次结果相同 或 不需要计算边缘点 的时候就不再计算，直接draw已有的path
        if (!paths.isEmpty() && !forceToDraw) {
            drawExsitDirectly(canvas);
            //画线上的点
            drawPoint(canvas);
            drawClickHint(canvas);
//            Log.d("测试", "1111111111");
        } else if (!paths.isEmpty()) {
            // 因为手指或fling计算出的offset不是连续按1px递增/减的，即无法准确地确定当前suitEdge和linesArea之间的相对位置
            // 所以不适合直接加减suitEdge来划定数据区间
            drawLines(canvas, 0, datas.get(0).size() - 1);
            drawPoint(canvas);
            drawClickHint(canvas);
//            Log.d("测试", "22222222222");
        }

        // x 蓝色会稍增加
        if (!datas.isEmpty())
            drawX(canvas, 0, datas.get(0).size() - 1);

        forceToDraw = false;
        canvas.restore();

        // 不位移
        drawHintLine(canvas);
    }


    //画辅助线
    private void drawHintLine(Canvas canvas) {
        if (limits != null && limits.size() > 0) {
            Set<Float> integers = limits.keySet();
            for (float value : integers) {
                String text = limits.get(value);
                maxValueOfY = value > maxValueOfY ? value : maxValueOfY;
                minValueY = value < minValueY ? value : minValueY;

                float y = linesArea.top + (linesArea.height() * (0.8f - spaceMin)) * (1 - value / maxValueOfY + spaceMax);

                xyPaint.setTextAlign(Paint.Align.LEFT);

                canvas.drawText(text, linesArea.left + basePadding, y, xyPaint);
                LimitLinePaint.setPathEffect(new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0));
                canvas.drawLine(linesArea.left + xyPaint.measureText(text) + basePadding * 2, y,
                        linesArea.right, y, LimitLinePaint);
            }
        }
    }

    //画线上的点
    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            for (int j = 0; j < datas.get(i).size(); j++) {
                Unit unit = datas.get(i).get(j);
                if (unit.isShowPoint())
                    canvas.drawCircle(unit.getXY().x, unit.getXY().y, 10f, hintPaint);
            }
        }
    }


    public void scrollLast() {
        offset = maxOffset;
        invalidate();
    }


    /**
     * 开始连接每条线的各个点<br>
     * 最耗费性能的地方：canvas.drawPath
     *
     * @param canvas
     * @param startIndex
     * @param endIndex
     */
    private void drawLines(Canvas canvas, int startIndex, int endIndex) {
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).reset();
        }
        for (int i = startIndex; i <= endIndex; i++) {
            for (int j = 0; j < datas.size(); j++) {
                Unit current = datas.get(j).get(i);

                float curY = linesArea.bottom - (linesArea.bottom - current.getXY().y) * current.getPercent();
                if (i == startIndex) {
                    paths.get(j).moveTo(current.getXY().x, curY);
                    continue;
                }

                if (current.getLineType() == SEGMENT) {
                    paths.get(j).lineTo(current.getXY().x, curY);
                } else if (current.getLineType() == CURVE) {
                    // 到这里肯定不是起始点，所以可以减1
                    Unit previous = datas.get(j).get(i - 1);
                    // 两个锚点的坐标x为中点的x，y分别是两个连接点的y
                    paths.get(j).cubicTo((previous.getXY().x + current.getXY().x) / 2,
                            linesArea.bottom - (linesArea.bottom - previous.getXY().y) * previous.getPercent(),
                            (previous.getXY().x + current.getXY().x) / 2, curY,
                            current.getXY().x, curY);
                }
                //绘制阴影需要连接底部的点
                boolean fill = current.isFill();
                if (!needCoverLine && fill && i == endIndex) {
                    paths.get(j).lineTo(current.getXY().x, linesArea.bottom);
                    paths.get(j).lineTo(datas.get(j).get(startIndex).getXY().x, linesArea.bottom);
                    paths.get(j).close();
                }
            }
        }
        drawExsitDirectly(canvas);
    }

    /**
     * 直接draw现成的
     *
     * @param canvas
     */
    private void drawExsitDirectly(Canvas canvas) {
        // TODO 需要优化
        for (int j = 0; j < datas.size(); j++) {
            boolean isFill = datas.get(j).get(0).isFill();
            paints.get(j).setStyle(isFill ? Paint.Style.FILL : Paint.Style.STROKE);
            canvas.drawPath(paths.get(j), paints.get(j));
        }
    }

    /**
     * 画提示文本和辅助线
     *
     * @param canvas
     */
    public void drawClickHint(Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            Unit unit = datas.get(i).get(clickIndexs[0]);
            if (unit.isShowPoint())
                canvas.drawCircle(unit.getXY().x, unit.getXY().y, 20f, selectPaint);
        }
    }

    /**
     * 画x轴,默认取第一条线的值
     *
     * @param canvas
     * @param startIndex
     * @param endIndex
     */
    private void drawX(Canvas canvas, int startIndex, int endIndex) {
//        canvas.drawLine(datas.get(0).get(startIndex).getXY().x, xArea.top,
//                datas.get(0).get(endIndex).getXY().x, xArea.top, xyPaint);
        for (int i = startIndex; i <= endIndex; i++) {
            String extX = datas.get(0).get(i).getExtX();
            if (TextUtils.isEmpty(extX)) {
                continue;
            }
            xyPaint.setTextAlign(Paint.Align.CENTER);
            if (i == clickIndexs[0]) {
                xyPaint.setAlpha(255);
                pointLinePaint.setAlpha(255);
            } else {
                xyPaint.setAlpha((int) (255 * 0.6f));
                pointLinePaint.setAlpha((int) (255 * 0.6f));
            }

            canvas.drawText(extX, datas.get(0).get(i).getXY().x, Util.calcTextSuitBaseY(xArea, xyPaint) + 20, xyPaint);
            pointLinePaint.setPathEffect(new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0));
            canvas.drawLine(datas.get(0).get(i).getXY().x, linesArea.bottom, datas.get(0).get(i).getXY().x, linesArea.top, pointLinePaint);
        }

//        canvas.drawLine(datas.get(0).get(startIndex).getXY().x, 50f,
//                datas.get(0).get(endIndex).getXY().x, 50f, xyPaint);

    }

    /**
     * @param color 不能为null
     * @return
     */
    private LinearGradient buildPaintColor(int[] color) {
        int[] bakColor = color;
        if (color != null && color.length < 2) {
            bakColor = new int[2];
            bakColor[0] = color[0];
            bakColor[1] = color[0];
        }
        return new LinearGradient(linesArea.left, linesArea.top,
                linesArea.left, linesArea.bottom, bakColor, null, Shader.TileMode.CLAMP);
    }

    /**
     * 基于orgPaint的clone
     *
     * @return
     */
    private Paint buildNewPaint() {
        Paint paint = new Paint();
        paint.set(basePaint);
        return paint;
    }


    /**
     * 更新图表
     */
    private void feedInternal(Map<Integer, List<Unit>> entry, List<Paint> entryPaints,
                              Map<Integer, int[]> colors) {
        reset(); // 该方法调用了datas.clear();
        if (entry.isEmpty()) {
            invalidate();
            return;
        }
        paints.clear();
        paints.addAll(entryPaints);
        if (entry.size() != paths.size()) {
            paths.clear();
            for (int i = 0; i < entry.size(); i++) {
                paths.add(new Path());
            }
        }
        datas.putAll(entry);
        calcMaxUnit(datas);
        calcAreas();
        calcUnitXY();
        forceToDraw = true;
        for (int i = 0; i < paints.size(); i++) {
            Paint paint = paints.get(i);
            paint.setColor(colors.get(0)[0]);
            paint.setShader(buildPaintColor(colors.get(i)));
        }
        clickIndexs = new int[]{entry.get(0).size() - 1, 0};
        notifyValueChange();
        invalidate();

        postAction(new Runnable() {
            @Override
            public void run() {
                scrollLast();
            }
        });

    }

    /**
     * 得到最大值，最小值作为图表的上下间隙
     *
     * @param datas
     */
    private void calcMaxUnit(Map<Integer, List<Unit>> datas) {
        // 先“扁平”
        List<Unit> allUnits_1 = new ArrayList<>();

        for (List<Unit> line : datas.values()) {
            allUnits_1.addAll(line);
        }

        // 再拷贝，防止引用问题
        List<Unit> bakUnits_1 = new ArrayList<>();

        for (int i = 0; i < allUnits_1.size(); i++) {
            bakUnits_1.add(allUnits_1.get(i).clone());
        }

        // 最后排序，得到最大值
        Collections.sort(bakUnits_1);

        maxValueOfY = bakUnits_1.get(bakUnits_1.size() - 1).getValue();
        minValueY = bakUnits_1.get(0).getValue();

        if (limits != null && limits.size() > 0) {
            Set<Float> integers = limits.keySet();
            for (float value : integers) {
                maxValueOfY = value > maxValueOfY ? value : maxValueOfY;
                minValueY = value < minValueY ? value : minValueY;
            }
        }
    }

    /**
     * 重新计算三个区域的大小
     */
    private void calcAreas() {
        RectF validArea = new RectF(getPaddingLeft() + basePadding, getPaddingTop() + basePadding,
                getMeasuredWidth() - getPaddingRight() - basePadding, getMeasuredHeight() - getPaddingBottom());
        yArea = new RectF(validArea.left, validArea.top,
                validArea.left,
                validArea.bottom - Util.getTextHeight(xyPaint) - basePadding * 2);
        xArea = new RectF(validArea.left, yArea.top, validArea.right, validArea.top);

        linesArea = new RectF(validArea.left, validArea.top, validArea.right, validArea.bottom);
    }

    /**
     * 计算所有点的坐标
     * <br>同时得到了realBetween，maxOffset
     */
    private void calcUnitXY() {
        realBetween = linesArea.width() / maxOfVisible;
        for (int i = 0; i < datas.get(0).size(); i++) {
            for (int j = 0; j < datas.size(); j++) {

                datas.get(j).get(i).setXY(new PointF(linesArea.left + realBetween * i + linesArea.width() * 0.5f,
                        linesArea.top + (linesArea.height() * (0.8f - spaceMin)) * (1 - datas.get(j).get(i).getValue() / maxValueOfY + spaceMax)));

                if (i == datas.get(0).size() - 1) {
                    maxOffset = Math.abs(datas.get(j).get(i).getXY().x) - linesArea.width() * 0.5f - linesArea.left;
                    maxOffset = -maxOffset;
                }
            }
        }
    }


    /**
     * 重置相关状态
     */
    private void reset() {
        invaliateYBuffer();
        offset = 0;
        realBetween = 0;
        suitEdge = null;
        datas.clear();
    }

    private void invaliateYBuffer() {
        if (yAreaBuffer != null) {
            yAreaBuffer.recycle();
            yAreaBuffer = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        cancelAllAnims();
//        reset();
    }

    ///APIs/////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 设置默认一条line时的颜色
     *
     * @param colors 默认为defaultLineColor
     */
    public void setDefaultOneLineColor(int... colors) {
        if (colors == null || colors.length < 1) return;
        defaultLineColor = colors;
        basePaint.setColor(colors[0]);
        if (linesArea != null) {// 区域还未初始化
            basePaint.setShader(buildPaintColor(colors));
        }
        if (!datas.isEmpty() && datas.size() == 1) {
            paints.get(0).set(basePaint);
            postInvalidate();
        }
    }

    /**
     * 设置提示辅助线、文字颜色
     *
     * @param hintColor
     */
    public void setHintColor(int hintColor) {
        needShowHint = true;
        this.hintColor = hintColor;
        hintPaint.setColor(hintColor);
        if (!datas.isEmpty()) {
            postInvalidate();
        }
    }

    /**
     * 设置xy轴文字的颜色
     *
     * @param color 默认为Color.GRAY
     */
    public void setXyColor(int color) {
        defaultXyColor = color;
        xyPaint.setColor(defaultXyColor);
        if (!datas.isEmpty()) {
            invaliateYBuffer();
            forceToDraw = true;
            postInvalidate();
        }
    }

    /**
     * 设置xy轴文字大小
     *
     * @param sp
     */
    public void setXySize(float sp) {
        defaultXySize = sp;
        xyPaint.setTextSize(Util.size2sp(defaultXySize, getContext()));
        if (!datas.isEmpty()) {
            invaliateYBuffer();
            calcAreas();
            calcUnitXY();
            offset = 0;// fix bug.
            forceToDraw = true;
            postInvalidate();
        }
    }


    /**
     * 设置line的SEGMENT时的大小
     *
     * @param lineSize
     */
    public void setLineSize(int index, float lineSize) {
        basePaint.setStyle(Paint.Style.STROKE);
        basePaint.setStrokeWidth(lineSize);
        // 同时更新当前已存在的paint
        forceToDraw = true;
        paints.get(index).setStyle(basePaint.getStyle());
        paints.get(index).setStrokeWidth(lineSize);
        postInvalidate();
    }

    /**
     * 指定line类型：CURVE / SEGMENT
     *
     * @param lineType 默认CURVE
     */
    public void setLineType(int lineType) {
        this.lineType = lineType;
        forceToDraw = true;
        postInvalidate();
    }

    public int getLineType() {
        return lineType;
    }

    /**
     * 设置line的形态：是否填充
     *
     * @param isFill 默认为false
     */
    public void setLineForm(final int index, final boolean isFill) {
        if (!datas.isEmpty()) {
            // 同时更新当前已存在的paint
            postAction(new Runnable() {
                @Override
                public void run() {
                    forceToDraw = true;
                    paints.get(index % datas.size()).setStyle(isFill ? Paint.Style.FILL : Paint.Style.STROKE);
                    postInvalidate();
                }
            });
        }
    }

    public boolean isLineFill() {
        return basePaint.getStyle() == Paint.Style.FILL;
    }

    public void setLineStyle(final int index, int style) {
        lineStyle = style;

        basePaint.setPathEffect(lineStyle == DASHED ? new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0) : null);
        if (!datas.isEmpty()) {
            // 同时更新当前已存在的paint
            forceToDraw = true;
            paints.get(index % datas.size()).setPathEffect(basePaint.getPathEffect());
            postInvalidate();
        }
    }

    public boolean isLineDashed() {
        return basePaint.getPathEffect() != null;
    }


    /**
     * 图形上下显示的区域
     */
    public void setSpaceMaxMin(float max, float min) {
        this.spaceMax = max;
        this.spaceMin = min;
    }


    /**
     * 关闭点击提示信息，默认开启
     */
    public void disableClickHint() {
        needShowHint = false;
    }

    /**
     * 指定边缘效果的颜色
     *
     * @param color 默认为Color.GRAY
     */
    public void setEdgeEffectColor(int color) {
        needEdgeEffect = true;
        edgeEffectColor = color;
        Util.trySetColorForEdgeEffect(edgeEffectLeft, edgeEffectColor);
        Util.trySetColorForEdgeEffect(edgeEffectRight, edgeEffectColor);
        postInvalidate();
    }

    /**
     * 设置辅助线
     */
    public void setlimitLabels(Map<Float, String> limits) {
        this.limits = limits;
        invalidate();
    }

    public int[] getClickIndexs() {
        return clickIndexs == null ? new int[]{} : clickIndexs;
    }

    public void postAction(Runnable runnable) {
        handler.post(runnable);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    // 多条线的情况应该采用该构建方式
    public static class LineBuilder {
        private int curIndex;
        private Map<Integer, List<Unit>> datas;
        private Map<Integer, int[]> colors;

        public LineBuilder() {
            datas = new HashMap<>();
            colors = new HashMap<>();
        }

        /**
         * 该方式是用于构建多条line，单条line可使用lineGraph#feed
         *
         * @param data  单条line的数据集合
         * @param color 指定当前line的颜色。默认取数组的第一个颜色；另外如果开启了填充，则整个数组颜色作为填充色的渐变。
         * @return
         */
        public LineBuilder add(List<Unit> data, int... color) {
            if (data == null || data.isEmpty() || color == null || color.length <= 0) {
                Log.e("LineBuilder", "无效参数data或color");
            } else {
                int bakIndex = curIndex;
                datas.put(bakIndex, data);
                colors.put(bakIndex, color);
                curIndex++;
            }
            return this;
        }

        /**
         * 调用该方法开始填充数据，该方法需要保证SuitLines已经初始化
         *
         * @param suitLines 需要被填充的图表
         * @param needAnim  是否需要动画
         */
        public void build(final SuitLines suitLines, final boolean needAnim) {
            final List<Paint> tmpPaints = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                Paint paint = suitLines.buildNewPaint();
                paint.setPathEffect(datas.get(i).get(0).getLineStyle() == DASHED ? new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0) : null);
//                paint.setStyle(datas.get(i).get(0).isFill() ? Paint.Style.FILL : Paint.Style.STROKE);
                tmpPaints.add(i, paint);
            }

            suitLines.postAction(new Runnable() {
                @Override
                public void run() {
                    suitLines.feedInternal(datas, tmpPaints, colors);
                }
            });
        }
    }
}