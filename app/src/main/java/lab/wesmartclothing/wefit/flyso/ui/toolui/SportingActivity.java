package lab.wesmartclothing.wefit.flyso.ui.toolui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

public class SportingActivity extends AppCompatActivity {
    LineChart mMLineChart;
    TextView tv_HeartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sporting);

        tv_HeartRate = findViewById(R.id.tv_HeartRate);
        mMLineChart = findViewById(R.id.lineCHart_HeartRate);
        initChart(mMLineChart);

        Disposable register = RxBus.getInstance().register(SportsDataTab.class, new Consumer<SportsDataTab>() {
            @Override
            public void accept(SportsDataTab sportsDataTab) throws Exception {
                setHeartRateData(sportsDataTab.getCurHeart());
                tv_HeartRate.append("当前心率:" + sportsDataTab.getCurHeart() + "--真实心率：" + sportsDataTab.getRealHeart() + "--卡路里：" + sportsDataTab.getKcal() + "\n");
            }
        });
        RxBus.getInstance().addSubscription(this, register);

    }



    private void initChart(LineChart lineChartBase) {
        lineChartBase.setMaxVisibleValueCount(60);
        lineChartBase.getLegend().setEnabled(false);
        lineChartBase.getDescription().setEnabled(false);
        lineChartBase.setTouchEnabled(true);//可以点击
        lineChartBase.setDragEnabled(true);
        lineChartBase.setScaleEnabled(false);
        lineChartBase.setPinchZoom(false);//X，Y轴缩放

//        lineChartBase.setViewPortOffsets(0, 0, 160, 0);
        XAxis xAxis = lineChartBase.getXAxis();
        xAxis.setEnabled(false);

        YAxis leftAxis = lineChartBase.getAxisLeft();
        YAxis rightAxis = lineChartBase.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);

        YAxis yAxis = lineChartBase.getAxisLeft();
        yAxis.setAxisMaximum(200);
        yAxis.setAxisMinimum(50);
        XAxis x = lineChartBase.getXAxis();
        x.setSpaceMin(14f);
        lineChartBase.setData(new LineData());
        addLimitLine2Y(lineChartBase, new float[]{60f, 80f, 100f, 120f, 140f, 160f, 180f, 200f}, new String[]{"60", "80", "100", "120", "140", "160", "180", "200"});
        lineChartBase.invalidate();

    }

    private void setHeartRateData(int heartRate) {
        LineData data = mMLineChart.getData();
        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        data.addEntry(new Entry(data.getDataSetByIndex(0).getEntryCount(), heartRate), 0);
        data.notifyDataChanged();
        mMLineChart.notifyDataSetChanged();
        mMLineChart.setVisibleXRangeMaximum(15);
        mMLineChart.moveViewTo(data.getEntryCount() - 15, 50f, YAxis.AxisDependency.LEFT);
    }

    //添加提示线
    public void addLimitLine2Y(BarLineChartBase lineChartBase, float[] value, String[] label) {
        YAxis y = lineChartBase.getAxisLeft();
        y.removeAllLimitLines();
        if (value == null) return;
        for (int i = 0; i < value.length; i++) {
            //提示线，
            LimitLine ll = new LimitLine(value[i], label[i]);//线条颜色宽度等
            ll.setLineColor(Color.GRAY);
            ll.setLineWidth(0.1f);
            //加入到 mXAxis 或 mYAxis
            y.addLimitLine(ll);
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "heartRate");
        set.setDrawIcons(false);
        set.setDrawValues(true);
        set.setColor(getResources().getColor(R.color.red));
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(3f);
        set.setHighlightEnabled(false);
//        set.setCircleColors();
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setCubicIntensity(0.2f);
        return set;
    }
}
