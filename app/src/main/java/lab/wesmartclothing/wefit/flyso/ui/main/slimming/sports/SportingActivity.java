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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxTextviewVertical;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.SwitchView;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.zchu.rxcache.CacheTarget;
import com.zchu.rxcache.RxCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.sql.HeartRateTab;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.Number2Chinese;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    @BindView(R.id.tv_startSportingTime)
    TextView mTvStartSportingTime;
    @BindView(R.id.pro_sportingTime)
    RxRoundProgressBar mProSportingTime;
    @BindView(R.id.tv_endSportingTime)
    TextView mTvEndSportingTime;
    @BindView(R.id.layout_pro)
    LinearLayout mLayoutPro;
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


    private Button btn_Connect;
    private int currentTime = 0;
    private int type = -1;//运动上一次状态
    private HeartLineChartUtils lineChartUtils;
    private HeartRateBean mHeartRateBean = new HeartRateBean();
    private List<HeartRateTab> heartLists = new ArrayList<>();
    private double currentKcal = 0;

    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //监听瘦身衣连接情况
            if (Key.ACTION_CLOTHING_CONNECT.equals(intent.getAction())) {
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_CLOTHING_CONNECT, false);
                btn_Connect.setText(state ? R.string.connected : R.string.disConnected);
                speakFlush(state ? "设备已连接" : "设备连接已断开");

            } else if (Key.ACTION_CLOTHING_STOP.equals(intent.getAction())) {
                //瘦身衣运动结束
                if (mContext != null)
                    stopSporting();
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
        IntentFilter filter = new IntentFilter();
        filter.addAction(Key.ACTION_CLOTHING_CONNECT);
        filter.addAction(Key.ACTION_CLOTHING_STOP);
        registerReceiver(registerReceiver, filter);

        lineChartUtils = new HeartLineChartUtils(mChartHeartRate);

        XAxis xAxis = mChartHeartRate.getXAxis();
        xAxis.setAxisMaximum(30);
        mChartHeartRate.invalidate();

        mLayoutLegend.setVisibility(View.GONE);

        RxTextUtils.getBuilder("0.0")
                .append("kcal").setProportion(0.5f)
                .into(mTvKcal);
    }


    float text = 100;
    boolean b = false;
    MyTimer test = new MyTimer(500, 100, new MyTimerListener() {
        @Override
        public void enterTimer() {
            if (mChartHeartRate.getData().getEntryCount() > 30) {
                XAxis xAxis = mChartHeartRate.getXAxis();
                xAxis.resetAxisMaximum();
            }

            if (mChartHeartRate.getData().getEntryCount() % 100 == 0) {
                b = !b;
            } else if (mChartHeartRate.getData().getEntryCount() % 49 == 0) {
                b = !b;
            }
            if (b) {
                text++;
            } else {
                text--;
            }
            lineChartUtils.setRealTimeData(text);
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

                        lineChartUtils.setRealTimeData(sportsDataTab.getCurHeart());

                        if (mChartHeartRate.getData().getEntryCount() > 30) {
                            XAxis xAxis = mChartHeartRate.getXAxis();
                            xAxis.resetAxisMaximum();
                        }


                        HeartRateTab heartRateTab = new HeartRateTab();
                        heartRateTab.setHeartRate(sportsDataTab.getCurHeart());
                        heartRateTab.setHeartTime(sportsDataTab.getDate());
                        heartRateTab.setStepTime(2);
                        heartRateTab.setIsfree(true);
                        heartLists.add(heartRateTab);

                        //如果上传失败则保存本地
                        RxCache.getDefault().save(Key.CACHE_ATHL_RECORD_FREE, heartLists, CacheTarget.Disk)
                                .subscribe(new RxSubscriber<Boolean>() {
                                    @Override
                                    protected void _onNext(Boolean aBoolean) {
                                        RxLogUtils.d("心率保存成功");
                                    }
                                });

                        mHeartRateBean.setStepNumber(sportsDataTab.getSteps());

                        freeTextSpeak(sportsDataTab.getCurHeart());
                        mTvAvHeartRate.setText(sportsDataTab.getCurHeart() + "");
                        mTvMaxHeartRate.setText(sportsDataTab.getMaxHeart() + "");

                        currentKcal = RxFormatValue.format4S5R(sportsDataTab.getKcal(), 1);
                        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(sportsDataTab.getKcal(), 1))
                                .append("kcal").setProportion(0.5f)
                                .into(mTvKcal);
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
        mSwMusic.setOpened(SPUtils.getBoolean(SPKey.SP_VoiceTip, false));

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

            if (currentTime % 180 == 0) {
                speakAdd(getString(R.string.speech_currentKcal) +
                        Number2Chinese.number2Chinese(currentKcal) + "卡路里的能量");
            }
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
        speakFlush("运动已结束,您一共消耗：" + Number2Chinese.number2Chinese(currentKcal) + "卡路里的能量");

        //如果检测到运动已经结束，直接保存数据并进入详情页
        saveHeartRate();
    }

    public void saveHeartRate() {
        mHeartRateBean.setHeartList(heartLists);
        String s = MyAPP.getGson().toJson(mHeartRateBean, HeartRateBean.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addAthleticsInfo(body))
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
                    protected void _onError(String error) {
                        super._onError(error);
                        RxToast.normal("保存失败");
                        RxActivityUtils.finishActivity();
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
            new RxDialogSureCancel(mContext)
                    .setTitle("运动提示")
                    .setContent("您当前运动时间过短，此次运动记录将不会被保存")
                    .setSure("确定")
                    .setSureListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RxActivityUtils.finishActivity();
                        }
                    }).show();
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

    private void speakFlush(String text) {
        if (mSwMusic.isOpened())
            TextSpeakUtils.speakFlush(text);
    }

    private void speakAdd(String text) {
        if (mSwMusic.isOpened())
            TextSpeakUtils.speakAdd(text);
    }
}
