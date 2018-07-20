package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.graphics.Color;
import android.media.AsyncPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.view.SwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;

/**
 * Created by jk on 2018/7/19.
 */
public class SportingFragment extends BaseAcFragment {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.sportsStatus)
    TextView mSportsStatus;
    @BindView(R.id.tv_sportsStatus)
    TextView mTvSportsStatus;
    @BindView(R.id.sw_music)
    SwitchView mSwMusic;
    @BindView(R.id.tv_kcal)
    TextView mTvKcal;
    @BindView(R.id.tv_avHeartRate)
    TextView mTvAvHeartRate;
    @BindView(R.id.tv_maxHeartRate)
    TextView mTvMaxHeartRate;
    @BindView(R.id.tv_sportsTime)
    TextView mTvSportsTime;
    @BindView(R.id.layout_sports)
    LinearLayout mLayoutSports;
    @BindView(R.id.lineCHart_HeartRate)
    LineChart mMLineChart;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new SportingFragment();
    }

    private Button btn_Connect;
    private AsyncPlayer player;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_sportsing, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("运动进行中");
        btn_Connect = mQMUIAppBarLayout.addRightTextButton("未连接", R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);

        player = new AsyncPlayer("heartRate");
        mSwMusic.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.setOpened(true);
//                player.play(mContext, Uri.parse("android.resource://" + mContext.getPackageName()
//                        + "/" + R.raw.timetofit1), false, AudioAttributes.CONTENT_TYPE_MUSIC);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.setOpened(false);
//                player.play(mContext, Uri.parse("android.resource://" + mContext.getPackageName()
//                        + "/" + R.raw.timetofit2), false, AudioAttributes.CONTENT_TYPE_MUSIC);
            }
        });

        initChart(mMLineChart);
        initAsyncPlayer();
    }

    private void initAsyncPlayer() {

    }


    private void initChart(LineChart lineChartBase) {
//        lineChartBase.setEnabled(false);
        lineChartBase.setMaxVisibleValueCount(60);
        lineChartBase.getLegend().setEnabled(false);
        lineChartBase.getDescription().setEnabled(false);
        lineChartBase.setTouchEnabled(true);//可以点击
        lineChartBase.setDragEnabled(true);
        lineChartBase.setScaleEnabled(false);
        lineChartBase.setPinchZoom(false);//X，Y轴缩放

        lineChartBase.setViewPortOffsets(0, 0, 160, 0);
//        lineChartBase.setBackgroundColor(getResources().getColor(R.color.gray_FBFBFC));
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
        yAxis.setAxisMinimum(100);
        XAxis x = lineChartBase.getXAxis();
        x.setSpaceMin(14f);
        lineChartBase.setData(new LineData());
        addLimitLine2Y(lineChartBase, new float[]{120f, 140f, 160f, 180f}, new String[]{"", "", "", ""});
        lineChartBase.invalidate();
    }


    private void setHeartRateData(int heartRate) {
        mTvKcal.setText(heartRate + "");
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

        mMLineChart.moveViewTo(data.getEntryCount() - 15, 50f, YAxis.AxisDependency.LEFT);

        mMLineChart.setVisibleXRangeMaximum(15);
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

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "heartRate");
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setColor(getResources().getColor(R.color.red));
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setLineWidth(3f);
        set.setHighlightEnabled(false);
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set.setCubicIntensity(0.2f);
        return set;
    }

    MyTimer timer = new MyTimer(2000, new MyTimerListener() {
        @Override
        public void enterTimer() {
            setHeartRateData((int) (Math.random() * 100) + 100);
        }
    });


    @Override
    public void onDestroy() {
        timer.stopTimer();
        super.onDestroy();
    }

    @OnClick(R.id.tv_kcal)
    public void onViewClicked() {
        timer.startTimer();

    }
}
