package lab.wesmartclothing.wefit.flyso.ui.main.record;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.chart.bar.BarVerticalChart;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.waveview.RxWaveHelper;
import com.vondear.rxtools.view.waveview.RxWaveView;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.SlimmingRecordBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.HealthLevelView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.main.record
 * @FileName SlimmingRecordFragment
 * @Date 2018/10/31 15:50
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class SlimmingRecordFragment extends BaseAcFragment {


    @BindView(R.id.tv_continuousRecord)
    TextView mTvContinuousRecord;
    @BindView(R.id.tv_progress)
    TextView mTvProgress;
    @BindView(R.id.img_head)
    ImageView mImgHead;
    @BindView(R.id.img_email)
    ImageView mImgEmail;
    @BindView(R.id.pro_complete)
    RxWaveView mProComplete;
    @BindView(R.id.img_breakfast)
    RxImageView mImgBreakfast;
    @BindView(R.id.img_lunch)
    RxImageView mImgLunch;
    @BindView(R.id.img_dinner)
    RxImageView mImgDinner;
    @BindView(R.id.img_sporting)
    RxImageView mImgSporting;
    @BindView(R.id.img_weigh)
    RxImageView mImgWeigh;
    @BindView(R.id.img_userImg)
    RxImageView mImgUserImg;
    @BindView(R.id.iv_Healthy)
    ImageView mIvHealthy;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.layout_Health_title)
    RelativeLayout mLayoutHealthTitle;
    @BindView(R.id.tv_weightInfo)
    TextView mTvWeightInfo;
    @BindView(R.id.tv_healthScore)
    TextView mTvHealthScore;
    @BindView(R.id.tv_healthDate)
    TextView mTvHealthDate;
    @BindView(R.id.tv_healthInfo)
    TextView mTvHealthInfo;
    @BindView(R.id.Healthy_level)
    HealthLevelView mHealthyLevel;
    @BindView(R.id.layout_title)
    RelativeLayout mLayoutTitle;
    @BindView(R.id.iv_weight)
    ImageView mIvWeight;
    @BindView(R.id.layout_weight_title)
    RelativeLayout mLayoutWeightTitle;
    @BindView(R.id.mLineChart)
    LineChart mMLineChart;
    @BindView(R.id.tv_currentWeight)
    TextView mTvCurrentWeight;
    @BindView(R.id.tv_weight_chart_1)
    TextView mTvWeightChart1;
    @BindView(R.id.tv_weight_chart_2)
    TextView mTvWeightChart2;
    @BindView(R.id.tv_weight_chart_3)
    TextView mTvWeightChart3;
    @BindView(R.id.tv_weight_chart_4)
    TextView mTvWeightChart4;
    @BindView(R.id.tv_weight_chart_5)
    TextView mTvWeightChart5;
    @BindView(R.id.tv_weight_chart_6)
    TextView mTvWeightChart6;
    @BindView(R.id.tv_weight_chart_7)
    RxTextView mTvWeightChart7;
    @BindView(R.id.iv_diet)
    ImageView mIvDiet;
    @BindView(R.id.layout_diet_title)
    RelativeLayout mLayoutDietTitle;
    @BindView(R.id.tv_currentDiet)
    TextView mTvCurrentDiet;
    @BindView(R.id.dietProgress_1)
    BarVerticalChart mDietProgress1;
    @BindView(R.id.dietProgress_2)
    BarVerticalChart mDietProgress2;
    @BindView(R.id.dietProgress_3)
    BarVerticalChart mDietProgress3;
    @BindView(R.id.dietProgress_4)
    BarVerticalChart mDietProgress4;
    @BindView(R.id.dietProgress_5)
    BarVerticalChart mDietProgress5;
    @BindView(R.id.dietProgress_6)
    BarVerticalChart mDietProgress6;
    @BindView(R.id.dietProgress_7)
    BarVerticalChart mDietProgress7;
    @BindView(R.id.layout_dietChart)
    LinearLayout mLayoutDietChart;
    @BindView(R.id.line_chart_diet)
    View mLineChartDiet;
    @BindView(R.id.tv_diet_chart_1)
    TextView mTvDietChart1;
    @BindView(R.id.tv_diet_chart_2)
    TextView mTvDietChart2;
    @BindView(R.id.tv_diet_chart_3)
    TextView mTvDietChart3;
    @BindView(R.id.tv_diet_chart_4)
    TextView mTvDietChart4;
    @BindView(R.id.tv_diet_chart_5)
    TextView mTvDietChart5;
    @BindView(R.id.tv_diet_chart_6)
    TextView mTvDietChart6;
    @BindView(R.id.tv_diet_chart_7)
    RxTextView mTvDietChart7;
    @BindView(R.id.iv_sports)
    ImageView mIvSports;
    @BindView(R.id.layout_sports_title)
    RelativeLayout mLayoutSportsTitle;
    @BindView(R.id.tv_currentKcal)
    TextView mTvCurrentKcal;
    @BindView(R.id.sportingProgress_1)
    BarVerticalChart mSportingProgress1;
    @BindView(R.id.sportingProgress_2)
    BarVerticalChart mSportingProgress2;
    @BindView(R.id.sportingProgress_3)
    BarVerticalChart mSportingProgress3;
    @BindView(R.id.sportingProgress_4)
    BarVerticalChart mSportingProgress4;
    @BindView(R.id.sportingProgress_5)
    BarVerticalChart mSportingProgress5;
    @BindView(R.id.sportingProgress_6)
    BarVerticalChart mSportingProgress6;
    @BindView(R.id.sportingProgress_7)
    BarVerticalChart mSportingProgress7;
    @BindView(R.id.layout_sportingChart)
    LinearLayout mLayoutSportingChart;
    @BindView(R.id.line_chart)
    View mLineChart;
    @BindView(R.id.tv_sporting_chart_1)
    TextView mTvSportingChart1;
    @BindView(R.id.tv_sporting_chart_2)
    TextView mTvSportingChart2;
    @BindView(R.id.tv_sporting_chart_3)
    TextView mTvSportingChart3;
    @BindView(R.id.tv_sporting_chart_4)
    TextView mTvSportingChart4;
    @BindView(R.id.tv_sporting_chart_5)
    TextView mTvSportingChart5;
    @BindView(R.id.tv_sporting_chart_6)
    TextView mTvSportingChart6;
    @BindView(R.id.tv_sporting_chart_7)
    RxTextView mTvSportingChart7;
    @BindView(R.id.iv_energy)
    ImageView mIvEnergy;
    @BindView(R.id.layout_energy_title)
    RelativeLayout mLayoutEnergyTitle;
    @BindView(R.id.tv_currentEnergy)
    TextView mTvCurrentEnergy;
    @BindView(R.id.energyProgress_1)
    BarVerticalChart mEnergyProgress1;
    @BindView(R.id.energyProgress_2)
    BarVerticalChart mEnergyProgress2;
    @BindView(R.id.energyProgress_3)
    BarVerticalChart mEnergyProgress3;
    @BindView(R.id.energyProgress_4)
    BarVerticalChart mEnergyProgress4;
    @BindView(R.id.energyProgress_5)
    BarVerticalChart mEnergyProgress5;
    @BindView(R.id.energyProgress_6)
    BarVerticalChart mEnergyProgress6;
    @BindView(R.id.energyProgress_7)
    BarVerticalChart mEnergyProgress7;
    @BindView(R.id.layout_energyChart)
    LinearLayout mLayoutEnergyChart;
    @BindView(R.id.line_chart_energy)
    View mLineChartEnergy;
    @BindView(R.id.tv_energy_chart_1)
    TextView mTvEnergyChart1;
    @BindView(R.id.tv_energy_chart_2)
    TextView mTvEnergyChart2;
    @BindView(R.id.tv_energy_chart_3)
    TextView mTvEnergyChart3;
    @BindView(R.id.tv_energy_chart_4)
    TextView mTvEnergyChart4;
    @BindView(R.id.tv_energy_chart_5)
    TextView mTvEnergyChart5;
    @BindView(R.id.tv_energy_chart_6)
    TextView mTvEnergyChart6;
    @BindView(R.id.tv_energy_chart_7)
    RxTextView mTvEnergyChart7;


    private RxWaveHelper mWaveHelper;
    private int[] colors = {R.color.GrayWrite, R.color.GrayWrite, R.color.GrayWrite,
            R.color.GrayWrite, R.color.GrayWrite, R.color.GrayWrite, R.color.green_61D97F};
    private List<String> weightDates = new ArrayList<>();
    private List<String> dietDates = new ArrayList<>();

    private List<String> eneryDates = new ArrayList<>();


    public static SlimmingRecordFragment newInstance() {
        Bundle args = new Bundle();
        SlimmingRecordFragment fragment = new SlimmingRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_slimming_record;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initWareView();
        initTextTypeface();
        initChart(mMLineChart);
    }

    private void initTextTypeface() {
        mTvContinuousRecord.setTypeface(MyAPP.typeface);
        mTvProgress.setTypeface(MyAPP.typeface);
        mTvWeightInfo.setTypeface(MyAPP.typeface);
        mTvHealthScore.setTypeface(MyAPP.typeface);
        mTvCurrentWeight.setTypeface(MyAPP.typeface);
        mTvCurrentDiet.setTypeface(MyAPP.typeface);
        mTvCurrentKcal.setTypeface(MyAPP.typeface);
        mTvCurrentEnergy.setTypeface(MyAPP.typeface);


        RxTextUtils.getBuilder("0")
                .append("\tkcal").setProportion(0.5f)
                .into(mTvCurrentDiet);

        RxTextUtils.getBuilder("0")
                .append("\tkcal").setProportion(0.5f)
                .into(mTvCurrentDiet);

        RxTextUtils.getBuilder("0")
                .append("\tkcal").setProportion(0.5f)
                .into(mTvCurrentKcal);


        RxTextUtils.getBuilder("0")
                .append("\tkcal").setProportion(0.5f)
                .into(mTvCurrentEnergy);
    }

    private void initWareView() {
        mProComplete.setBorder(1, ContextCompat.getColor(mContext, R.color.GrayWrite));
//        mProComplete.setWaveColor(Color.parseColor("#6ABEBD"), Color.parseColor("#B8FFDC"));
        mWaveHelper = new RxWaveHelper(mProComplete);
    }


    @Override
    protected void initNetData() {
        super.initNetData();
        initUserInfo();
        getData();
    }

    private void initUserInfo() {
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = MyAPP.getGson().fromJson(string, UserInfo.class);
        if (info != null) {
            MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg);
        }
    }

    private void getData() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.indexInfo(1, 7))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("indexInfo", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        SlimmingRecordBean bean = MyAPP.getGson().fromJson(s, SlimmingRecordBean.class);
                        updateUI(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });

    }

    private void updateUI(SlimmingRecordBean bean) {
        slimmingTarget(bean);
        healthScore(bean);
        weightRecord(bean);
        sportingRecord(bean.getAthleticsInfoList());
        energyAndDietRecord(bean.getDataList());
    }

    /**
     * 能量和饮食记录
     *
     * @param list
     */
    private void energyAndDietRecord(List<SlimmingRecordBean.DataListBean> list) {
        Calendar calendar = Calendar.getInstance();
        float max = 100, dietMax = 100;
        List<Integer> entry = new ArrayList<>();
        List<Integer> dietEntry = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        if (!list.isEmpty()) {
            calendar.setTimeInMillis(list.get(0).getRecordDate());
            int size = list.size();
            for (int i = 6; i >= 0; i--) {
                size--;
                if (size >= 0) {
                    SlimmingRecordBean.DataListBean bean = list.get(size);
                    dates.set(i, RxFormat.setFormatDate(bean.getRecordDate(), "MM/dd"));
                    int value = bean.getAthlCalorie() + bean.getBasalCalorie() - bean.getHeatCalorie();
                    max = max > value ? max : value;
                    entry.add(i, value);
                    dietEntry.add(i, bean.getHeatCalorie());
                    dietMax = dietMax > bean.getHeatCalorie() ? dietMax : bean.getHeatCalorie();
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.set(i, RxFormat.setFormatDate(calendar, "MM/dd"));
                    entry.add(i, 0);
                    dietEntry.add(i, 0);
                }
            }

            SlimmingRecordBean.DataListBean bean = list.get(0);
            int value = bean.getAthlCalorie() + bean.getBasalCalorie() - bean.getHeatCalorie();

            RxTextUtils.getBuilder(value + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentDiet);

            RxTextUtils.getBuilder(bean.getHeatCalorie() + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentEnergy);

        } else {
            for (int i = 0; i < 7; i++) {
                weightDates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                entry.add(i, 0);
                dietEntry.add(i, 0);
            }
        }
        mEnergyProgress7.setProgress((int) (entry.get(0) * 100 / max));
        mEnergyProgress6.setProgress((int) (entry.get(1) * 100 / max));
        mEnergyProgress5.setProgress((int) (entry.get(2) * 100 / max));
        mEnergyProgress4.setProgress((int) (entry.get(3) * 100 / max));
        mEnergyProgress3.setProgress((int) (entry.get(4) * 100 / max));
        mEnergyProgress2.setProgress((int) (entry.get(5) * 100 / max));
        mEnergyProgress1.setProgress((int) (entry.get(6) * 100 / max));

        mTvEnergyChart7.setText(dates.get(0));
        mTvEnergyChart6.setText(dates.get(1));
        mTvEnergyChart5.setText(dates.get(2));
        mTvEnergyChart4.setText(dates.get(3));
        mTvEnergyChart3.setText(dates.get(4));
        mTvEnergyChart2.setText(dates.get(5));
        mTvEnergyChart1.setText(dates.get(6));

        mTvDietChart7.setText(dates.get(0));
        mTvDietChart6.setText(dates.get(1));
        mTvDietChart5.setText(dates.get(2));
        mTvDietChart4.setText(dates.get(3));
        mTvDietChart3.setText(dates.get(4));
        mTvDietChart2.setText(dates.get(5));
        mTvDietChart1.setText(dates.get(6));

        mDietProgress7.setProgress((int) (entry.get(0) * 100 / dietMax));
        mDietProgress6.setProgress((int) (entry.get(1) * 100 / dietMax));
        mDietProgress5.setProgress((int) (entry.get(2) * 100 / dietMax));
        mDietProgress4.setProgress((int) (entry.get(3) * 100 / dietMax));
        mDietProgress3.setProgress((int) (entry.get(4) * 100 / dietMax));
        mDietProgress2.setProgress((int) (entry.get(5) * 100 / dietMax));
        mDietProgress1.setProgress((int) (entry.get(6) * 100 / dietMax));


    }

    /**
     * 运动记录
     *
     * @param list
     */
    private void sportingRecord(List<SlimmingRecordBean.AthleticsInfoListBean> list) {
        Calendar calendar = Calendar.getInstance();
        float max = 100;
        List<Integer> entry = new ArrayList<>();
        List<String> sportingDates = new ArrayList<>();
        if (!list.isEmpty()) {
            calendar.setTimeInMillis(list.get(0).getAthlDate());
            int size = list.size();
            for (int i = 6; i >= 0; i--) {
                size--;
                if (size >= 0) {
                    sportingDates.set(i, RxFormat.setFormatDate(list.get(size).getAthlDate(), "MM/dd"));
                    max = max > list.get(size).getCalorie() ? max : list.get(size).getCalorie();
                    entry.add(i, list.get(size).getCalorie());
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    sportingDates.set(i, RxFormat.setFormatDate(calendar, "MM/dd"));
                    entry.add(i, 0);
                }
            }

            RxTextUtils.getBuilder(list.get(0).getCalorie() + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentEnergy);

        } else {
            for (int i = 0; i < 7; i++) {
                weightDates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                entry.add(i, 0);
            }
        }
        mSportingProgress7.setProgress((int) (entry.get(0) * 100 / max));
        mTvSportingChart7.setText(sportingDates.get(0));
        mSportingProgress6.setProgress((int) (entry.get(1) * 100 / max));
        mTvSportingChart6.setText(sportingDates.get(1));
        mSportingProgress5.setProgress((int) (entry.get(2) * 100 / max));
        mTvSportingChart5.setText(sportingDates.get(2));
        mSportingProgress4.setProgress((int) (entry.get(3) * 100 / max));
        mTvSportingChart4.setText(sportingDates.get(3));
        mSportingProgress3.setProgress((int) (entry.get(4) * 100 / max));
        mTvSportingChart3.setText(sportingDates.get(4));
        mSportingProgress2.setProgress((int) (entry.get(5) * 100 / max));
        mTvSportingChart2.setText(sportingDates.get(5));
        mSportingProgress1.setProgress((int) (entry.get(6) * 100 / max));
        mTvSportingChart1.setText(sportingDates.get(6));
    }


    /**
     * 体重记录
     *
     * @param bean
     */
    private void weightRecord(SlimmingRecordBean bean) {
        setLineChartData(bean);
        if (bean.getNormWeight() != 0)
            addLimitLine2Y(mMLineChart, (float) bean.getNormWeight(), bean.getNormWeight() + "kg");

    }


    private void initChart(BarLineChartBase lineChartBase) {
        lineChartBase.setEnabled(false);
        lineChartBase.setTouchEnabled(false);//可以点击
        lineChartBase.setMaxVisibleValueCount(60);
        lineChartBase.getLegend().setEnabled(false);
        lineChartBase.getDescription().setEnabled(false);
        lineChartBase.setDrawGridBackground(false);
        XAxis xAxis = lineChartBase.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);

        YAxis leftAxis = lineChartBase.getAxisLeft();
        YAxis rightAxis = lineChartBase.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        mMLineChart.setData(new LineData());
        createSet();
    }

    private void setLineChartData(SlimmingRecordBean bean) {
        weightDates.clear();
        List<SlimmingRecordBean.WeightInfoListBean> list = bean.getWeightInfoList();
        Calendar calendar = Calendar.getInstance();
        ArrayList<Entry> lineEntry = new ArrayList<>();
        float max = 100;
        float min = 0;

        if (!list.isEmpty()) {
            calendar.setTimeInMillis(list.get(0).getWeightDate());
            int size = list.size();
            for (int i = 6; i >= 0; i--) {
                size--;
                if (size >= 0) {
                    lineEntry.set(i, new BarEntry(i, (float) list.get(size).getWeight()));
                    weightDates.set(i, RxFormat.setFormatDate(list.get(size).getWeightDate(), "MM/dd"));
                    max = max > list.get(size).getWeight() ? max : list.get(size).getWeight();
                    min = min < list.get(size).getWeight() ? min : list.get(size).getWeight();
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    weightDates.set(i, RxFormat.setFormatDate(calendar, "MM/dd"));
                    lineEntry.set(i, new BarEntry(i, 0));
                }
            }
            RxTextUtils.getBuilder(list.get(0).getWeight() + "")
                    .append("\tkg").setProportion(0.5f)
                    .into(mTvCurrentWeight);
        } else {
            for (int i = 0; i < 7; i++) {
                weightDates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                lineEntry.add(new Entry(i, 0));
            }
        }

        YAxis yAxis = mMLineChart.getAxisLeft();
        min = list.size() == 7 ? min * 0.7f : 0;
        max *= 1.3f;
        max = Math.max(max, bean.getNormWeight());
        min = Math.max(min, bean.getNormWeight());
        yAxis.setAxisMaximum(max);
        yAxis.setAxisMinimum(min);

        LineDataSet set = (LineDataSet) mMLineChart.getData().getDataSetByIndex(0);
        set.setValues(lineEntry);
        mMLineChart.getData().notifyDataChanged();
        mMLineChart.notifyDataSetChanged();
        mMLineChart.invalidate();
        mMLineChart.setVisibleXRangeMaximum(7);
        mMLineChart.animateX(1000);

        mTvWeightChart1.setText(weightDates.get(0));
        mTvWeightChart2.setText(weightDates.get(1));
        mTvWeightChart3.setText(weightDates.get(2));
        mTvWeightChart4.setText(weightDates.get(3));
        mTvWeightChart5.setText(weightDates.get(4));
        mTvWeightChart6.setText(weightDates.get(5));
        mTvWeightChart7.setText(weightDates.get(6));


    }

    private void createSet() {
        LineDataSet set1 = new LineDataSet(null, "weight");
        set1.setDrawIcons(false);
        set1.setDrawValues(false);
        set1.setColor(getResources().getColor(R.color.GrayWrite));
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(true);
        set1.setCircleRadius(5f);
        set1.setLineWidth(1f);
        set1.setCircleColors(colors, mActivity);
        mMLineChart.getData().addDataSet(set1);
        mMLineChart.invalidate();
    }

    //添加提示线
    public void addLimitLine2Y(BarLineChartBase lineChartBase, float value, String label) {
        //提示线，
        LimitLine ll = new LimitLine(value, label);//线条颜色宽度等
        ll.setLineColor(getResources().getColor(R.color.gray_ECEBF0));
        ll.setLineWidth(1f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);//文字颜色、大小
        ll.setTextColor(getResources().getColor(R.color.GrayWrite));
        ll.setTextSize(10f);
        YAxis y = lineChartBase.getAxisLeft();
        y.removeAllLimitLines();
        //加入到 mXAxis 或 mYAxis
        y.addLimitLine(ll);
    }

    /**
     * 健康分数
     *
     * @param bean
     */
    private void healthScore(SlimmingRecordBean bean) {
        SlimmingRecordBean.WeightInfoBean weightInfo = bean.getWeightInfo();
        if (weightInfo == null) return;
        RxTextUtils.getBuilder(weightInfo.getWeight() + "")
                .append("\t体重(kg)\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.getBmi() + "")
                .append("\tBMI\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.getBodyFat() + "")
                .append("\t体脂率(%)\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.getBmr() + "")
                .append("\t基础代谢(kcal)\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvWeightInfo);

        RxTextUtils.getBuilder(weightInfo.getHealthScore() + "")
                .append("\t分").setProportion(0.5f)
                .into(mTvHealthScore);


        RxTextUtils.getBuilder("身材：\t").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.getBodyType())
                .append("\t\t\t患病风险：").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(bean.getLevelDesc())
                .into(mTvHealthInfo);

        mHealthyLevel.switchLevel(bean.getSickLevel());
    }

    /**
     * 今日完成目标
     *
     * @param bean
     */
    private void slimmingTarget(SlimmingRecordBean bean) {
        mImgBreakfast.getHelper().setIconNormal(bean.isBreakfastDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.GrayWrite)));
        mImgLunch.getHelper().setIconNormal(bean.isLunchDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.GrayWrite)));
        mImgDinner.getHelper().setIconNormal(bean.isDinnerDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.GrayWrite)));
        mImgSporting.getHelper().setIconNormal(bean.isAthlDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.GrayWrite)));
        mImgWeigh.getHelper().setIconNormal(bean.isWeightDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.GrayWrite)));

        int complete = 0;
        if (bean.isBreakfastDone()) complete += 20;
        if (bean.isLunchDone()) complete += 20;
        if (bean.isDinnerDone()) complete += 20;
        if (bean.isAthlDone()) complete += 20;
        if (bean.isWeightDone()) complete += 20;

        mProComplete.setWaterLevelRatio(complete / 100f);
        if (bean.getComplete() == 1)
            mTvProgress.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        RxTextUtils.getBuilder(complete + "")
                .append("\t%").setProportion(0.5f)
                .into(mTvProgress);


        RxTextUtils.getBuilder("已连续记录\t")
                .append(bean.getHasDays() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("\t天").into(mTvContinuousRecord);


    }


    @Override
    protected void onInvisible() {
        super.onInvisible();
        mWaveHelper.cancel();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        mWaveHelper.start();
    }


    @OnClick({
            R.id.layout_Health_title,
            R.id.layout_weight_title,
            R.id.layout_diet_title,
            R.id.layout_sports_title,
            R.id.layout_energy_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_Health_title:
                break;
            case R.id.layout_weight_title:
                break;
            case R.id.layout_diet_title:
                break;
            case R.id.layout_sports_title:
                break;
            case R.id.layout_energy_title:
                break;
        }
    }
}
