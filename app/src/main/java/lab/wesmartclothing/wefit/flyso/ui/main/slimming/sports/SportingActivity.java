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
import com.github.mikephil.charting.components.XAxis;
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
import com.zchu.rxcache.RxCache;

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
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
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
public class SportingActivity extends BaseActivity implements SportInterface {


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
    @BindView(R.id.layout_sports)
    LinearLayout mLayoutSports;
    @BindView(R.id.tv_expectKcal)
    TextView mTvExpectKcal;
    @BindView(R.id.layout_sportingTime)
    LinearLayout mLayoutSportingTime;
    @BindView(R.id.tv_sportskcal)
    TextView mTvSportskcal;
    @BindView(R.id.layout_sportingKcal)
    LinearLayout mLayoutSportingKcal;
    @BindView(R.id.layout_legend)
    RelativeLayout mLayoutLegend;
    @BindView(R.id.tv_currentTime)
    TextView mTvCurrentTime;
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;
    @BindView(R.id.tv_playOrPause)
    RxTextView mTvPlayOrPause;
    @BindView(R.id.circleProgressBar)
    RoundProgressBar mCircleProgressBar;
    @BindView(R.id.tv_finish)
    RxTextView mTvFinish;
    @BindView(R.id.parent)
    RelativeLayout mParent;
    @BindView(R.id.sw_heat)
    SwitchView mSwHeat;

    private Button btn_Connect;
    private int currentTime = 0;
    private long stopTime = 0;
    private HeartLineChartUtils lineChartUtils;
    private HeartRateBean mHeartRateBean = new HeartRateBean();
    private double currentKcal = 0;
    private RxDialogSureCancel connectFailDialog;
    private RxDialogSure sportingShortDialog;
    private boolean pause = false;//运动是否暂停
    private HeartRateUtil mHeartRateUtil = new HeartRateUtil();
    private int kilometreFlag = 0;

    /**
     * 关闭所有弹窗
     */
    private void dismissAllDialog() {
        if (connectFailDialog != null && connectFailDialog.isShowing())
            connectFailDialog.dismiss();
        if (sportingShortDialog != null && sportingShortDialog.isShowing())
            sportingShortDialog.dismiss();
    }


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
        initChart();
        finishAnim();

        mLayoutLegend.setVisibility(View.GONE);

        RxTextUtils.getBuilder("0.0")
                .append("\tkcal").setProportion(0.3f)
                .into(mTvKcal);

