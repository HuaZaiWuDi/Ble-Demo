package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import lab.wesmartclothing.wefit.flyso.ble.listener.BleChartChangeCallBack;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.HeartRateChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
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
    private int type = -1;//运动上一次状态
    private HeartLineChartUtils lineChartUtils;
    private HeartRateBean mHeartRateBean = new HeartRateBean();
    private double currentKcal = 0;
    private RxDialogSureCancel connectFailDialog;
    private RxDialogSure sportingShortDialog;
    private boolean pause = false;//运动是否暂停
    private HeartRateUtil mHeartRateUtil = new HeartRateUtil();
    private int heartRateFlag = 0;

    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mContext == null) return;
            //监听瘦身衣连接情况
            if (Key.ACTION_CLOTHING_CONNECT.equals(intent.getAction())) {
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_CLOTHING_CONNECT, false);
                btn_Connect.setText(state ? R.string.connected : R.string.connecting);
                if (state) {
                    dismissAllDialog();
                } else {
                    trySporting();
                }
                pause = !state;
                startOrPauseSport();
            }
        }
    };


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
        initBroadcast();
        initChart();
        finishAnim();

        mLayoutLegend.setVisibility(View.GONE);

        RxTextUtils.getBuilder("0.0")
                .append("\tkcal").setProportion(0.3f)
                .into(mTvKcal);

        speakAdd("运动模式已启动，让我们开始自由运动瘦身训练吧");

    }


    /**
     * 暂停或继续运动
     */
    private void startOrPauseSport() {
        speakAdd(pause ? "运动已暂停" : "运动已恢复");
        Drawable drawable = null;
        if (pause) {
            mQMUIAppBarLayout.setTitle("运动已暂停");
            mTvPlayOrPause.setText("继续");
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_pause);
        } else {
            mQMUIAppBarLayout.setTitle("自由运动中");
            mTvPlayOrPause.setText("暂停");
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
        mTvFinish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTvFinish.setScaleX(0.8f);
                        mTvFinish.setScaleY(0.8f);
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
                            builder.setOffsetX(-RxUtils.dp2px(5));
                            builder.setGravity(RxToolTip.GRAVITY_CENTER);
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
            }
        });
    }


    private void initChart() {
        lineChartUtils = new HeartLineChartUtils(mChartHeartRate);
//        mChartHeartRate.setViewPortOffsets(0, 0, RxUtils.dp2px(86), 0);

        XAxis xAxis = mChartHeartRate.getXAxis();
        xAxis.setAxisMaximum(60);
        mChartHeartRate.invalidate();
    }


    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Key.ACTION_CLOTHING_CONNECT);
        filter.addAction(Key.ACTION_CLOTHING_STOP);
        registerReceiver(registerReceiver, filter);
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
    protected void initRxBus2() {
        super.initRxBus2();
        RxBus.getInstance().register2(HeartRateChangeBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<HeartRateChangeBus>() {
                    @Override
                    protected void _onNext(HeartRateChangeBus heartRateData) {

                        if (pause) return;
                        heartRateFlag = 0;

                        //撤销最大显示范围，让一屏显示所有数据
                        if (mChartHeartRate.getData().getEntryCount() > 60) {
                            XAxis xAxis = mChartHeartRate.getXAxis();
                            xAxis.resetAxisMaximum();
                        }
                        SportsDataTab sportsDataTab = mHeartRateUtil.addRealTimeData(heartRateData.heartRateData);

                        int currentHeart = sportsDataTab.getCurHeart();
                        currentKcal = RxFormatValue.format4S5R(sportsDataTab.getKcal(), 1);

                        lineChartUtils.setRealTimeData(currentHeart);

                        mTvAvHeartRate.setText(currentHeart + "");
                        mTvMaxHeartRate.setText(sportsDataTab.getMaxHeart() + "");
                        RxTextUtils.getBuilder(currentKcal + "")
                                .append("\tkcal").setProportion(0.3f)
                                .into(mTvKcal);

                        freeTextSpeak(currentHeart);
                        guideLineMove();
                        saveData(sportsDataTab);

                    }
                });
    }


    /**
     * 实时保存数据
     *
     * @param sportsDataTab
     */
    private void saveData(SportsDataTab sportsDataTab) {
        mHeartRateBean.setPlanFlag(0);
        mHeartRateBean.setTotalCalorie((int) sportsDataTab.getKcal());
        mHeartRateBean.setHeartList(sportsDataTab.getHeartLists());
        mHeartRateBean.setStepNumber(sportsDataTab.getSteps());

        RxCache.getDefault().save(Key.CACHE_ATHL_RECORD_FREE, mHeartRateBean)
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        RxLogUtils.d("心率保存" + aBoolean);
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
        BleAPI.syncSetting(Key.heartRates, 60, 50, isHeat, new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                mSwHeat.setOpened(isHeat);
            }
        });
    }


    private void initTopBar() {
        mQMUIAppBarLayout.setTitle("自由运动中");
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


    private void freeTextSpeak(int heart) {
        byte[] heartRates = Key.HRART_SECTION;
        int heart_0 = heartRates[0] & 0xff;
        int heart_1 = heartRates[1] & 0xff;
        int heart_2 = heartRates[2] & 0xff;
        int heart_3 = heartRates[3] & 0xff;
        int heart_4 = heartRates[4] & 0xff;
        int heart_5 = heartRates[5] & 0xff;
        int heart_6 = heartRates[6] & 0xff;
        if (heart >= heart_1 && heart <= heart_2) {
            if (type != 0) {
                mTvSportsStatus.setText("热身");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.brown_ABA08E));
                if (!TextSpeakUtils.isSpeak()) {
                    speakAdd(getString(R.string.speech_warm));
                    type = 0;
                }
            }
        } else if (heart >= heart_2 && heart < heart_3) {
            if (type != 1) {
                mTvSportsStatus.setText("燃脂");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.yellow_FFBC00));
                if (!TextSpeakUtils.isSpeak()) {
                    speakAdd(getString(R.string.speech_grease));
                    type = 1;
                }
            }
        } else if (heart >= heart_3 && heart < heart_4) {
            if (type != 2) {
                mTvSportsStatus.setText("有氧");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.green_61D97F));
                if (!TextSpeakUtils.isSpeak()) {
                    type = 2;
                    speakAdd(getString(R.string.speech_aerobic));
                }
            }
        } else if (heart >= heart_4 && heart < heart_5) {
            if (type != 3) {
                mTvSportsStatus.setText("无氧");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.orange_FF7200));
                if (!TextSpeakUtils.isSpeak()) {
                    type = 3;
                    speakAdd(getString(R.string.speech_anaerobic));
                }
            }
        } else if (heart >= heart_5) {
            if (type != 4) {
                mTvSportsStatus.setText("极限");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.red));
                if (!TextSpeakUtils.isSpeak()) {
                    type = 4;
                    speakAdd(getString(R.string.speech_limit));
                }
            }
        } else if (heart < heart_1) {
            if (type != 5) {
                mTvSportsStatus.setText("静息");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.Gray_DCDAE6));
                if (!TextSpeakUtils.isSpeak()) {
                    type = 5;
//                    speakAdd(getString(R.string.speech_limit));
                }
            }
        }
    }


    MyTimer timer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            currentTime++;

            heartRateFlag++;
            //3秒钟没有心率则，显示‘--’，有心率则重置为标记为0
            if (heartRateFlag == 10) {
                mTvAvHeartRate.setText("--");
            }

            mTvSportsTime.setText(RxFormat.setSec2MS(currentTime));
            if (currentTime % 120 == 0) {
                speakAdd(getString(R.string.speech_currentKcal) +
                        Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(currentKcal, 1)) + "千卡的能量");
            }

            mTvCurrentTime.setText(RxFormat.setSec2MS(currentTime));
        }
    });


    @Override
    public void onDestroy() {
        unregisterReceiver(registerReceiver);
        timer.stopTimer();
        TextSpeakUtils.stop();
        dismissAllDialog();

        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }


    public void saveHeartRate() {
        String s = JSON.toJSONString(mHeartRateBean);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .addAthleticsInfo(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        if (RxDataUtils.isNullString(s)) {
                            RxToast.normal("数据保存失败");
                            return;
                        }
                        RxLogUtils.d("添加心率：保存成功删除本地缓存：");
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
                                .setTitle("提示")
                                .setContent("因网络异常，运动数据上传失败，您可在运动记录中进行查看")
                                .setSureListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RxActivityUtils.finishActivity();
                                    }
                                }).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
