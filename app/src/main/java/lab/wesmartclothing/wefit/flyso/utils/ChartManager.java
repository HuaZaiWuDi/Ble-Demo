package lab.wesmartclothing.wefit.flyso.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.vondear.rxtools.utils.RxLogUtils;

import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created icon_hide_password jk on 2018/5/23.
 */
public class ChartManager {
    private XAxis x;
    private YAxis y;
    private Context mContext;
    private LineChart lineChartBase;


    //初始化图表
    public void initChart(LineChart lineChartBase) {
        this.lineChartBase = lineChartBase;
        lineChartBase.getLegend().setEnabled(false);
        lineChartBase.getDescription().setEnabled(false);
        lineChartBase.setTouchEnabled(false);//可以点击
        lineChartBase.setDragEnabled(false);
        lineChartBase.setScaleEnabled(false);
        lineChartBase.setPinchZoom(false);//X，Y轴缩放
//        lineChartBase.setViewPortOffsets(0, 0, 160, 0);


        XAxis xAxis = lineChartBase.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);


        YAxis leftAxis = lineChartBase.getAxisLeft();
        YAxis rightAxis = lineChartBase.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);


        lineChartBase.setData(new LineData());
        addLimitLine2Y(lineChartBase, new float[]{120f, 140f, 160f, 180f}, new String[]{"", "", "", ""});

        lineChartBase.invalidate();
    }




    public void createSet(@ColorInt int color, String label) {
        LineData data = lineChartBase.getData();
        // set.addEntry(...); // can be called as well

        LineDataSet set = new LineDataSet(null, label);
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setColor(color);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(3f);
        set.setHighlightEnabled(false);
//        set.setCircleColors();
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setCubicIntensity(0.2f);
        data.addDataSet(set);
        lineChartBase.invalidate();
    }

    //添加提示线
    public void addLimitLine2Y(BarLineChartBase lineChartBase, float[] value, String[] label) {
        YAxis y = lineChartBase.getAxisLeft();
        y.removeAllLimitLines();
        if (value == null) return;
        for (int i = 0; i < value.length; i++) {
            //提示线，
            LimitLine ll = new LimitLine(value[i], label[i]);//线条颜色宽度等
            ll.setLineColor(ContextCompat.getColor(mContext, R.color.GrayWrite));
            ll.setLineWidth(0.1f);
            //加入到 mXAxis 或 mYAxis
            y.addLimitLine(ll);
        }
    }


    public void addLegend() {
        Legend legend = lineChartBase.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setDrawInside(true);
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(12);
        legend.setEnabled(true);
        legend.setForm(Legend.LegendForm.LINE);
    }

    //添加提示线
    public void addLimitLine2Y(float value, String label) {
        //提示线，
        LimitLine ll = new LimitLine(value, label);//线条颜色宽度等
        ll.setLineColor(mContext.getResources().getColor(R.color.colorTheme));
        ll.setLineWidth(2f);
//        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);//文字颜色、大小
        ll.setTextColor(mContext.getResources().getColor(R.color.white));
        ll.setTextSize(12f);

        YAxis y = lineChartBase.getAxisLeft();
        y.removeAllLimitLines();
        //加入到 mXAxis 或 mYAxis
        y.addLimitLine(ll);
    }


    //添加Tip
    public void addMarker(MarkerView mv) {
        mv.setChartView(lineChartBase); // For bounds control
        lineChartBase.setMarker(mv); // Set the marker to the chart
        lineChartBase.invalidate();
    }

    //添加数据
    public void setData(ArrayList<Entry> yVals, List<String> xVals) {
        addXLabel(xVals);

        // create a dataset and give it a type
        LineDataSet set1 = null;
        set1 = addLine(yVals, mContext.getString(R.string.heat));

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setDrawValues(false);

        // set data
        lineChartBase.setData(data);

        data.notifyDataChanged();
        lineChartBase.notifyDataSetChanged();
        lineChartBase.invalidate();

//        mLineChart.animateX(500);

        lineChartBase.setVisibleXRangeMinimum(7);
        lineChartBase.moveViewToX(yVals.size() - 4);


    }

    //添加X轴标签
    private void addXLabel(List<String> days) {
        XAxis x = lineChartBase.getXAxis();
        x.setValueFormatter(new MyXFormatter(days));
        lineChartBase.invalidate();
    }


    //添加X轴标签
    private void addXLabel(BarLineChartBase lineChartBase, final String[] label) {
        if (lineChartBase instanceof LineChart) {
            RxLogUtils.e("LineChart");
        } else if (lineChartBase instanceof BarChart) {
            RxLogUtils.e("BarChart");
        }
        XAxis x = lineChartBase.getXAxis();
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                RxLogUtils.d("X轴：" + value);
                if (value > label.length) return "";
                return label[(int) value % label.length];
            }
        });
        lineChartBase.invalidate();
    }


    //添加实线
    private LineDataSet addLine(ArrayList<Entry> YAxis, String label) {
        //热量
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(YAxis, label);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setLineWidth(3f);
        set1.setDrawCircles(true);//是否显示节点圆心
        set1.setDrawCircleHole(true);
        set1.setCircleHoleRadius(2f);
        set1.setCircleRadius(5f);
        set1.setCircleColor(mContext.getResources().getColor(R.color.lineColor));    //可以设置Entry节点的颜色
        set1.setDrawCircleHole(true); //是否定制节点圆心的颜色，若为false，则节点为单一的同色点，若为true则可以设置节点圆心的颜色
        set1.setCircleColorHole(Color.WHITE);  //设置节点圆心的颜色

        set1.setColor(Color.WHITE);
        set1.setHighlightLineWidth(2f);
        set1.setHighLightColor(Color.WHITE);
        set1.enableDashedHighlightLine(10f, 10f, 0f);
//        set1.setDrawVerticalHighlightIndicator(false);
        set1.setDrawHorizontalHighlightIndicator(false);

        return set1;
    }


    //添加虚线
    private LineDataSet addDashedLine(ArrayList<Entry> YAxis, String label) {
        //心率
        LineDataSet set2 = new LineDataSet(YAxis, label);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.enableDashedLine(10f, 10f, 0f); //设置线条为虚线 1.线条宽度2.间隔宽度3.角度
        set2.setLineWidth(3f);
        set2.setColor(Color.WHITE);
        //设置节点圆角
        set2.setDrawCircles(true);//是否显示节点圆心
        set2.setDrawCircleHole(true);
        set2.setCircleHoleRadius(2f);
        set2.setCircleRadius(5f);
        set2.setCircleColor(mContext.getResources().getColor(R.color.lineColor));    //可以设置Entry节点的颜色
        set2.setDrawCircleHole(true); //是否定制节点圆心的颜色，若为false，则节点为单一的同色点，若为true则可以设置节点圆心的颜色
        set2.setCircleColorHole(Color.WHITE);  //设置节点圆心的颜色

        set2.setHighlightLineWidth(2f);
        set2.setHighLightColor(Color.WHITE);
        set2.enableDashedHighlightLine(10f, 10f, 0f);
//        set2.setDrawVerticalHighlightIndicator(false);
        set2.setDrawHorizontalHighlightIndicator(false);
        set2.setHighlightEnabled(true);

        return set2;
    }

    //添加两条线
    public void setData(ArrayList<Entry> line1, ArrayList<Entry> line2, List<String> days) {
        addXLabel(days);

        LineDataSet set1, set2;

        set1 = addLine(line1, mContext.getString(R.string.heat));

        set2 = addDashedLine(line2, mContext.getString(R.string.sportsTime));

        // create a data object with the datasets
        LineData data = new LineData(set1, set2);
        data.setDrawValues(false);

        // set data
        lineChartBase.setData(data);

        data.notifyDataChanged();
        lineChartBase.notifyDataSetChanged();
        lineChartBase.invalidate();

//        mLineChart.animateX(500);
        lineChartBase.setVisibleXRangeMaximum(7);
        lineChartBase.setVisibleXRangeMinimum(7);
        lineChartBase.moveViewToX(line1.size() - 4);


//        addLegend();
    }


}
