package lab.wesmartclothing.wefit.flyso.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName HeartLineChartUtils
 * @Date 2018/11/6 15:27
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class HeartLineChartUtils {

    private LineChart mLineChart;
    private int planLineColor = Color.parseColor("#3F3943");
    private int realTimeColor = Color.parseColor("#FFFFFF");


    public HeartLineChartUtils(LineChart lineChart) {
        mLineChart = lineChart;
        initLineChart();
    }

    public void setPlanLineColor(@ColorInt int planColor, @ColorInt int realTimeColor) {
        planLineColor = planColor;
        this.realTimeColor = realTimeColor;
    }

    private void initLineChart() {
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setTouchEnabled(false);//可以点击
        mLineChart.setDragEnabled(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setPinchZoom(false);//X，Y轴缩放
        mLineChart.setViewPortOffsets(0, 0, 0, 0);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setEnabled(false);

        YAxis leftAxis = mLineChart.getAxisLeft();
        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);

        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setAxisMaximum((Key.HRART_SECTION[6] & 0xFF));
        yAxis.setAxisMinimum((Key.HRART_SECTION[0] & 0xFF));

        mLineChart.setData(new LineData());
        createRealTimeSet(null);
        mLineChart.invalidate();
    }


    private void createDefaultSet(List<Entry> heartValues) {
        LineDataSet set = new LineDataSet(heartValues, "heartPlan");
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setColor(planLineColor);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setHighlightEnabled(false);
//        set.setCircleColors();
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setCubicIntensity(0.2f);

        LineData data = mLineChart.getData();
        data.addDataSet(set);
    }


    private void createRealTimeSet(List<Entry> heartValues) {
        LineDataSet set = new LineDataSet(heartValues, "RealTime");
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setColor(realTimeColor);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(1f);

        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);

        //渐变
        set.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(mLineChart.getContext(), R.drawable.fade_write);
        set.setFillDrawable(drawable);

        //高亮
        set.setHighlightEnabled(true);
        set.setHighLightColor(Color.parseColor("#D8D8D8"));
        set.setHighlightLineWidth(0.5f);
        set.setDrawVerticalHighlightIndicator(true);
        set.setDrawHorizontalHighlightIndicator(false);

        LineData data = mLineChart.getData();
        data.addDataSet(set);
    }


    private void createRealTimeSet2(List<Entry> heartValues) {
        LineDataSet set = (LineDataSet) mLineChart.getData().getDataSetByLabel("RealTime", true);
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setColor(realTimeColor);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(1f);

        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);

//        //渐变
//        set.setDrawFilled(true);
//        Drawable drawable = ContextCompat.getDrawable(mLineChart.getContext(), R.drawable.fade_write);
//        set.setFillDrawable(drawable);
//
//        //高亮
        set.setHighlightEnabled(false);
//        set.setHighLightColor(Color.parseColor("#D8D8D8"));
//        set.setHighlightLineWidth(0.5f);
//        set.setDrawVerticalHighlightIndicator(true);
//        set.setDrawHorizontalHighlightIndicator(false);

        set.setValues(heartValues);
    }


    public void setRealTimeData(float value) {
        LineDataSet realTimeSet = (LineDataSet) mLineChart.getData().getDataSetByLabel("RealTime", true);

        mLineChart.highlightValue(realTimeSet.getEntryCount(), mLineChart.getData().getIndexOfDataSet(realTimeSet));
        realTimeSet.addEntry(new Entry(realTimeSet.getEntryCount(), value));

        mLineChart.getData().notifyDataChanged();
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();

    }


    public void setRealTimeData(List<Integer> athlList) {
        if (athlList == null || athlList.isEmpty()) return;
        List<Entry> heartValues = new ArrayList<>();
        for (int i = 0; i < athlList.size(); i++) {
            int value = athlList.get(i);
            if (value > (Key.HRART_SECTION[6] & 0xFF)) {
                value = (Key.HRART_SECTION[6] & 0xFF);
            } else if (value < (Key.HRART_SECTION[0] & 0xFF)) {
                value = (Key.HRART_SECTION[0] & 0xFF);
            }
            heartValues.add(new Entry(i, value));
        }

        if (realTimeColor == Color.parseColor("#FFFFFF")) {
            LineDataSet realTimeSet = (LineDataSet) mLineChart.getData().getDataSetByLabel("RealTime", true);
            realTimeSet.setValues(heartValues);
        } else
            createRealTimeSet2(heartValues);

        mLineChart.getData().notifyDataChanged();
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }

//    //定制计划线条
//    public void setPlanLineData(List<AthlPlanListBean> planList) {
//        if (planList == null || planList.isEmpty()) return;
//        List<Entry> heartValues = new ArrayList<>();
//        int index = 0;
//        for (int i = 0; i < planList.get(planList.size() - 1).getTime() * 60 / 2; i++) {
//
//            if (planList.get(index).getTime() * 60 / 2 == heartValues.size()) {
//                index++;
//            }
//            AthlPlanListBean bean = planList.get(index);
//            //心率区间
//            int startYValue = bean.getRange();
//            int endYValue = index + 1 >= planList.size() ? planList.get(planList.size() - 1).getRange()
//                    : planList.get(index + 1).getRange();
//
//
//            int section = (Key.HRART_SECTION[6] & 0xFF) - (Key.HRART_SECTION[5] & 0xFF);
//
//            //画最后一个点，判断最后一个点是上升还是下降
//            if (index + 1 >= planList.size()) {
//                endYValue = endYValue + ((planList.get(index).getRange() - planList.get(index - 1).getRange() > 0) ? section : -section);
//            }
//
//            //区间前一个点，
//            int startXValue = (index - 1) < 0 ? 0 : planList.get(index - 1).getTime() * 60 / 2;
//            //区间后一个点
//            int endXValue = bean.getTime() * 60 / 2;
//
//            float value = 0;
//            //上升
//            if (endYValue - startYValue > 0) {
//                value = (heartValues.size() - startXValue) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + startYValue;
//            } else {
//                //下降，没有平行
//                value = (endXValue - heartValues.size()) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + endYValue;
//            }
//
//            heartValues.add(new Entry(i, value));
//        }
//
//        LineDataSet defaultSet = (LineDataSet) mLineChart.getData().getDataSetByLabel("heartPlan", true);
//        if (defaultSet == null) {
//            createDefaultSet(heartValues);
//        } else {
//            defaultSet.setValues(heartValues);
//        }
//        mLineChart.getData().notifyDataChanged();
//        mLineChart.notifyDataSetChanged();
//        mLineChart.invalidate();
//    }


    //定制计划线条
    public void setPlanLineData(List<AthlPlanListBean> planList) {
        List<Entry> heartValues = drawPlanLine(planList);
        if (heartValues == null || heartValues.isEmpty()) return;
        LineDataSet defaultSet = (LineDataSet) mLineChart.getData().getDataSetByLabel("heartPlan", true);
        if (defaultSet == null) {
            createDefaultSet(heartValues);
        } else {
            defaultSet.setValues(heartValues);
        }
        mLineChart.getData().notifyDataChanged();
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }


    private List<Entry> drawPlanLine(List<AthlPlanListBean> planList) {
        if (planList == null || planList.isEmpty()) return null;
        List<Entry> heartValues = new ArrayList<>();
        int index = 0, value = 0;
        int endYValue = 0, startYValue = 0, startXValue = 0, endXValue = 0;
        for (int i = 0; i < planList.get(planList.size() - 1).getTime() * 60 / 2; i++) {

            if (planList.get(index).getTime() * 60 / 2 == i) {
                index++;
//                startYValue = endYValue;
//                startXValue = endXValue;
            }

            //心率区间
            if (index == 0) {
                AthlPlanListBean first = planList.get(0);
                if (i <= 30) {
                    startYValue = Key.HRART_SECTION[0] & 0xff;
                    endYValue = first.getMidRange();
                    startXValue = 0;
                    endXValue = 30;
                    value = (heartValues.size() - startXValue) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + startYValue;
                } else if (i <= first.getTime() * 30) {
                    value = first.getMidRange();
                }
            } else {
                AthlPlanListBean firstBean = planList.get(index - 1);
                AthlPlanListBean nextBean = planList.get(index);

                startYValue = firstBean.getMidRange();
                startXValue = firstBean.getTime() * 30;

                if (i >= firstBean.getTime() * 30 && i <= firstBean.getTime() * 30 + 30) {
                    //区间后一个点
                    endXValue = firstBean.getTime() * 30 + 30;
                    //上升
                    if (firstBean.getRange() - nextBean.getRange() < 0) {
                        endYValue = nextBean.getMidRange();

                        value = (heartValues.size() - startXValue) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + startYValue;
                    } else if (firstBean.getRange() - nextBean.getRange() > 0) {
                        //下降
                        endYValue = nextBean.getMidRange();

                        value = (endXValue - heartValues.size()) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + endYValue;
                    }
                } else if (i <= nextBean.getTime() * 30) {
                    value = nextBean.getMidRange();
                }
            }
            heartValues.add(new Entry(i, value));
        }
        return heartValues;
    }


//    private List<Entry> drawPlanLine(List<AthlPlanListBean> planList) {
//        if (planList == null || planList.isEmpty()) return null;
//        List<Entry> heartValues = new ArrayList<>();
//        int index = 0, value = 0;
//        int endYValue = 0, startYValue = 0, startXValue = 0, endXValue = 0;
//        for (int i = 0; i < planList.get(planList.size() - 1).getTime() * 60 / 2; i++) {
//
//            if (planList.get(index).getTime() * 60 / 2 == i) {
//                index++;
//                startYValue = endYValue;
//                startXValue = endXValue;
//            }
//            //心率区间
//            if (index == 0) {
//                AthlPlanListBean first = planList.get(0);
//                startYValue = Key.HRART_SECTION[1] & 0xff;
//                endYValue = first.getRange2();
//                startXValue = 0;
//                //区间后一个点
//                endXValue = first.getTime() * 60 / 2;
//                value = (heartValues.size() - startXValue) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + startYValue;
//            } else {
//                AthlPlanListBean firstBean = planList.get(index - 1);
//                AthlPlanListBean nextBean = planList.get(index);
//                //区间后一个点
//                endXValue = nextBean.getTime() * 60 / 2;
//                //上升
//                if (firstBean.getRange3() - nextBean.getRange3() < 0) {
//                    endYValue = nextBean.getRange2();
//
//                    value = (heartValues.size() - startXValue) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + startYValue;
//                } else if (firstBean.getRange3() - nextBean.getRange3() > 0) {
//                    //下降
//                    endYValue = nextBean.getRange();
//
//                    value = (endXValue - heartValues.size()) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + endYValue;
//                }
//            }
//            heartValues.add(new Entry(i, value));
//        }
//        return heartValues;
//    }
}
