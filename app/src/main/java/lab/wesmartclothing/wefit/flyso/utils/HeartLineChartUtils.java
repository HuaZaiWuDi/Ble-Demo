package lab.wesmartclothing.wefit.flyso.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

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


    public HeartLineChartUtils(LineChart lineChart) {
        mLineChart = lineChart;
        initLineChart();
    }

    private void initLineChart() {
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setTouchEnabled(true);//可以点击
        mLineChart.setDragEnabled(true);
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
        mLineChart.invalidate();
    }


    private void createDefaultSet(List<Entry> heartValues) {
        LineDataSet set = new LineDataSet(heartValues, "heartPlan");
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setColor(Color.parseColor("#E4CA9F"));
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(1.5f);
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
        set.setColor(Color.parseColor("#312C35"));
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(1f);
        set.setHighlightEnabled(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        LineData data = mLineChart.getData();
        data.addDataSet(set);
    }


    public void setRealTimeData(float value) {
        if (value > (Key.HRART_SECTION[6] & 0xFF)) {
            value = (Key.HRART_SECTION[6] & 0xFF);
        } else if (value < (Key.HRART_SECTION[0] & 0xFF)) {
            value = (Key.HRART_SECTION[0] & 0xFF);
        }
        LineDataSet realTimeSet = (LineDataSet) mLineChart.getData().getDataSetByLabel("RealTime", true);
        if (realTimeSet == null) {
            createRealTimeSet(null);
        } else {
            realTimeSet.addEntry(new Entry(realTimeSet.getEntryCount(), value));
        }
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

        LineDataSet defaultSet = (LineDataSet) mLineChart.getData().getDataSetByLabel("RealTime", true);
        if (defaultSet == null) {
            createRealTimeSet(heartValues);
        } else {
            defaultSet.setValues(heartValues);
        }
        mLineChart.getData().notifyDataChanged();
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }


    //定制计划线条
    public void setPlanLineData(List<AthlPlanListBean> planList) {
        if (planList == null || planList.isEmpty()) return;
        List<Entry> heartValues = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < planList.get(planList.size() - 1).getTime() * 60 / 2; i++) {

            if (planList.get(index).getTime() * 60 / 2 == heartValues.size()) {
                index++;
            }
            AthlPlanListBean bean = planList.get(index);
            //心率区间
            int startYValue = bean.getRange();
            int endYValue = index + 1 >= planList.size() ? planList.get(planList.size() - 1).getRange()
                    : planList.get(index + 1).getRange();


            int section = (Key.HRART_SECTION[6] & 0xFF) - (Key.HRART_SECTION[5] & 0xFF);

            //画最后一个点，判断最后一个点是上升还是下降
            if (index + 1 >= planList.size()) {
                endYValue = endYValue + ((planList.get(index).getRange() - planList.get(index - 1).getRange() > 0) ? section : -section);
            }

            //区间前一个点，
            int startXValue = (index - 1) < 0 ? 0 : planList.get(index - 1).getTime() * 60 / 2;
            //区间后一个点
            int endXValue = bean.getTime() * 60 / 2;

            float value = 0;
            //上升
            if (endYValue - startYValue > 0) {
                value = (heartValues.size() - startXValue) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + startYValue;
            } else {
                //下降，没有平行
                value = (endXValue - heartValues.size()) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + endYValue;
            }

            heartValues.add(new Entry(i, value));
        }

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

}
