package lab.wesmartclothing.wefit.flyso.ui.toolui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.vondear.rxtools.model.state.PageLayout;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRandom;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.chart.bar.BarGroupChart;
import com.vondear.rxtools.view.layout.RxLinearLayout;
import com.vondear.rxtools.view.roundprogressbar.RxIconRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxTextRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.common.RxBaseRoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;

public class TestBleScanActivity extends AppCompatActivity {

    @BindView(R.id.parent)
    RelativeLayout mParent;

    private RxTextRoundProgressBar mTextProgress;

    TextView tv_score;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ble_scan);
        ButterKnife.bind(this);
        RxLogUtils.i("启动时长：无网络请求的界面");

        final RxLinearLayout tv_test = findViewById(R.id.tv_test);

        tv_score = findViewById(R.id.tv_score);

        BarGroupChart mVerticalProgress = findViewById(R.id.mVerticalProgress);
        mVerticalProgress.setProgress(80);
        mVerticalProgress.setTopValue(1000);
//
//        mParent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RxAnimationUtils.doCircle(mParent, 0, 0);
//            }
//        });


//        mEmptyView = new QMUIEmptyView(this);
//
//
//        mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//        mParent.addView(mEmptyView);


//        initLine();

        final RxRoundProgressBar progressBar = findViewById(R.id.mProgress);
        progressBar.setProgress(50);

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(RxRandom.getRandom(100));
                tv_test.setElevation(RxUtils.dp2px(progressBar.getProgress()));
            }
        });
        progressBar.setOnProgressChangedListener(new RxBaseRoundProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress) {
                RxLogUtils.e("进度：" + progress);
            }
        });

        mTextProgress = findViewById(R.id.mTextProgress);
        mTextProgress.setProgress(50);


        mTextProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mTextProgress.setProgress(RxRandom.getRandom(100));
