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
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.entity.PlanBean;
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
public class PlanSportingActivity extends BaseActivity {


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


    private Button btn_Connect;
    private int currentTime = 0;
    private int type = -1;//运动上一次状态
    private HeartLineChartUtils lineChartUtils;
    private HeartRateUtil mHeartRateUtil = new HeartRateUtil(false);
    private List<AthlPlanListBean> planList;
    private double sportingScore = 0, sportingKcal = 0;

    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //监听瘦身衣连接情况
            if (Key.ACTION_CLOTHING_CONNECT.equals(intent.getAction())) {
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_CLOTHING_CONNECT, false);
                btn_Connect.setText(state ? R.string.connected : R.string.disConnected);
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

        RxTextUtils.getBuilder("0.0")
                .append("分").setProportion(0.5f)
                .into(mTvKcal);

        if (BleTools.getInstance().isConnect())
            speakAdd("运动模式已启动、让我们开始运动瘦身课程训练吧！");
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
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        PlanBean planBean = MyAPP.getGson().fromJson(bundle.getString(Key.BUNDLE_SPORTING_PLAN), PlanBean.class);
        planList = planBean.getAthlPlanList();
        int sunTime = 0;
        for (AthlPlanListBean bean : planList) {
            sunTime += bean.getDuration();
            bean.setTime(sunTime);
            RxLogUtils.d("计划:" + bean.toString());
        }
        mQMUIAppBarLayout.setTitle("定制运动中");
        mLayoutPro.setVisibility(View.VISIBLE);
        mLayoutSportingTime.setVisibility(View.GONE);
        mLayoutSportingKcal.setVisibility(View.VISIBLE);
        mTvStartSportingTime.setText(RxFormat.setFormatDate(0, RxFormat.Date_Time_MS));
        mTvEndSportingTime.setText(RxFormat.setFormatDate(sunTime * 60 * 1000, RxFormat.Date_Time_MS));
        lineChartUtils.setPlanLineData(planList);
        mProSportingTime.setMax(sunTime * 60);
        timer.startTimer();

        RxTextUtils.getBuilder("0.0")
                .append("分").setProportion(0.5f)
                .into(mTvKcal);

        RxTextUtils.getBuilder("0.0")
                .append("kcal").setProportion(0.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvSportskcal);

        mTvExpectKcal.setText(getString(R.string.expectKcal, sunTime, planBean.getTotalDeplete()));

        mTvHeartCount.setText("0/" + planList.size());
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

                        planSportingTextSpeak();

                        freeTextSpeak(sportsDataTab.getCurHeart());

                        sportingScore(sportsDataTab.getCurHeart());

