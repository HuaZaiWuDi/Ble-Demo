package lab.wesmartclothing.wefit.flyso.ui.main.record;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.smartclothing.blelibrary.BleTools;
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
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.roundprogressbar.RoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.VerticalProgress;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.entity.PlanBean;
import lab.wesmartclothing.wefit.flyso.entity.SportingDetailBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.MessageChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.NetWorkType;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MessageFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.UserInfofragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.RecipesActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.FoodDetailsFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.FoodRecommend;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.HeatDetailFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanDetailsActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanMatterActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanWebActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.PlanSportingActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SportingActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.SettingTargetFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.TargetDetailsFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightAddFragment;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
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

    private PlanBean bean;
    private HeartLineChartUtils lineChartUtils;
    private int lastKcal = 0;

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
        UserInfo info = JSON.parseObject(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);
        if (info != null) {
            mTvUserName.setText(info.getUserName());
            MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mIvUserImg);
        }
        getData();
    }


    boolean isFold = false;//是否折叠

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

        mScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= RxUtils.dp2px(32) && isFold) {
                    isFold = false;
                    //展开
                    RxAnimationUtils.animateHeight(RxUtils.dp2px(64), RxUtils.dp2px(95), mLayoutTitle);
                    mIvUserImg.animate().scaleX(1f).scaleY(1f).setDuration(500).start();
                    mImgSeeRecord.animate().scaleX(1f).scaleY(1f).setDuration(500).alpha(1f).start();
                    mImgSeeRecord.setEnabled(true);
                } else if (scrollY > RxUtils.dp2px(32) && !isFold) {
                    isFold = true;
                    //收缩
                    RxAnimationUtils.animateHeight(RxUtils.dp2px(95), RxUtils.dp2px(64), mLayoutTitle);
                    mIvUserImg.animate().scaleX(0.45f).scaleY(0.45f).setDuration(500).start();
                    mImgSeeRecord.animate().scaleX(0f).scaleY(0f).setDuration(500).alpha(0f).start();
                    mImgSeeRecord.setEnabled(false);
                }
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
        //只有在显示时才会网络请求
        RxBus.getInstance().register2(NetWorkType.class)
                .compose(RxComposeUtils.<NetWorkType>bindLifeResume(lifecycleSubject))
                .subscribe(new RxSubscriber<NetWorkType>() {
                    @Override
                    protected void _onNext(NetWorkType netWorkType) {
                        if (netWorkType.isBoolean())
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


        RxBus.getInstance().register2(SportsDataTab.class)
                .compose(RxComposeUtils.<SportsDataTab>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<SportsDataTab>() {
                    @Override
                    protected void _onNext(SportsDataTab sportsDataTab) {
                        if (BleService.clothingFinish && isVisibled()) {
                            BleService.clothingFinish = false;

                            if (bean != null && !RxDataUtils.isEmpty(bean.getAthlPlanList())) {
                                RxDialogSureCancel rxDialog = new RxDialogSureCancel(RxActivityUtils.currentActivity())
                                        .setTitle("运动提示")
                                        .setContent("瘦身衣已连接，是否立即开始运动？")
                                        .setCancel("自由运动")
                                        .setCancelListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), SportingActivity.class);
                                            }
                                        })
                                        .setSure("课程运动")
                                        .setSureListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString(Key.BUNDLE_SPORTING_PLAN, JSON.toJSONString(bean));
                                                RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), PlanSportingActivity.class, bundle);
                                            }
                                        });
                                rxDialog.show();
                            } else {
                                RxDialogSureCancel rxDialog = new RxDialogSureCancel(RxActivityUtils.currentActivity())
                                        .setTitle("运动提示")
                                        .setContent("瘦身衣已连接，是否开始自由运动？")
                                        .setSure("进入")
                                        .setSureListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), SportingActivity.class);
                                            }
                                        });
                                rxDialog.show();
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
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("planIndex", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
//                       bean=MyAPP.getGson().fromJson(s,PlanBean.class);
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
                .append("本次运动时间")
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvSportingTime);

        RxTextUtils.getBuilder(bean.getTotalDeplete() + "\t")
                .append("预计消耗热量(kcal)").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvSportingKcal);
    }

    private void getCurrentRealHeart() {
        JsonObject object = new JsonObject();
        object.addProperty("athlDate", System.currentTimeMillis());
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .courseAthlDetail(NetManager.fetchRequest(object.toString())))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("courseAthlDetail", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        List<SportingDetailBean.RealAthlListBean> realAthlList = JSON.parseObject(s, new TypeToken<List<SportingDetailBean.RealAthlListBean>>() {
                        }.getType());
                        List<Integer> realLists = new ArrayList<>();
                        for (SportingDetailBean.RealAthlListBean bean : realAthlList) {
                            realLists.add(bean.getHeartRate());
                        }
                        lineChartUtils.setRealTimeData(realLists);
                        mLineChart.animateX(1000);
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

        SPUtils.put(SPKey.SP_realWeight, (float) weight);

        if (weight > minNormalWeight && weight <= maxNormalWeight) {
            mTvWeightStatus.setText("标准");
            mTvWeightStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
            mTvWeightStatus.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.green_61D97F))
                    .setBorderWidthNormal(RxUtils.dp2px(1));
        } else if (weight <= minNormalWeight) {
            mTvWeightStatus.setText("偏瘦");
            mTvWeightStatus.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
            mTvWeightStatus.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.blue))
                    .setBorderWidthNormal(RxUtils.dp2px(1));
        } else if (weight > maxNormalWeight) {
            mTvWeightStatus.setText("偏胖");
            mTvWeightStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_FF7200));
            mTvWeightStatus.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.orange_FF7200))
                    .setBorderWidthNormal(RxUtils.dp2px(1));
        }

        RxTextUtils.getBuilder(weight + "")
                .append(" 体重(kg)").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.5f).into(mTvWeight);

        RxTextUtils.getBuilder(weightChangeVO.getWeight().getBodyFat() + "")
                .append(" 体脂率(%)").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.6f)
                .into(mTvBodyFat);

        RxTextUtils.getBuilder(weightChangeVO.getWeight().getBmi() + "")
                .append(" BMI").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.6f)
                .into(mTvBmi);

        RxTextUtils.getBuilder(weightChangeVO.getWeight().getBmr() + "")
                .append(" 基础代谢(kcal)").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
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

        RxTextUtils.getBuilder(heatInfoVO.getAbleIntake() < 0 ? "多摄入热量\n" : "还可摄入热量\n")
                .append(Math.abs(heatInfoVO.getAbleIntake()) + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kcal").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvIngestionHeat);


        if (warning && !TextSpeakUtils.isSpeak() && lastKcal != heatInfoVO.getAbleIntake()) {
            lastKcal = heatInfoVO.getAbleIntake();
            TextSpeakUtils.speakFlush("主人你吃的太多啦，今天不要再吃了");
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
        mMCountDownView.setCountDownFinishCallBack(new CountDownView.CountDownFinishCallBack() {
            @Override
            public void finish() {
                RxLogUtils.d("目标结束");
                targetComplete(true, bean.getComplete() == 1);
            }
        });
        if (System.currentTimeMillis() < bean.getTargetInfo().getTargetDate()) {
            targetComplete(bean.getComplete() == 1, bean.getComplete() == 1);
        } else {
            targetComplete(true, bean.getComplete() == 1);
        }

        mMCountDownView.setCountDownDays(bean.getTargetInfo().getTargetDate());

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(bean.getWeightChangeVO().getWeight().getWeight(), 1))
                .append("\tkg").setProportion(0.5f).into(mTvCurrentWeight);

        RxTextUtils.getBuilder("起始体重\n")
                .append(bean.getTargetInfo().getInitialWeight() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvInitWeight);

        RxTextUtils.getBuilder("目标体重\n")
                .append(bean.getTargetInfo().getTargetWeight() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvTargetWeight);

        if (bean.getComplete() < 0) {
            mProTarget.setProgressColor(ContextCompat.getColor(mContext, R.color.red));
            mProTarget.setProgress(5);
        } else {
            mProTarget.setProgressColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
            mProTarget.setProgress((float) (bean.getComplete() * 100));
        }


    }

    private void targetComplete(boolean finish, boolean complete) {
        mLayoutTargetTitle.setAlpha(finish ? 0.3f : 1f);
        mLayoutTargetContent.setAlpha(finish ? 0.3f : 1f);
        mResetTarget.setEnabled(!finish);
        mLayoutTargetComplete.setVisibility(finish ? View.VISIBLE : View.GONE);
        mTvTargetComplete.setText(complete ? "恭喜您已达成瘦身目标！" : "很遗憾，您的瘦身目标未达成！");
        mTvResetTarget.setText(complete ? "开启新目标" : "重置目标");
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
        } else if (planState == 3) {
            mImgPlanMark.setVisibility(View.GONE);
            mImgRecipes.setVisibility(View.VISIBLE);
            mImgSeeRecord.setVisibility(View.VISIBLE);
            mLayoutSlimmingTarget.setVisibility(View.VISIBLE);
        } else {
            mLayoutSlimmingTarget.setVisibility(View.VISIBLE);
            mImgPlanMark.setVisibility(View.GONE);
            mImgSeeRecord.setVisibility(View.VISIBLE);
        }

        //瘦身衣状态
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
            mLayoutClothingDefault.setVisibility(View.VISIBLE);
            mCardFreeSporting.setVisibility(View.GONE);
            mCardCurriculumSporting.setVisibility(View.GONE);
            mBtnGoBindClothing.setText(R.string.goBind);
            mTvClothingTip.setText("请绑定您的燃脂瘦身衣");
        } else if (planState != 3) {
            mLayoutClothingDefault.setVisibility(View.VISIBLE);
            mCardFreeSporting.setVisibility(View.GONE);
            mCardCurriculumSporting.setVisibility(View.GONE);
            mBtnGoBindClothing.setText("去运动");
            mTvClothingTip.setText("今天还没运动哦~");
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
            mTvWeightTip.setText("请绑定您的体脂称");
        } else if (!bean.isHasTodayWeight()) {//去称重
            mLayoutScaleDefault.setVisibility(View.VISIBLE);
            mBtnGoBindScale.setText("去称重");
            mTvWeightTip.setText("今天还没有称重哦～");
        } else {
            mLayoutScaleDefault.setVisibility(View.GONE);
        }
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
            R.id.layout_foodRecord
    })
    public void onViewClicked(View view) {
        RxLogUtils.d("点击");
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_userImg:
                RxActivityUtils.skipActivity(mContext, UserInfofragment.class);
                break;
            case R.id.layout_notify:
                RxActivityUtils.skipActivity(mContext, MessageFragment.class);
                break;
            case R.id.img_seeRecord:
                if (bean.getPlanState() == 3) {
                    bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.SHARE_INFORM_URL + SPUtils.getString(SPKey.SP_UserId) + "&sign=true");
                    RxActivityUtils.skipActivity(mContext, PlanWebActivity.class, bundle);
                } else if (bean.getPlanState() != 0) {
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
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_BREAKFAST);
                RxActivityUtils.skipActivity(mActivity, FoodDetailsFragment.class, bundle);
                break;
            case R.id.layout_lunch:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_LUNCH);
                RxActivityUtils.skipActivity(mActivity, FoodDetailsFragment.class, bundle);
                break;
            case R.id.layout_dinner:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_DINNER);
                RxActivityUtils.skipActivity(mActivity, FoodDetailsFragment.class, bundle);
                break;
            case R.id.layout_meal:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPED_MEAL);
                RxActivityUtils.skipActivity(mActivity, FoodDetailsFragment.class, bundle);
                break;
            case R.id.reWeigh:
                if (!BleTools.getBleManager().isBlueEnable()) {
                    showOpenBlueTooth();
                } else {
                    RxActivityUtils.skipActivity(mContext, WeightAddFragment.class);
                }
                break;
            case R.id.btn_goBind_scale:
                if (!BleTools.getBleManager().isBlueEnable()) {
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
                    if (!BleTools.getBleManager().isBlueEnable()) {
                        showOpenBlueTooth();
                    } else if (!BleTools.getInstance().isConnect()) {
                        tipDialog.showInfo("您还未连接瘦身衣", 1500);
                        mActivity.startService(new Intent(mContext, BleService.class));
                    } else {
                        RxActivityUtils.skipActivity(mContext, SportingActivity.class);
                    }
                }
                break;
            case R.id.tv_freeSporting:
                //进入实时运动界面，定制课程
                if (!BleTools.getBleManager().isBlueEnable()) {
                    showOpenBlueTooth();
                } else if (!BleTools.getInstance().isConnect()) {
                    tipDialog.showInfo("您还未连接瘦身衣", 1500);
                    mActivity.startService(new Intent(mContext, BleService.class));
                } else {
                    //进入实时运动界面，没有定制课程
                    RxActivityUtils.skipActivity(mContext, SportingActivity.class);
                }
                break;
            case R.id.tv_curriculumSporting:
                //进入实时运动界面，定制课程
                if (!BleTools.getBleManager().isBlueEnable()) {
                    showOpenBlueTooth();
                } else if (!BleTools.getInstance().isConnect()) {
                    tipDialog.showInfo("您还未连接瘦身衣", 1500);
                    mActivity.startService(new Intent(mContext, BleService.class));
                } else {
                    if (!RxDataUtils.isEmpty(bean.getAthlPlanList())) {
                        bundle.putString(Key.BUNDLE_SPORTING_PLAN, JSON.toJSONString(bean));
                        RxActivityUtils.skipActivity(mContext, PlanSportingActivity.class, bundle);
                    } else {
                        bundle.putInt(Key.BUNDLE_PLAN_STATUS, bean.getPlanState());
                        RxActivityUtils.skipActivity(mContext, PlanActivity.class, bundle);
                    }
                }
                break;
            case R.id.layout_gotoPlanSporting:
                //进入实时运动界面，定制课程
                if (!BleTools.getBleManager().isBlueEnable()) {
                    showOpenBlueTooth();
                } else if (!BleTools.getInstance().isConnect()) {
                    tipDialog.showInfo("您还未连接瘦身衣", 1500);
                    mActivity.startService(new Intent(mContext, BleService.class));
                } else {
                    if (!RxDataUtils.isEmpty(bean.getAthlPlanList())) {
                        bundle.putString(Key.BUNDLE_SPORTING_PLAN, JSON.toJSONString(bean));
                        RxActivityUtils.skipActivity(mContext, PlanSportingActivity.class, bundle);
                    } else {
                        showFreeSportDialog();
                    }
                }
                break;
            case R.id.img_weight_tip:
                new RxDialogSure(mContext)
                        .setTitle("今日体重")
                        .setContent("您今日的体重数据，变化趋势是与您首次称重数据进行对比")
                        .show();
                break;
            case R.id.img_sporting_tip:
                new RxDialogSure(mContext)
                        .setTitle("今日能量")
                        .setContent("您今日的能量摄入与消耗，综合消耗是通过摄入、运动消耗、基础代谢综合计算所得")
                        .show();
                break;
            case R.id.layout_foodRecord:
                RxActivityUtils.skipActivity(mContext, FoodRecommend.class);
                break;
            case R.id.tv_resetTarget:
                bundle.putDouble(Key.BUNDLE_TARGET_WEIGHT, bean.getTargetInfo().getTargetWeight());
                bundle.putDouble(Key.BUNDLE_INITIAL_WEIGHT, bean.getWeightChangeVO().getWeight().getWeight());
                RxActivityUtils.skipActivity(mContext, SettingTargetFragment.class, bundle);
                break;
        }
    }


    private void showFreeSportDialog() {
        RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setContent("您还未制定课程，去自由运动试试吧")
                .setSure("自由运动")
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进入实时运动界面，没有定制课程
                        RxActivityUtils.skipActivity(mContext, SportingActivity.class);
                    }
                });
        rxDialog.show();
    }


    private void showOpenBlueTooth() {
        RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setTitle("提示")
                .setContent("请先开启蓝牙，并允许新连接")
                .setSure("开启")
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipDialog.show("正在开启蓝牙", 3000);
                        BleTools.getBleManager().enableBluetooth();
                    }
                });
        rxDialog.show();
    }


    private void initPermissions() {
        new RxPermissions(mActivity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            new RxDialogSureCancel(mContext)
                                    .setTitle("提示")
                                    .setContent("不定位权限，手机将无法连接蓝牙")
                                    .setSure("去开启")
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
