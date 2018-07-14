package lab.wesmartclothing.wefit.flyso.ui.main.slimming;

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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.smartclothing.module_wefit.activity.MessageActivity;
import com.smartclothing.module_wefit.activity.PersonalDataActivity;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.entity.FirstPageBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.AddFoodActivity_;
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

    public String[] add_food;
    private String[] barXLists = new String[7];
    private String[] lineXLists = new String[7];
    private int[] colors = {R.color.gray_ECEBF0, R.color.gray_ECEBF0, R.color.gray_ECEBF0,
            R.color.gray_ECEBF0, R.color.gray_ECEBF0, R.color.gray_ECEBF0, R.color.red};

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
    RelativeLayout mLayoutHeat;
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
    RelativeLayout mLayoutSports;
    @BindView(R.id.btn_bind_scale)
    QMUIRoundButton mBtnBindScale;
    @BindView(R.id.iv_weight)
    ImageView mIvWeight;
    @BindView(R.id.layout_weight)
    RelativeLayout mLayoutWeight;
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

    public static Fragment getInstance() {
        return new Slimming2Fragment();
    }

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_layout_slimming2, null);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        initData();
        return rootView;
    }


    private void initView() {
        initChart(mMBarChart);
        setDefaultBarData(null);

    }


    private void initChart(BarLineChartBase lineChartBase) {
        lineChartBase.setEnabled(false);
        lineChartBase.setTouchEnabled(false);//可以点击
        lineChartBase.getDescription().setEnabled(false);
        lineChartBase.setMaxVisibleValueCount(60);
        lineChartBase.getLegend().setEnabled(false);
        lineChartBase.getDescription().setEnabled(false);
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
        leftAxis.setEnabled(false);
        rightAxis.setEnabled(false);

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


    private void setDefaultBarData(FirstPageBean bean) {
        boolean isDefault = true;
        Calendar calendar = Calendar.getInstance();
        ArrayList<BarEntry> barEntry = new ArrayList<>();
        int max = 15;
        if (bean != null && bean.getAthleticsInfoList().size() != 0) {
            List<FirstPageBean.AthleticsInfoListBean> list = bean.getAthleticsInfoList();
            isDefault = false;
            calendar.setTimeInMillis(Long.parseLong(list.get(0).getAthlDate()));

            int size = list.size();
            for (int i = 6; i >= 0; i--) {
                if (i <= 6 - list.size()) {
                    barXLists[i] = RxFormat.setFormatDate(calendar, "MM/dd");
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    barEntry.add(i, new BarEntry(i, max * 0.1f));
                } else {
                    size--;
                    barEntry.add(i, new BarEntry(i, list.get(size).getCalorie()));
                    barXLists[i] = RxFormat.setFormatDate(Long.parseLong(list.get(size).getAthlDate()), "MM/dd");
                    max = max > list.get(size).getCalorie() ? max : list.get(size).getCalorie();
                    max = (int) (max * 1.3);
                }
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                barXLists[i] = RxFormat.setFormatDate(calendar, "MM/dd");
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                barEntry.add(i, new BarEntry(i, 1));
            }
        }

        
        for (int i = 0; i < 7; i++) {

            barEntry.add(new BarEntry(i, 1));

            if (isDefault) {
                calendar.add(Calendar.DAY_OF_MONTH, -3);
                barXLists[i] = RxFormat.setFormatDate(calendar, "MM/dd");
            }
        }

        YAxis yAxis = mMBarChart.getAxisLeft();
        yAxis.setAxisMaximum(max);

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
            colors[6] = R.color.red;
            set1.setColors(colors, mActivity);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.25f);
            mMBarChart.setData(data);
            mMBarChart.setFitBars(true);
        }

        addXLabel(mMBarChart, barXLists);
        mMBarChart.invalidate();
        mMBarChart.setVisibleXRangeMaximum(7);
    }

    private void initData() {
        getFirstPageData();
        add_food = getResources().getStringArray(R.array.add_food);
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = new Gson().fromJson(string, UserInfo.class);
        if (info == null) {
            RxLogUtils.e("UserInfo is Null");
            return;
        }
        mTvUserName.setText(info.getUserName());
        Glide.with(mActivity).load(info.getUserImg())
                .asBitmap()
                .placeholder(R.mipmap.userimg_man)
                .into(mIvUserImg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_userImg, R.id.layout_notify, R.id.layout_Healthy, R.id.layout_heat, R.id.layout_breakfast, R.id.layout_lunch, R.id.layout_dinner, R.id.layout_meal, R.id.btn_bind_clothing, R.id.layout_sports, R.id.btn_bind_scale, R.id.layout_weight, R.id.btn_bind})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.ADD_FOOD_DATE, System.currentTimeMillis() + "");
        switch (view.getId()) {
            case R.id.iv_userImg:
                //跳转个人主页
                RxActivityUtils.skipActivity(mActivity, PersonalDataActivity.class);
                break;
            case R.id.layout_notify:
                //跳转消息通知
                RxActivityUtils.skipActivity(mActivity, MessageActivity.class);
                break;
            case R.id.layout_Healthy:
                //点击健康评级
                break;
            case R.id.layout_heat:
                //跳转热量记录
                break;
            case R.id.layout_breakfast:
                bundle.putString(Key.ADD_FOOD_NAME, add_food[0]);
                bundle.putInt(Key.ADD_FOOD_TYPE, 1);
                RxActivityUtils.skipActivity(mActivity, AddFoodActivity_.class, bundle);
                break;
            case R.id.layout_lunch:
                bundle.putString(Key.ADD_FOOD_NAME, add_food[1]);
                bundle.putInt(Key.ADD_FOOD_TYPE, 2);
                RxActivityUtils.skipActivity(mActivity, AddFoodActivity_.class, bundle);
                break;
            case R.id.layout_dinner:
                bundle.putString(Key.ADD_FOOD_NAME, add_food[2]);
                bundle.putInt(Key.ADD_FOOD_TYPE, 3);
                RxActivityUtils.skipActivity(mActivity, AddFoodActivity_.class, bundle);
                break;
            case R.id.layout_meal:
                bundle.putString(Key.ADD_FOOD_NAME, add_food[3]);
                bundle.putInt(Key.ADD_FOOD_TYPE, 4);
                RxActivityUtils.skipActivity(mActivity, AddFoodActivity_.class, bundle);
                break;
            case R.id.btn_bind_clothing:
                mBtnBindClothing.setVisibility(View.GONE);
                break;
            case R.id.layout_sports:
                break;
            case R.id.btn_bind_scale:
                mBtnBindScale.setVisibility(View.GONE);
                break;
            case R.id.layout_weight:
                break;
            case R.id.btn_bind:
                RxActivityUtils.skipActivity(mActivity, AddDeviceActivity_.class);
                break;
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 接口
    ///////////////////////////////////////////////////////////////////////////

    private void getFirstPageData() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.indexInfo(1, 7))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        FirstPageBean bean = new Gson().fromJson(s, FirstPageBean.class);
                        notifyData(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error);
                    }
                });
    }

    private void notifyData(FirstPageBean bean) {
        mTvBreakfastKcal.setText(bean.getBreakfast() + "");
        mTvLunchKcal.setText(bean.getLunch() + "");
        mTvDinnerKcal.setText(bean.getLunch() + "");
        mTvMealKcal.setText(bean.getSnacks() + "");
        mTvKcal.setText(bean.getAbleIntake() + "");

        mTvKcal.setTextColor(getResources().getColor(bean.isWarning() ? R.color.orange_FF7200 : R.color.green_61D97F));
        mTvHeatUnit.setTextColor(getResources().getColor(bean.isWarning() ? R.color.orange_FF7200 : R.color.green_61D97F));
        mIvNotify.setBackgroundResource(bean.getUnreadCount() == 0 ? R.mipmap.icon_email_white : R.mipmap.icon_email_white_mark);
        if (bean.getTargetWeight() == 0) {
            //提示需要录入目标体重
        }
        if (bean.getWeightInfo() != null) {
            mTvBody.setText(RxDataUtils.isNullString(bean.getWeightInfo().getBodyType()) ? "--.--" : bean.getWeightInfo().getBodyType());
            mTvWeightStart.setText(bean.getWeightInfo().getWeight() + "kg");
            mTvWeightEnd.setText(bean.getTargetWeight() + "kg");
//            mTvDate.setText(RxFormat.setFormatDate(bean.getWeightInfo().getWeightDate(), RxFormat.Date)));
        }
        if (!RxDataUtils.isNullString(bean.getSickLevel())) {
            mTvRisk.setText(bean.getSickLevel());
        }
        mTvTarget.setText(bean.getHasDays() == 0 ? "请到体重记录页设定小目标哟！ ^-^" : "离目标完成还剩 " + bean.getHasDays() + " 天");

        mBtnBindClothing.setVisibility(bean.getAthleticsInfoList().size() == 0 ? View.VISIBLE : View.GONE);
        mBtnBindScale.setVisibility(bean.getAthleticsInfoList().size() == 0 ? View.VISIBLE : View.GONE);
    }

}
