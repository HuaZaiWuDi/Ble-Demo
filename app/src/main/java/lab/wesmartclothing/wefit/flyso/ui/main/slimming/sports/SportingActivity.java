package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRandom;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxTextviewVertical;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.SwitchView;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.zchu.rxcache.RxCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateItemBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateToKcal;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.Number2Chinese;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;

/**
 * Created by jk on 2018/7/19.
 */
public class SportingActivity extends BaseActivity {


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
    @BindView(R.id.layout_tip)
    LinearLayout mLayoutTip;
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
    @BindView(R.id.tv_verticalText)
    RxTextviewVertical mTvVerticalText;
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

    private Button btn_Connect;
    private int currentTime = 0;
    private int type = -1;//运动上一次状态
    private HeartLineChartUtils lineChartUtils;
    private HeartRateBean mHeartRateBean = new HeartRateBean();
    private List<HeartRateItemBean> heartLists = new ArrayList<>();
    private double currentKcal = 0;
    private int maxHeartRate = 0;
    private RxDialogSureCancel rxDialogSureCancel;

    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mContext == null) return;
            //监听瘦身衣连接情况
            if (Key.ACTION_CLOTHING_CONNECT.equals(intent.getAction())) {
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_CLOTHING_CONNECT, false);
                btn_Connect.setText(state ? R.string.connected : R.string.disConnected);
                speakAdd(state ? "设备已连接" : "设备连接已断开");
                if (state) {
                    timer.startTimer();
                } else {
                    timer.stopTimer();
                }
            } else if (Key.ACTION_CLOTHING_STOP.equals(intent.getAction())) {
                //用户当前运动时间<3min，提示用户此次记录将不被保存
                if (currentTime < 180) {
                    if (rxDialogSureCancel != null && !rxDialogSureCancel.isShowing()) {
                        rxDialogSureCancel.show();
                    }
                } else {
                    //瘦身衣运动结束
                    if (!heartLists.isEmpty())
                        stopSporting();
                }
            }
        }
    };


    @Override
    protected int layoutId() {
        return R.layout.activity_sportsing;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initVerticalText();
        initTopBar();
        initSwitch();
        initTypeface();
        initMyDialog();
        initBroadcast();
        initChart();


        mLayoutLegend.setVisibility(View.GONE);

        RxTextUtils.getBuilder("0.0")
                .append("\tkcal").setProportion(0.3f)
                .into(mTvKcal);

        speakAdd("运动模式已启动，让我们开始自由运动瘦身训练吧");

    }

    private void initChart() {
        lineChartUtils = new HeartLineChartUtils(mChartHeartRate);
//        mChartHeartRate.setViewPortOffsets(0, 0, RxUtils.dp2px(86), 0);

        XAxis xAxis = mChartHeartRate.getXAxis();
        xAxis.setAxisMaximum(30);
        mChartHeartRate.invalidate();
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Key.ACTION_CLOTHING_CONNECT);
        filter.addAction(Key.ACTION_CLOTHING_STOP);
        registerReceiver(registerReceiver, filter);
    }

    private void initMyDialog() {
        //       用户当前运动时间<3min，提示用户此次记录将不被保存
        rxDialogSureCancel = new RxDialogSureCancel(mContext)
                .setTitle("运动提示")
                .setContent("您当前运动时间过短，此次运动记录将不会被保存")
                .setSure("确定")
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxActivityUtils.finishActivity();
                    }
                });
    }


    float text = 100;
    boolean b = false;
    MyTimer test = new MyTimer(500, 1000, new MyTimerListener() {
        @Override
        public void enterTimer() {
            if (mChartHeartRate.getData().getEntryCount() > 30) {
                XAxis xAxis = mChartHeartRate.getXAxis();
                xAxis.resetAxisMaximum();
            }

            lineChartUtils.setRealTimeData(RxRandom.getRandom(80, 180));

            LineDataSet realTimeSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("RealTime", true);
            int count = realTimeSet.getEntryCount();

            int width = RxUtils.dp2px(325) / 30 * count;
            if (width < RxUtils.dp2px(24)) {
                width = RxUtils.dp2px(30);
            } else if (width > RxUtils.dp2px(325)) {
                width = RxUtils.dp2px(320);
            }

            ViewGroup.LayoutParams layoutParams = mTvCurrentTime.getLayoutParams();
            layoutParams.width = width;
            mTvCurrentTime.setLayoutParams(layoutParams);

        }
    });


    private void initVerticalText() {
        mTvVerticalText.setText(11, 0, ContextCompat.getColor(mContext, R.color.GrayWrite));
        mTvVerticalText.setAnimTime(500);
        mTvVerticalText.setTextStillTime(3000);
        mTvVerticalText.setTextList(Arrays.asList("保持燃脂衣与app的连接，可提高数据准确性", "如关闭或脱掉减肥衣将自动结束本次运动"));
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
        mTvVerticalText.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTvVerticalText.stopAutoScroll();
    }


    @Override
    protected void initRxBus2() {
        super.initRxBus2();
        RxBus.getInstance().register2(SportsDataTab.class)
                .compose(RxComposeUtils.<SportsDataTab>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<SportsDataTab>() {
                    @Override
                    protected void _onNext(SportsDataTab sportsDataTab) {
                        RxLogUtils.i("瘦身衣心率数据：" + sportsDataTab.toString());

                        timer.startTimer();
                        int currentHeart = sportsDataTab.getCurHeart();

                        lineChartUtils.setRealTimeData(currentHeart);

                        if (mChartHeartRate.getData().getEntryCount() > 30) {
                            XAxis xAxis = mChartHeartRate.getXAxis();
                            xAxis.resetAxisMaximum();
                        }


                        HeartRateItemBean heartRateTab = new HeartRateItemBean();
                        heartRateTab.setHeartRate(currentHeart);
                        heartRateTab.setHeartTime(sportsDataTab.getDate());
                        heartRateTab.setStepTime(2);
                        heartRateTab.setIsfree(true);
                        heartLists.add(heartRateTab);

                        mHeartRateBean.setStepNumber(currentHeart);

                        freeTextSpeak(currentHeart);

                        if (currentHeart > (Key.HRART_SECTION[6] & 0xFF)) {
                            currentHeart = (Key.HRART_SECTION[6] & 0xFF);
                        } else if (currentHeart < (Key.HRART_SECTION[0] & 0xFF)) {
                            currentHeart = (Key.HRART_SECTION[0] & 0xFF);
                        }

                        mTvAvHeartRate.setText(currentHeart + "");


                        maxHeartRate = Math.max(maxHeartRate, currentHeart);
                        mTvMaxHeartRate.setText(maxHeartRate + "");

                        //心率处于静息心率区间时不计算卡路里，
                        if (currentHeart >= Key.HRART_SECTION[1]) {
                            currentKcal += RxFormatValue.format4S5R(HeartRateToKcal.getCalorie(currentHeart, 2f / 3600), 1);
                        }

                        RxLogUtils.d("当前kacl：" + currentKcal);

                        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(currentKcal, 1))
                                .append("\tkcal").setProportion(0.3f)
                                .into(mTvKcal);

                        mHeartRateBean.setTotalCalorie(currentKcal);
                        mHeartRateBean.setHeartList(heartLists);


                        RxCache.getDefault().save(Key.CACHE_ATHL_RECORD_FREE, mHeartRateBean)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new RxSubscriber<Boolean>() {
                                    @Override
                                    protected void _onNext(Boolean aBoolean) {
                                        RxLogUtils.d("心率保存" + aBoolean);
                                    }
                                });

                        LineDataSet realTimeSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("RealTime", true);
                        int count = realTimeSet.getEntryCount();

                        int width = RxUtils.dp2px(325) / 30 * count;
                        if (width < RxUtils.dp2px(24)) {
                            width = RxUtils.dp2px(30);
                        } else if (width > RxUtils.dp2px(325)) {
                            width = RxUtils.dp2px(320);
                        }

                        ViewGroup.LayoutParams layoutParams = mTvCurrentTime.getLayoutParams();
                        layoutParams.width = width;
                        mTvCurrentTime.setLayoutParams(layoutParams);

                    }
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

    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("自由运动中");
        btn_Connect = mQMUIAppBarLayout.addRightTextButton(getString(
                !BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)) ? R.string.unBind : BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected), R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
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
        super.onDestroy();
    }

    @OnClick(R.id.tv_kcal)
    public void onViewClicked() {

    }

    //运动停止
    private void stopSporting() {
        timer.stopTimer();
        currentTime = 0;

        speakAdd("运动已结束,您一共消耗：" + Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(currentKcal, 1)) + "千卡的能量");

        //如果检测到运动已经结束，直接保存数据并进入详情页
        saveHeartRate();
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
        //未开启运动直接结束
        if (currentTime == 0) {
            RxActivityUtils.finishActivity();
        } else if (currentTime < 180) {
            //       用户当前运动时间<3min，提示用户此次记录将不被保存
            if (rxDialogSureCancel != null && !rxDialogSureCancel.isShowing()) {
                rxDialogSureCancel.show();
            }
        } else {
            //用户运动到达合理时间，想要退出，提示用户是否结束当前运动
            new RxDialogSureCancel(mContext)
                    .setContent("检测到您正在运动，是否结束当前运动？")
                    .setSure("结束并保存")
                    .setSureListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveHeartRate();
                        }
                    }).show();
        }
    }


    private void speakAdd(String text) {
        if (mSwMusic.isOpened() && isVisibility())
            TextSpeakUtils.speakAdd(text);
    }


}
