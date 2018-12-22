package com.vondear.rxtools.view.chart.line;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

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
    private int lineType = SuitLines.SEGMENT;


    /**
     * 线条主题:实线、虚线
     */
    private int lineStyle = SuitLines.SOLID;


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
        this.lineType = isCurve ? SuitLines.CURVE : SuitLines.SEGMENT;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setDashed(boolean isDashed) {
        this.lineStyle = isDashed ? SuitLines.DASHED : SuitLines.SOLID;
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

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public List<Path> getPaths() {
        return mPaths;
    }

    public void setPaths(List<Path> paths) {
        mPaths = paths;
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

    @Override
    public String toString() {
        return "LineBean{" +
                "showPoint=" + showPoint +
                ", lineType=" + lineType +
                ", lineStyle=" + lineStyle +
                ", isFill=" + isFill +
                ", mUnits=" + mUnits +
                ", maxValue=" + maxValue +
                ", minValue=" + minValue +
                ", color=" + Arrays.toString(color) +
                ", mPaint=" + mPaint +
                ", mPaths=" + mPaths +
                ", lineWidth=" + lineWidth +
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

        // 再拷贝，防止引用问题
        List<Unit> bakUnits_1 = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            bakUnits_1.add(datas.get(i).clone());
        }

        // 最后排序，得到最大值
        Collections.sort(bakUnits_1);

        max = bakUnits_1.get(bakUnits_1.size() - 1).getValue();
        min = bakUnits_1.get(0).getValue();


        Log.d("max", "checkMaxAndMinValue: " + max);

        setMaxValue(max);
        setMinValue(min);

    }

}
