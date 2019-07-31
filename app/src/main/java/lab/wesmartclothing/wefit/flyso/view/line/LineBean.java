package lab.wesmartclothing.wefit.flyso.view.line;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.vondear.rxtools.utils.RxLogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Package tech.linjiang.suitlines
 * @FileName LineBean
 * @Date 2018/9/20 10:22
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit
 */
public class LineBean {


    /**
     * 是否显示点
     */
    private boolean showPoint = false;

    /**
     * 线条类型：曲线、线段
     */
    private int lineType = SuitLines.LineType.SEGMENT;


    /**
     * 线条主题:实线、虚线
     */
    private int lineStyle = SuitLines.LineStyle.SOLID;

    /**
     * 图标类型：线条、柱状图
     */

    private int chartType = SuitLines.ChartType.TYPE_LINE;

    /**
     * 是否填充阴影
     **/
    private boolean isFill = false;


    /**
     * 线段所有的点
     */
    private List<Unit> mUnits = new ArrayList<>();


    /**
     * 线段的最大最小值
     */
    private float maxValue = 100;
    private float minValue = 0;

    /**
     * 线段颜色，当数组长度大于1时颜色为渐变色
     */
    private int[] color = {Color.WHITE};

    /**
     * 画笔
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 路劲
     */
    private List<Path> mPaths = new ArrayList<>();


    /**
     * 线条宽度
     */
    private int lineWidth = 1;

    /**
     * 柱状图 的宽度
     */
    private int barWidth = 20;


    /**
     * 是否显示图表上的文字
     */
    private boolean showUpText = true;


    public boolean isShowUpText() {
        return showUpText;
    }

    public void setShowUpText(boolean showUpText) {
        this.showUpText = showUpText;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
    }

    public int getLineType() {
        return lineType;
    }

    public void setCurve(boolean isCurve) {
        this.lineType = isCurve ? SuitLines.LineType.CURVE : SuitLines.LineType.SEGMENT;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setDashed(boolean isDashed) {
        this.lineStyle = isDashed ? SuitLines.LineStyle.DASHED : SuitLines.LineStyle.SOLID;
    }

    public boolean isFill() {
        return isFill;
    }

    public void setFill(boolean fill) {
        isFill = fill;
    }

    public List<Unit> getUnits() {
        return mUnits;
    }

    public void setUnits(List<Unit> units) {
        if (mUnits != null) {
            mUnits.clear();
            mUnits = null;
        }
        mUnits = units;
        checkMaxAndMinValue(units);
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public int[] getColor() {
        return color;
    }

    public void setColor(int... color) {
        this.color = color;
    }

    public Paint getPaint() {
        return mPaint;
    }


    public List<Path> getPaths() {
        return mPaths;
    }


    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    @Override
    public String toString() {
        return "LineBean{" +
                "showPoint=" + showPoint +
                ", lineType=" + lineType +
                ", lineStyle=" + lineStyle +
                ", isFill=" + isFill +
                ", maxValue=" + maxValue +
                ", minValue=" + minValue +
                ", lineWidth=" + lineWidth +
                ", barWidth=" + barWidth +
                ", chartType=" + chartType +
                ", showUpText=" + showUpText +
                ", mUnits=" + mUnits +
                ", color=" + Arrays.toString(color) +
                '}';
    }

    /**
     * 得到最大值，最小值作为图表的上下间隙
     *
     * @param datas
     */
    private void checkMaxAndMinValue(List<Unit> datas) {
        if (datas == null || datas.isEmpty()) return;

        float max, min;

        max = Collections.max(datas, (o1, o2) ->
                (int) (o1.getValue() - o2.getValue())).getValue();
        min = Collections.min(datas, (o1, o2) ->
                (int) (o1.getValue() - o2.getValue())).getValue();

        RxLogUtils.d("max: " + max + "-----min：" + min);

        setMaxValue(max);
        setMinValue(min);

    }

}
