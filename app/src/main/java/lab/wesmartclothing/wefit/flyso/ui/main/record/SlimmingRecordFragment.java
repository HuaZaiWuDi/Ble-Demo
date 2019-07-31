package lab.wesmartclothing.wefit.flyso.ui.main.record;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.antishake.AntiShake;
import com.vondear.rxtools.model.tool.RxQRCode;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.chart.bar.BarVerticalChart;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxLinearLayout;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.waveview.RxWaveHelper;
import com.vondear.rxtools.view.waveview.RxWaveView;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.BaseShareActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.DataListBean;
import lab.wesmartclothing.wefit.flyso.entity.SlimmingRecordBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.energy.EnergyActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.DietRecordActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanWebActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SmartClothingFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightRecordFragment;
import lab.wesmartclothing.wefit.flyso.utils.EnergyUtil;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.HealthLevelView;
import lab.wesmartclothing.wefit.flyso.view.NormalLineView;

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
    @BindView(R.id.layout_diet_title)
    RxLinearLayout mLayoutDietTitle;
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
    @BindView(R.id.layout_sports_title)
    RxLinearLayout mLayoutSportsTitle;
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
    @BindView(R.id.layout_energy_title)
    RxLinearLayout mLayoutEnergyTitle;
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
    @BindView(R.id.scroll)
    NestedScrollView mScroll;
    @BindView(R.id.prant)
    LinearLayout mPrant;
    @BindView(R.id.layout_complete)
    RelativeLayout mLayoutComplete;
    @BindView(R.id.tv_appVersion)
    TextView mTvAppVersion;
    @BindView(R.id.tv_continuousRecord2)
    TextView mTvContinuousRecord2;
    @BindView(R.id.pro_complete2)
    RxWaveView mProComplete2;
    @BindView(R.id.tv_progress2)
    TextView mTvProgress2;
    @BindView(R.id.layout_complete2)
    RelativeLayout mLayoutComplete2;
    @BindView(R.id.img_breakfast2)
    RxImageView mImgBreakfast2;
    @BindView(R.id.img_lunch2)
    RxImageView mImgLunch2;
    @BindView(R.id.img_dinner2)
    RxImageView mImgDinner2;
    @BindView(R.id.img_sporting2)
    RxImageView mImgSporting2;
    @BindView(R.id.img_weigh2)
    RxImageView mImgWeigh2;
    @BindView(R.id.img_userImg2)
    RxImageView mImgUserImg2;
    @BindView(R.id.layout_shareHead)
    RelativeLayout mLayoutShareHead;
    @BindView(R.id.img_userImg3)
    RxImageView mImgUserImg3;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.layout_shareTitle)
    RelativeLayout mLayoutShareTitle;
    @BindView(R.id.img_QRcode)
    ImageView mImgQRcode;
    @BindView(R.id.img_weightSwitch)
    ImageView imgWeightSwitch;
    @BindView(R.id.line_normalDiet)
    NormalLineView lineNormalDiet;
    @BindView(R.id.line_normalSport)
    NormalLineView lineNormalSport;
    @BindView(R.id.line_normalEnergy)
    NormalLineView lineNormalEnergy;

    Unbinder unbinder;


    private RxWaveHelper mWaveHelper;

    private List<String> dates = new ArrayList<>(7);
    private List<Integer> recommendList = new ArrayList<>(7);
    private List<Integer> item = new ArrayList<>(7);


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
        initShare();
    }

    private void initShare() {
        RxQRCode.builder(ServiceAPI.APP_DOWN_LOAD_URL)
                .codeSide(800)
                .logoBitmap(R.mipmap.icon_app, getResources())
                .into(mImgQRcode);

        UserInfo info = MyAPP.getgUserInfo();
        MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg2);
        MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg3);


        RxTextUtils.getBuilder(info.getUserName() + "\n")
                .append(getString(R.string.appDays, getString(R.string.appName), info.getRegisterTime())).setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvUserName);
        mTvDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd"));

        RxTextUtils.getBuilder(getString(R.string.HealthSlim))
                .append(getString(R.string.appName) + "v" + RxDeviceUtils.getAppVersionName())
                .setProportion(0.5f)
                .into(mTvAppVersion);

    }

    private void initTextTypeface() {
        mTvContinuousRecord.setTypeface(MyAPP.typeface);
        mTvContinuousRecord2.setTypeface(MyAPP.typeface);
        mTvProgress.setTypeface(MyAPP.typeface);
        mTvProgress2.setTypeface(MyAPP.typeface);
        mTvWeightInfo.setTypeface(MyAPP.typeface);
        mTvHealthScore.setTypeface(MyAPP.typeface);
        mTvCurrentWeight.setTypeface(MyAPP.typeface);
        mTvCurrentDiet.setTypeface(MyAPP.typeface);
        mTvCurrentKcal.setTypeface(MyAPP.typeface);
        mTvCurrentEnergy.setTypeface(MyAPP.typeface);
    }

    private void initWareView() {
        mProComplete.setBorder(1, ContextCompat.getColor(mContext, R.color.GrayWrite));
        mProComplete.setWaveColor(Color.parseColor("#7DB8FFDC"), Color.parseColor("#FFB8FFDC"));
        mProComplete2.setBorder(1, ContextCompat.getColor(mContext, R.color.GrayWrite));
        mProComplete2.setWaveColor(Color.parseColor("#7DB8FFDC"), Color.parseColor("#FFB8FFDC"));
        mProComplete.setWaterLevelRatio(0f);
//        mProComplete.setWaveColor(Color.parseColor("#6ABEBD"), Color.parseColor("#B8FFDC"));
        mProComplete.setShowWave(true);

    }


    @Override
    protected void initRxBus() {
        RxBus.getInstance().register2(RefreshSlimming.class)
                .compose(RxComposeUtils.<RefreshSlimming>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<RefreshSlimming>() {
                    @Override
                    protected void _onNext(RefreshSlimming refreshSlimming) {
                        initNetData();
                    }
                });


    }


    @Override
    protected void initNetData() {
        super.initNetData();
        initUserInfo();
        getData();
    }


    private void initUserInfo() {
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = JSON.parseObject(string, UserInfo.class);
        MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg);
    }

    private void getData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().indexInfo(1, 7))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("indexInfo", String.class,
                        CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        SlimmingRecordBean bean = JSON.parseObject(s, SlimmingRecordBean.class);
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
        dietRecord(bean.getDataList());
        sportingRecord(bean.getDataList());
        energyRecord(bean.getDataList());
    }

    /**
     * 食材记录
     *
     * @param list
     */
    private void dietRecord(List<DataListBean> list) {
        Calendar calendar = Calendar.getInstance();
        float max = 1;
        recommendList.clear();
        dates.clear();
        item.clear();

        if (!RxDataUtils.isEmpty(list)) {
            calendar.setTimeInMillis(list.get(list.size() - 1).getRecordDate());
            int size = list.size();
            for (int i = 0; i < 7; i++) {
                if (i < size) {
                    DataListBean bean = list.get(i);
                    dates.add(RxFormat.setFormatDate(bean.getRecordDate(), "MM/dd"));
                    item.add((int) bean.getHeatCalorie());
                    max = (float) Math.max(max, bean.getHeatCalorie());
                    recommendList.add((int) bean.getDietPlan());
                    max = (float) Math.max(max, bean.getDietPlan());
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                    item.add(0);
                    recommendList.add(0);
                }
            }
        } else {
            RxTextUtils.getBuilder(0 + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentDiet);

            for (int i = 0; i < 7; i++) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                item.add(i, 0);
                recommendList.add(0);
            }
        }

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(item.get(0), 0))
                .append("\tkcal").setProportion(0.5f)
                .into(mTvCurrentDiet);

        Collections.reverse(recommendList);
        Collections.reverse(item);
        Collections.reverse(dates);


        lineNormalDiet.setBezierLine(true);
        lineNormalDiet.setDashPath(new DashPathEffect(new float[]{10, 5}, 5));
        lineNormalDiet.setColor(ContextCompat.getColor(mContext, R.color.Gray));
        lineNormalDiet.setTexts(new String[]{getString(R.string.proposal), getString(R.string.intake)});
        lineNormalDiet.setData(recommendList);
        lineNormalDiet.setMaxMinValue((int) max, 0);

        mTvDietChart7.setText(dates.get(6));
        mTvDietChart6.setText(dates.get(5));
        mTvDietChart5.setText(dates.get(4));
        mTvDietChart4.setText(dates.get(3));
        mTvDietChart3.setText(dates.get(2));
        mTvDietChart2.setText(dates.get(1));
        mTvDietChart1.setText(dates.get(0));

        mDietProgress6.setColor(item.get(5) > recommendList.get(5) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mDietProgress5.setColor(item.get(4) > recommendList.get(4) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mDietProgress4.setColor(item.get(3) > recommendList.get(3) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mDietProgress3.setColor(item.get(2) > recommendList.get(2) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mDietProgress2.setColor(item.get(1) > recommendList.get(1) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mDietProgress1.setColor(item.get(0) > recommendList.get(0) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));

        mDietProgress7.setProgress((int) (item.get(6) * 100 / max), false);
        mDietProgress6.setProgress((int) (item.get(5) * 100 / max), false);
        mDietProgress5.setProgress((int) (item.get(4) * 100 / max), false);
        mDietProgress4.setProgress((int) (item.get(3) * 100 / max), false);
        mDietProgress3.setProgress((int) (item.get(2) * 100 / max), false);
        mDietProgress2.setProgress((int) (item.get(1) * 100 / max), false);
        mDietProgress1.setProgress((int) (item.get(0) * 100 / max), false);
    }


    /**
     * 能量记录
     *
     * @param list
     */
    private void energyRecord(List<DataListBean> list) {
        Calendar calendar = Calendar.getInstance();
        float max = 1;
        dates.clear();
        item.clear();
        recommendList.clear();
        if (list != null && !list.isEmpty()) {
            calendar.setTimeInMillis(list.get(list.size() - 1).getRecordDate());
            int size = list.size();
            for (int i = 0; i < 7; i++) {
                if (i < size) {
                    DataListBean bean = list.get(i);
                    dates.add(RxFormat.setFormatDate(bean.getRecordDate(), "MM/dd"));
                    double value = EnergyUtil.energy(bean.getAthlCalorie(), bean.getHeatCalorie(), bean.getBasalCalorie());
                    max = (float) Math.max(max, Math.abs(value));
                    item.add((int) Math.abs(value));
                    double normalEnergy = EnergyUtil.energy(bean.getAthlPlan(), bean.getDietPlan(), bean.getBasalCalorie());

                    normalEnergy = normalEnergy < 0 ? 0 : normalEnergy;
                    recommendList.add((int) normalEnergy);
                    max = (float) Math.max(max, Math.abs(normalEnergy));
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                    item.add(0);
                    recommendList.add(0);
                }
            }
        } else {
            for (int i = 0; i < 7; i++) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                item.add(i, 0);
                recommendList.add(0);
            }
        }

        int value = item.get(0);
        mEnergyProgress7.setColor(ContextCompat.getColor(mContext, value < 0 ? R.color.red : R.color.orange_FF7200));
        mTvCurrentEnergy.setTextColor(ContextCompat.getColor(mContext, value < 0 ? R.color.red : R.color.orange_FF7200));
        mTvEnergyChart7.getHelper().setBackgroundColorNormal(ContextCompat.getColor(mContext, value < 0 ? R.color.red : R.color.orange_FF7200));
        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(Math.abs(value), 0))
                .append("\tkcal").setProportion(0.5f)
                .into(mTvCurrentEnergy);

        Collections.reverse(recommendList);
        Collections.reverse(item);
        Collections.reverse(dates);

        lineNormalEnergy.setBezierLine(true);
        lineNormalEnergy.setDashPath(new DashPathEffect(new float[]{10, 5}, 5));
        lineNormalEnergy.setColor(ContextCompat.getColor(mContext, R.color.Gray));
        lineNormalEnergy.setTexts(new String[]{getString(R.string.energy), getString(R.string.proposal)});
        lineNormalEnergy.setData(recommendList);
        lineNormalEnergy.setMaxMinValue((int) max, 0);


        mEnergyProgress7.setProgress((int) (item.get(6) * 100 / max), false);
        mEnergyProgress6.setProgress((int) (item.get(5) * 100 / max), false);
        mEnergyProgress5.setProgress((int) (item.get(4) * 100 / max), false);
        mEnergyProgress4.setProgress((int) (item.get(3) * 100 / max), false);
        mEnergyProgress3.setProgress((int) (item.get(2) * 100 / max), false);
        mEnergyProgress2.setProgress((int) (item.get(1) * 100 / max), false);
        mEnergyProgress1.setProgress((int) (item.get(0) * 100 / max), false);

        mEnergyProgress6.setColor(item.get(5) > recommendList.get(5) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mEnergyProgress5.setColor(item.get(4) > recommendList.get(4) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mEnergyProgress4.setColor(item.get(3) > recommendList.get(3) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mEnergyProgress3.setColor(item.get(2) > recommendList.get(2) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mEnergyProgress2.setColor(item.get(1) > recommendList.get(1) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mEnergyProgress1.setColor(item.get(0) > recommendList.get(0) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));

        mTvEnergyChart7.setText(dates.get(6));
        mTvEnergyChart6.setText(dates.get(5));
        mTvEnergyChart5.setText(dates.get(4));
        mTvEnergyChart4.setText(dates.get(3));
        mTvEnergyChart3.setText(dates.get(2));
        mTvEnergyChart2.setText(dates.get(1));
        mTvEnergyChart1.setText(dates.get(0));

    }

    /**
     * 运动记录
     *
     * @param list
     */
    private void sportingRecord(List<DataListBean> list) {
        Calendar calendar = Calendar.getInstance();
        float max = 1;
        dates.clear();
        item.clear();
        recommendList.clear();
        if (list != null && !list.isEmpty()) {
            calendar.setTimeInMillis(list.get(list.size() - 1).getRecordDate());
            int size = list.size();
            for (int i = 0; i < 7; i++) {
                if (i < size) {
                    DataListBean bean = list.get(i);
                    dates.add(RxFormat.setFormatDate(bean.getRecordDate(), "MM/dd"));
                    max = (float) Math.max(max, bean.getAthlCalorie());
                    item.add((int) bean.getAthlCalorie());
                    recommendList.add((int) bean.getAthlPlan());
                    max = (float) Math.max(max, bean.getAthlPlan());
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                    item.add(0);
                    recommendList.add(0);
                }
            }
        } else {
            for (int i = 0; i < 7; i++) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                item.add(i, 0);
                recommendList.add(0);
            }
        }

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(item.get(0), 0))
                .append("\tkcal").setProportion(0.5f)
                .into(mTvCurrentKcal);

        Collections.reverse(recommendList);
        Collections.reverse(item);
        Collections.reverse(dates);

        lineNormalSport.setBezierLine(true);
        lineNormalSport.setDashPath(new DashPathEffect(new float[]{20, 5}, 0));
        lineNormalSport.setColor(ContextCompat.getColor(mContext, R.color.Gray));
        lineNormalSport.setTexts(new String[]{getString(R.string.proposal), getString(R.string.consume)});
        lineNormalSport.setData(recommendList);
        lineNormalSport.setMaxMinValue((int) max, 0);


        mSportingProgress6.setColor(item.get(5) > recommendList.get(5) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mSportingProgress5.setColor(item.get(4) > recommendList.get(4) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mSportingProgress4.setColor(item.get(3) > recommendList.get(3) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mSportingProgress3.setColor(item.get(2) > recommendList.get(2) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mSportingProgress2.setColor(item.get(1) > recommendList.get(1) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));
        mSportingProgress1.setColor(item.get(0) > recommendList.get(0) ?
                ContextCompat.getColor(mContext, R.color.Gray) : ContextCompat.getColor(mContext, R.color.BrightGray));

        mSportingProgress7.setProgress((int) (item.get(6) * 100 / max), false);
        mSportingProgress6.setProgress((int) (item.get(5) * 100 / max), false);
        mSportingProgress5.setProgress((int) (item.get(4) * 100 / max), false);
        mSportingProgress4.setProgress((int) (item.get(3) * 100 / max), false);
        mSportingProgress3.setProgress((int) (item.get(2) * 100 / max), false);
        mSportingProgress2.setProgress((int) (item.get(1) * 100 / max), false);
        mSportingProgress1.setProgress((int) (item.get(0) * 100 / max), false);

        mTvSportingChart7.setText(dates.get(6));
        mTvSportingChart6.setText(dates.get(5));
        mTvSportingChart5.setText(dates.get(4));
        mTvSportingChart4.setText(dates.get(3));
        mTvSportingChart3.setText(dates.get(2));
        mTvSportingChart2.setText(dates.get(1));
        mTvSportingChart1.setText(dates.get(0));
    }


    /**
     * 体重记录
     *
     * @param bean
     */
    private void weightRecord(SlimmingRecordBean bean) {
        imgWeightSwitch.setOnClickListener(v -> {
                    if ((Boolean) imgWeightSwitch.getTag()) {
                        imgWeightSwitch.setTag(false);
                        setLineChartData(bean, false);
                        imgWeightSwitch.setImageResource(R.mipmap.ic_weight_close);
                    } else {
                        imgWeightSwitch.setImageResource(R.mipmap.ic_weight_open);
                        imgWeightSwitch.setTag(true);
                        setLineChartData(bean, true);
                    }
                }
        );
        imgWeightSwitch.setTag(true);
        setLineChartData(bean, true);
    }

    private void initChart(BarLineChartBase lineChartBase) {
        lineChartBase.setEnabled(false);
        lineChartBase.setTouchEnabled(false);//可以点击
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

    }

    private void setLineChartData(SlimmingRecordBean bean, boolean isWeight) {
        dates.clear();
        List<SlimmingRecordBean.WeightInfoListBean> list = bean.getWeightInfoList();
        Calendar calendar = Calendar.getInstance();
        ArrayList<Entry> lineEntry = new ArrayList<>(7);

        if (list != null && !list.isEmpty()) {
            calendar.setTimeInMillis(list.get(list.size() - 1).getWeightDate());
            int size = list.size();
            for (int i = 0; i < 7; i++) {
                if (i < size) {
                    double value = 0;
                    if (isWeight) {
                        value = list.get(i).getWeight();
                    } else
                        value = list.get(i).getBodyFat();
                    lineEntry.add(new Entry(6 - i, (float) value));
                    dates.add(RxFormat.setFormatDate(list.get(i).getWeightDate(), "MM/dd"));
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                    lineEntry.add(new Entry(6 - i, 0));
                }
            }
        } else {
            RxTextUtils.getBuilder(0 + "")
                    .append("\t" + (isWeight ? "kg" : "%")).setProportion(0.5f)
                    .into(mTvCurrentWeight);

            for (int i = 0; i < 7; i++) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                lineEntry.add(new Entry(6 - i, 0));
            }
        }

        RxTextUtils.getBuilder((String.format("%.1f", lineEntry.get(0).getY())))
                .append("\t" + (isWeight ? "kg" : "%")).setProportion(0.5f)
                .into(mTvCurrentWeight);

        int max = (int) Collections.max(lineEntry, (o1, o2) ->
                (int) (o1.getY() - o2.getY())).getY();
        if (max == 0) {
            YAxis yAxis = mMLineChart.getAxisLeft();
            yAxis.setAxisMaximum(10);
        }

        LineDataSet set = (LineDataSet) mMLineChart.getData().getDataSetByLabel("weight", false);
        if (set == null) {
            set = createSet();
            mMLineChart.getLineData().addDataSet(set);
        }
        Collections.reverse(lineEntry);
//        Logger.d("体重数据：" + lineEntry.size());
        set.setValues(lineEntry);

        mMLineChart.getData().notifyDataChanged();
        mMLineChart.notifyDataSetChanged();
        mMLineChart.invalidate();
        mMLineChart.animateX(500);

        mTvWeightChart1.setText(dates.get(6));
        mTvWeightChart2.setText(dates.get(5));
        mTvWeightChart3.setText(dates.get(4));
        mTvWeightChart4.setText(dates.get(3));
        mTvWeightChart5.setText(dates.get(2));
        mTvWeightChart6.setText(dates.get(1));
        mTvWeightChart7.setText(dates.get(0));
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "weight");
        set.setDrawIcons(false);
        set.setDrawValues(true);
        int[] colors = {R.color.BrightGray, R.color.BrightGray, R.color.BrightGray,
                R.color.BrightGray, R.color.BrightGray, R.color.BrightGray, R.color.green_61D97F};

        set.setValueTextColor(ContextCompat.getColor(mContext, R.color.Gray));
        set.setValueTextSize(8);
        set.setColor(ContextCompat.getColor(mContext, R.color.green_62D981));
        //渐变
        set.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_green);
        set.setFillDrawable(drawable);

        set.setDrawCircleHole(false);
        set.setDrawCircles(true);
        set.setCircleRadius(3f);
        set.setLineWidth(1f);
        set.setCircleColors(colors, mActivity);
        return set;
    }


    /**
     * 健康分数
     *
     * @param bean
     */
    private void healthScore(SlimmingRecordBean bean) {
        SlimmingRecordBean.WeightInfoBean weightInfo = bean.getWeightInfo();
        if (weightInfo == null) {
            RxTextUtils.getBuilder("--")
                    .append("\t" + getString(R.string.weightAndUnit) + "\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\tBMI\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\t" + getString(R.string.bodyFatAndUnit) + "\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\t" + getString(R.string.bmrAndUnit)).setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvWeightInfo);

            RxTextUtils.getBuilder("0.0")
                    .append("\t分").setProportion(0.5f)
                    .into(mTvHealthScore);


            RxTextUtils.getBuilder(getString(R.string.body) + "：\t").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append(getString(R.string.unknown))
                    .append("\t\t\t" + getString(R.string.riskOfLllness) + "：").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append(getString(R.string.unknown))
                    .into(mTvHealthInfo);

            mTvHealthDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), "yyyy/MM/dd"));
            return;
        }

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(weightInfo.getWeight(), 1))
                .append("\t" + getString(R.string.weightAndUnit) + "\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(RxFormatValue.fromat4S5R(weightInfo.getBmi(), 1))
                .append("\tBMI\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(RxFormatValue.fromat4S5R(weightInfo.getBodyFat(), 1))
                .append("\t" + getString(R.string.bodyFatAndUnit) + "\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.getBmr() + "")
                .append("\t" + getString(R.string.bmrAndUnit)).setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvWeightInfo);

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(weightInfo.getHealthScore(), 1))
                .append("\t分").setProportion(0.5f)
                .into(mTvHealthScore);

        RxTextUtils.getBuilder(getString(R.string.body) + "：\t").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.getBodyType())
                .append("\t\t\t" + getString(R.string.riskOfLllness) + "：").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(bean.getLevelDesc())
                .into(mTvHealthInfo);

        mHealthyLevel.switchLevel(bean.getSickLevel());
        mTvHealthDate.setText(RxFormat.setFormatDate(weightInfo.getWeightDate(), "yyyy/MM/dd"));
    }

    /**
     * 今日完成目标
     *
     * @param bean
     */
    private void slimmingTarget(SlimmingRecordBean bean) {
        mImgBreakfast.getHelper().setIconNormal(bean.isBreakfastDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgLunch.getHelper().setIconNormal(bean.isLunchDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgDinner.getHelper().setIconNormal(bean.isDinnerDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgSporting.getHelper().setIconNormal(bean.isAthlDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgWeigh.getHelper().setIconNormal(bean.isWeightDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));

        mImgBreakfast2.getHelper().setIconNormal(bean.isBreakfastDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgLunch2.getHelper().setIconNormal(bean.isLunchDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgDinner2.getHelper().setIconNormal(bean.isDinnerDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgSporting2.getHelper().setIconNormal(bean.isAthlDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgWeigh2.getHelper().setIconNormal(bean.isWeightDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));

        int complete = 0;
        if (bean.isBreakfastDone()) complete += 20;
        if (bean.isLunchDone()) complete += 20;
        if (bean.isDinnerDone()) complete += 20;
        if (bean.isAthlDone()) complete += 20;
        if (bean.isWeightDone()) complete += 20;

        mProComplete.setWaterLevelRatio(complete / 100f);
        mProComplete2.setWaterLevelRatio(complete / 100f);
//        mProComplete.setWaterLevelRatio(0.8f);
        if (complete / 100f > 0) {
            mWaveHelper = new RxWaveHelper(mProComplete);
            mWaveHelper.start();
        }
        RxTextUtils.getBuilder(complete + "")
                .append("\t%").setProportion(0.5f)
                .into(mTvProgress);
        RxTextUtils.getBuilder(complete + "")
                .append("\t%").setProportion(0.5f)
                .into(mTvProgress2);


        RxTextUtils.getBuilder(getString(R.string.totalRecord) + "\t")
                .append(bean.getTotalCompleteDays() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("\t天").into(mTvContinuousRecord);
        RxTextUtils.getBuilder(getString(R.string.totalRecord) + "\t")
                .append(bean.getTotalCompleteDays() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("\t天").into(mTvContinuousRecord2);

    }


    @Override
    protected void onInvisible() {
        super.onInvisible();
        if (mWaveHelper != null)
            mWaveHelper.cancel();
        //屏幕沉浸
        StatusBarUtils.from(this)
                .setStatusBarColor(ContextCompat.getColor(mContext, R.color.Gray))
                .setLightStatusBar(false)
                .process();
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        if (mWaveHelper != null)
            mWaveHelper.start();
        //屏幕沉浸
        StatusBarUtils.from(this)
                .setStatusBarColor(Color.parseColor("#ABCBE0"))
                .setLightStatusBar(false)
                .process();
    }


    @OnClick({
            R.id.img_email,
            R.id.layout_weight_title,
            R.id.layout_diet_title,
            R.id.layout_sports_title,
            R.id.layout_HealthReport,
            R.id.layout_energy_title})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check(view.getId())) return;
        switch (view.getId()) {
            case R.id.img_email:
                //分享
                share(true);
                break;
            case R.id.layout_weight_title:
                RxActivityUtils.skipActivity(mContext, WeightRecordFragment.class);
                break;
            case R.id.layout_diet_title:
                RxActivityUtils.skipActivity(mContext, DietRecordActivity.class);
                break;
            case R.id.layout_sports_title:
                RxActivityUtils.skipActivity(mContext, SmartClothingFragment.class);
                break;
            case R.id.layout_energy_title:
                RxActivityUtils.skipActivity(mContext, EnergyActivity.class);
                break;
            case R.id.layout_HealthReport:
                //判断是否审核通过
                UserInfo userInfo = MyAPP.getgUserInfo();
                if (userInfo.getPlanState() == 3) {
                    String url = ServiceAPI.SHARE_INFORM_URL + userInfo.getInformId() + "&sign=true";
                    PlanWebActivity.startActivity(mContext, url);
                }
                break;
        }
    }

    private void share(boolean isStart) {
        mLayoutShareHead.setVisibility(isStart ? View.VISIBLE : View.INVISIBLE);
        mLayoutShareTitle.setVisibility(isStart ? View.VISIBLE : View.GONE);

        if (isStart) {
            mProComplete2.setShowWave(true);
            //延迟500毫秒，需要等到控件显示
            mProComplete2.postDelayed(() -> {
                //控件转图片
                Bitmap bitmap = RxImageUtils.view2Bitmap(mPrant, ContextCompat.getColor(mContext, R.color.white));

                if (getActivity() instanceof BaseShareActivity) {
                    BaseShareActivity activity = (BaseShareActivity) getActivity();
                    if (activity != null)
                        activity.shareBitmap(bitmap);
                }
                share(false);
            }, 100);
        }
    }

}
