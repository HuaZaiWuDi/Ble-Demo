package lab.wesmartclothing.wefit.flyso.ui.main.slimming;

import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.FirstPageBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MessageFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.UserInfofragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.FoodDetailsFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.HeatDetailFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SmartClothingFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightRecordFragment;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.HealthLevelView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

/**
 * Created by jk on 2018/7/13.
 */
public class Slimming2Fragment extends BaseAcFragment {

    @BindView(R.id.layout_bind)
    RelativeLayout mLayoutBind;
    @BindView(R.id.tv_currentKcal)
    TextView mTvCurrentKcal;
    @BindView(R.id.tv_currentkg)
    TextView mTvCurrentkg;
    @BindView(R.id.iv_userImg)
    QMUIRadiusImageView mIvUserImg;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.tv_weight_start)
    TextView mTvWeightStart;
    @BindView(R.id.tv_weight_end)
    TextView mTvWeightEnd;
    @BindView(R.id.pro_weight)
    RxRoundProgressBar mProWeight;
    @BindView(R.id.layout_progress)
    RelativeLayout mLayoutProgress;
    @BindView(R.id.iv_notify)
    ImageView mIvNotify;
    @BindView(R.id.layout_notify)
    LinearLayout mLayoutNotify;
    @BindView(R.id.iv_Healthy)
    ImageView mIvHealthy;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.tv_body)
    TextView mTvBody;
    @BindView(R.id.tv_risk)
    TextView mTvRisk;
    @BindView(R.id.layout_title)
    LinearLayout mLayoutTitle;
    @BindView(R.id.Healthy_level)
    HealthLevelView mIvHealthyLevel;
    @BindView(R.id.tv_weight)
    TextView mTvWeight;
    @BindView(R.id.tv_BMI)
    TextView mTvBMI;
    @BindView(R.id.tv_bodyFat)
    TextView mTvBodyFat;
    @BindView(R.id.layout_Healthy)
    RelativeLayout mLayoutHealthy;
    @BindView(R.id.iv_heat)
    ImageView mIvHeat;
    @BindView(R.id.layout_heat)
    QMUIRoundLinearLayout mLayoutHeat;
    @BindView(R.id.circleProgressBar)
    QMUIProgressBar mCircleProgressBar;
    @BindView(R.id.tv_heat_title)
    TextView mTvHeatTitle;
    @BindView(R.id.tv_kcal)
    TextView mTvKcal;
    @BindView(R.id.iv_breakfast)
    ImageView mIvBreakfast;
    @BindView(R.id.tv_breakfast)
    TextView mTvBreakfast;
    @BindView(R.id.tv_breakfast_kcal)
    TextView mTvBreakfastKcal;
    @BindView(R.id.layout_breakfast)
    RelativeLayout mLayoutBreakfast;
    @BindView(R.id.iv_lunch)
    ImageView mIvLunch;
    @BindView(R.id.tv_lunch)
    TextView mTvLunch;
    @BindView(R.id.tv_lunch_kcal)
    TextView mTvLunchKcal;
    @BindView(R.id.layout_lunch)
    RelativeLayout mLayoutLunch;
    @BindView(R.id.iv_dinner)
    ImageView mIvDinner;
    @BindView(R.id.tv_dinner)
    TextView mTvDinner;
    @BindView(R.id.tv_dinner_kcal)
    TextView mTvDinnerKcal;
    @BindView(R.id.layout_dinner)
    RelativeLayout mLayoutDinner;
    @BindView(R.id.iv_meal)
    ImageView mIvMeal;
    @BindView(R.id.tv_meal)
    TextView mTvMeal;
    @BindView(R.id.tv_meal_kcal)
    TextView mTvMealKcal;
    @BindView(R.id.layout_meal)
    RelativeLayout mLayoutMeal;
    @BindView(R.id.btn_bind_clothing)
    QMUIRoundButton mBtnBindClothing;
    @BindView(R.id.iv_sports)
    ImageView mIvSports;
    @BindView(R.id.layout_sports)
    LinearLayout mLayoutSports;
    @BindView(R.id.btn_bind_scale)
    QMUIRoundButton mBtnBindScale;
    @BindView(R.id.iv_weight)
    ImageView mIvWeight;
    @BindView(R.id.layout_weight)
    LinearLayout mLayoutWeight;
    @BindView(R.id.btn_bind)
    QMUIRoundButton mBtnBind;
    Unbinder unbinder;
    @BindView(R.id.tv_target)
    TextView mTvTarget;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_heatUnit)
    TextView mTvHeatUnit;
    @BindView(R.id.mBarChart)
    BarChart mMBarChart;
    @BindView(R.id.mLineChart)
    LineChart mMLineChart;
    @BindView(R.id.collapsing_topbar_layout)
    QMUICollapsingTopBarLayout mCollapsingTopbarLayout;
    @BindView(R.id.iv_userImg2)
    QMUIRadiusImageView mIvUserImg2;
    @BindView(R.id.tv_userName2)
    TextView mTvUserName2;
    @BindView(R.id.iv_notify2)
    ImageView mIvNotify2;
    @BindView(R.id.layout_notify2)
    LinearLayout mLayoutNotify2;
    @BindView(R.id.layout_title2)
    RelativeLayout mLayoutTitle2;

    public static Fragment getInstance() {
        return new Slimming2Fragment();
    }

    private String[] barXLists = new String[7];
    private String[] lineXLists = new String[7];
    private int[] colors = {R.color.gray_ECEBF0, R.color.gray_ECEBF0, R.color.gray_ECEBF0,
            R.color.gray_ECEBF0, R.color.gray_ECEBF0, R.color.gray_ECEBF0, R.color.gray_ECEBF0};
    public String[] add_food;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_layout_slimming2, null);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }


    private void initView() {
        add_food = getResources().getStringArray(R.array.add_food);
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvWeightStart.setTypeface(typeface);
        mTvWeightEnd.setTypeface(typeface);

        mTvBreakfastKcal.setTypeface(typeface);
        mTvLunchKcal.setTypeface(typeface);
        mTvDinnerKcal.setTypeface(typeface);
        mTvMealKcal.setTypeface(typeface);
        mTvKcal.setTypeface(typeface);
        mTvWeight.setTypeface(typeface);
        mTvCurrentkg.setTypeface(typeface);
        mTvCurrentKcal.setTypeface(typeface);
        mTvBMI.setTypeface(typeface);
        mTvBodyFat.setTypeface(typeface);
        initChart(mMBarChart);
        initChart(mMLineChart);
        setDefaultBarData(null);
        setLineChartData(null);
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        getFirstPageData();

        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = MyAPP.getGson().fromJson(string, UserInfo.class);
        RxLogUtils.d("用户数据:" + info);
        if (info != null) {
            mTvUserName.setText(info.getUserName());
            mTvUserName2.setText(info.getUserName());
            Glide.with(mActivity).load(info.getImgUrl())
                    .asBitmap()
                    .placeholder(R.mipmap.userimg)
                    .into(mIvUserImg);

            Glide.with(mActivity).load(info.getImgUrl())
                    .asBitmap()
                    .placeholder(R.mipmap.userimg)
                    .into(mIvUserImg2);
        }
        mTvDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date));
        if (!RxDataUtils.isNullString(SPUtils.getString(SPKey.SP_clothingMAC))
                && !RxDataUtils.isNullString(SPUtils.getString(SPKey.SP_scaleMAC))) {
            mLayoutBind.setVisibility(View.GONE);
        }

        mCollapsingTopbarLayout.setScrimUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLayoutTitle2.setVisibility((int) animation.getAnimatedValue() < 200 ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    private void initChart(BarLineChartBase lineChartBase) {
        lineChartBase.setEnabled(false);
        lineChartBase.setTouchEnabled(false);//可以点击
        lineChartBase.setMaxVisibleValueCount(60);
        lineChartBase.getLegend().setEnabled(false);
        lineChartBase.getDescription().setText("");
        lineChartBase.setDrawGridBackground(false);
        lineChartBase.setBackgroundColor(getResources().getColor(R.color.gray_FBFBFC));
        XAxis xAxis = lineChartBase.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(7);
        xAxis.setAxisLineColor(getResources().getColor(R.color.gray_ECEBF0));
        xAxis.setTextColor(getResources().getColor(R.color.GrayWrite));

        YAxis leftAxis = lineChartBase.getAxisLeft();
        YAxis rightAxis = lineChartBase.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        lineChartBase.invalidate();
    }

    //添加X轴标签
    private void addXLabel(BarLineChartBase lineChartBase, final String[] label) {
        XAxis x = lineChartBase.getXAxis();
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return label[(int) value % label.length];
            }
        });
        lineChartBase.invalidate();
    }

    //添加提示线
    public void addLimitLine2Y(BarLineChartBase lineChartBase, float value, String label) {
        //提示线，
        LimitLine ll = new LimitLine(value, label);//线条颜色宽度等
        ll.setLineColor(getResources().getColor(R.color.gray_ECEBF0));
        ll.setLineWidth(2f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);//文字颜色、大小
        ll.setTextColor(getResources().getColor(R.color.gray_ECEBF0));
        ll.setTextSize(10f);
        YAxis y = lineChartBase.getAxisLeft();
        y.removeAllLimitLines();
        //加入到 mXAxis 或 mYAxis
        y.addLimitLine(ll);
    }

    private void setDefaultBarData(FirstPageBean bean) {
        Calendar calendar = Calendar.getInstance();
        ArrayList<BarEntry> barEntry = new ArrayList<>();

        int max = 3000;
        colors[6] = R.color.red;
        for (int i = 0; i < 7; i++) {
            barXLists[6 - i] = RxFormat.setFormatDate(calendar, "MM/dd");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            barEntry.add(i, new BarEntry(i, max * 0.1f));
        }

        if (bean != null && bean.getAthleticsInfoList().size() != 0) {
            List<FirstPageBean.AthleticsInfoListBean> list = bean.getAthleticsInfoList();
            calendar.setTimeInMillis(list.get(0).getAthlDate());
            max = bean.getPeakValue() == 0 ? 3000 : (int) (bean.getPeakValue() * 1.3f);
            int size = list.size();
            for (int i = 6; i >= 0; i--) {
                size--;
                if (size < 0) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    barXLists[i] = RxFormat.setFormatDate(calendar, "MM/dd");
                    barEntry.set(i, new BarEntry(i, max * 0.1f));
                } else {
                    float calorie = list.get(size).getCalorie();
                    calorie = calorie < max * 0.1f ? (int) (max * 0.1f) : calorie;
                    barEntry.set(i, new BarEntry(i, calorie));
                    barXLists[i] = RxFormat.setFormatDate(list.get(size).getAthlDate(), "MM/dd");
                }
            }
        }

        YAxis yAxis = mMBarChart.getAxisLeft();
        yAxis.setAxisMaximum(max);
        yAxis.setAxisMinimum(0f);

        BarDataSet set1;
        if (mMBarChart.getData() != null &&
                mMBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mMBarChart.getData().getDataSetByIndex(0);
            set1.setValues(barEntry);
            mMBarChart.getData().notifyDataChanged();
            mMBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(barEntry, "sports");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.25f);
            mMBarChart.setData(data);
            mMBarChart.setFitBars(true);
            RxLogUtils.d("运动数据:" + Arrays.toString(barXLists));
            addXLabel(mMBarChart, barXLists);
        }

        set1.setColors(colors, mActivity);
        mMBarChart.invalidate();
        mMBarChart.setVisibleXRangeMaximum(7);
    }

    private void setLineChartData(FirstPageBean bean) {
        Calendar calendar = Calendar.getInstance();

        ArrayList<Entry> lineEntry = new ArrayList<>();
        int max = 100;
        colors[6] = R.color.green_61D97F;
        for (int i = 0; i < 7; i++) {
            lineXLists[6 - i] = RxFormat.setFormatDate(calendar, "MM/dd");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            lineEntry.add(new Entry(i, 0));
        }

        if (bean != null && bean.getWeightInfoList().size() != 0) {
            List<FirstPageBean.WeightInfoListBean> list = bean.getWeightInfoList();
            calendar.setTimeInMillis(list.get(0).getWeightDate());
            int size = list.size();

            for (int i = 6; i >= 0; i--) {
                size--;
                if (size < 0) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    lineXLists[i] = RxFormat.setFormatDate(calendar, "MM/dd");
                    lineEntry.set(i, new BarEntry(i, 0));
                } else {
                    lineEntry.set(i, new BarEntry(i, (float) list.get(size).getWeight()));
                    lineXLists[i] = RxFormat.setFormatDate(list.get(size).getWeightDate(), "MM/dd");
                    max = max > (int) list.get(size).getWeight() ? max : (int) list.get(size).getWeight();
                    RxLogUtils.d("最大范围：" + max);
                }
            }
        }

        max = (int) (max * 1.3);
        YAxis yAxis = mMLineChart.getAxisLeft();
        yAxis.setAxisMaximum(max);
        yAxis.setAxisMinimum(0f);

        mMLineChart.getXAxis().setLabelCount(7, true);
        LineDataSet set1;
        if (mMLineChart.getData() != null &&
                mMLineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mMLineChart.getData().getDataSetByIndex(0);
            set1.setValues(lineEntry);
            mMLineChart.getData().notifyDataChanged();
            mMLineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(lineEntry, "weight");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColor(getResources().getColor(R.color.gray_ECEBF0));

            set1.setDrawCircleHole(false);
            set1.setDrawCircles(true);
            set1.setCircleRadius(6f);
            set1.setLineWidth(2f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            mMLineChart.setData(data);

            addXLabel(mMLineChart, lineXLists);
        }
        set1.setCircleColors(colors, mActivity);
        if (bean != null && bean.getNormWeight() != 0)
            addLimitLine2Y(mMLineChart, (float) bean.getNormWeight(), bean.getNormWeight() + "kg");
        mMLineChart.invalidate();
        mMLineChart.setVisibleXRangeMaximum(7);
    }


    @OnClick({R.id.iv_userImg2, R.id.layout_notify2, R.id.iv_userImg, R.id.layout_notify, R.id.layout_Healthy, R.id.layout_heat, R.id.layout_breakfast, R.id.layout_lunch, R.id.layout_dinner, R.id.layout_meal, R.id.btn_bind_clothing, R.id.layout_sports, R.id.btn_bind_scale, R.id.layout_weight, R.id.btn_bind})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong(Key.ADD_FOOD_DATE, System.currentTimeMillis());
        bundle.putBoolean(Key.ADD_FOOD_NAME, true);//是否是主页跳转的
        switch (view.getId()) {
            case R.id.iv_userImg:
                //跳转个人主页
                startFragment(UserInfofragment.getInstance());
                break;
            case R.id.layout_notify:
                //跳转消息通知
                startFragment(MessageFragment.getInstance());
                break;
            case R.id.layout_Healthy:
                //点击健康评级
                break;
            case R.id.layout_heat:
                //跳转热量记录
                startFragment(HeatDetailFragment.getInstance());
                break;
            case R.id.layout_breakfast:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_BREAKFAST);
                QMUIFragment breakfast = FoodDetailsFragment.getInstance();
                breakfast.setArguments(bundle);
                startFragment(breakfast);
                break;
            case R.id.layout_lunch:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_LUNCH);
                QMUIFragment lunch = FoodDetailsFragment.getInstance();
                lunch.setArguments(bundle);
                startFragment(lunch);
                break;
            case R.id.layout_dinner:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_DINNER);
                QMUIFragment dinner = FoodDetailsFragment.getInstance();
                dinner.setArguments(bundle);
                startFragment(dinner);
                break;
            case R.id.layout_meal:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPED_MEAL);
                QMUIFragment meal = FoodDetailsFragment.getInstance();
                meal.setArguments(bundle);
                startFragment(meal);
                break;
            case R.id.btn_bind_clothing:
                mBtnBindClothing.setVisibility(View.GONE);
                break;
            case R.id.layout_sports:
                startFragment(SmartClothingFragment.getInstance());
                break;
            case R.id.btn_bind_scale:
                mBtnBindScale.setVisibility(View.GONE);
                break;
            case R.id.layout_weight:
                startFragment(WeightRecordFragment.getInstance());
                break;
            case R.id.btn_bind:
                RxActivityUtils.skipActivity(mActivity, AddDeviceActivity_.class);
                break;
            case R.id.layout_notify2:
                //跳转消息通知
                startFragment(MessageFragment.getInstance());
                break;
            case R.id.iv_userImg2:
                //跳转个人主页
                startFragment(UserInfofragment.getInstance());
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 接口
    ///////////////////////////////////////////////////////////////////////////

    private void getFirstPageData() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.indexInfo(1, 7))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("indexInfo", String.class, CacheStrategy.cacheAndRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("加载数据：" + s);
                        FirstPageBean bean = MyAPP.getGson().fromJson(s, FirstPageBean.class);
                        notifyData(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void notifyData(FirstPageBean bean) {
        setDefaultBarData(bean);
        setLineChartData(bean);

        mTvBreakfastKcal.setText(bean.getBreakfast() + "");
        mTvLunchKcal.setText(bean.getLunch() + "");
        mTvDinnerKcal.setText(bean.getDinner() + "");
        mTvMealKcal.setText(bean.getSnacks() + "");

        int heatProgress = (int) (bean.getIntakePercent() * 100);
        mCircleProgressBar.setProgress(heatProgress == 0 ? 1 : heatProgress);

        mTvHeatTitle.setText(bean.getAbleIntake() >= 0 ? R.string.can_eatHeat : R.string.EatMore);
        RxLogUtils.d("热量：" + bean.getAbleIntake());
        mTvKcal.setText(Math.abs(bean.getAbleIntake()) + "");

        mTvKcal.setTextColor(getResources().getColor(bean.isWarning() ? R.color.orange_FF7200 : R.color.green_61D97F));
        mTvHeatUnit.setTextColor(getResources().getColor(bean.isWarning() ? R.color.orange_FF7200 : R.color.green_61D97F));
        mIvNotify.setBackgroundResource(bean.getUnreadCount() == 0 ? R.mipmap.icon_email_white : R.mipmap.icon_email_white_mark);
        mIvNotify2.setBackgroundResource(bean.getUnreadCount() == 0 ? R.mipmap.icon_email_white : R.mipmap.icon_email_white_mark);


        mTvBody.setText(bean.getWeightInfo() == null ? "--" : bean.getBodyType());
        mTvWeight.setText(bean.getWeightInfo() == null ? "--" : bean.getWeightInfo().getWeight() + "");
        mTvCurrentkg.setText(bean.getWeightInfo() == null ? "--" : bean.getWeightInfo().getWeight() + "");
        mTvBMI.setText(bean.getWeightInfo() == null ? "--" : bean.getWeightInfo().getBmi() + "");
        mTvBodyFat.setText(bean.getWeightInfo() == null ? "--" : bean.getWeightInfo().getBodyFat() + "");
        mIvHealthyLevel.switchLevel(bean.getWeightInfo() == null ? "" : bean.getSickLevel());
        mTvRisk.setText(bean.getWeightInfo() == null ? "--" : bean.getLevelDesc());

        int targetProgress = (int) (bean.getComplete() * 100);
        mProWeight.setProgress(targetProgress);
        mTvTarget.setText(bean.getHasDays() == 0 ? "请到体重记录页设定小目标哟！ ^-^" : targetProgress != 100 ? "离目标完成还剩 " + bean.getHasDays() + " 天" : "体重目标已完成");

        if (bean.getInitialWeight() != 0) {
            mTvWeightStart.setText(bean.getInitialWeight() + "kg");
        }
        if (bean.getTargetWeight() != 0) {
            mTvWeightEnd.setText(bean.getTargetWeight() + "kg");
        } else if (bean.getTargetWeight() == 0) {
            //TODO 提示需要录入目标体重
        }


//        if (bean.getWeightInfo() != null) {
//            mTvDate.setText(RxFormat.setFormatDate(bean.getWeightInfo().getWeightDate(), RxFormat.Date));
//
//
//            if (bean.getWeightInfo().getWeight() != 0) {
//                mTvWeight.setText(bean.getWeightInfo().getWeight() + "");
//            }
//            if (bean.getWeightInfo().getWeight() != 0) {
//                mTvWeight.setText(bean.getWeightInfo().getWeight() + "");
//                mTvCurrentkg.setText(bean.getWeightInfo().getWeight() + "");
//            }
//            if (bean.getWeightInfo().getBmi() != 0) {
//                mTvBMI.setText(bean.getWeightInfo().getBmi() + "");
//            }
//            if (bean.getWeightInfo().getBodyFat() != 0) {
//                mTvBodyFat.setText(bean.getWeightInfo().getBodyFat() + "");
//            }
//
//            if (!RxDataUtils.isNullString(bean.getSickLevel())) {
//                mIvHealthyLevel.switchLevel(bean.getSickLevel());
//            }
//            if (!RxDataUtils.isNullString(bean.getLevelDesc())) {
//                mTvRisk.setText(bean.getLevelDesc());
//            }
//
//        }

        mBtnBindClothing.setVisibility(bean.getAthleticsInfoList().size() == 0 ? View.VISIBLE : View.GONE);
        mBtnBindScale.setVisibility(bean.getWeightInfoList().size() == 0 ? View.VISIBLE : View.GONE);


        int size = bean.getAthleticsInfoList().size();
        if (size != 0) {
            mTvCurrentKcal.setText(RxFormatValue.fromatUp(bean.getAthleticsInfoList().get(size - 1).getCalorie(), 0));
        }
    }
}
