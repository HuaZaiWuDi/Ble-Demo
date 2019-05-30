package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxConstUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.dateUtils.RxTimeUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.SwitchView;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.roundprogressbar.RoundProgressBar;
import com.vondear.rxtools.view.tooltips.RxToolTip;
import com.vondear.rxtools.view.tooltips.RxToolTipsManager;
import com.zchu.rxcache.CacheTarget;
import com.zchu.rxcache.RxCache;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.util.ScreenUtils;
import io.reactivex.schedulers.Schedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.base.SportInterface;
import lab.wesmartclothing.wefit.flyso.ble.BleAPI;
import lab.wesmartclothing.wefit.flyso.ble.BleTools;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.PlanBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.ClothingConnectBus;
import lab.wesmartclothing.wefit.flyso.rxbus.HeartRateChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.HeartSectionUtil;
import lab.wesmartclothing.wefit.flyso.utils.Number2Chinese;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;

import static no.nordicsemi.android.dfu.DfuBaseService.NOTIFICATION_ID;

/**
 * Created by jk on 2018/7/19.
 */
public class PlanSportingActivity extends BaseActivity implements SportInterface {


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
    @BindView(R.id.tv_heartCount)
    TextView mTvHeartCount;
    @BindView(R.id.chart_HeartRate)
    LineChart mChartHeartRate;
    @BindView(R.id.layout_sporting)
    RxRelativeLayout mLayoutSporting;
    @BindView(R.id.tv_avHeartRate)
    TextView mTvAvHeartRate;
    @BindView(R.id.tv_maxHeartRate)
    TextView mTvMaxHeartRate;
    @BindView(R.id.tv_sportsTime)
    TextView mTvSportsTime;
    @BindView(R.id.tv_expectKcal)
    TextView mTvExpectKcal;
    @BindView(R.id.layout_sportingTime)
    LinearLayout mLayoutSportingTime;
    @BindView(R.id.tv_sportskcal)
    TextView mTvSportskcal;
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;
    @BindView(R.id.tv_currentTime)
    TextView mTvCurrentTime;
    @BindView(R.id.layout_sportingKcal)
    LinearLayout mLayoutcurrentKcal;
    @BindView(R.id.layout_legend)
    RelativeLayout mLayoutLegend;
    @BindView(R.id.circleProgressBar)
    RoundProgressBar mCircleProgressBar;
    @BindView(R.id.tv_finish)
    RxTextView mTvFinish;
    @BindView(R.id.tv_playOrPause)
    RxTextView mTvPlayOrPause;
    @BindView(R.id.parent)
    RelativeLayout mParent;
    @BindView(R.id.sw_heat)
    SwitchView mSwHeat;
    @BindView(R.id.tipWeigh)
    TextView mTipWeigh;

    private Button btn_Connect;
    private int currentTime = 0, stepSpeed = 0, type = -1, reversePace;//运动上一次状态
    private long stopTime = 0;
    private HeartLineChartUtils lineChartUtils;
    private List<AthlPlanListBean> planList;
    private double sportingScore = 0, currentKcal = 0, totalSum, kilometre;
    private HeartRateBean mHeartRateBean = new HeartRateBean();
    private RxDialogSureCancel connectFailDialog;
    private RxDialogSure sportingShortDialog;
    private boolean pause = false;
    private int sportUpWarnCount = 0;
    private int sportDownWarnCount = 0;
    private HeartRateUtil mHeartRateUtil = new HeartRateUtil();
    private int kilometreFlag = 0;

    @Override
    protected int layoutId() {
        return R.layout.activity_sportsing;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        initSwitch();
        initTypeface();
        finishAnim();
        lineChartUtils = new HeartLineChartUtils(mChartHeartRate);

        if (SPUtils.getFloat(SPKey.SP_realWeight, 0) == 0) {
            new RxDialogSure(mContext)
                    .setTitle(getString(R.string.sportTip))
                    .setContent("请使用体脂称记录体重信息，以便准确记算运动消耗的卡路里")
                    .setSure(getString(R.string.ok))
                    .setSureListener(v -> {
                        RxActivityUtils.finishActivity();
                    }).show();
        } else {
            mTipWeigh.setVisibility(View.GONE);
        }


    }