//                tv_test.setTranslationZ(RxUtils.dp2px(mTextProgress.getProgress()));
                mTimer.startTimer();
            }
        });
        final RxIconRoundProgressBar mIconProgress = findViewById(R.id.mIconProgress);
        mIconProgress.setProgress(50);

        mIconProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIconProgress.setProgress(RxRandom.getRandom(100));
            }
        });

        TextView emptyView = new TextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        emptyView.setLayoutParams(params);
        emptyView.setGravity(Gravity.CENTER);

        emptyView.setText("我是空数据");

        TextView errorView = new TextView(this);
        errorView.setLayoutParams(params);
        errorView.setGravity(Gravity.CENTER);
        errorView.setText("我是异常数据");

        TextView LoadingView = new TextView(this);
        LoadingView.setLayoutParams(params);
        LoadingView.setGravity(Gravity.CENTER);
        LoadingView.setText("我是加载数据");


        pageLayout = new PageLayout.Builder(this)
                .initPage(findViewById(R.id.layout_test))
                .setEmpty(emptyView)
                .setError(errorView)
                .setLoading(LoadingView)
                .setOnRetryListener(new PageLayout.OnRetryClickListener() {
                    @Override
                    public void onRetry() {
                        Toast.makeText(TestBleScanActivity.this, "重试", Toast.LENGTH_SHORT).show();
                    }
                }).create();


        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if (index % 4 == 0) {
                    pageLayout.showLoading();
                } else if (index % 4 == 1) {
                    pageLayout.showEmpty();
                } else if (index % 4 == 2) {
                    pageLayout.showError();
                } else if (index % 4 == 3) {
                    pageLayout.hide();
                }
            }
        });

        initLineChart();
        initLineChartData();
    }

    class DefaultSportingTime {
        public int totalTime;
        public int typeHeartRate;

    }


    private void initLineChartData() {
        List<Entry> heartValues = new ArrayList<>();
        List<Entry> defaultValues = new ArrayList<>();

        int[] ints = {5, 15, 20, 35, 40, 50, 55, 70};
        int[] yValues = {100, 120, 140, 160, 180, 160, 140, 180, 200};

        //30分钟
        int min = 0;
        for (int i = 0; i < 70 * 60 / 2; i++) {
            if (ints[min] * 60 / 2 == heartValues.size()) {
                min++;
            }
            //心率区间
            int startYValue = yValues[min];
            int endYValue = min + 1 >= yValues.length ? yValues[yValues.length - 1] : yValues[min + 1];

            //区间前一个点，
            int startXValue = (min - 1) < 0 ? 0 : ints[min - 1] * 30;
            //区间后一个点
            int endXValue = ints[min] * 30;

            float value = 0;
            //上升
            if (endYValue - startYValue > 0) {
                value = (heartValues.size() - startXValue) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + startYValue;
            } else {
                //下降，没有平行
                value = (endXValue - heartValues.size()) * Math.abs(startYValue - endYValue) / (endXValue - startXValue) + endYValue;
            }

            heartValues.add(new Entry(i, value));

            if (lastValue == 0) {
                lastValue = value;
            }

            float value2 =
                    i % 2 == 0 ? lastValue - (float) (Math.random() * 5) : lastValue + (float) (Math.random() * 5);
            lastValue = value2;
            defaultValues.add(new Entry(i, value2));
        }
        defaultSet.setValues(heartValues);
        set.setValues(defaultValues);
        lineChartBase.getData().notifyDataChanged();
        lineChartBase.notifyDataSetChanged();

//        mMyTimer.startTimer();

        RxLogUtils.e("折现图:" + set.getEntryCount());
    }


    int scoreSum = 0;

    float lastValue = 0;
    MyTimer mMyTimer = new MyTimer(500, 500, new MyTimerListener() {
        @Override
        public void enterTimer() {
            int count = set.getEntryCount();
            if (count == 1000) {
                mMyTimer.stopTimer();
                return;
            }
            Entry setEntryForIndex = defaultSet.getEntryForIndex(set.getEntryCount());

            if (lastValue == 0) {
                lastValue = setEntryForIndex.getY();
            }

            float value =
                    count % 2 == 0 ? lastValue - (float) (Math.random() * 5) : lastValue + (float) (Math.random() * 5);
            lastValue = value;

            set.addEntryOrdered(new Entry(count, value));
            lineChartBase.getData().notifyDataChanged();
            lineChartBase.notifyDataSetChanged();
            lineChartBase.setVisibleXRangeMaximum(30);
            lineChartBase.moveViewTo(count - 15, 50f, YAxis.AxisDependency.LEFT);


//            RxLogUtils.e("折现图: 当前值" + value);
//            RxLogUtils.e("折现图: 默认值" + setEntryForIndex.getY());
            int score = 0;
            float abs = Math.abs(value - setEntryForIndex.getY());
            if (abs < 5) {
                score = 100;
            } else if (abs >= 5 && abs < 10) {
                score = 80;
            } else if (abs >= 10 && abs < 15) {
                score = 60;
            } else if (abs >= 15 && abs < 20) {
                score = 40;
            } else if (abs >= 20 && abs < 25) {
                score = 20;
            } else {
                score = 0;
            }

            scoreSum += score;


            int Score = count > 0 ? scoreSum / count : scoreSum;

            tv_score.setText("当前分数：" + Score);

//            RxLogUtils.e("折现图: 分数" + Score);
        }
    });


    LineDataSet set, defaultSet;
    LineChart lineChartBase;

    private void initLineChart() {
        lineChartBase = findViewById(R.id.lineChart);
        lineChartBase.setMaxVisibleValueCount(60);
        lineChartBase.getLegend().setEnabled(false);
        lineChartBase.getDescription().setEnabled(false);
        lineChartBase.setTouchEnabled(false);//可以点击
        lineChartBase.setDragEnabled(true);
        lineChartBase.setScaleEnabled(false);
        lineChartBase.setPinchZoom(false);//X，Y轴缩放

        XAxis xAxis = lineChartBase.getXAxis();
        xAxis.setEnabled(false);

        YAxis leftAxis = lineChartBase.getAxisLeft();
        YAxis rightAxis = lineChartBase.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);

        YAxis yAxis = lineChartBase.getAxisLeft();
        yAxis.setAxisMaximum(210);
        yAxis.setAxisMinimum(60);
        XAxis x = lineChartBase.getXAxis();
        x.setSpaceMin(14f);
        lineChartBase.setData(new LineData());
        addLimitLine2Y(lineChartBase, new float[]{100f, 120f, 140f, 160f, 180f}, new String[]{"", "", "", "", ""});

        LineData data = lineChartBase.getData();

        if (defaultSet == null) {
            defaultSet = defaultSet();
            data.addDataSet(defaultSet);
        }

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }


        lineChartBase.invalidate();
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "heartRate");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setColor(getResources().getColor(R.color.green_61D97F));
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setHighlightEnabled(false);
//        set.setCircleColors();
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setCubicIntensity(0.2f);
        return set;
    }

    private LineDataSet defaultSet() {
        LineDataSet set = new LineDataSet(null, "default");
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setColor(getResources().getColor(R.color.yellow_FFBC00));
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setHighlightEnabled(false);
//        set.setCircleColors();
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setCubicIntensity(0.2f);
        return set;
    }


    //添加提示线
    public void addLimitLine2Y(BarLineChartBase lineChartBase, float[] value, String[] label) {
        YAxis y = lineChartBase.getAxisLeft();
        y.removeAllLimitLines();
        if (value == null) return;
        for (int i = 0; i < value.length; i++) {
            //提示线，
            LimitLine ll = new LimitLine(value[i], label[i]);//线条颜色宽度等
            ll.setLineColor(getResources().getColor(R.color.GrayWrite));
            ll.setLineWidth(0.1f);
            //加入到 mXAxis 或 mYAxis
            y.addLimitLine(ll);
        }
    }


    int index = 0;
    PageLayout pageLayout;

    int progress = 0;
    MyTimer mTimer = new MyTimer(1000, 100, new MyTimerListener() {
        @Override
        public void enterTimer() {
            progress++;
            if (progress == 100) progress = 0;
            mTextProgress.setProgress(progress, false);
        }
    });


    public void onClick(View view) {

    }
}
