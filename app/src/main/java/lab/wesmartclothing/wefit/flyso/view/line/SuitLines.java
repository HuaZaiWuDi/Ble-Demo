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

package lab.wesmartclothing.wefit.flyso.view.line;

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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.IntDef;

/**
 * https://github.com/whataa
 */
public class SuitLines extends View {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineType.CURVE, LineType.SEGMENT})
    public @interface LineType {
        int CURVE = 0;//曲线
        int SEGMENT = 1;//折线
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineType.CURVE, LineType.SEGMENT})
    public @interface LineStyle {
        int SOLID = 0;//实线
        int DASHED = 1;//虚线
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineType.CURVE, LineType.SEGMENT})
    public @interface ChartType {
        int TYPE_LINE = 0;//线条
        int TYPE_BAR = 1;//柱状图
    }

    public static final String TAG = com.vondear.rxtools.view.chart.line.SuitLines.class.getSimpleName();

    public SuitLines(Context context) {
        this(context, null);
    }

    public SuitLines(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuitLines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        basePadding = RxUtils.dp2px(basePadding);

        scroller = new Scroller(context);


        //XY轴的画笔
        xyPaint.setTextSize(RxUtils.sp2px((int) defaultXySize));
        xyPaint.setColor(defaultXyColor);

        //线上的点的画笔
        hintPaint.setTextSize(RxUtils.sp2px(12));
        hintPaint.setColor(defaultXyColor);
        hintPaint.setStyle(Paint.Style.FILL);
        hintPaint.setStrokeWidth(2);
        hintPaint.setTextAlign(Paint.Align.CENTER);
        hintPaint.setAlpha((int) (255 * 0.6f));

        //点击的圆圈画笔
        selectPaint.setTextSize(RxUtils.sp2px(12));
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
        LimitLinePaint.setPathEffect(new DashPathEffect(new float[]{RxUtils.dp2px(3),
                RxUtils.dp2px(6)}, 0));
        LimitLinePaint.setStrokeWidth(2f);
        LimitLinePaint.setColor(defaultXyColor);
        LimitLinePaint.setAlpha((int) (255 * 0.6f));
    }


    protected int mHeight;
    protected float mWidth;

    // 创建自己的Handler，与ViewRootImpl的Handler隔离，方便detach时remove。
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


    /**
     * 点击的颜色
     */
    private int selectColor = defaultXyColor;

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
    private float maxValueY = 0;
    /**
     * y轴的最小刻度值，保留一位小数
     */
    private float minValueY = Integer.MAX_VALUE;

    /**
     * Y轴Top控件
     */
    private float yTopSpace = 1f;

    /**
     * Y轴bottom控件
     */
    private float yBottomSpace = 1f;

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

    /**
     * 更新数据之前的偏移
     */
    private float lastOffset;

    private VelocityTracker velocityTracker;
    private Scroller scroller;

    /**
     * 实际的点击位置，0为x索引，1为某条line
     */
    private int[] clickIndexs = new int[]{-1, -1};

    /**
     * 上次点击的点
     */
    private int lastIndex = -1;

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
     * 柱状图的尺量范围
     */
    private RectF rect = new RectF();

    /**
     * 画辅助线
     */
    private Map<Float, String> limits;

    //滑动的位置包含经过的地方
    public interface LineChartSelectItemListener {
        void selectItem(int valueX);
    }

    //滑动最好停留的地方
    public interface LineChartStopItemListener {
        void stopItem(int valueX);
    }

    //滑动到边缘的地方
    public interface LineChartScrollEdgeListener {
        void leftEdge();

        void rightEdge();
    }

    private LineChartSelectItemListener mLineChartSelectItemListener;
    private LineChartStopItemListener mLineChartStopItemListener;
    private LineChartScrollEdgeListener mLineChartScrollEdgeListener;

    public void setLineChartStopItemListener(LineChartStopItemListener lineChartStopItemListener) {
        mLineChartStopItemListener = lineChartStopItemListener;
    }

    public void setLineChartSelectItemListener(LineChartSelectItemListener lineChartSelectItemListener) {
        mLineChartSelectItemListener = lineChartSelectItemListener;
    }

    public void setLineChartScrollEdgeListener(LineChartScrollEdgeListener lineChartScrollEdgeListener) {
        mLineChartScrollEdgeListener = lineChartScrollEdgeListener;
    }


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
                //停止所有的动画
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
        } else if (offset >= 0) {
            offset = 0;
            mMove = 0;
        }

        int click = clickIndexs[0];

        clickIndexs[0] = (Math.round(Math.abs(offset) * 1.0f / realBetween));