        speakAdd(getString(R.string.speech_sprotStart));
    }


    /**
     * 暂停或继续运动
     */
    private void startOrPauseSport() {
        Drawable drawable = null;
        if (pause) {
            mQMUIAppBarLayout.setTitle(getString(R.string.speech_sportPause));
            mTvPlayOrPause.setText(R.string.play);
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_pause);
        } else {
            mQMUIAppBarLayout.setTitle(R.string.freeSporting);
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


    private void initChart() {
        lineChartUtils = new HeartLineChartUtils(mChartHeartRate);
        XAxis xAxis = mChartHeartRate.getXAxis();
        xAxis.setAxisMaximum(60);
        mChartHeartRate.invalidate();
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

                        //撤销最大显示范围，让一屏显示所有数据
                        if (mChartHeartRate.getData().getEntryCount() > 60) {
                            XAxis xAxis = mChartHeartRate.getXAxis();
                            xAxis.resetAxisMaximum();
                        }
                        SportsDataTab sportsDataTab = mHeartRateUtil.addRealTimeData(heartRateData.heartRateData);
                        //配速
                        int stepSpeed = sportsDataTab.getStepSpeed();

                        if (stepSpeed == 0) {
                            mTvAvHeartRate.setText("--");
                            return;
                        }

                        currentKcal = RxFormatValue.format4S5R(sportsDataTab.getKcal(), 1);

                        mTvAvHeartRate.setText(RxFormat.setSec2MS(stepSpeed));
                        double kilometre = sportsDataTab.getKilometre();
                        mTvMaxHeartRate.setText(RxFormatValue.fromat4S5R(kilometre, 2));
                        RxTextUtils.getBuilder(currentKcal + "")
                                .append("\tkcal").setProportion(0.3f)
                                .into(mTvKcal);

                        //每km播报一次
                        if (kilometreFlag != (int) kilometre) {
                            kilometreFlag = (int) kilometre;
                            speakAdd(getString(R.string.Speech_eachKm,
                                    Number2Chinese.number2Chinese((int) kilometre + ""),
                                    Number2Chinese.number2Chinese((int) currentKcal + ""),
                                    Number2Chinese.number2Chinese(stepSpeed / 60 + "") + "分钟"
                                            + Number2Chinese.number2Chinese(stepSpeed % 60 + "") + "秒",
                                    Number2Chinese.number2Chinese((int) currentTime / 60 + "")
                            ));
                        }

                        lineChartUtils.setRealTimeData(sportsDataTab.getReversePace());
                        freeTextSpeak(sportsDataTab.getReversePace());
                        guideLineMove();
                        saveData(sportsDataTab);
                    }
                });

        RxBus.getInstance().registerSticky(ClothingConnectBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(clothingConnect -> {
                    boolean state = clothingConnect.isConnect();
                    btn_Connect.setText(state ? R.string.connected : R.string.connecting);
                    if (state) {
                        dismissAllDialog();
                    } else {
                        trySporting();
                    }
                    pause = !state;
                    startOrPauseSport();
                });
    }


    /**
     * 实时保存数据
     *
     * @param sportsDataTab
     */
    private void saveData(SportsDataTab sportsDataTab) {
        mHeartRateBean.setPlanFlag(0);
        mHeartRateBean.setTotalCalorie(sportsDataTab.getKcal());
        mHeartRateBean.setHeartList(sportsDataTab.getHeartLists());
        mHeartRateBean.setAvgPace(sportsDataTab.getAvPace());
        mHeartRateBean.setMaxPace(sportsDataTab.getMaxPace());
        mHeartRateBean.setMinPace(sportsDataTab.getMinPace());
        mHeartRateBean.setStepNumber(sportsDataTab.getSteps());
        mHeartRateBean.setKilometers(sportsDataTab.getKilometre());
        if (currentTime != 0)
            mHeartRateBean.setCadence(sportsDataTab.getSteps() * 1f / currentTime);

        RxCache.getDefault().save(Key.CACHE_ATHL_RECORD_FREE, mHeartRateBean)
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                    }
                });

    }

    /**
     * 图上指导线移动
     */
    private void guideLineMove() {
        LineDataSet realTimeSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("RealTime", true);
        int count = realTimeSet.getEntryCount();

        int width = RxUtils.dp2px(325) / 60 * count;
        if (width < RxUtils.dp2px(24)) {
            width = RxUtils.dp2px(30);
        } else if (width > RxUtils.dp2px(325)) {
            width = RxUtils.dp2px(320);
        }

        ViewGroup.LayoutParams layoutParams = mTvCurrentTime.getLayoutParams();
        layoutParams.width = width;
        mTvCurrentTime.setLayoutParams(layoutParams);
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
        mQMUIAppBarLayout.setTitle(R.string.freeSporting);
        btn_Connect = mQMUIAppBarLayout.addRightTextButton(getString(
                !BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)) ? R.string.unBind : BleTools.getInstance().isConnect() ? R.string.connected : R.string.connecting), R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
        btn_Connect.setOnClickListener(view -> {
            if (!BleTools.getInstance().isConnect()) {
                RxToast.normal("蓝牙正在连接", 2000);
            }
        });
    }

    /**
     * speakAdd(getString(R.string.speech_warm));
     * <p>
     * speakAdd(getString(R.string.speech_grease));
     * <p>
     * speakAdd(getString(R.string.speech_aerobic));
     * <p>
     * speakAdd(getString(R.string.speech_anaerobic));
     * <p>
     * speakAdd(getString(R.string.speech_limit));
     *
     * @param heart
     */
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


    MyTimer timer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            currentTime++;

            mTvSportsTime.setText(RxFormat.setSec2MS(currentTime));
            mTvCurrentTime.setText(RxFormat.setSec2MS(currentTime));
            if (currentTime % (60 * 3) == 0) {
                speakAdd(getString(R.string.speech_currentKcal, mTvSportsStatus.getText().toString()));
            }
        }
    });


    @Override
    public void onDestroy() {
        timer.stopTimer();
        dismissAllDialog();
        BleAPI.clearStep();
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.cancel(NOTIFICATION_ID);
        super.onDestroy();
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
                        //这里因为是后台上传数据，并不是跳转，使用RxBus方式
                        RxBus.getInstance().post(new RefreshSlimming());
                        new HeartRateUtil().clearData(mHeartRateBean);

                        Bundle bundle = new Bundle();
                        bundle.putString(Key.BUNDLE_DATA_GID, s);
                        bundle.putBoolean(Key.BUNDLE_SPORTING_PLAN, false);
                        RxActivityUtils.skipActivityAndFinish(mContext, SportsDetailsFragment.class, bundle);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        new RxDialogSure(mContext)
                                .setTitle(getString(R.string.tip))
                                .setContent("因网络异常，运动数据上传失败，您可在运动记录中进行查看")
                                .setSureListener(v -> RxActivityUtils.finishActivity()).show();
                    }
                });
    }

    private void speakAdd(String text) {
        if (mSwMusic.isOpened())
            TextSpeakUtils.speakAdd(text);
    }


    /**
     * 运动结束
     * force 是否强制退出
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
            //       用户当前运动没有心率，提示用户此次记录将不被保存
            sportingShortDialog = new RxDialogSure(mContext)
                    .setTitle(getString(R.string.sportTip))
                    .setContent("您的运动数据异常，此次运动记录将不会被保存")
                    .setSure(getString(R.string.ok))
                    .setSureListener(v -> RxActivityUtils.finishActivity());
            sportingShortDialog.show();
        } else {
            timer.stopTimer();
            speakAdd(getString(R.string.speech_freeSportFinish,
                    Number2Chinese.number2Chinese((currentTime / 60) + ""),
                    Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(currentKcal, 1))));
            //如果检测到运动已经结束，直接保存数据并进入详情页
            saveHeartRate();
        }
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
                    .setTitle("提示")
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenUtils.keepBright(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.tv_playOrPause})
    public void onViewClicked(View view) {
        trySporting();

    }
}
