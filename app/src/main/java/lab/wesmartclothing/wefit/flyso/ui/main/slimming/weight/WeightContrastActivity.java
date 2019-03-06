package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HealthyInfoBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.picker.CustomDatePicker;

public class WeightContrastActivity extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_startDate)
    RxTextView mTvStartDate;
    @BindView(R.id.tv_to)
    TextView mTvTo;
    @BindView(R.id.tv_endDate)
    RxTextView mTvEndDate;
    @BindView(R.id.tv_diffWeight)
    TextView mTvDiffWeight;
    @BindView(R.id.tv_startWeight)
    TextView mTvStartWeight;
    @BindView(R.id.layout_startWeight)
    LinearLayout mLayoutStartWeight;
    @BindView(R.id.tv_endWeight)
    TextView mTvEndWeight;
    @BindView(R.id.layout_endWeight)
    LinearLayout mLayoutEndWeight;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_maxAndMin)
    TextView mTvMaxAndMin;
    @BindView(R.id.img_line)
    RxImageView mImgLine;
    @BindView(R.id.img_bar)
    RxImageView mImgBar;
    @BindView(R.id.chart_combined)
    CombinedChart mCombinedChart;
    @BindView(R.id.tv_unit)
    TextView mTvUnit;
    @BindView(R.id.tv_weightDiffDec)
    TextView mTvWeightDiffDec;


    private Long startDate, endDate;
    private boolean isLine = true;
    ArrayList<Entry> lineEntrys = new ArrayList<>();
    ArrayList<BarEntry> barEntrys = new ArrayList<>();
    private List<HealthyInfoBean> mInfoBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_weight_contrast;
    }

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.green_61D97F);
    }

    @Override
    protected void initViews() {
        super.initViews();

        Calendar instance = Calendar.getInstance();
        endDate = instance.getTimeInMillis();
        instance.set(Calendar.DAY_OF_MONTH, -30);
        startDate = instance.getTimeInMillis();

        mTvStartDate.setText(RxFormat.setFormatDate(startDate, RxFormat.Date));
        mTvEndDate.setText(RxFormat.setFormatDate(endDate, RxFormat.Date));

        initChart();
        initTitle();
        initTabLayout();
        initTextUtils(0, 0);

        mImgLine.setEnabled(false);
    }

    private void initChart() {
        //替换系统的渲染器，设置成圆角柱状图
//        mCombinedChart.setRenderer(new BarRoundChartRenderer(
//                mCombinedChart,
//                mCombinedChart.getAnimator(),
//                mCombinedChart.getViewPortHandler(),
//                true,
//                10f
//        ));
        mCombinedChart.setNoDataText("");
        mCombinedChart.getDescription().setEnabled(false);
        mCombinedChart.getLegend().setEnabled(false);
        mCombinedChart.setHighlightFullBarEnabled(false);
        mCombinedChart.setScaleEnabled(false);
        mCombinedChart.setPinchZoom(false);//X，Y同时轴缩放


        YAxis yLAxis = mCombinedChart.getAxisLeft();
        yLAxis.setDrawAxisLine(false);
        yLAxis.setGridColor(Color.parseColor("#FFF7F7F9"));
        yLAxis.setDrawTopYLabelEntry(true);

        YAxis yRAxis = mCombinedChart.getAxisRight();
        yRAxis.setEnabled(false);
        yRAxis.setMaxWidth(0);

        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(ContextCompat.getColor(mContext, R.color.GrayWrite));
        xAxis.setTextSize(10f);

        mCombinedChart.invalidate();

    }

    private LineData setLine(ArrayList<Entry> valueLine) {
        LineDataSet set = new LineDataSet(valueLine, "lineChart");
        set.setColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
        set.setLineWidth(3f);

        set.setHighlightEnabled(false);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
//            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return new LineData(set);
    }

    private BarData setData(ArrayList<BarEntry> valuesBar) {
        BarDataSet set = new BarDataSet(valuesBar, "barChart");
        set.setColor(ContextCompat.getColor(mContext, R.color.GrayWrite));
        set.setDrawValues(false);
        set.setHighlightEnabled(true);
        set.setHighLightColor(ContextCompat.getColor(mContext, R.color.orange_FF7200));
        set.setHighLightAlpha(255);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarData data = new BarData(set);

        data.setBarWidth(0.25f);
        return data;
    }


    private void initTitle() {
        mQMUIAppBarLayout.setTitle("对比");
        mQMUIAppBarLayout.addLeftBackImageButton()
                .setOnClickListener(view -> onBackPressed());
    }


    private void initTextUtils(double startWeight, double endWeight) {
        //增重
        if (startWeight < endWeight) {
            mTvWeightDiffDec.setText("周期内减重失败");
            mTvDiffWeight.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }
        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(Math.abs(startWeight - endWeight), 1))
                .append("\tkg")
                .setProportion(0.5f)
                .into(mTvDiffWeight);

        RxTextUtils.getBuilder("起始体重\n")
                .append(startWeight + "").setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setProportion(1.3f)
                .append("\tkg").setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setProportion(0.5f)
                .into(mTvStartWeight);
        RxTextUtils.getBuilder("最终体重\n")
                .append(endWeight + "").setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setProportion(1.3f)
                .append("\tkg").setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setProportion(0.5f)
                .into(mTvEndWeight);
    }


    private void initTabLayout() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (RxDataUtils.isEmpty(mInfoBeans))
                    getWeightContrast();
                else {
                    updateUI(mInfoBeans);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        String[] tabs = {"体脂率", "BMI", "内脏脂肪等级", "肌肉量", "基础代谢率",
                "水分", "骨量", "身体年龄", "去脂体重", "皮下脂肪率", "骨骼肌率", "蛋白质"};

        for (String s : tabs) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(s);
            mTabLayout.addTab(tab);
        }
    }

    @Override
    protected void initNetData() {
        super.initNetData();
    }


    private void showDateDialog(final boolean isStart) {
        Calendar nowCalendar = Calendar.getInstance();
        CustomDatePicker datePicker = new CustomDatePicker(mActivity);
        datePicker.setRangeStart(1940, 01, 01);
        datePicker.setRangeEnd(nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH) + 1, nowCalendar.get(Calendar.DAY_OF_MONTH));
        nowCalendar.setTimeInMillis(isStart ? startDate : endDate);
        datePicker.setTextColor(getResources().getColor(R.color.Gray));
        datePicker.setSelectedItem(nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH) + 1, nowCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> {
            RxLogUtils.d("年：" + year + "------月：" + month + "---------日：" + day);

            Date date = RxFormat.setParseDate(year + "-" + month + "-" + day, RxFormat.Date);
            if (isStart) {
                if (date.getTime() >= endDate) {
                    RxToast.warning("开始时间不能早于结束时间");
                    return;
                }
                mTvStartDate.setText(year + "-" + month + "-" + day);
                startDate = date.getTime();
            } else {
                if (date.getTime() >= startDate) {
                    RxToast.warning("结束时间不能晚于开始时间");
                    return;
                }
                mTvEndDate.setText(year + "-" + month + "-" + day);
                endDate = date.getTime();
            }
            getWeightContrast();

        });
        datePicker.show();
    }


    private void getWeightContrast() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("startTime", startDate);
            jsonObject.put("endTime", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //当日多次体重详情
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().weightCompare(
                        NetManager.fetchRequest(jsonObject.toString())))
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().transformObservable("weightCompare" + startDate + endDate, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        Type type = new TypeToken<List<HealthyInfoBean>>() {
                        }.getType();

                        mInfoBeans = JSON.parseObject(s, type);
                        updateUI(mInfoBeans);

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void updateUI(List<HealthyInfoBean> weightInfoLists) {
        lineEntrys.clear();
        barEntrys.clear();
        RxLogUtils.d("数据个数：" + weightInfoLists.size());
        if (RxDataUtils.isEmpty(weightInfoLists)) {
            initTextUtils(0, 0);
            return;
        } else {
            initTextUtils(weightInfoLists.get(0).getWeight(),
                    weightInfoLists.get(weightInfoLists.size() - 1).getWeight());
        }

        for (int i = 0; i < weightInfoLists.size(); i++) {
            HealthyInfoBean bean = weightInfoLists.get(i);
            lineEntrys.add(new Entry(i, (float) getHealthyValue(bean)));
            barEntrys.add(new BarEntry(i, (float) getHealthyValue(bean)));
        }

        //删除
        CombinedData data = mCombinedChart.getData();
        if (data == null) {
            data = new CombinedData();
        }

        IBarLineScatterCandleBubbleDataSet<? extends Entry> lineChart = data.getDataSetByLabel(!isLine ? "lineChart" : "barChart", true);
        if (lineChart != null) {
            data.removeDataSet(lineChart);
            data.notifyDataChanged();
            mCombinedChart.notifyDataSetChanged();
        }

        if (isLine) {
            data.setData(setLine(lineEntrys));
        } else
            data.setData(setData(barEntrys));

        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setValueFormatter((value, axis) ->
                RxFormat.setFormatDate(weightInfoLists.get(
                        (int) Math.max(0, Math.min(value, weightInfoLists.size() - 1))).getWeightDate(),
                        "MM/dd"));
        xAxis.setSpaceMax(0.5f);
        xAxis.setSpaceMin(0.2f);

        //添加
        mCombinedChart.setData(data);
        mCombinedChart.invalidate();

        //移动
        mCombinedChart.setVisibleXRangeMaximum(6);
        mCombinedChart.moveViewTo(mCombinedChart.getData().getEntryCount() - 6, 0, YAxis.AxisDependency.LEFT);
        mCombinedChart.invalidate();

        float yMax = mCombinedChart.getYMax();
        float yMin = mCombinedChart.getYMin();
        String unit = mTvUnit.getText().toString();

        mTvMaxAndMin.setText("最高 " + yMax + unit + "\n最低 " + yMin + unit);
    }


    private double getHealthyValue(HealthyInfoBean weightInfoBean) {
        double value = 0;
        mTvUnit.setText("%");
        switch (mTabLayout.getSelectedTabPosition()) {
            //体脂率
            case 0:
                value = weightInfoBean.getBodyFat();
                break;
            //BMI
            case 1:
                mTvUnit.setText("");
                value = weightInfoBean.getBmi();
                break;
            //内脏脂肪等级
            case 2:
                mTvUnit.setText("");
                value = weightInfoBean.getVisfat();
                break;
            //肌肉量
            case 3:
                mTvUnit.setText("kg");
                value = weightInfoBean.getSinew();
                break;
            //基础代谢率
            case 4:
                mTvUnit.setText("kcal");
                value = weightInfoBean.getBmr();
                break;
            //水分
            case 5:
                value = weightInfoBean.getWater();
                break;
            //骨量
            case 6:
                value = weightInfoBean.getBone();
                mTvUnit.setText("kg");
                break;
            //身体年龄
            case 7:
                mTvUnit.setText("岁");
                value = weightInfoBean.getBodyAge();
                break;
            //去脂体重
            case 8:
                mTvUnit.setText("kg");
                value = weightInfoBean.getBodyFfm();
                break;
            //皮下脂肪率
            case 9:
                value = weightInfoBean.getSubfat();
                break;
            //骨骼肌率
            case 10:
                value = weightInfoBean.getMuscle();
                break;
            //蛋白质
            case 11:
                value = weightInfoBean.getProtein();
                break;
        }
        return value;
    }

    @OnClick({R.id.tv_startDate, R.id.tv_endDate, R.id.img_line, R.id.img_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_startDate:
                showDateDialog(true);
                break;
            case R.id.tv_endDate:
                showDateDialog(false);
                break;
            case R.id.img_line:
                isLine = true;
                mImgLine.setEnabled(false);
                mImgBar.setEnabled(true);
                updateUI(mInfoBeans);
                break;
            case R.id.img_bar:
                isLine = false;
                mImgBar.setEnabled(false);
                mImgLine.setEnabled(true);
                updateUI(mInfoBeans);
                break;
        }
    }
}