    /**
     * 关闭所有弹窗
     */
    private void dismissAllDialog() {
        if (connectFailDialog != null && connectFailDialog.isShowing())
            connectFailDialog.dismiss();
        if (sportingShortDialog != null && sportingShortDialog.isShowing())
            sportingShortDialog.dismiss();
    }

    /**
     * 暂停或继续运动
     */
    private void startOrPauseSport() {
        Drawable drawable = null;
        if (pause) {
            mQMUIAppBarLayout.setTitle(R.string.speech_sportPause);
            mTvPlayOrPause.setText(R.string.play);
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_pause);
        } else {
            mQMUIAppBarLayout.setTitle(R.string.planSporting);
            mTvPlayOrPause.setText(R.string.pause);
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_play);
        }
        mTvPlayOrPause.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

        if (pause) {
            timer.stopTimer();
        } else {
            timer.startTimer();
        }

        startAnim(pause);
    }


    private void startAnim(boolean isOpen) {
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) mTvPlayOrPause.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) mTvFinish.getLayoutParams();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(isOpen ? 0 : 1, isOpen ? 1 : 0);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            //150-105
            //150-195
            float animatedValue = (float) valueAnimator1.getAnimatedValue();
            layoutParams1.leftMargin = (int) ((RxUtils.dp2px(150) - animatedValue * RxUtils.dp2px(45)));
            layoutParams2.leftMargin = (int) ((RxUtils.dp2px(150) + animatedValue * RxUtils.dp2px(45)));

            mTvPlayOrPause.setLayoutParams(layoutParams1);
            mTvFinish.setLayoutParams(layoutParams2);
        });
        valueAnimator.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void finishAnim() {
        RoundProgressBar.TOTAL_DURATION = 2000;
        RxToolTipsManager toolTipsManager = new RxToolTipsManager();
        mTvFinish.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTvFinish.setScaleX(1.3f);
                    mTvFinish.setScaleY(1.3f);
                    mCircleProgressBar.setProgress(2000);
                    mCircleProgressBar.setVisibility(View.VISIBLE);
                    toolTipsManager.clear();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mCircleProgressBar.getProgress() != 2000) {
                        RxToolTip.Builder builder = new RxToolTip.Builder(mContext, mTvFinish, mParent, "长按结束", RxToolTip.POSITION_ABOVE);
                        builder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                        builder.setTextColor(ContextCompat.getColor(mContext, R.color.Gray));
                        builder.setGravity(RxToolTip.GRAVITY_CENTER);
                        builder.setOffsetX(5);
                        toolTipsManager.show(builder.build(), 1000);
                    } else {
                        finishSporting();
                    }
                    mCircleProgressBar.setProgress(0);
                    mCircleProgressBar.setVisibility(View.GONE);
                    mTvFinish.setScaleX(1f);
                    mTvFinish.setScaleY(1f);
                    break;
                default:
            }
            return true;
        });
    }


    @Override
    protected void initNetData() {
        super.initNetData();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!pause) {
            if (stopTime > 0) {
                RxLogUtils.d("运动返回前台");
                currentTime += RxTimeUtils.getIntervalTime(System.currentTimeMillis(), stopTime, RxConstUtils.TimeUnit.SEC);
            }
            timer.startTimer();
        }
    }

    /**
     * 防止应用后台休眠定时器失效的问题，
     * 在退出后台或者息屏时暂停定时器，然后在界面展示时重启定时器并加上耗时
     */
    @Override
    protected void onStop() {
        super.onStop();
        RxLogUtils.d("运动退回后台");
        Intent intent = new Intent(this, BleService.class);
        intent.putExtra("APP_BACKGROUND", true);

        // Android 8.0使用startForegroundService在前台启动新服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        if (!pause) {
            stopTime = System.currentTimeMillis();
            timer.stopTimer();
        }
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        RxLogUtils.e("计划：" + bundle.getString(Key.BUNDLE_SPORTING_PLAN));
        PlanBean planBean = JSON.parseObject(bundle.getString(Key.BUNDLE_SPORTING_PLAN), PlanBean.class);
        planList = planBean.getAthlPlanList();
        int sunTime = 0;
        for (AthlPlanListBean bean : planList) {
            sunTime += bean.getDuration();
            bean.setTime(sunTime);
            RxLogUtils.d("计划:" + bean.toString());
        }
        mTvEndTime.setText(RxFormat.setS2MS(sunTime * 60));
        mLayoutSportingTime.setVisibility(View.GONE);
        mLayoutcurrentKcal.setVisibility(View.VISIBLE);
        lineChartUtils.setPlanLineData(planList);

        RxTextUtils.getBuilder("0.0")
                .append("\t分").setProportion(0.3f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvKcal);

        RxTextUtils.getBuilder("0")
                .append("kcal").setProportion(0.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvSportskcal);

        mTvExpectKcal.setText(getString(R.string.expectKcal, sunTime, planBean.getTotalDeplete()));

        mTvHeartCount.setText("0/" + planList.size());

        speakAdd(getString(R.string.speech_planStart,
                Number2Chinese.number2Chinese(sunTime + ""),
                Number2Chinese.number2Chinese(planBean.getTotalDeplete() + "")
        ));

    }


    @SuppressLint("CheckResult")
    @Override
    protected void initRxBus2() {
        super.initRxBus2();
        RxBus.getInstance().register2(HeartRateChangeBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<HeartRateChangeBus>() {
                    @Override
                    protected void _onNext(HeartRateChangeBus heartRateData) {

                        if (pause) return;
                        SportsDataTab sportsDataTab = mHeartRateUtil.addRealTimeData(heartRateData.heartRateData);
                        if (sportsDataTab == null) {
                            mTvAvHeartRate.setText("--");
                            return;
                        }
                        //配速
                        stepSpeed = sportsDataTab.getStepSpeed();

                        if (stepSpeed == 0) {
                            mTvAvHeartRate.setText("--");
                            return;
                        }

                        reversePace = sportsDataTab.getReversePace();
                        kilometre = sportsDataTab.getKilometre();
                        currentKcal = RxFormatValue.format4S5R(sportsDataTab.getKcal(), 1);

                        mTvAvHeartRate.setText(RxFormat.setSec2MS(stepSpeed));

                        mTvMaxHeartRate.setText(RxFormatValue.fromat4S5R(sportsDataTab.getKilometre(), 2));

                        RxTextUtils.getBuilder(currentKcal + "")
                                .append("\tkcal").setProportion(0.5f)
                                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                                .into(mTvSportskcal);

                        lineChartUtils.setRealTimeData(reversePace);
                        freeTextSpeak(reversePace);
                        sportingScore(reversePace);
                        saveData(sportsDataTab);

                        //每km播报一次
                        if (kilometreFlag != (int) kilometre) {
                            kilometreFlag = (int) kilometre;
                            speakAdd(getString(R.string.Speech_each8Min,
                                    Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(kilometre, 2)),
                                    Number2Chinese.number2Chinese((int) currentKcal + ""),
                                    Number2Chinese.number2Chinese(stepSpeed / 60 + "") + "分钟"
                                            + Number2Chinese.number2Chinese(stepSpeed % 60 + "") + "秒",
                                    Number2Chinese.number2Chinese(currentTime / 60 + "") + "分钟" +
                                            (currentTime % 60 == 0 ? "" : Number2Chinese.number2Chinese(currentTime % 60 + "") + "秒"),
                                    Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(sportingScore, 1))
                            ));
                        }
                    }
                });

        RxBus.getInstance().registerSticky(ClothingConnectBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(clothingConnect -> {
                    boolean state = clothingConnect.isConnect();
                    btn_Connect.setText(state ? R.string.connected : R.string.connecting);
                    if (state) {
                        dismissAllDialog();
                    } else
                        trySporting();
                    pause = !state;
                    startOrPauseSport();
                });
    }

    /**
     * 实时保存数据到本地，保证异常情况下，数据不会丢失
     *
     * @param sportsDataTab
     */
    private void saveData(SportsDataTab sportsDataTab) {
        mHeartRateBean.setAthlDesc(mTvHeartCount.getText().toString());
        mHeartRateBean.setPlanFlag(1);
        mHeartRateBean.setAthlScore(sportingScore);
        mHeartRateBean.setTotalCalorie(sportsDataTab.getKcal());
        mHeartRateBean.setHeartList(sportsDataTab.getHeartLists());
        mHeartRateBean.setAvgPace(sportsDataTab.getAvPace());
        mHeartRateBean.setMaxPace(sportsDataTab.getMaxPace());
        mHeartRateBean.setMinPace(sportsDataTab.getMinPace());
        mHeartRateBean.setStepNumber(sportsDataTab.getSteps());
        mHeartRateBean.setKilometers(sportsDataTab.getKilometre());
        if (currentTime != 0)
            mHeartRateBean.setCadence(sportsDataTab.getSteps() * 1f / currentTime);

        //如果上传失败则保存本地
        RxCache.getDefault().save(Key.CACHE_ATHL_RECORD_PLAN, mHeartRateBean, CacheTarget.Disk)
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                    }
                });
    }


    /**
     * 全程语音按照课程的每个层级时间和频率进行指导，语音播报逻辑：
     * 1.进入课程实时运动页面（检测到配速）：
     * 运动模式已启动，本次运动时间XXX分钟，开始运动，READY GO！
     * 第一节 xx运动，请调节配速至xx运动区间，保持匀速xxx分钟。
     * 恭喜您完成xx训练，下一节xx运动，请调节配速至xx运动区间，保持匀速xxx分钟。
     * 当完成后提示：
     * 好棒呀，恭喜您完成本次瘦身训练。运动完记得做一组拉伸运动哦！本次训练共计燃烧XXX千卡，再接再厉。
     * 当用户处于极限配速超过3分钟
     * 您当前运动量已超过身体最大极限，请立刻降低运动强度，防止意外损伤！
     * 当处于训练过程中，运动节奏高于（低于）当前要求的水平
     * 请提高（降低）运动强度，保持匀速有节奏的运动才能高效瘦身哦。
     * <p>
     * 连续语音播报时间间隔：30s；
     */

    //瘦身衣运动结束
    private void sportingFinish(boolean isComplete) {
        if (isComplete) {
            speakAdd(getString(R.string.speech_planFinishSuccess, Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(currentKcal, 1))));
        } else {
            speakAdd(getString(R.string.speech_planFinishFail));
        }
        timer.stopTimer();
        saveHeartRate();
    }


    /**
     * 完成运动区间
     *
     * @param bean
     */
    private void completeHeartRange(AthlPlanListBean bean, AthlPlanListBean nextBean, int completeStage) {
        String speed = "";
        if (reversePace < Key.HRART_SECTION[nextBean.getRange()]) {
            speed = "提高";
        } else if (reversePace > Key.HRART_SECTION[nextBean.getRange() + 1]) {
            speed = "降低";
        } else {
            speed = "保持当前";
        }

        speakAdd(getString(R.string.speech_completeStage, completeStage + "", speed, nextBean.strRange(mContext)));
    }


    /**
     * 0<=bmp<2 502<=bmp<4 404<=bmp<6 306<=bmp<8 208<=bmp<10 1010<=bmp
     * 通过计算实时运动曲线与运动课程规划曲线偏差绝对值进行计
     * 算，得出当前运动的平均分；
     * 运动完成度得分：(当前已完成运动时间/课程总时间）*50
     */
    private void sportingScore(int currentValue) {
        LineDataSet defaultSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("heartPlan", true);
        LineDataSet realTimeSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("RealTime", true);
        if (defaultSet != null && realTimeSet != null) {
            int count = realTimeSet.getEntryCount();
            float tatolCount = defaultSet.getEntryCount();

            //运动开始语音
            if (count == 1) {
                mTvHeartCount.setText("1/" + planList.size());
//                speakAdd(getString(R.string.speech_ourseOne,
//                        HeartSectionUtil.strRange(mContext, planList.get(0).getRange()),
//                        HeartSectionUtil.strRange(mContext, planList.get(0).getRange()),
//                        planList.get(0).getTime()));
            }

            for (int i = 0; i < planList.size(); i++) {
                AthlPlanListBean bean = planList.get(i);
                if (count * 2 == bean.getTime() * 60) {
                    if ((i + 1) < planList.size())
                        completeHeartRange(bean, planList.get((i + 1) % planList.size()), i + 1);
                    mTvHeartCount.setText((i + 2) + "/" + planList.size());
                }
            }

            mHeartRateBean.setComplete(count / tatolCount);
            //运动结束
            if (count == tatolCount) {
                sportingFinish(true);
                return;
            }

            //防止下标越界
            if (count > tatolCount) return;

            int score = 0;
            float abs = Math.abs(defaultSet.getEntryForIndex(count).getY() - currentValue);
            if (abs <= 10) {
                score = 50;
            } else if (abs <= 20) {
                score = 40;
            } else if (abs <= 30) {
                score = 30;
            } else if (abs <= 40) {
                score = 20;
            } else if (abs <= 50) {
                score = 20;
            }
            totalSum += score;
            if (count > 0)
                sportingScore = totalSum / count + count * 50 / tatolCount;

            RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(sportingScore, 1))
                    .append("\t分").setProportion(0.3f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvKcal);

            float width = RxUtils.dp2px(325) * 1f / defaultSet.getEntryCount() *
                    realTimeSet.getEntryCount() + RxUtils.dp2px(24) * 0.5f;
            if (width < RxUtils.dp2px(24)) {
                width = RxUtils.dp2px(30);
            } else if (width > RxUtils.dp2px(325)) {
                width = RxUtils.dp2px(320);
            }

            RxLogUtils.d("宽度：" + width);
            ViewGroup.LayoutParams layoutParams = mTvCurrentTime.getLayoutParams();
            layoutParams.width = (int) width;
            mTvCurrentTime.setLayoutParams(layoutParams);
        }
    }


    private void initTypeface() {
        Typeface typeface = MyAPP.typeface;
        mTvKcal.setTypeface(typeface);
        mTvAvHeartRate.setTypeface(typeface);
        mTvMaxHeartRate.setTypeface(typeface);
        mTvSportsTime.setTypeface(typeface);
    }

    private void initSwitch() {
        mSwMusic.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(final SwitchView view) {
                mSwMusic.setOpened(true);
                SPUtils.put(SPKey.SP_VoiceTip, true);
                type = -1;
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.setOpened(false);
                SPUtils.put(SPKey.SP_VoiceTip, false);
                TextSpeakUtils.stop();
            }
        });
        mSwMusic.setOpened(SPUtils.getBoolean(SPKey.SP_VoiceTip, true));

        mSwHeat.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView switchView) {
                toggleHeat(true);
            }

            @Override
            public void toggleToOff(SwitchView switchView) {
                toggleHeat(false);
            }
        });
    }

    /**
     * 切换是否加热
     */
    private void toggleHeat(boolean isHeat) {
        BleAPI.syncSetting(isHeat, data -> mSwHeat.setOpened(isHeat));
    }

    private void initTopBar() {
        mQMUIAppBarLayout.setTitle(R.string.planSporting);
        btn_Connect = mQMUIAppBarLayout.addRightTextButton(getString(
                !BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)) ?
                        R.string.unBind : BleTools.getInstance().isConnect() ?
                        R.string.connected : R.string.connecting), R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
        btn_Connect.setOnClickListener(view -> {
            if (!BleTools.getInstance().isConnect()) {
                RxToast.normal("蓝牙正在连接", 2000);
            }
        });
    }


    private void freeTextSpeak(int heart) {
        int index = HeartSectionUtil.currentSection(heart);
        switch (index) {
            case 0:
                mTvSportsStatus.setText(R.string.calm);
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.resting));
                break;
            case 1:
                mTvSportsStatus.setText(R.string.warm);
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.warm));
                break;
            case 2:
                mTvSportsStatus.setText(R.string.grease);
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.fatBurning));
                break;
            case 3:
                mTvSportsStatus.setText(R.string.aerobic);
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.aerobic));
                break;
            case 4:
                mTvSportsStatus.setText(R.string.anaerobic);
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.anaerobic));
                break;
            case 5:
                mTvSportsStatus.setText(R.string.limit);
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.limit));
                break;
        }
    }

    //当用户极限运动超过 3 分钟
    MyTimer limitTimer = new MyTimer(3 * 60 * 1000, 3 * 60 * 1000, () ->
            speakAdd(getString(R.string.speech_keepLimitSpeak)));


    MyTimer timer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            currentTime++;

            mTvCurrentTime.setText(RxFormat.setSec2MS(currentTime));

            if (type == 4) {
                limitTimer.startTimer();
            } else {
                limitTimer.stopTimer();
            }

            LineDataSet defaultSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("heartPlan", true);
            LineDataSet realTimeSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("RealTime", true);

            if (realTimeSet.getEntryCount() >= defaultSet.getEntryCount()) return;

            int section = Math.abs((Key.HRART_SECTION[6]) - (Key.HRART_SECTION[5]));

            RxLogUtils.d("配速区间：" + section);
            //这里当运动比计划配速差值大于20提示用户块（慢）点
            if ((defaultSet.getEntryForIndex(realTimeSet.getEntryCount()).getY() - reversePace) >= section) {
                sportUpWarnCount++;
            } else if ((defaultSet.getEntryForIndex(realTimeSet.getEntryCount()).getY() - reversePace) <= -section) {
                sportDownWarnCount++;
            } else {
                sportUpWarnCount = 0;
                sportDownWarnCount = 0;
            }
            if (sportUpWarnCount != 0 && sportUpWarnCount % 30 == 0) {
                speakAdd(getString(R.string.speech_sportFast));
            }
            if (sportDownWarnCount != 0 && sportDownWarnCount % 30 == 0) {
                speakAdd(getString(R.string.speech_sportSlow));
            }
        }
    });


    @Override
    public void onDestroy() {
        timer.stopTimer();
        dismissAllDialog();
        limitTimer.stopTimer();
        BleAPI.clearStep();
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }


    /**
     * 运动结束
     */
    private void finishSporting() {
        if (currentTime < 180) {
            //       用户当前运动时间<3min，提示用户此次记录将不被保存
            sportingShortDialog = new RxDialogSure(mContext)
                    .setTitle(getString(R.string.sportTip))
                    .setContent("您当前运动时间过短，此次运动记录将不会被保存")
                    .setSure(getString(R.string.ok))
                    .setSureListener(v -> {
                        RxActivityUtils.finishActivity();
                    });
            sportingShortDialog.show();
        } else if (mChartHeartRate.getData().getEntryCount() <= 0) {
            //       用户当前运动没有配速，提示用户此次记录将不被保存
            sportingShortDialog = new RxDialogSure(mContext)
                    .setTitle(getString(R.string.sportTip))
                    .setContent("您的运动数据异常，此次运动记录将不会被保存")
                    .setSure(getString(R.string.ok))
                    .setSureListener(v -> RxActivityUtils.finishActivity());
            sportingShortDialog.show();
        } else {
            sportingFinish(false);
        }
    }


    public void saveHeartRate() {
        String s = JSON.toJSONString(mHeartRateBean);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .addRunningData(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        if (RxDataUtils.isNullString(s)) {
                            RxToast.normal("数据保存失败");
                            return;
                        }
                        mHeartRateUtil.clearData(mHeartRateBean);

                        //这里因为是后台上传数据，并不是跳转，使用RxBus方式
                        RxBus.getInstance().post(new RefreshSlimming());

                        Bundle bundle = new Bundle();
                        bundle.putString(Key.BUNDLE_DATA_GID, s);
                        bundle.putBoolean(Key.BUNDLE_SPORTING_PLAN, true);
                        RxActivityUtils.skipActivityAndFinish(mContext, SportsDetailsFragment.class, bundle);
                    }


                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        new RxDialogSure(mContext)
                                .setTitle(getString(R.string.tip))
                                .setContent("因" + error + "，运动数据上传失败，您可在网络恢复后再进行查看")
                                .setSureListener(v -> RxActivityUtils.finishActivity()).show();
                    }
                });
    }

    private void speakAdd(String text) {
        if (mSwMusic.isOpened())
            TextSpeakUtils.speakAdd(text);
    }

    /**
     * 尝试继续运动，继续运动：提示开启蓝牙，并重连10秒,结束运动：直接结束
     */
    private void trySporting() {
        if (BleTools.getInstance().isConnect()) {
            pause = !pause;
            startOrPauseSport();
            speakAdd(pause ? getString(R.string.speech_sportPause) : getString(R.string.speech_sportPlay));
        } else {
            connectFailDialog = new RxDialogSureCancel(mContext)
                    .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                    .setTitle(getString(R.string.tip))
                    .setContent("蓝牙设备已断开")
                    .setCancel("继续运动")
                    .setCancelListener(view -> {
                        if (!BleTools.getBleManager().isBlueEnable()) {
                            BleTools.getBleManager().enableBluetooth();
                        }
                    })
                    .setSure("结束运动")
                    .setSureListener(v -> finishSporting());
            connectFailDialog.show();
        }
    }

    //拦截返回事件
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenUtils.keepBright(this);
        super.onCreate(savedInstanceState);
    }


    @OnClick({R.id.tv_playOrPause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_playOrPause:
                trySporting();
                break;
        }
    }
}