//        RxLogUtils.e(TAG, "正在滑动changeMoveAndValue: " + clickIndexs[0]);
        if (click != clickIndexs[0]) {
            notifyValueChange();
        }
        postInvalidate();
    }

    private void countMoveEnd() {
        offset -= mMove;
        if (offset <= maxOffset) {
            offset = maxOffset;
            RxLogUtils.d("测试：", "滑动到右边");
            if (mLineChartScrollEdgeListener != null)
                mLineChartScrollEdgeListener.rightEdge();
        } else if (offset >= 0) {
            offset = 0;
            RxLogUtils.d("测试：", "滑动到左边");
            if (mLineChartScrollEdgeListener != null)
                mLineChartScrollEdgeListener.leftEdge();
        }
        mLastX = 0;
        mMove = 0;

        clickIndexs[0] = (Math.round(Math.abs(offset) * 1.0f / realBetween));
        offset = -clickIndexs[0] * realBetween; // 矫正位置,保证不会停留在两个相邻刻度之间
//        RxLogUtils.e(TAG, "滑动停止countMoveEnd: " + clickIndexs[0]);
        notifyValueChange();
        notifyValueStopChange();
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
//            RxLogUtils.d(TAG, "computeScroll--isFinished: " + scroller.isFinished());
//            RxLogUtils.d(TAG, "computeScroll--getCurrX: " + scroller.getCurrX());
//            RxLogUtils.d(TAG, "computeScroll--getFinalX: " + scroller.getFinalX());
//            RxLogUtils.d(TAG, "computeScroll--getStartX: " + scroller.getStartX());

            if (scroller.isFinished()) { // over
                countMoveEnd();
            } else {
                int xPosition = scroller.getCurrX();
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }

    private void notifyValueChange() {
        if (mLineChartSelectItemListener != null && clickIndexs[0] >= 0) {
            mLineChartSelectItemListener.selectItem(clickIndexs[0]);
        }
    }

    private void notifyValueStopChange() {
        if (mLineChartStopItemListener != null && clickIndexs[0] >= 0) {
            mLineChartStopItemListener.stopItem(clickIndexs[0]);
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

        //画柱状图
        drawBar(canvas);

        // X轴
        drawX(canvas);

        //画线上的点
        drawPoint(canvas);

        //画选中效果
        drawClickHint(canvas);

        //画图标顶上的文字
        drawUpText(canvas);


        forceToDraw = false;
        canvas.restore();

        // 不位移
        drawHintLine(canvas);


//
//        canvas.drawRect(linesArea, xyPaint);
//
//        canvas.drawRect(xArea, selectPaint);
    }

    private void drawUpText(Canvas canvas) {
        for (int i = 0; i < mLineBeans.size(); i++) {
            for (int j = 0; j < mLineBeans.get(i).getUnits().size(); j++) {
                Unit unit = mLineBeans.get(i).getUnits().get(j);
                //选中效果
                float value = unit.getValue();

                if (j == clickIndexs[0]) {
                    xyPaint.setTextSize(Util.size2sp(13, getContext()));
                    xyPaint.setAlpha(255);
                }
                //画每个点上的文字
                if (mLineBeans.get(i).isShowUpText())
                    canvas.drawText(String.format("%.1f", value), unit.getXY().x, unit.getXY().y - Util.dip2px(6), xyPaint);
                xyPaint.setTextSize(Util.size2sp(8, getContext()));
                xyPaint.setAlpha((int) (255 * 0.6f));
            }
        }
    }

    private void drawBar(Canvas canvas) {
        for (int i = 0; i < mLineBeans.size(); i++) {
            for (int j = 0; j < mLineBeans.get(i).getUnits().size(); j++) {
                Unit unit = mLineBeans.get(i).getUnits().get(j);
                if (mLineBeans.get(i).getChartType() == ChartType.TYPE_BAR) {
                    Paint paint = mLineBeans.get(i).getPaint();
                    paint.setColor(unit.getColor());
                    int barWidth = mLineBeans.get(i).getBarWidth();
                    rect.set(
                            unit.getXY().x - barWidth / 2,
                            unit.getXY().y,
                            unit.getXY().x + barWidth / 2,
                            linesArea.bottom
                    );
                    canvas.drawRoundRect(
                            rect
                            , barWidth / 2, barWidth / 2, mLineBeans.get(i).getPaint());
                }
            }
        }
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

                LimitLinePaint.setPathEffect(new DashPathEffect(new float[]{RxUtils.dp2px(3),
                        RxUtils.dp2px(6)}, 0));
                dashPath2.reset();
                dashPath2.moveTo(linesArea.left + xyPaint.measureText(text) + basePadding * 2, y);
                dashPath2.lineTo(linesArea.right, y);
                dashPath2.close();
                canvas.drawPath(dashPath2, LimitLinePaint);
            }
        }
    }

    //画线上的点
    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < mLineBeans.size(); i++) {
            LineBean lineBean = mLineBeans.get(i);
            for (int j = 0; j < lineBean.getUnits().size(); j++) {
                Unit unit = lineBean.getUnits().get(j);
                if (lineBean.getChartType() == ChartType.TYPE_LINE && lineBean.isShowPoint())
                    canvas.drawCircle(unit.getXY().x, unit.getXY().y, 10f, hintPaint);
            }
        }
    }


    /**
     * 开始连接每条线的各个点<br>
     * 最耗费性能的地方：canvas.drawPath
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        Path path = null;
        List<Path> paths;
        int lastIndex = 0;

        for (int i = 0; i < mLineBeans.size(); i++) {
            LineBean lineBean = mLineBeans.get(i);
            if (lineBean.getChartType() != ChartType.TYPE_LINE) continue;

            paths = lineBean.getPaths();
            paths.clear();
            int size = lineBean.getUnits().size();

            for (int j = 0; j < size; j++) {
                Unit current = lineBean.getUnits().get(j);
                float curY = current.getXY().y;
                //每30个点分一个path保存，防止出现一个Path保存的数据过大的问题
                if (path == null || (!lineBean.isFill() && j % 50 == 0)) {
                    path = new Path();
                    paths.add(path);
                    path.moveTo(current.getXY().x, curY);
                    continue;
                }

                if (lineBean.getLineType() == LineType.SEGMENT) {
                    path.lineTo(current.getXY().x, curY);
                } else if (lineBean.getLineType() == LineType.CURVE) {
                    // 到这里肯定不是起始点，所以可以减1
                    Unit previous = lineBean.getUnits().get(j - 1);
                    // 两个锚点的坐标x为中点的x，y分别是两个连接点的y
                    path.cubicTo((previous.getXY().x + current.getXY().x) / 2,
                            linesArea.bottom - (linesArea.bottom - previous.getXY().y) * previous.getPercent(),
                            (previous.getXY().x + current.getXY().x) / 2, curY,
                            current.getXY().x, curY);
                }

                //绘制阴影需要连接底部的点
                if (lineBean.isFill() && j == size - 1) {
                    path.lineTo(lineBean.getUnits().get(j).getXY().x, linesArea.bottom);
                    path.lineTo(lineBean.getUnits().get(0).getXY().x, linesArea.bottom);
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
        for (int i = 0; i < mLineBeans.size(); i++) {
            LineBean lineBean = mLineBeans.get(i);
            if (lineBean.getChartType() == ChartType.TYPE_LINE)
                for (Path path : mLineBeans.get(i).getPaths()) {
                    canvas.drawPath(path, mLineBeans.get(i).getPaint());
                }
        }
    }

    /**
     * 画选中的视图
     *
     * @param canvas
     */
    public void drawClickHint(Canvas canvas) {
        for (int i = 0; i < mLineBeans.size(); i++) {
            LineBean lineBean = mLineBeans.get(i);
            if (RxDataUtils.isEmpty(lineBean.getUnits())) continue;
            Unit unit = lineBean.getUnits().get(clickIndexs[0]);
            if (lineBean.getChartType() == ChartType.TYPE_LINE && lineBean.isShowPoint()) {
                canvas.drawCircle(unit.getXY().x, unit.getXY().y, 15f, selectPaint);
                lastIndex = clickIndexs[0];
            } else if (lineBean.getChartType() == ChartType.TYPE_BAR) {
//                //点击放大
//                int barWidth = lineBean.getBarWidth();
//                rect.set(
//                        unit.getXY().x - barWidth / 2,
//                        unit.getXY().y,
//                        unit.getXY().x + barWidth / 2,
//                        linesArea.bottom
//                );
//                canvas.drawRoundRect(
//                        rect
//                        , barWidth / 2, barWidth / 2, selectPaint);
            }
        }
    }

    /**
     * 画x轴及x轴文字（默认取第一条线的值）和垂直的网格线
     *
     * @param canvas
     */
    private void drawX(Canvas canvas) {
//        canvas.drawLine(datas.get(0).get(startIndex).getXY().x, xArea.top,
//                datas.get(0).get(endIndex).getXY().x, xArea.top, xyPaint);
        for (int i = 0; i <= mLineBeans.get(0).getUnits().size() - 1; i++) {
            LineBean lineBean = mLineBeans.get(0);

            Unit unit = lineBean.getUnits().get(i);

            String extX = unit.getExtX();
            if (TextUtils.isEmpty(extX)) {
                continue;
            }
            xyPaint.setTextAlign(Paint.Align.CENTER);
            xyPaint.setTextSize(Util.size2sp(8, getContext()));

            if (i == clickIndexs[0]) {
                xyPaint.setAlpha(255);
                pointLinePaint.setAlpha(255);
            } else {
                xyPaint.setAlpha((int) (255 * 0.6f));
                pointLinePaint.setAlpha((int) (255 * 0.6f));
            }

            canvas.drawText(extX, unit.getXY().x, Util.calcTextSuitBaseY(xArea, xyPaint) + basePadding, xyPaint);


            //垂直的网格线
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

        this.mLineBeans = mLineBeans;
        if (mLineBeans.isEmpty()) {
            RxLogUtils.d(TAG, "feedInternal: line is empty");
            return;
        } else if (RxDataUtils.isEmpty(mLineBeans.get(0).getUnits())) {
            RxLogUtils.d(TAG, "feedInternal: data is empty");
            return;
        }

        calcAreas();
        calcUnitXY();
        forceToDraw = true;

        clickIndexs = new int[]{mLineBeans.get(0).getUnits().size() - 1, 0};
        notifyValueChange();
        notifyValueStopChange();

        for (LineBean bean : mLineBeans) {
            Paint paint = bean.getPaint();
            if (bean.getChartType() == ChartType.TYPE_LINE) {
                paint.setStrokeWidth(bean.getLineWidth());
                paint.setStyle(bean.isFill() ? Paint.Style.FILL : Paint.Style.STROKE);
                paint.setPathEffect(bean.getLineStyle() == LineStyle.DASHED ?
                        new DashPathEffect(new float[]{20, 5}, 0) : null);

                if (bean.getColor().length == 1) {
                    paint.setColor(bean.getColor()[0]);
                } else {
                    paint.setShader(bean.isFill() ? buildPaintColor_V(bean.getColor()) : buildPaintColor_H(bean.getColor()));
                }
            } else {
                paint.setStyle(Paint.Style.FILL);
            }

            RxLogUtils.d("原始数据：" + bean.toString());
        }

        /**
         * 滑动的右边
         */
        post(() -> {
            offset = maxOffset;
            lastOffset = maxOffset;
            postInvalidate();
        });
    }


    /**
     * 更新图表数据
     */
    public void addDataChart(List<List<Unit>> linePoint) {
        if (mLineBeans == null || mLineBeans.isEmpty()) {
            RxLogUtils.d(TAG, "feedInternal: line is empty");
            return;
        }

        if (linePoint == null || linePoint.size() != mLineBeans.size()) {
            RxLogUtils.d(TAG, "feedInternal: update is error");
            return;
        }

        for (int i = 0; i < mLineBeans.size(); i++) {
            mLineBeans.get(i).setUnits(linePoint.get(i));
        }

        calcUnitXY();
        forceToDraw = true;

        post(() -> {
            offset = maxOffset - lastOffset;
            lastOffset = maxOffset;
            postInvalidate();
        });
    }


    /**
     * 重新计算三个区域的大小
     */
    private void calcAreas() {
        //整个View绘制区域
        RectF validArea = new RectF(getPaddingLeft() + basePadding, getPaddingTop() + basePadding,
                getMeasuredWidth() - getPaddingRight() - basePadding, getMeasuredHeight() - getPaddingBottom() - basePadding);
//        yArea = new RectF(validArea.left, validArea.top,
//                validArea.left,
//                validArea.bottom - Util.getTextHeight(xyPaint) - basePadding * 2);
        //X轴的区间范围
        xArea = new RectF(validArea.left, validArea.bottom - Util.getTextHeight(xyPaint) * 1.5f, validArea.right, validArea.bottom);

        //line图标绘制区域
        linesArea = new RectF(validArea.left, validArea.top, validArea.right, xArea.top);
        //每个点的间距
        realBetween = linesArea.width() / maxOfVisible;
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
                maxValueY = Math.max(value, maxValueY);
                minValueY = Math.min(value, minValueY);
            }
        }

        //统一的最大最小值
        if (isUnifiedInterval) {
            for (int i = 0; i < mLineBeans.size(); i++) {
                maxValueY = Math.max(mLineBeans.get(i).getMaxValue(), maxValueY);
                minValueY = Math.min(mLineBeans.get(i).getMinValue(), minValueY);
            }
        }

        maxValueY = yTopSpace * maxValueY;
        minValueY = yBottomSpace * minValueY;

        for (int i = 0; i < mLineBeans.get(0).getUnits().size(); i++) {
            for (int j = 0; j < mLineBeans.size(); j++) {
                Unit unit = mLineBeans.get(j).getUnits().get(i);

                if (isUnifiedInterval) {
                    float p = 1f - (unit.getValue() - minValueY) / (maxValueY - minValueY);
                    if (minValueY == maxValueY) {
                        if (maxValueY != 0)
                            p = 0.5f;
                        else
                            p = 1f;
                    }
                    unit.setXY(new PointF(linesArea.left + realBetween * i + linesArea.width() * 0.5f,
                            Math.min(linesArea.height(), Util.getTextHeight(xyPaint) * 2f + linesArea.height() * p)));
                } else {
//                    unit.setXY(new PointF(linesArea.left + realBetween * i + linesArea.width() * 0.5f,
//                            linesArea.top + linesArea.height() * (1 - (unit.getValue() - mLineBeans.get(j).getMinValue())
//                                    / (mLineBeans.get(j).getMaxValue() - mLineBeans.get(j).getMinValue()))));
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
        mLineBeans = null;

        maxValueY = 0;
        minValueY = Integer.MAX_VALUE;

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    ///APIs/////////////////////////////////////////////////////////////////////////////////////////

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


    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    /**
     * 设置Y轴空间
     */
    public void setYSpace(float yTopSpace, float yBottomSpace) {
        this.yTopSpace = yTopSpace;
        this.yBottomSpace = yBottomSpace;
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
            suitLines.post(() ->
                    suitLines.feedInternal(mLineBeans));
        }


    }

}