                        mTvAvHeartRate.setText(sportsDataTab.getCurHeart() + "");
                        mTvMaxHeartRate.setText(sportsDataTab.getMaxHeart() + "");
                        sportingKcal = sportsDataTab.getKcal();
                        RxTextUtils.getBuilder(sportsDataTab.getKcal() + "")
                                .append("kcal").setProportion(0.5f)
                                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                                .into(mTvSportskcal);
                    }
                });
    }


    /**
     * 定制语音播报逻辑：
     * 1、在已连接状态穿上衣服或者进入实时训练界面时（2 秒后）：运动模式已启动、让我们开始运动瘦身课程训练吧！
     * 2、检测到用户在运动时本次运动时间 XXX 分钟，开始热身，READY GO！
     * 3、热身阶段心率区间时您当前处于热身阶段，请保持匀速 XX 分钟。3、5 分钟倒计时结束后播报恭喜您完成热身训练，请提高运动量进行燃脂训练 XXX 分
     * 4、燃脂阶段心率区间时
     * 您当前处于燃脂阶段，请保持匀速 XXX 分钟。
     * 5、当燃脂阶段完成后提示：
     * 恭喜您完成燃脂训练，请提高运动量进行有氧训练 XX 分钟。
     * 6、当完成有氧训练心率区间时
     * 您当前处于有氧训练阶段，请保持匀速 XX 分钟。
     * 当完成后提示：
     * 恭喜您完成有氧训练，让我们放松一下吧，请降低运动量至燃脂训练 5 分
     * 钟。
     * <p>
     * 当完成后提示：
     * 恭喜您完成放松训练，请降低运动量至热身阶段 XXX 分钟。
     * 当完成后提示：
     * 好棒呀，恭喜您完成本次瘦身训练。运动完记得做一下拉伸运动哦！
     * 本次训练共计燃烧 XXX 卡路里，请继续保持。
     * 6、当用户运动到无氧阶段时
     * 您当前处于无氧训练阶段，无氧训练有助于增强肌肉生长，提高基础代谢
     * 率。建议运动时长不超过 15 分钟
     * 7、当用户处于极限训练阶段时
     * 您当前处于极限训练阶段，请慢一点。高负荷运动不利于运动效果，还容易
     * 导致身体损伤哦，科学运动才能达到良好的训练效果。
     * 8、当用户极限运动超过 3 分钟
     * 您当前运动量已超过身体最大极限，请立刻降低运动水平，防止意外损伤！
     * 9、系统默认的课程计划还没结束，用户提前放弃的。
     * 本次训练计划还未达标哦，加油加油，今天的坚持是为了更美的明天。
     * 10、当处于训练过程中，运动节奏超过当前要求的水平，快了或者慢了。
     * 请快（慢）一点，保持匀速有节奏的运动才能高效瘦身哦。
     */
    private void planSportingTextSpeak() {
        //运动开始语音
        if (currentTime == 2) {
            speakAdd("本次运动时间 " + planList.get(planList.size() - 1).getTime() + " 分钟，开始热身，READY GO！");
            intoHeartRange(planList.get(0));
            mTvHeartCount.setText("1/" + planList.size());
        }

        for (int i = 0; i < planList.size(); i++) {
            AthlPlanListBean bean = planList.get(i);
            if (currentTime == bean.getTime() * 60) {
                completeHeartRange(bean);
                intoHeartRange(bean);
                mTvHeartCount.setText((i + 1) + "/" + planList.size());
            }
        }

        //运动结束
        if (currentTime == planList.get(planList.size() - 1).getTime() * 60) {
            speakAdd("好棒呀，恭喜您完成本次瘦身训练。运动完记得做一下拉伸运动哦！本次训练共计燃烧"
                    + Number2Chinese.number2Chinese(sportingKcal) + "卡路里，请继续保持。");
            timer.stopTimer();
            mHeartRateUtil.setSportingScore(sportingScore);
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
    }


    /**
     * 进入运动区间
     *
     * @param bean
     */
    private void intoHeartRange(AthlPlanListBean bean) {
        switch (bean.getRange()) {
            case 80:
                break;
            case 100:
                speakAdd("您当前处于热身阶段，请保持匀速运动" + bean.getDuration() + "分钟");
                break;
            case 120:
                speakAdd("您当前处于燃脂阶段，请保持匀速运动" + bean.getDuration() + "分钟");
                break;
            case 140:
                speakAdd("您当前处于有氧阶段，请保持匀速运动" + bean.getDuration() + "分钟");
                break;
            case 160:
                speakAdd("您当前处于无氧训练阶段，无氧训练有助于增强肌肉生长，提高基础代谢\n" +
                        "率。建议运动时长不超过 15 分钟。");
                break;
            case 180:
                speakAdd("您当前处于极限训练阶段，请慢一点。高负荷运动不利于运动效果，还容易\n" +
                        "导致身体损伤哦，科学运动才能达到良好的训练效果。");
                break;
        }
    }

    /**
     * 完成运动区间
     *
     * @param bean
     */
    private void completeHeartRange(AthlPlanListBean bean) {
        switch (bean.getRange()) {
            case 80:
                break;
            case 100:
                speakAdd("恭喜您完成热身训练，请提高运动量进行燃脂训练");
                break;
            case 120:
                speakAdd("恭喜您完成燃脂训练，请提高运动量进行有氧训练");
                break;
            case 140:
                speakAdd("恭喜您完成有氧训练，让我们放松一下吧，请降低运动量至燃脂训练");
                break;
            case 160:
                speakAdd("您当前处于无氧训练阶段，无氧训练有助于增强肌肉生长，提高基础代谢\n" +
                        "率。建议运动时长不超过 15 分钟。");
                break;
            case 180:
                break;
        }
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
            int tatolCount = defaultSet.getEntryCount();
            if (count >= tatolCount) return;
            double totalScore = 0;
            int totalSum = 0;
            int score = 0;
            float abs = Math.abs(defaultSet.getEntryForIndex(count).getY() - currentValue);
            if (abs < 2) {
                score = 50;
            } else if (abs >= 2 && abs < 4) {
                score = 40;
            } else if (abs >= 4 && abs < 6) {
                score = 30;
            } else if (abs >= 6 && abs < 8) {
                score = 20;
            } else if (abs >= 8 && abs < 10) {
                score = 20;
            } else {
                score = 0;
            }
            totalSum += score;
            if (count > 0)
                totalScore = totalSum / count + count / tatolCount * 50f;

            sportingScore = totalScore;

            RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(totalScore, 1))
                    .append("分").setProportion(0.5f)
                    .into(mTvKcal);


//            //这里当运动比计划心率差值大于20提示用户块（慢）点
//            if (defaultSet.getEntryForIndex(count).getY() - currentValue > 20) {
//                speakFlush("请快一点，保持匀速有节奏的运动才能高效瘦身哦");
//            } else if (defaultSet.getEntryForIndex(count).getY() - currentValue < -20) {
//                speakFlush("请慢一点，保持匀速有节奏的运动才能高效瘦身哦");
//            }
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
            }
        } else if (heart >= heart_1 && heart < heart_2) {
            if (type != 1) {
                mTvSportsStatus.setText("燃脂");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.yellow_FFBC00));
            }
        } else if (heart >= heart_2 && heart < heart_3) {
            if (type != 2) {
                mTvSportsStatus.setText("有氧");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.green_61D97F));

            }
        } else if (heart >= heart_3 && heart < heart_4) {
            if (type != 3) {
                mTvSportsStatus.setText("无氧");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.orange_FF7200));

            }
        } else if (heart >= heart_4) {
            if (type != 4) {
                mTvSportsStatus.setText("极限");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.red));
                limitTimer.startTimer();
            }
        } else if (heart < heart_0) {
            if (type != 5) {
                mTvSportsStatus.setText("静息");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.Gray_DCDAE6));
            }
        }
        if (type != 4) {
            limitTimer.stopTimer();
        }
    }

    //当用户极限运动超过 3 分钟
    MyTimer limitTimer = new MyTimer(3 * 60 * 1000, 3 * 60 * 1000, new MyTimerListener() {
        @Override
        public void enterTimer() {
            speakFlush("您当前运动量已超过身体最大极限，请立刻降低运动水平，防止意外损伤！");
        }
    });


    MyTimer timer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            currentTime++;
            mTvSportsTime.setText(RxFormat.setSec2MS(currentTime));
            mProSportingTime.setProgress(currentTime, false);
            mTvStartSportingTime.setText(RxFormat.setFormatDate(currentTime * 1000, RxFormat.Date_Time_MS));


            int heart = (int) (Math.random() * 120 + 80);
            lineChartUtils.setRealTimeData(heart);

            planSportingTextSpeak();
            freeTextSpeak(heart);
            sportingScore(heart);

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
        speakFlush("本次训练计划还未完成哦，加油加油，今天的坚持是为了更美的明天。");
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
                            mHeartRateUtil.setSportingScore(sportingScore);
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
