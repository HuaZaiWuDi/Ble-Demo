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
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxTextviewVertical;
import com.vondear.rxtools.view.SwitchView;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.BleService;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.Number2Chinese;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;

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
    private HeartRateUtil mHeartRateUtil = new HeartRateUtil();

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
        mLayoutLegend.setVisibility(View.GONE);

        RxTextUtils.getBuilder("0.0")
                .append("kcal").setProportion(0.5f)
                .into(mTvKcal);

    }

    private void initVerticalText() {
        mTvVerticalText.setText(11, 0, ContextCompat.getColor(mContext, R.color.GrayWrite));
        mTvVerticalText.setAnimTime(500);
        mTvVerticalText.setTextStillTime(3000);
        mTvVerticalText.setTextList(Arrays.asList("保持燃脂衣与app的连接，可提高数据数据准确性", "如关闭或脱掉减肥衣将自动结束本次运动"));
    }


    @Override
    protected void initNetData() {
        super.initNetData();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!BleTools.getBleManager().isBlueEnable())
            BleTools.getBleManager().enableBluetooth();
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

                        if (BleService.clothingFinish || currentTime == 0) {
                            currentTime = sportsDataTab.getDuration();
                            lineChartUtils.setRealTimeData(sportsDataTab.getAthlRecord_2());
                        } else {
                            lineChartUtils.setRealTimeData(sportsDataTab.getCurHeart());
                        }

                        freeTextSpeak(sportsDataTab.getCurHeart());
                        mTvAvHeartRate.setText(sportsDataTab.getCurHeart() + "");
                        mTvMaxHeartRate.setText(sportsDataTab.getMaxHeart() + "");

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
        byte[] heartRates = BleKey.heartRates;
        int heart_0 = heartRates[0] & 0xff;
        int heart_1 = heartRates[1] & 0xff;
        int heart_2 = heartRates[2] & 0xff;
        int heart_3 = heartRates[3] & 0xff;
        int heart_4 = heartRates[4] & 0xff;
        if (heart_0 <= heart && heart <= heart_1) {
            if (type != 0) {
                mTvSportsStatus.setText("热身");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.brown_ABA08E));
                if (!TextSpeakUtils.isSpeak()) {
                    speakAdd(getString(R.string.speech_warm));
                    type = 0;
                }
            }
        } else if (heart >= heart_1 && heart < heart_2) {
            if (type != 1) {
                mTvSportsStatus.setText("燃脂");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.yellow_FFBC00));
                if (!TextSpeakUtils.isSpeak()) {
                    speakAdd(getString(R.string.speech_grease));
                    type = 1;
                }
            }
        } else if (heart >= heart_2 && heart < heart_3) {
            if (type != 2) {
                mTvSportsStatus.setText("有氧");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.green_61D97F));
                if (!TextSpeakUtils.isSpeak()) {
                    type = 2;
                    speakAdd(getString(R.string.speech_aerobic));
                }
            }
        } else if (heart >= heart_3 && heart < heart_4) {
            if (type != 3) {
                mTvSportsStatus.setText("无氧");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.orange_FF7200));
                if (!TextSpeakUtils.isSpeak()) {
                    type = 3;
                    speakAdd(getString(R.string.speech_anaerobic));
                }
            }
        } else if (heart >= heart_4) {
            if (type != 4) {
                mTvSportsStatus.setText("极限");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.red));
                if (!TextSpeakUtils.isSpeak()) {
                    type = 4;
                    speakAdd(getString(R.string.speech_limit));
                }
            }
        } else if (heart < heart_0) {
            if (type != 5) {
                mTvSportsStatus.setText("静息");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.Gray_DCDAE6));
                if (!TextSpeakUtils.isSpeak()) {
                    type = 5;
                    speakAdd(getString(R.string.speech_limit));
                }
            }
        }
    }


    MyTimer timer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            currentTime++;
            mTvSportsTime.setText(RxFormat.setSec2MS(currentTime));
            mProSportingTime.setProgress(currentTime, false);
            mTvStartSportingTime.setText(RxFormat.setFormatDate(currentTime * 1000, RxFormat.Date_Time_MS));

            lineChartUtils.setRealTimeData((int) (Math.random() * 120 + 80));
            if (currentTime % 180 == 0) {
                speakAdd(getString(R.string.speech_currentKcal) +
                        Number2Chinese.number2Chinese(Double.parseDouble(mTvKcal.getText().toString())) + "千卡的能量");
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
        speakFlush("运动已结束");

//        Bundle bundle = new Bundle();
//        bundle.putString(Key.BUNDLE_DATA_GID, gid);
//        bundle.putBoolean(Key.BUNDLE_SPORTING_PLAN, true);
//        RxActivityUtils.skipActivity(mContext, SportsDetailsFragment.class, bundle);
    }


    @Override
    public void onBackPressed() {
        //异常情况
        //       用户当前运动时间<3min，提示用户此次记录将不被保存
        if (currentTime < 180) {
            new RxDialogSure(mContext)
                    .setTitle("运动提示")
                    .setSure("您当前运动时间过短，此次运动记录将不会被保存！")
                    .show();
        } else {
            new RxDialogSureCancel(mContext)
                    .setContent("运动未结束，是否保存数据？")
                    .setSure("保存")
                    .setSureListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mHeartRateUtil.setSaveComplete(new HeartRateUtil.SaveComplete() {
                                @Override
                                public void complete(String gid) {
                                    if (mContext != null) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Key.BUNDLE_DATA_GID, gid);
                                        bundle.putBoolean(Key.BUNDLE_SPORTING_PLAN, true);
                                        RxActivityUtils.skipActivity(mContext, SportsDetailsFragment.class, bundle);
                                    }
                                }
                            });
                            mHeartRateUtil.uploadHeartRate();
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
