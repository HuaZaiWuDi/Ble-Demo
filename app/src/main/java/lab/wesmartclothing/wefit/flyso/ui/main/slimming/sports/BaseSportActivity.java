package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.LineChart;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.utils.RxConstUtils;
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
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.CacheTarget;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;

import java.util.ArrayList;
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
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateItemBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.ClothingConnectBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.HeartSectionUtil;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;

/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports
 * @FileName BaseSportActivity
 * @Date 2019/5/31 14:39
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public abstract class BaseSportActivity extends BaseActivity implements SportInterface {

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
    public int currentTime = 0, stepSpeed = 0, type = -1, reversePace;//运动上一次状态
    private long stopTime = 0;
    public HeartLineChartUtils lineChartUtils;
    public double sportingScore = 0, currentKcal = 0, totalSum, kilometre;
    public HeartRateBean mHeartRateBean = new HeartRateBean();
    private RxDialogSureCancel connectFailDialog;
    public RxDialogSure sportingShortDialog;
    public boolean pause = false;
    public HeartRateUtil mHeartRateUtil = new HeartRateUtil();
    public int kilometreFlag = 0;
    public String SportKey = System.currentTimeMillis() + "";
    public MyTimer timeTimer = null;
    public String currentMode;

    public abstract void sportFinish();

    @Override
    protected int layoutId() {
        return R.layout.activity_sportsing;
    }


    @Override
    protected void initViews() {
        super.initViews();
        BleAPI.clearStep();
        initTopBar();
        initSwitch();
        initTypeface();
        finishAnim();
        weightWarn();
        sportTip();
        lineChartUtils = new HeartLineChartUtils(mChartHeartRate);
    }

    /**
     * 1、每次在自由运动或者定制运动进去的第一时间弹出一个温馨提醒的弹窗，
     * 用户点确认或不再提醒后消失（默认连续提醒10次后面就别提醒了）。
     */
    private void sportTip() {
        RxDialogSureCancel runTipDialog = new RxDialogSureCancel(mContext)
                .setTitle(getString(R.string.tip))
                .setContent(getString(R.string.runTip))
                .setCancel(getString(R.string.NoReminders))
                .setCancelListener(v -> SPUtils.put(SPKey.SP_RUN_TIP_DIALOG_COUNT, 10));
        runTipDialog.getTvContent().setGravity(Gravity.START);

        int count = SPUtils.getInt(SPKey.SP_RUN_TIP_DIALOG_COUNT, 0);
        if (count < 10) {
            count++;
            SPUtils.put(SPKey.SP_RUN_TIP_DIALOG_COUNT, count);
            //showDialog
            runTipDialog.show();
        }
    }

    private void weightWarn() {
        if (SPUtils.getFloat(SPKey.SP_realWeight, 0) == 0) {
            new RxDialogSure(mContext)
                    .setTitle(getString(R.string.sportTip))
                    .setContent(getString(R.string.goweighTip))
                    .setSure(getString(R.string.ok))
                    .setSureListener(v -> {
                        RxActivityUtils.finishActivity();
                    }).show();
        } else {
            mTipWeigh.setVisibility(View.GONE);
        }
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
            mQMUIAppBarLayout.setTitle(currentMode);
            mTvPlayOrPause.setText(R.string.pause);
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_play);
        }
        mTvPlayOrPause.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

        if (pause) {
            timeTimer.stopTimer();
        } else {
            timeTimer.startTimer();
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
                        sportFinish();
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


    private void initTypeface() {
        Typeface typeface = MyAPP.typeface;
        mTvKcal.setTypeface(typeface);
        mTvAvHeartRate.setTypeface(typeface);
        mTvMaxHeartRate.setTypeface(typeface);
        mTvSportsTime.setTypeface(typeface);
    }

    private void initSwitch() {
        mSwMusic.setOpened(SPUtils.getBoolean(SPKey.SP_VoiceTip, true));

        mSwHeat.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView switchView) {
                toggleHeat(true);

                SPUtils.put(SPKey.SP_VoiceTip, true);
                type = -1;
            }

            @Override
            public void toggleToOff(SwitchView switchView) {
                toggleHeat(false);
                SPUtils.put(SPKey.SP_VoiceTip, false);
                TextSpeakUtils.stop();
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
        mQMUIAppBarLayout.setTitle(currentMode);
        btn_Connect = mQMUIAppBarLayout.addRightTextButton(getString(
                !BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)) ?
                        R.string.unBind : BleTools.getInstance().isConnect() ?
                        R.string.connected : R.string.connecting), R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
        btn_Connect.setOnClickListener(view -> {
            if (!BleTools.getInstance().isConnect()) {
                RxToast.normal(getString(R.string.connecting), 2000);
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
    public void freeTextSpeak(int heart) {
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

    public void uploadData() {
        timeTimer.stopTimer();
        String s = JSON.toJSONString(mHeartRateBean);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .addRunningData(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        mHeartRateUtil.clearData(SportKey);

                        BleService.clothingFinish = true;
                        //这里因为是后台上传数据，并不是跳转，使用RxBus方式
                        RxBus.getInstance().post(new RefreshSlimming());

                        Bundle bundle = new Bundle();
                        bundle.putString(Key.BUNDLE_DATA_GID, s);
                        bundle.putBoolean(Key.BUNDLE_SPORTING_PLAN, mHeartRateBean.getPlanFlag() == 1);
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

    /**
     * 实时保存数据到本地，保证异常情况下，数据不会丢失
     *
     * @param sportsDataTab
     */
    public void saveData(SportsDataTab sportsDataTab) {
        mHeartRateBean.setAthlDesc(mTvHeartCount.getText().toString());
        mHeartRateBean.setAthlScore(sportingScore);
        mHeartRateBean.setAthlDate(SportKey);
        mHeartRateBean.setTotalCalorie(sportsDataTab.getKcal());
        mHeartRateBean.setHeartList(sportsDataTab.getHeartLists());
        mHeartRateBean.setAvgPace(sportsDataTab.getAvPace());
        mHeartRateBean.setMaxPace(sportsDataTab.getMaxPace());
        mHeartRateBean.setMinPace(sportsDataTab.getMinPace());
        mHeartRateBean.setStepNumber(sportsDataTab.getSteps());
        mHeartRateBean.setKilometers(sportsDataTab.getKilometre());
        mHeartRateBean.setCadence(sportsDataTab.getSteps() * 0.5f / sportsDataTab.getHeartLists().size());

        //如果上传失败则保存本地
        RxCache.getDefault().save(SportKey, mHeartRateBean, CacheTarget.Disk)
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                    }
                });
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initRxBus2() {
        super.initRxBus2();

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


    public void speakAdd(String text) {
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
                    .setContent(getString(R.string.bleDeviceDisconnected))
                    .setCancel(getString(R.string.continueRun))
                    .setCancelListener(view -> {
                        if (!BleTools.getBleManager().isBlueEnable()) {
                            BleTools.getBleManager().enableBluetooth();
                        }
                    })
                    .setSure(getString(R.string.finishRun))
                    .setSureListener(v -> sportFinish());
            connectFailDialog.show();
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

    @SuppressLint("CheckResult")
    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);

        String key = bundle.getString(Key.BUNDLE_SPORTING_LAST_DATA, "");
        RxCache.getDefault().<HeartRateBean>load(key, HeartRateBean.class)
                .map(new CacheResult.MapFunc<>())
                .subscribe(bean -> {
                            //获取到数据之后进行数据复位
                            RxLogUtils.d("上一次的运动数据", bean.toString());
                            mHeartRateBean = bean;
                            mHeartRateUtil.setInitData(bean);

                            currentTime = mHeartRateBean.getHeartList().size() * 2;
                            mTvHeartCount.setText(mHeartRateBean.getAthlDesc());
                            sportingScore = mHeartRateBean.getAthlScore();
                            SportKey = mHeartRateBean.getAthlDate();
                            currentKcal = RxFormatValue.format4S5R(mHeartRateBean.getTotalCalorie(), 1);

                            List<Integer> realLists = new ArrayList<>();
                            for (HeartRateItemBean itemBean : mHeartRateBean.getHeartList()) {
                                realLists.add(HeartRateUtil.reversePace(itemBean.getPace()));
                            }
                            lineChartUtils.setRealTimeData(realLists);

                            //自由运动
                            if (bean.getPlanFlag() == 0) {
                                RxTextUtils.getBuilder(currentKcal + "")
                                        .append("\tkcal").setProportion(0.3f)
                                        .into(mTvKcal);
                            } else {
                                RxTextUtils.getBuilder(currentKcal + "")
                                        .append("\tkcal").setProportion(0.5f)
                                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                                        .into(mTvSportskcal);

                                RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(sportingScore, 1))
                                        .append("\t分").setProportion(0.3f)
                                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                                        .into(mTvKcal);
                            }

                            mTvMaxHeartRate.setText(RxFormatValue.fromat4S5R(bean.getKilometers(), 2));
                            mTvSportsTime.setText(RxFormat.setSec2MS(currentTime));
                            mTvCurrentTime.setText(RxFormat.setSec2MS(currentTime));

//                            mHeartRateUtil.saveSportKey(SportKey);
                        },
                        e -> {
                            RxLogUtils.e("上一次的运动数据", e);
                            mHeartRateUtil.saveSportKey(SportKey);
                        });
    }

    @Override
    protected void initNetData() {
        super.initNetData();
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!pause) {
            if (stopTime > 0) {
                RxLogUtils.d("运动返回前台");
                currentTime += RxTimeUtils.getIntervalTime(System.currentTimeMillis(), stopTime, RxConstUtils.TimeUnit.SEC);
            }
            timeTimer.startTimer();
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

        ContextCompat.startForegroundService(mContext, intent);

        if (!pause) {
            stopTime = System.currentTimeMillis();
            timeTimer.stopTimer();
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
        trySporting();
    }

    @Override
    public void onDestroy() {
        timeTimer.stopTimer();
        dismissAllDialog();
        super.onDestroy();
    }

}