//        finishSporting();
    }

    private void speakAdd(String text) {
        if (mSwMusic.isOpened() && isVisibility())
            TextSpeakUtils.speakAdd(text);
    }

    /**
     * 运动结束
     * force 是否强制退出
     */
    private void finishSporting() {


//        //未开启运动直接结束
//        if (currentTime == 0) {
//            RxActivityUtils.finishActivity();
//        } else
        if (currentTime < 180) {
            //       用户当前运动时间<3min，提示用户此次记录将不被保存
            sportingShortDialog = new RxDialogSure(mContext)
                    .setTitle("运动提示")
                    .setContent("您当前运动时间过短，此次运动记录将不会被保存")
                    .setSure("确定")
                    .setSureListener(v -> {
                        RxActivityUtils.finishActivity();
                    });
            sportingShortDialog.show();
        } else if (mChartHeartRate.getData().getEntryCount() <= 0) {
            //       用户当前运动没有心率，提示用户此次记录将不被保存
            sportingShortDialog = new RxDialogSure(mContext)
                    .setTitle("运动提示")
                    .setContent("您的运动数据异常，此次运动记录将不会被保存")
                    .setSure("确定")
                    .setSureListener(v -> RxActivityUtils.finishActivity());
            sportingShortDialog.show();
        } else {
            timer.stopTimer();
            currentTime = 0;
            speakAdd("运动已结束,您一共消耗：" + Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(currentKcal, 1)) + "千卡的能量");
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

    @OnClick({R.id.tv_playOrPause, R.id.tv_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_playOrPause:
                trySporting();
                break;
            case R.id.tv_finish:

                break;
        }
    }
}
