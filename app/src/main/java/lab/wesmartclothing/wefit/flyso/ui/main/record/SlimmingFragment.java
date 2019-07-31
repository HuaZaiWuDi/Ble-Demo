package lab.wesmartclothing.wefit.flyso.ui.main.record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxAnimationUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.chart.bar.BarGroupChart;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.roundprogressbar.RoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.VerticalProgress;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.MyBleManager;
import lab.wesmartclothing.wefit.flyso.chat.ChatManager;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateItemBean;
import lab.wesmartclothing.wefit.flyso.entity.PlanBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.HeartRateChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.MessageChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MessageFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.UserInfofragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.FoodDetailsFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.FoodRecommend;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.RecipesActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanDetailsActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanMatterActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanWebActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.RecordInfoActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.PlanSportingActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SportingActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.TargetDetailsFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightAddFragment;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.Number2Chinese;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;
import lab.wesmartclothing.wefit.flyso.view.CountDownView;

/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.main.record
 * @FileName SlimmingFragment
 * @Date 2018/10/22 16:50
 * @Author JACK
 * @Describe TODO记录界面
 * @Project Android_WeFit_2.0
 */
public class SlimmingFragment extends BaseAcFragment {


    @BindView(R.id.iv_userImg)
    RxImageView mIvUserImg;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.iv_notify)
    ImageView mIvNotify;
    @BindView(R.id.layout_notify)
    LinearLayout mLayoutNotify;
    @BindView(R.id.img_seeRecord)
    RxTextView mImgSeeRecord;
    @BindView(R.id.img_planMark)
    RxImageView mImgPlanMark;
    @BindView(R.id.mCountDownView)
    CountDownView mMCountDownView;
    @BindView(R.id.resetTarget)
    RxTextView mResetTarget;
    @BindView(R.id.tv_weight_title)
    TextView mTvWeightTitle;
    @BindView(R.id.tv_currentWeight)
    TextView mTvCurrentWeight;
    @BindView(R.id.tv_initWeight)
    TextView mTvInitWeight;
    @BindView(R.id.pro_target)
    RxRoundProgressBar mProTarget;
    @BindView(R.id.tv_targetWeight)
    TextView mTvTargetWeight;
    @BindView(R.id.layout_gotoRecipes)
    RelativeLayout mImgRecipes;
    @BindView(R.id.tv_IngestionHeat)
    TextView mTvIngestionHeat;
    @BindView(R.id.circleProgressBar)
    RoundProgressBar mCircleProgressBar;
    @BindView(R.id.line_vertical)
    View mLineVertical;
    @BindView(R.id.breakfastProgress)
    VerticalProgress mBreakfastProgress;
    @BindView(R.id.lunchProgress)
    VerticalProgress mLunchProgress;
    @BindView(R.id.dinnerProgress)
    VerticalProgress mDinnerProgress;
    @BindView(R.id.mealProgress)
    VerticalProgress mMealProgress;
    @BindView(R.id.layout_breakfast)
    CardView mLayoutBreakfast;
    @BindView(R.id.layout_lunch)
    CardView mLayoutLunch;
    @BindView(R.id.layout_dinner)
    CardView mLayoutDinner;
    @BindView(R.id.layout_meal)
    CardView mLayoutMeal;
    @BindView(R.id.layout_heat)
    LinearLayout mLayoutHeat;
    @BindView(R.id.layout_sportTitle)
    LinearLayout mLayoutSportTitle;
    @BindView(R.id.lineChart)
    LineChart mLineChart;
    @BindView(R.id.tv_sportingTime)
    TextView mTvSportingTime;
    @BindView(R.id.tv_sportingKcal)
    TextView mTvSportingKcal;
    @BindView(R.id.tv_freeSporting)
    RxTextView mTvFreeSporting;
    @BindView(R.id.tv_curriculumSporting)
    RxTextView mTvCurriculumSporting;
    @BindView(R.id.layout_sporting)
    RelativeLayout mLayoutSporting;
    @BindView(R.id.layout_weightTitle)
    LinearLayout mLayoutWeightTitle;
    @BindView(R.id.tv_weight)
    TextView mTvWeight;
    @BindView(R.id.tv_weight_status)
    RxTextView mTvWeightStatus;
    @BindView(R.id.tv_bodyFat)
    TextView mTvBodyFat;
    @BindView(R.id.tv_bmi)
    TextView mTvBmi;
    @BindView(R.id.tv_bmr)
    TextView mTvBmr;
    @BindView(R.id.reWeigh)
    RxTextView mReWeigh;
    @BindView(R.id.layout_weight)
    RelativeLayout mLayoutWeight;
    @BindView(R.id.layout_energyTitle)
    LinearLayout mLayoutEnergyTitle;
    @BindView(R.id.pro_diet_plan)
    BarGroupChart mProDietPlan;
    @BindView(R.id.pro_diet_real)
    BarGroupChart mProDietReal;
    @BindView(R.id.pro_sport_plan)
    BarGroupChart mProSportPlan;
    @BindView(R.id.pro_sport_real)
    BarGroupChart mProSportReal;
    @BindView(R.id.pro_heat_plan)
    BarGroupChart mProHeatPlan;
    @BindView(R.id.pro_heat_real)
    BarGroupChart mProHeatReal;
    @BindView(R.id.barChart)
    LinearLayout mBarChart;
    @BindView(R.id.line_chart)
    View mLineView;
    @BindView(R.id.layout_energy)
    LinearLayout mLayoutEnergy;
    @BindView(R.id.tv_clothing_tip)
    TextView mTvClothingTip;
    @BindView(R.id.btn_goBind_clothing)
    RxTextView mBtnGoBindClothing;
    @BindView(R.id.layout_clothing_default)
    LinearLayout mLayoutClothingDefault;
    @BindView(R.id.tv_weight_tip)
    TextView mTvWeightTip;
    @BindView(R.id.btn_goBind_scale)
    RxTextView mBtnGoBindScale;
    @BindView(R.id.layout_scale_default)
    LinearLayout mLayoutScaleDefault;
    @BindView(R.id.layout_Legend)
    LinearLayout mLayoutLegend;
    @BindView(R.id.card_freeSporting)
    CardView mCardFreeSporting;
    @BindView(R.id.card_curriculumSporting)
    CardView mCardCurriculumSporting;
    @BindView(R.id.layout_title)
    RelativeLayout mLayoutTitle;
    @BindView(R.id.layout_lineChart)
    RelativeLayout mLayoutLineChart;
    @BindView(R.id.scroll)
    NestedScrollView mScroll;
    @BindView(R.id.tv_Nutrition)
    TextView mTvNutrition;
    @BindView(R.id.tv_Bodybuilding)
    TextView mTvBodybuilding;
    @BindView(R.id.layout_gotoPlanSporting)
    RelativeLayout mLayoutGotoPlanSporting;
    @BindView(R.id.img_weight_tip)
    ImageView mImgWeightTip;
    @BindView(R.id.img_sporting_tip)
    ImageView mImgSportingTip;
    @BindView(R.id.layout_targetTitle)
    RelativeLayout mLayoutTargetTitle;
    @BindView(R.id.layout_targetContent)
    RelativeLayout mLayoutTargetContent;
    @BindView(R.id.tv_targetComplete)
    TextView mTvTargetComplete;
    @BindView(R.id.tv_resetTarget)
    RxTextView mTvResetTarget;
    @BindView(R.id.layout_targetComplete)
    LinearLayout mLayoutTargetComplete;
    @BindView(R.id.layout_slimmingTarget)
    RelativeLayout mLayoutSlimmingTarget;
    @BindView(R.id.img_weightFlag)
    ImageView ImgWeightFlag;
    @BindView(R.id.iv_kefu)
    ImageView mIvKefu;

    private PlanBean bean;
    private HeartLineChartUtils lineChartUtils;
    private int lastKcal = 0;
    private boolean isFold = false;//是否折叠
    private static boolean firstShowDialog = true;

    public static SlimmingFragment newInstance() {
        Bundle args = new Bundle();
        SlimmingFragment fragment = new SlimmingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int layoutId() {
        return R.layout.fragment_slimming;
    }

    @Override
    protected void initNetData() {
        super.initNetData();
        UserInfo info = MyAPP.getgUserInfo();
        mTvUserName.setText(info.getUserName());
        MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mIvUserImg);
        getData();
    }


    @Override
    protected void initViews() {
        super.initViews();
        initPermissions();

        mTvCurrentWeight.setTypeface(MyAPP.typeface);
        mTvInitWeight.setTypeface(MyAPP.typeface);
        mTvTargetWeight.setTypeface(MyAPP.typeface);
        mTvIngestionHeat.setTypeface(MyAPP.typeface);
        mTvSportingTime.setTypeface(MyAPP.typeface);
        mTvSportingKcal.setTypeface(MyAPP.typeface);
        mTvWeight.setTypeface(MyAPP.typeface);
        mTvBodyFat.setTypeface(MyAPP.typeface);
        mTvBmi.setTypeface(MyAPP.typeface);
        mTvBmr.setTypeface(MyAPP.typeface);
        lineChartUtils = new HeartLineChartUtils(mLineChart);
        lineChartUtils.setPlanLineColor(Color.parseColor("#E4CA9F"), Color.parseColor("#312C35"));


        //版本：只有智享瘦有这个版本
        if (BuildConfig.Wesmart) {
            mIvKefu.setVisibility(View.VISIBLE);
        } else {
            mIvKefu.setVisibility(View.GONE);
        }

        mScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY <= RxUtils.dp2px(32) && isFold) {
                isFold = false;
                //展开
                RxAnimationUtils.animateHeight(RxUtils.dp2px(64), RxUtils.dp2px(95), mLayoutTitle);
                mIvUserImg.animate().scaleX(1f).scaleY(1f).setDuration(500).start();
                mImgSeeRecord.animate().scaleX(1f).scaleY(1f).setDuration(500).alpha(1f).start();
                mImgSeeRecord.setEnabled(true);
                if (BuildConfig.Wesmart)
                    mIvKefu.animate().translationY(0).translationX(0)
                            .setDuration(500).start();
            } else if (scrollY > RxUtils.dp2px(32) && !isFold) {
                isFold = true;
                //收缩
                RxAnimationUtils.animateHeight(RxUtils.dp2px(95), RxUtils.dp2px(64), mLayoutTitle);
                mIvUserImg.animate().scaleX(0.45f).scaleY(0.45f).setDuration(500).start();
                mImgSeeRecord.animate().scaleX(0f).scaleY(0f).setDuration(500).alpha(0f).start();
                mImgSeeRecord.setEnabled(false);
                if (BuildConfig.Wesmart)
                    mIvKefu.animate().translationY(-RxUtils.dp2px(15)).translationX(RxUtils.dp2px(100))
                            .setDuration(500).start();
            }
        });
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

        //消息通知
        RxBus.getInstance().register2(MessageChangeBus.class)
                .compose(RxComposeUtils.<MessageChangeBus>bindLifeResume(lifecycleSubject))
                .subscribe(new RxSubscriber<MessageChangeBus>() {
                    @Override
                    protected void _onNext(MessageChangeBus messageChangeBus) {
                        mIvNotify.setBackgroundResource(R.mipmap.icon_email_white);
                    }
                });


        //设备连接
        RxBus.getInstance().register2(HeartRateChangeBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<HeartRateChangeBus>() {
                    @SuppressLint("CheckResult")
                    @Override
                    protected void _onNext(HeartRateChangeBus sportsDataTab) {
                        if (BleService.clothingFinish && isVisibled()) {
                            BleService.clothingFinish = false;

                            String key = HeartRateUtil.getTodayData();
                            if (!RxDataUtils.isNullString(key)) {
                                RxCache.getDefault().<HeartRateBean>load(key, HeartRateBean.class)
                                        .map(new CacheResult.MapFunc<>())
                                        .subscribe(bean -> {
                                            int planFlag = bean.getPlanFlag();
                                            showSkipSportAcDialog(key, planFlag == 1);
                                        }, e -> {
                                            RxLogUtils.e("上一次的运动数据", e);
                                            showSkipSportAcDialog("", bean != null && !RxDataUtils.isEmpty(bean.getAthlPlanList()));
                                        });
                            } else {
                                showSkipSportAcDialog("", bean != null && !RxDataUtils.isEmpty(bean.getAthlPlanList()));
                            }
                        }
                    }
                });
    }


    ///////////////////////////////////////////////////////////////////////////
    // 接口
    ///////////////////////////////////////////////////////////////////////////
    private void getData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().planIndex())
                .compose(RxCache.getDefault().<String>transformObservable("planIndex", String.class,
                        CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        bean = JSON.parseObject(s, PlanBean.class);
                        updateUI();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


    private void updateUI() {
        if (bean == null) return;
        switchPlanStatus(bean.getPlanState());
        toDayWeight(bean.getWeightChangeVO());
        slimmingTarget();
        toDayHeat(bean.getHeatInfoVO());
        toDaySporting(bean.getAthlPlanList());
        toDayEnergy(bean.getTodayHeatVO());
        notifyData();
    }


    /**
     * 今日运动
     *
     * @param athlPlanList
     */
    private void toDaySporting(List<AthlPlanListBean> athlPlanList) {
        if (athlPlanList == null || athlPlanList.isEmpty()) {
            mLayoutLineChart.setBackgroundResource(R.mipmap.bg_custom_plam);
            return;
        } else {
            mLayoutLineChart.setBackground(null);
        }

        int sunTime = 0;
        for (AthlPlanListBean bean : athlPlanList) {
            sunTime += bean.getDuration();
            bean.setTime(sunTime);
            RxLogUtils.d("计划:" + bean.toString());
        }

        lineChartUtils.setPlanLineData(athlPlanList);
        getCurrentRealHeart();
        RxTextUtils.getBuilder(RxFormat.setSec2MS(bean.getTotalTime() * 60))
                .append(getString(R.string.thisRunTime))
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvSportingTime);

        RxTextUtils.getBuilder(bean.getTotalDeplete() + "\t")
                .append(getString(R.string.expectCalorie)).setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvSportingKcal);
    }

    private void getCurrentRealHeart() {
        JsonObject object = new JsonObject();
        object.addProperty("athlDate", System.currentTimeMillis());
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .courseAthlDetail(NetManager.fetchRequest(object.toString())))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("courseAthlDetail", String.class,
                        CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        List<HeartRateItemBean> realAthlList = JSON.parseObject(s, new TypeToken<List<HeartRateItemBean>>() {
                        }.getType());
                        List<Integer> realLists = new ArrayList<>();
                        for (HeartRateItemBean bean : realAthlList) {
                            realLists.add(HeartRateUtil.reversePace(bean.getPace()));
                        }
                        lineChartUtils.setRealTimeData(realLists);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });

    }


    /**
     * 消息通知
     */
    private void notifyData() {
        mIvNotify.setBackgroundResource(bean.getUnreadCount() == 0 ? R.mipmap.icon_email_white : R.mipmap.icon_email_white_mark);
        SPUtils.put(SPKey.SP_DIET_PLAN_USER, bean.getDietPlanBelongUser());
        SPUtils.put(SPKey.SP_ATHL_PLAN_USER, bean.getAthlPlanBelongUser());

        mTvNutrition.setText(getString(R.string.dietPlanUser, SPUtils.getString(SPKey.SP_DIET_PLAN_USER, "")));
        mTvBodybuilding.setText(getString(R.string.athlPlanUser, SPUtils.getString(SPKey.SP_ATHL_PLAN_USER, "")));

    }

    /**
     * 今日能量比例
     *
     * @param todayHeatVO
     */
    private void toDayEnergy(PlanBean.TodayHeatVOBean todayHeatVO) {
        if (todayHeatVO == null) return;
        int maxValue = checkMaxValue(todayHeatVO);
        RxLogUtils.d("最大值：" + maxValue);
        //饮食摄入预计
        mProDietPlan.setTopValue(todayHeatVO.getPlanIntake());
        mProDietPlan.setProgress(todayHeatVO.getPlanIntake() * 100 / maxValue, false);

        //饮食摄入实际
        mProDietReal.setTopValue(todayHeatVO.getRealIntake());
        mProDietReal.setProgress(todayHeatVO.getRealIntake() * 100 / maxValue, false);

        //运动预计
        mProSportPlan.setTopValue(todayHeatVO.getPlanDeplete());
        mProSportPlan.setProgress(todayHeatVO.getPlanDeplete() * 100 / maxValue, false);

        //运动实际
        mProSportReal.setTopValue(todayHeatVO.getRealDeplete());
        mProSportReal.setProgress(todayHeatVO.getRealDeplete() * 100 / maxValue, false);

        //盈余预计
        mProHeatPlan.setTopValue(todayHeatVO.getPlanSurplus());
        mProHeatPlan.setProgress(todayHeatVO.getPlanSurplus() * 100 / maxValue, false);

        //盈余热量：运动+基础代谢-摄入，正值为良好，负值为盈余
        //盈余实际
        mProHeatReal.setTopValue(Math.abs(todayHeatVO.getRealSurplus()));
        mProHeatReal.setColor(ContextCompat.getColor(mContext, todayHeatVO.getRealSurplus() < 0 ? R.color.red : R.color.yellow_FFBC00));
        mProHeatReal.setProgress(Math.abs(todayHeatVO.getRealSurplus()) * 100 / maxValue, false);
    }

    private int checkMaxValue(PlanBean.TodayHeatVOBean todayHeatVO) {
        List<Integer> heat = new ArrayList<>();
        heat.add(todayHeatVO.getPlanDeplete());
        heat.add(todayHeatVO.getRealDeplete());
        heat.add(todayHeatVO.getPlanSurplus());
        heat.add(todayHeatVO.getRealSurplus());
        heat.add(todayHeatVO.getPlanIntake());
        heat.add(todayHeatVO.getRealIntake());

        return Collections.max(heat) == 0 ? 1 : Collections.max(heat);
    }

    /**
     * 今日体重
     *
     * @param weightChangeVO
     */
    private void toDayWeight(PlanBean.WeightChangeVOBean weightChangeVO) {
        if (bean.getWeightChangeVO() == null || bean.getWeightChangeVO().getWeight() == null)
            return;
        double weight = RxFormatValue.format4S5R(weightChangeVO.getWeight().getWeight(), 1);
        boolean hasInitialWeight = bean.getWeightChangeVO().isHasInitialWeight();
        double maxNormalWeight = RxFormatValue.format4S5R(bean.getMaxNormalWeight(), 1);
        double minNormalWeight = RxFormatValue.format4S5R(bean.getMinNormalWeight(), 1);

        if (weight > minNormalWeight && weight <= maxNormalWeight) {
            mTvWeightStatus.setText(R.string.normal);
            mTvWeightStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
            mTvWeightStatus.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.green_61D97F))
                    .setBorderWidthNormal(RxUtils.dp2px(1));
        } else if (weight <= minNormalWeight) {
            mTvWeightStatus.setText(R.string.slim);
            mTvWeightStatus.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
            mTvWeightStatus.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.blue))
                    .setBorderWidthNormal(RxUtils.dp2px(1));
        } else if (weight > maxNormalWeight) {
            mTvWeightStatus.setText(R.string.fatter);
            mTvWeightStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_FF7200));
            mTvWeightStatus.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.orange_FF7200))
                    .setBorderWidthNormal(RxUtils.dp2px(1));
        }

        RxTextUtils.getBuilder(weight + "")
                .append("\t" + getString(R.string.weightAndUnit)).setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.5f).into(mTvWeight);

        RxTextUtils.getBuilder(weightChangeVO.getWeight().getBodyFat() + "")
                .append("\t" + getString(R.string.bodyFatAndUnit)).setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.6f)
                .into(mTvBodyFat);

        RxTextUtils.getBuilder(weightChangeVO.getWeight().getBmi() + "")
                .append("\tBMI").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.6f)
                .into(mTvBmi);

        RxTextUtils.getBuilder(weightChangeVO.getWeight().getBmr() + "")
                .append("\t" + getString(R.string.bmrAndUnit)).setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.6f)
                .into(mTvBmr);


        if (hasInitialWeight) {
            Drawable drawable1 = null;
            if (weightChangeVO.getWeightChange() > 0) {
                drawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_red_up);
            } else if (weightChangeVO.getWeightChange() < 0) {
                drawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_green_down);
            }
            mTvWeight.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable1, null);


            Drawable drawable2 = null;
            if (weightChangeVO.getBodyFatChange() > 0) {
                drawable2 = ContextCompat.getDrawable(mContext, R.mipmap.icon_red_up);
            } else if (weightChangeVO.getBodyFatChange() < 0) {
                drawable2 = ContextCompat.getDrawable(mContext, R.mipmap.icon_green_down);
            }
            mTvBodyFat.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable2, null);


            Drawable drawable3 = null;
            if (weightChangeVO.getBmiChange() > 0) {
                drawable3 = ContextCompat.getDrawable(mContext, R.mipmap.icon_red_up);
            } else if (weightChangeVO.getBmiChange() < 0) {
                drawable3 = ContextCompat.getDrawable(mContext, R.mipmap.icon_green_down);
            }
            mTvBmi.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable3, null);


            Drawable drawable4 = null;
            if (weightChangeVO.getBmrChange() > 0) {
                drawable4 = ContextCompat.getDrawable(mContext, R.mipmap.icon_red_up);
            } else if (weightChangeVO.getBmrChange() < 0) {
                drawable4 = ContextCompat.getDrawable(mContext, R.mipmap.icon_green_down);
            }
            mTvBmr.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable4, null);

        }

    }


    /**
     * 今日热量
     * <p>
     * 早、午、晚、加餐能量占每日建议摄入能量比：3:4:2:1；
     * <p>
     * 能量条颜色变化以 UI 设计为
     * 准，规则为摄入热量超过建议摄
     * 入热量 80%颜色开始变化；超过
     * 的摄入热量，能量条充满；
     * 能量条高度：每餐已摄入热量占
     * 每餐建议摄入热量比*能量条完
     * 整高度
     *
     * @param heatInfoVO
     */
    private void toDayHeat(PlanBean.HeatInfoVOBean heatInfoVO) {
        if (bean.getHeatInfoVO() == null) return;
        boolean warning = heatInfoVO.isWarning();
        int basalCalorie = heatInfoVO.getBasalCalorie();

        RxTextUtils.getBuilder(heatInfoVO.getAbleIntake() < 0 ? getString(R.string.calorieIntakeTooMuch) : getString(R.string.calorieAbleIntake))
                .append("\n" + Math.abs(heatInfoVO.getAbleIntake())).setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kcal").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvIngestionHeat);

        //已审核才会进行语音播报
        if (bean.getPlanState() == 3 && lastKcal != heatInfoVO.getAbleIntake()) {
            lastKcal = heatInfoVO.getAbleIntake();
            if (warning) {
                TextSpeakUtils.speakAdd(getString(R.string.speak_eatTooMuch));
            } else {
                TextSpeakUtils.speakAdd(getString(R.string.speak_ableIntake, Number2Chinese.number2Chinese(lastKcal + "")));
            }
        }

        mCircleProgressBar.setProgressColor(ContextCompat.getColor(mContext, warning ? R.color.red : R.color.orange_FF7200));
        mCircleProgressBar.setProgress((int) (heatInfoVO.getIntakePercent() * 100));

        int proBreakfast = (int) (heatInfoVO.getBreakfast().getCalorie() * 100 / (basalCalorie * 0.3f));
        int proLunch = (int) (heatInfoVO.getLunch().getCalorie() * 100 / (basalCalorie * 0.4f));
        int proDinner = (int) (heatInfoVO.getDinner().getCalorie() * 100 / (basalCalorie * 0.2f));
        int proMeal = (int) (heatInfoVO.getSnacks().getCalorie() * 100 / (basalCalorie * 0.1f));

        mBreakfastProgress.setProgressColor(ContextCompat.getColor(mContext, proBreakfast > 80 ? R.color.red : R.color.orange_FF7200));
        mBreakfastProgress.setProgress(proBreakfast);
        mLunchProgress.setProgressColor(ContextCompat.getColor(mContext, proLunch > 80 ? R.color.red : R.color.orange_FF7200));
        mLunchProgress.setProgress(proLunch);
        mDinnerProgress.setProgressColor(ContextCompat.getColor(mContext, proDinner > 80 ? R.color.red : R.color.orange_FF7200));
        mDinnerProgress.setProgress(proDinner);
        mMealProgress.setProgressColor(ContextCompat.getColor(mContext, proMeal > 80 ? R.color.red : R.color.orange_FF7200));
        mMealProgress.setProgress(proMeal);

    }


    /**
     * 瘦身目标
     */
    private void slimmingTarget() {
        if (bean.getTargetInfo() == null
                || bean.getWeightChangeVO() == null
                || bean.getWeightChangeVO().getWeight() == null)
            return;
        mMCountDownView.setCountDownFinishCallBack(() -> targetComplete(true, bean.getComplete() == 1));
        if (System.currentTimeMillis() >= bean.getTargetInfo().getTargetDate() || bean.getComplete() == 1) {
            targetComplete(true, bean.getComplete() == 1);
        } else {
            targetComplete(false, bean.getComplete() == 1);
            mMCountDownView.setCountDownDays(bean.getTargetInfo().getTargetDate());
        }


        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(bean.getWeightChangeVO().getWeight().getWeight(), 1))
                .append("\tkg").setProportion(0.5f).into(mTvCurrentWeight);

        RxTextUtils.getBuilder(getString(R.string.initWeight) + "\n")
                .append(bean.getTargetInfo().getInitialWeight() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvInitWeight);

        RxTextUtils.getBuilder(getString(R.string.targetWeight) + "\n")
                .append(bean.getTargetInfo().getTargetWeight() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvTargetWeight);

        if (bean.getComplete() < 0) {
            mProTarget.setProgressColor(ContextCompat.getColor(mContext, R.color.red));
            mProTarget.setProgress(5);
        } else {
            ImgWeightFlag.setTranslationX((float) (RxUtils.dp2px(230) * bean.getComplete()));
            mProTarget.setProgressColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
            mProTarget.setProgress((float) (bean.getComplete() * 100));
        }

    }


    private void targetComplete(boolean finish, boolean complete) {
        mLayoutTargetComplete.setVisibility(finish ? View.VISIBLE : View.GONE);
        mLayoutTargetTitle.setAlpha(finish ? 0.3f : 1f);
        mLayoutTargetContent.setAlpha(finish ? 0.3f : 1f);
        mResetTarget.setEnabled(!finish);
        mTvTargetComplete.setText(complete ? getString(R.string.completePlanTarget) : getString(R.string.incompletePlanTarget));
        mTvResetTarget.setText(complete ? getString(R.string.openNewTarget) : getString(R.string.resetTarget));
    }


    /**
     * 0-未参加，1-待分配，2-待审核，3-已审核
     *
     * @param planState
     */
    private void switchPlanStatus(int planState) {
        if (planState == 0) {
            mImgPlanMark.setVisibility(View.VISIBLE);
            mImgSeeRecord.setVisibility(View.GONE);
            mLayoutSlimmingTarget.setVisibility(View.GONE);
            mImgRecipes.setVisibility(View.GONE);
            firstUsedTip();
        } else if (planState == 3) {

            mImgPlanMark.setVisibility(View.GONE);
            mImgRecipes.setVisibility(View.VISIBLE);
            mImgSeeRecord.setVisibility(View.VISIBLE);
            mLayoutSlimmingTarget.setVisibility(View.VISIBLE);
        } else {

            mLayoutSlimmingTarget.setVisibility(View.GONE);
            mImgPlanMark.setVisibility(View.GONE);
            mImgSeeRecord.setVisibility(View.VISIBLE);
        }

        //瘦身衣状态
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
            mLayoutClothingDefault.setVisibility(View.VISIBLE);
            mCardFreeSporting.setVisibility(View.GONE);
            mCardCurriculumSporting.setVisibility(View.GONE);
            mBtnGoBindClothing.setText(R.string.goBind);
            mTvClothingTip.setText(R.string.bindClothing);
        } else if (planState != 3) {
            mLayoutClothingDefault.setVisibility(View.VISIBLE);
            mCardFreeSporting.setVisibility(View.GONE);
            mCardCurriculumSporting.setVisibility(View.GONE);
            mBtnGoBindClothing.setText(R.string.goSporting);
            mTvClothingTip.setText(R.string.todayNotPlan);
            mBtnGoBindClothing.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.orange_FF7200));
        } else {
            mLayoutClothingDefault.setVisibility(View.GONE);
            mCardFreeSporting.setVisibility(View.VISIBLE);
            mCardCurriculumSporting.setVisibility(View.VISIBLE);
        }

        //体脂称状态
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
            mLayoutScaleDefault.setVisibility(View.VISIBLE);
            mBtnGoBindScale.setText(R.string.goBind);
            mTvWeightTip.setText(R.string.bindScale);
        } else if (!bean.isHasTodayWeight()) {//去称重
            mLayoutScaleDefault.setVisibility(View.VISIBLE);
            mBtnGoBindScale.setText(R.string.goWeigh);
            mTvWeightTip.setText(R.string.todayNotWeigh);
        } else {
            mLayoutScaleDefault.setVisibility(View.GONE);
        }
    }

    private void firstUsedTip() {
        if (firstShowDialog) {
            firstShowDialog = false;
        } else
            return;
        RxDialog firsrUsedDialog = new RxDialog(mContext);
        View view = View.inflate(mContext, R.layout.dialog_first_tip, null);
        firsrUsedDialog.setContentView(view);
        firsrUsedDialog.show();
        view.<TextView>findViewById(R.id.tv_tip).setText(getString(R.string.tv_firstTip, getString(R.string.appName)));
        view.findViewById(R.id.tv_start)
                .setOnClickListener(view1 -> {
                    firsrUsedDialog.dismiss();
                    RxActivityUtils.skipActivity(mContext, PlanMatterActivity.class);
                });
    }


    @OnClick({
            R.id.iv_userImg,
            R.id.layout_notify,
            R.id.img_seeRecord,
            R.id.img_planMark,
            R.id.resetTarget,
            R.id.layout_breakfast,
            R.id.layout_lunch,
            R.id.layout_dinner,
            R.id.layout_meal,
            R.id.tv_freeSporting,
            R.id.tv_curriculumSporting,
            R.id.reWeigh,
            R.id.btn_goBind_scale,
            R.id.btn_goBind_clothing,
            R.id.layout_gotoPlanSporting,
            R.id.img_weight_tip,
            R.id.img_sporting_tip,
            R.id.img_Recipes,
            R.id.tv_resetTarget,
            R.id.layout_foodRecord,
            R.id.iv_kefu
    })
    public void onViewClicked(View view) {
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.iv_userImg:
                RxActivityUtils.skipActivity(mContext, UserInfofragment.class);
                break;
            case R.id.layout_notify:
                RxActivityUtils.skipActivity(mContext, MessageFragment.class);
                break;
            case R.id.img_seeRecord:
                if (bean.getPlanState() == 3) {
                    String url = ServiceAPI.SHARE_INFORM_URL + bean.getInformGid() + "&sign=true";
                    PlanWebActivity.startActivity(mContext, url);
                } else if (bean.getPlanState() != 0) {
                    bundle = new Bundle();
                    bundle.putInt(Key.BUNDLE_PLAN_STATUS, bean.getPlanState());
                    RxActivityUtils.skipActivity(mContext, PlanDetailsActivity.class, bundle);
                }
                break;
            case R.id.img_planMark:
                RxActivityUtils.skipActivity(mContext, PlanMatterActivity.class);
                break;
            case R.id.resetTarget:
                RxActivityUtils.skipActivity(mContext, TargetDetailsFragment.class);
                break;
            case R.id.img_Recipes:
                RxActivityUtils.skipActivity(mContext, RecipesActivity.class);
                break;
            case R.id.layout_breakfast:
                FoodDetailsFragment.start(mContext, Key.TYPE_BREAKFAST, System.currentTimeMillis(), true);
                break;
            case R.id.layout_lunch:
                FoodDetailsFragment.start(mContext, Key.TYPE_LUNCH, System.currentTimeMillis(), true);
                break;
            case R.id.layout_dinner:
                FoodDetailsFragment.start(mContext, Key.TYPE_DINNER, System.currentTimeMillis(), true);
                break;
            case R.id.layout_meal:
                FoodDetailsFragment.start(mContext, Key.TYPED_MEAL, System.currentTimeMillis(), true);
                break;
            case R.id.reWeigh:
                if (!MyBleManager.Companion.getInstance().isBLEEnabled()) {
                    showOpenBlueTooth();
                } else {
                    RxActivityUtils.skipActivity(mContext, WeightAddFragment.class);
                }
                break;
            case R.id.btn_goBind_scale:
                if (!MyBleManager.Companion.getInstance().isBLEEnabled()) {
                    showOpenBlueTooth();
                } else {
                    if (getString(R.string.goBind).equals(mBtnGoBindScale.getText().toString())) {
                        RxActivityUtils.skipActivity(mContext, AddDeviceActivity.class);
                    } else {
                        RxActivityUtils.skipActivity(mContext, WeightAddFragment.class);
                    }
                }
                break;
            case R.id.btn_goBind_clothing:
                if (getString(R.string.goBind).equals(mBtnGoBindClothing.getText().toString())) {
                    RxActivityUtils.skipActivity(mContext, AddDeviceActivity.class);
                } else {
                    skipSportAc(false, "");
                }
                break;
            case R.id.tv_freeSporting:
                skipSportAc(false, "");
                break;
            case R.id.tv_curriculumSporting:
                skipSportAc(true, "");
                break;
            case R.id.layout_gotoPlanSporting:
                skipSportAc(true, "");
                break;
            case R.id.img_weight_tip:
                new RxDialogSure(mContext)
                        .setTitle(getString(R.string.todayWeight))
                        .setContent(getString(R.string.todayWeightDetail))
                        .show();
                break;
            case R.id.img_sporting_tip:
                new RxDialogSure(mContext)
                        .setTitle(getString(R.string.todayEnergy))
                        .setContent(getString(R.string.todayEnergyDatail))
                        .show();
                break;
            case R.id.layout_foodRecord:
                FoodRecommend.start(mContext, System.currentTimeMillis());
                break;
            case R.id.tv_resetTarget:
                //2019-3-26更改，重置目标计划，重走健康报告流程
                RxActivityUtils.skipActivity(mActivity, RecordInfoActivity.class);

                break;
            case R.id.iv_kefu:
                ChatManager.INSTANCE.register();
                break;
        }
    }

    //跳转运动界面钱的判断
    private void skipSportAc(boolean isPlan, String key) {
        //进入实时运动界面，定制课程
        if (!MyBleManager.Companion.getInstance().isBLEEnabled()) {
            showOpenBlueTooth();
        } else if (!MyBleManager.Companion.getInstance().isConnect()) {
            tipDialog.show(getString(R.string.connecting), 3000);
            MyBleManager.Companion.getInstance().scanMacAddress();
            if (tipDialog.getTipDialog() != null) {
                tipDialog.getTipDialog().setDialogLifeCycleListener(new DialogLifeCycleListener() {
                    @Override
                    public void onCreate(Dialog alertDialog) {

                    }

                    @Override
                    public void onShow(Dialog alertDialog) {

                    }

                    @Override
                    public void onDismiss() {
                        if (MyBleManager.Companion.getInstance().isConnect()) {
                            if (isPlan) {
                                if (!RxDataUtils.isEmpty(bean.getAthlPlanList())) {
                                    PlanSportingActivity.start(mContext, bean, key);
                                } else {
                                    showFreeSportDialog();
                                }
                            } else {
                                //进入实时运动界面，没有定制课程
                                SportingActivity.start(mContext, key);
                            }
                        }
                    }
                });
            }
        } else {
            if (isPlan) {
                if (!RxDataUtils.isEmpty(bean.getAthlPlanList())) {
                    PlanSportingActivity.start(mContext, bean, key);
                } else {
                    showFreeSportDialog();
                }
            } else {
                //进入实时运动界面，没有定制课程
                SportingActivity.start(mContext, key);
            }
        }
    }


    private void showSkipSportAcDialog(String key, boolean isPlan) {
        if (!"".equals(key)) {
            RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                    .setTitle(getString(R.string.tip))
                    .setContent(getString(R.string.continueLastRun))
                    .setSure(getString(R.string.enter))
                    .setSureListener(v -> {
                        if (isPlan) {
                            PlanSportingActivity.start(mContext, bean, key);
                        } else
                            SportingActivity.start(mContext, key);
                    });
            rxDialog.show();
        } else {
            if (isPlan) {
                RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                        .setTitle(getString(R.string.tip))
                        .setContent(getString(R.string.deviceConnectAndRun))
                        .setCancel(getString(R.string.freeRun))
                        .setCancelListener(v -> {
                            SportingActivity.start(mContext, key);
                        })
                        .setSure(getString(R.string.planRun))
                        .setSureListener(v -> {
                            PlanSportingActivity.start(mContext, bean, key);
                        });
                rxDialog.show();
            } else {
                RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                        .setTitle(getString(R.string.tip))
                        .setContent(getString(R.string.deviceConnectAndRun))
                        .setSure(getString(R.string.enter))
                        .setSureListener(v -> {
                            SportingActivity.start(mContext, key);
                        });
                rxDialog.show();
            }
        }
    }


    private void showFreeSportDialog() {
        RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                .setTitle(getString(R.string.tip))
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setContent(getString(R.string.pleaseFreeRun))
                .setSure(getString(R.string.freeRun))
                .setSureListener(v -> {
                    //进入实时运动界面，没有定制课程
                    RxActivityUtils.skipActivity(mContext, SportingActivity.class);
                });
        rxDialog.show();
    }


    private void showOpenBlueTooth() {
        RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setTitle(getString(R.string.tip))
                .setContent(getString(R.string.open_BLE))
                .setSure(getString(R.string.open))
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyBleManager.Companion.getInstance().enableBLE();
                    }
                });
        rxDialog.show();
    }


    private void initPermissions() {
        new RxPermissions((FragmentActivity) mActivity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            new RxDialogSureCancel(mContext)
                                    .setTitle(getString(R.string.tip))
                                    .setContent(getString(R.string.open_loaction))
                                    .setSure(getString(R.string.open))
                                    .setSureListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initPermissions();
                                        }
                                    }).show();
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
