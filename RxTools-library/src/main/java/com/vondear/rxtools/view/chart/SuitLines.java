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

package com.vondear.rxtools.view.chart;

import android.content.Context;
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
import android.widget.Scroller;

import java.util.ArrayList;
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

        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        basePadding = Util.dip2px(basePadding);

        scroller = new Scroller(context);


        //XY轴的画笔
        xyPaint.setTextSize(Util.size2sp(defaultXySize, getContext()));
        xyPaint.setColor(defaultXyColor);

        //线上的点的画笔
        hintPaint.setTextSize(Util.size2sp(12, getContext()));
        hintPaint.setColor(defaultXyColor);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setStrokeWidth(2);
        hintPaint.setTextAlign(Paint.Align.CENTER);
        hintPaint.setAlpha((int) (255 * 0.6f));

        //点击的圆圈画笔
        selectPaint.setTextSize(Util.size2sp(12, getContext()));
        selectPaint.setColor(defaultXyColor);
        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setStrokeWidth(2);


        //每个点上面的线条
        pointLinePaint.setStyle(Paint.Style.STROKE);
        pointLinePaint.setStrokeWidth(0.3f);
        pointLinePaint.setColor(defaultXyColor);
        pointLinePaint.setAlpha((int) (255 * 0.6f));


        //辅助线
        LimitLinePaint.setStyle(Paint.Style.STROKE);
        LimitLinePaint.setPathEffect(new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0));
        LimitLinePaint.setStrokeWidth(2f);
        LimitLinePaint.setColor(defaultXyColor);
        LimitLinePaint.setAlpha((int) (255 * 0.6f));
    }


    protected int mHeight;
    protected float mWidth;

    // 创建自己的Handler，与ViewRootImpl的Handler隔离，方便detach时remove。
    private Handler handler = new Handler(Looper.getMainLooper());
    private RectF linesArea, xArea, yArea;
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
     * 虚线path
     */
    private Path dashPath = new Path();
    private Path dashPath2 = new Path();

    /**
     * xy轴文字颜色和大小
     */
    private int defaultXyColor = Color.parseColor("#FFFFFF");
    private float defaultXySize = 8;
    /**
     * 每根画笔对应一条线
     */
    /**
     * 约定：如果需要实现多组数据，那么每组数据的长度必须相同！
     * 多组数据的数据池；
     * Key：一组数据的唯一标识,注意：要求连续且从0开始
     * value：一组数据
     */
    private List<LineBean> mLineBeans = new ArrayList<>();

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
     * y轴的最大刻度值，保留一位小数
     */
    private float maxValueY = 100f;
    /**
     * y轴的最小刻度值，保留一位小数
     */
    private float minValueY = 0f;

    /**
     * 是否统一使用默认的最大最小值
     */
    private boolean isUnifiedInterval = true;

    /**
     * 根据可见点数计算出的两点之间的距离
     */
    private float realBetween;
    /**
     * 滚动当前偏移量
     */
    private float offset;
    /**
     * 滚动偏移量的边界
     */
    private float maxOffset;

    private VelocityTracker velocityTracker;
    private Scroller scroller;

    /**
     * 实际的点击位置，0为x索引，1为某条line
     */
    private int[] clickIndexs = new int[2];
    /**
     * 控制是否强制重新生成path，当改变lineType/paint时需要
     */
    private boolean forceToDraw;


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
    private int space = 0;

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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calcAreas();
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        if (!mLineBeans.isEmpty()) {
            calcUnitXY();
        }
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

        int click = clickIndexs[0];

        clickIndexs[0] = (Math.round(Math.abs(offset) * 1.0f / realBetween));
        if (click != clickIndexs[0]) {
            notifyValueChange();
        }
        postInvalidate();
    }

    private void notifyValueChange() {
        if (mLineChartSelectItemListener != null && !mLineBeans.isEmpty()) {
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
        Log.e(TAG, "countMoveEnd: ");
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
        if (mLineBeans.isEmpty()) return;
        canvas.save();
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.clipRect(linesArea.left, linesArea.top, linesArea.right, linesArea.bottom + xArea.height());
        canvas.translate(offset, 0);

        // 当滑动到边缘 或 上次与本次结果相同 或 不需要计算边缘点 的时候就不再计算，直接draw已有的path
        if (!forceToDraw) {
            drawExsitDirectly(canvas);
        } else {
            drawLines(canvas);
        }

        //画选中效果
        drawClickHint(canvas);
        //画线上的点
        drawPoint(canvas);
        // X轴
        drawX(canvas);

        forceToDraw = false;
        canvas.restore();

        // 不位移
        drawHintLine(canvas);
        //Y轴
    }

    //画辅助线
    private void drawHintLine(Canvas canvas) {

        if (limits != null && limits.size() > 0) {
            Set<Float> integers = limits.keySet();
            for (float value : integers) {
                String text = limits.get(value);

                float y = linesArea.top + linesArea.height() * (1 - (value - minValueY) / (maxValueY - minValueY));

                xyPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(text, linesArea.left + basePadding, y, xyPaint);

                LimitLinePaint.setPathEffect(new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0));
                dashPath2.reset();
                dashPath2.moveTo(linesArea.left + xyPaint.measureText(text) + basePadding * 2, y);
                dashPath2.lineTo(linesArea.right, y);
                dashPath2.close();
                canvas.drawPath(dashPath2, LimitLinePaint);
//                canvas.drawLine(linesArea.left + xyPaint.measureText(text) + basePadding * 2, y,
//                        linesArea.right, y, LimitLinePaint);
            }
        }
    }

    //画线上的点
    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < mLineBeans.size(); i++) {
            for (int j = 0; j < mLineBeans.get(i).getUnits().size(); j++) {
                Unit unit = mLineBeans.get(i).getUnits().get(j);
                if (mLineBeans.get(i).isShowPoint())
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
     */
    private void drawLines(Canvas canvas) {
        for (int i = 0; i < mLineBeans.size(); i++) {
            for (int j = 0; j < mLineBeans.get(i).getUnits().size(); j++) {
                Unit current = mLineBeans.get(i).getUnits().get(j);
                Path path = mLineBeans.get(i).getPath();

                float curY = linesArea.bottom - (linesArea.bottom - current.getXY().y) * current.getPercent();
                if (j == 0) {
                    path.reset();
                    path.moveTo(current.getXY().x, curY);
                    continue;
                }

                if (mLineBeans.get(i).getLineType() == SEGMENT) {
                    path.lineTo(current.getXY().x, curY);
                } else if (mLineBeans.get(i).getLineType() == CURVE) {
                    // 到这里肯定不是起始点，所以可以减1
                    Unit previous = mLineBeans.get(i).getUnits().get(j - 1);
                    // 两个锚点的坐标x为中点的x，y分别是两个连接点的y
                    path.cubicTo((previous.getXY().x + current.getXY().x) / 2,
                            linesArea.bottom - (linesArea.bottom - previous.getXY().y) * previous.getPercent(),
                            (previous.getXY().x + current.getXY().x) / 2, curY,
                            current.getXY().x, curY);
                }
                //绘制阴影需要连接底部的点
                if (mLineBeans.get(i).isFill() && j == mLineBeans.get(i).getUnits().size() - 1) {
                    path.lineTo(current.getXY().x, linesArea.bottom);
                    path.lineTo(mLineBeans.get(i).getUnits().get(0).getXY().x, linesArea.bottom);
                    path.close();
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
        for (int j = 0; j < mLineBeans.size(); j++) {
            canvas.drawPath(mLineBeans.get(j).getPath(), mLineBeans.get(j).getPaint());
        }
    }

    /**
     * 画提示文本和辅助线
     *
     * @param canvas
     */
    public void drawClickHint(Canvas canvas) {
        for (int i = 0; i < mLineBeans.size(); i++) {
            Unit unit = mLineBeans.get(i).getUnits().get(clickIndexs[0]);
            if (mLineBeans.get(i).isShowPoint())
                canvas.drawCircle(unit.getXY().x, unit.getXY().y, 15f, selectPaint);
        }
    }

    /**
     * 画x轴,默认取第一条线的值
     *
     * @param canvas
     */
    private void drawX(Canvas canvas) {
//        canvas.drawLine(datas.get(0).get(startIndex).getXY().x, xArea.top,
//                datas.get(0).get(endIndex).getXY().x, xArea.top, xyPaint);
        for (int i = 0; i <= mLineBeans.get(0).getUnits().size() - 1; i++) {
            Unit unit = mLineBeans.get(0).getUnits().get(i);

            String extX = unit.getExtX();
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

            canvas.drawText(extX, unit.getXY().x, Util.calcTextSuitBaseY(xArea, xyPaint) + 20, xyPaint);
            pointLinePaint.setPathEffect(new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0));

            dashPath.reset();
            dashPath.moveTo(unit.getXY().x, linesArea.bottom);
            dashPath.lineTo(unit.getXY().x, linesArea.top);
            dashPath.close();
            canvas.drawPath(dashPath, pointLinePaint);

        }

//        canvas.drawLine(datas.get(0).get(startIndex).getXY().x, 50f,
//                datas.get(0).get(endIndex).getXY().x, 50f, xyPaint);

    }

    /**
     * @param color 横向镜像
     * @return
     */
    private LinearGradient buildPaintColor_H(int[] color) {
        return new LinearGradient(0, 0,
                linesArea.width() / 2, 0, color, null, Shader.TileMode.MIRROR);
    }

    /**
     * @param color 竖向渐变
     * @return
     */
    private LinearGradient buildPaintColor_V(int[] color) {
        return new LinearGradient(0, 0,
                0, getHeight(), color, null, Shader.TileMode.CLAMP);
    }


    /**
     * 更新图表
     */
    private void feedInternal(final List<LineBean> mLineBeans) {
        reset(); // 该方法调用了datas.clear();
        if (mLineBeans.isEmpty()) {
            invalidate();
            Log.d(TAG, "feedInternal: data is empty");
            return;
        }
        this.mLineBeans = mLineBeans;
        calcAreas();
        calcUnitXY();
        forceToDraw = true;

        clickIndexs = new int[]{mLineBeans.get(0).getUnits().size() - 1, 0};
        notifyValueChange();

        postAction(new Runnable() {
            @Override
            public void run() {
                for (LineBean bean : mLineBeans) {
                    Paint paint = bean.getPaint();
                    paint.setStrokeWidth(bean.getLineWidth());
                    paint.setStyle(bean.isFill() ? Paint.Style.FILL : Paint.Style.STROKE);
                    if (bean.getColor().length == 1) {
                        paint.setColor(bean.getColor()[0]);
                    } else {
                        paint.setShader(bean.isFill() ? buildPaintColor_V(bean.getColor()) : buildPaintColor_H(bean.getColor()));
                    }
                    paint.setPathEffect(bean.getLineStyle() == DASHED ? new DashPathEffect(new float[]{Util.dip2px(3), Util.dip2px(6)}, 0) : null);
                }
                invalidate();
            }
        });

        postAction(new Runnable() {
            @Override
            public void run() {
                scrollLast();
            }
        });


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

        linesArea = new RectF(validArea.left, xArea.bottom, validArea.right, validArea.bottom);
    }

    /**
     * 计算所有点的坐标
     * <br>同时得到了realBetween，maxOffset
     */
    private void calcUnitXY() {
        if (mLineBeans.isEmpty()) return;
        if (limits != null && limits.size() > 0) {
            Set<Float> integers = limits.keySet();
            for (float value : integers) {
                maxValueY = value > maxValueY ? value : maxValueY;
                minValueY = value < minValueY ? value : minValueY;
            }
        }

        //统一的最大最小值
        if (isUnifiedInterval) {
            for (int i = 0; i < mLineBeans.size(); i++) {
                maxValueY = mLineBeans.get(i).getMaxValue() > maxValueY ? mLineBeans.get(i).getMaxValue() : maxValueY;
                minValueY = mLineBeans.get(i).getMinValue() < minValueY ? mLineBeans.get(i).getMinValue() : minValueY;
            }
        }
        minValueY = minValueY * 0.8f;
        maxValueY = maxValueY * 1.1f;
        realBetween = linesArea.width() / maxOfVisible;
        for (int i = 0; i < mLineBeans.get(0).getUnits().size(); i++) {
            for (int j = 0; j < mLineBeans.size(); j++) {
                Unit unit = mLineBeans.get(j).getUnits().get(i);

                if (isUnifiedInterval) {
                    unit.setXY(new PointF(linesArea.left + realBetween * i + linesArea.width() * 0.5f,
                            linesArea.top - space + linesArea.height() * (1 - (unit.getValue() - minValueY) / (maxValueY - minValueY))));
                } else {
                    unit.setXY(new PointF(linesArea.left + realBetween * i + linesArea.width() * 0.5f,
                            linesArea.top + linesArea.height() * (1 - (unit.getValue() - mLineBeans.get(j).getMinValue())
                                    / (mLineBeans.get(j).getMaxValue() - mLineBeans.get(j).getMinValue()))));
                }

                if (i == mLineBeans.get(0).getUnits().size() - 1) {
                    maxOffset = Math.abs(unit.getXY().x) - linesArea.width() * 0.5f - linesArea.left;
                    maxOffset = -maxOffset;
                }
            }
        }
    }


    /**
     * 重置相关状态
     */
    private void reset() {
        offset = 0;
        realBetween = 0;
        mLineBeans.clear();
    }

    ///APIs/////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置xy轴文字的颜色
     *
     * @param color 默认为Color.GRAY
     */
    public void setXyColor(int color) {
        defaultXyColor = color;
        xyPaint.setColor(defaultXyColor);
        if (!mLineBeans.isEmpty()) {
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
        if (!mLineBeans.isEmpty()) {
            calcAreas();
            calcUnitXY();
            offset = 0;
            forceToDraw = true;
            postInvalidate();
        }
    }


    /**
     * 图形上下显示的区域
     */
    public void setSpaceMin(int space) {
        this.space = space;
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

        private List<LineBean> mLineBeans;

        public LineBuilder() {
            mLineBeans = new ArrayList<>();
        }

        /**
         * 该方式是用于构建多条line，单条line可使用lineGraph#feed
         *
         * @param lineBean 单条line的数据集合
         * @return
         */
        public LineBuilder add(LineBean lineBean) {
            if (lineBean != null) {
                mLineBeans.add(lineBean);
            }
            return this;
        }

        /**
         * 调用该方法开始填充数据，该方法需要保证SuitLines已经初始化
         *
         * @param suitLines 需要被填充的图表
         */
        public void build(final SuitLines suitLines) {
            suitLines.postAction(new Runnable() {
                @Override
                public void run() {
                    suitLines.feedInternal(mLineBeans);
                }
            });
        }
    }

}