package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
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
import io.reactivex.schedulers.Schedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.PlanBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.HeartRateChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.Number2Chinese;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;

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
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;
    @BindView(R.id.tv_currentTime)
    TextView mTvCurrentTime;
    @BindView(R.id.layout_sportingKcal)
    LinearLayout mLayoutSportingKcal;
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


    private Button btn_Connect;
    private int currentTime = 0, currentHeart = 0, type = -1;//运动上一次状态
    private HeartLineChartUtils lineChartUtils;
    private List<AthlPlanListBean> planList;
    private double sportingScore = 0, sportingKcal = 0, totalSum;
    private HeartRateBean mHeartRateBean = new HeartRateBean();
    private RxDialogSureCancel connectFailDialog;
    private RxDialogSure sportingShortDialog;
    private boolean pause = false;
    private HeartRateUtil mHeartRateUtil = new HeartRateUtil();


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
                    connectTimeoutTimer.stopTimer();
                    dismissAllDialog();
                } else {
                    timer.stopTimer();
                }
                pause = !state;
                startOrPauseSport();
            } else if (Key.ACTION_CLOTHING_STOP.equals(intent.getAction())) {
                finishSporting();
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
        initTopBar();
        initSwitch();
        initTypeface();
        initBroadcast();
        finishAnim();
        lineChartUtils = new HeartLineChartUtils(mChartHeartRate);

        speakAdd("设备已连接、让我们开始运动瘦身课程训练吧！");
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
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        mQMUIAppBarLayout.setTitle("定制运动中");
        mLayoutSportingTime.setVisibility(View.GONE);
        mLayoutSportingKcal.setVisibility(View.VISIBLE);
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
                        timer.startTimer();
                        SportsDataTab sportsDataTab = mHeartRateUtil.addRealTimeData(heartRateData.heartRateData);

                        currentHeart = sportsDataTab.getCurHeart();
                        sportingKcal = sportsDataTab.getKcal();

                        lineChartUtils.setRealTimeData(currentHeart);
                        mTvAvHeartRate.setText(currentHeart + "");
                        mTvMaxHeartRate.setText(sportsDataTab.getMaxHeart() + "");

                        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(sportingKcal, 1))
                                .append("\tkcal").setProportion(0.5f)
                                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                                .into(mTvSportskcal);

                        freeTextSpeak(currentHeart);
                        sportingScore(currentHeart);
                        saveData(sportsDataTab);
                    }
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
        mHeartRateBean.setStepNumber(sportsDataTab.getSteps());

        //如果上传失败则保存本地
        RxCache.getDefault().save(Key.CACHE_ATHL_RECORD_PLAN, mHeartRateBean, CacheTarget.Disk)
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        RxLogUtils.d("心率保存" + aBoolean);
                    }
                });
    }


    /**
     * 全程语音按照课程的每个层级时间和频率进行指导，语音播报逻辑：
     * 1.进入课程实时运动页面（检测到心率）：
     * 运动模式已启动，本次运动时间XXX分钟，开始运动，READY GO！
     * 第一节 xx运动，请调节心率至xx运动区间，保持匀速xxx分钟。
     * 恭喜您完成xx训练，下一节xx运动，请调节心率至xx运动区间，保持匀速xxx分钟。
     * 当完成后提示：
     * 好棒呀，恭喜您完成本次瘦身训练。运动完记得做一组拉伸运动哦！本次训练共计燃烧XXX千卡，再接再厉。
     * 当用户处于危险心率超过3分钟
     * 您当前运动量已超过身体最大极限，请立刻降低运动强度，防止意外损伤！
     * 当处于训练过程中，运动节奏高于（低于）当前要求的水平
     * 请提高（降低）运动强度，保持匀速有节奏的运动才能高效瘦身哦。
     * <p>
     * 连续语音播报时间间隔：30s；
     */

    //瘦身衣运动结束
    private void sportingFinish(boolean isComplete) {
        if (isComplete) {
            speakAdd("好棒呀，恭喜您完成本次瘦身训练。运动完记得做一下拉伸运动哦！本次训练共计燃烧"
                    + Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(sportingKcal, 1)) + "千卡，请继续保持。");
        } else {
            speakAdd("本次训练计划还未完成哦，加油加油，今天的坚持是为了更美的明天。");
        }

        timer.stopTimer();
        currentTime = 0;
        saveHeartRate();
    }


    /**
     * 完成运动区间
     *
     * @param bean
     */
    private void completeHeartRange(AthlPlanListBean bean, AthlPlanListBean nextBean) {
        switch (bean.getRange()) {
            //静息
            case 0:
                break;
            //热身
            case 1:
                speakAdd(" 恭喜您完成热身训练，下一节燃脂运动，请调节心率至燃脂运动区间，保持匀速" + nextBean.getDuration() + "分钟。");
                break;
            //燃脂
            case 2:
                speakAdd(" 恭喜您完成燃脂训练，下一节有氧运动，请调节心率至有氧运动区间，保持匀速" + nextBean.getDuration() + "分钟。");
                break;
            //有氧
            case 3:
                speakAdd(" 恭喜您完成有氧训练，下一节无氧运动，请调节心率至无氧运动区间，保持匀速" + nextBean.getDuration() + "分钟。");
                break;
            //无氧
            case 4:
                speakAdd("您当前处于无氧训练阶段，无氧训练有助于增强肌肉生长，提高基础代谢\n" +
                        "率。建议运动时长不超过 15 分钟。");
                break;
            //极限
            case 5:
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
            float tatolCount = defaultSet.getEntryCount();

            //运动开始语音
            if (count == 1) {
                speakAdd("运动模式已启动，本次课程时间 " + planList.get(planList.size() - 1).getTime() + " 分钟，开始运动");
                mTvHeartCount.setText("1/" + planList.size());
                speakAdd("第一节" + planList.get(0).strRange() + "运动 请调节心率至" + planList.get(0).strRange() + "运动区间，保持匀速" + planList.get(0).getTime() + " 分钟");
            }

            for (int i = 0; i < planList.size(); i++) {
                AthlPlanListBean bean = planList.get(i);
                if (count * 2 == bean.getTime() * 60) {
                    if ((i + 1) < planList.size())
                        completeHeartRange(bean, planList.get((i + 1) % planList.size()));
                    mTvHeartCount.setText((i + 2) + "/" + planList.size());
                }
            }

            mHeartRateBean.setComplete(count / tatolCount);
            //运动结束
            if (count == tatolCount) {
                sportingFinish(true);
                return;
            }
            if (count >= tatolCount) return;

            int score = 0;
            float abs = Math.abs(defaultSet.getEntryForIndex(count).getY() - currentValue);
            if (abs <= 2) {
                score = 50;
            } else if (abs > 2 && abs <= 4) {
                score = 40;
            } else if (abs > 4 && abs <= 6) {
                score = 30;
            } else if (abs > 6 && abs <= 8) {
                score = 20;
            } else if (abs > 8 && abs <= 10) {
                score = 20;
            }
            totalSum += score;
            if (count > 0)
                sportingScore = totalSum / count + count * 50 / tatolCount;

            RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(sportingScore, 1))
                    .append("\t分").setProportion(0.3f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvKcal);


            float width = RxUtils.dp2px(325) * 1f / defaultSet.getEntryCount() * realTimeSet.getEntryCount() + RxUtils.dp2px(24) * 0.5f;
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

    }

    private void initTopBar() {
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
            }
        } else if (heart >= heart_2 && heart < heart_3) {
            if (type != 1) {
                mTvSportsStatus.setText("燃脂");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.yellow_FFBC00));
            }
        } else if (heart >= heart_3 && heart < heart_4) {
            if (type != 2) {
                mTvSportsStatus.setText("有氧");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.green_61D97F));

            }
        } else if (heart >= heart_4 && heart < heart_5) {
            if (type != 3) {
                mTvSportsStatus.setText("无氧");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.orange_FF7200));
            }
        } else if (heart >= heart_5) {
            if (type != 4) {
                mTvSportsStatus.setText("极限");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.red));
            }
        } else if (heart < heart_1) {
            if (type != 5) {
                mTvSportsStatus.setText("静息");
                mTvSportsStatus.setTextColor(getResources().getColor(R.color.Gray_DCDAE6));
            }
        }
    }

    //当用户极限运动超过 3 分钟
    MyTimer limitTimer = new MyTimer(3 * 60 * 1000, 3 * 60 * 1000, new MyTimerListener() {
        @Override
        public void enterTimer() {
            speakAdd("您当前运动量已超过身体最大极限，请立刻降低运动水平，防止意外损伤！");
        }
    });


    MyTimer timer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            currentTime++;

            mTvCurrentTime.setText(RxFormat.setSec2MS(currentTime));
            if (currentTime % 120 == 0) {
                speakAdd(getString(R.string.speech_currentKcal) +
                        Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(sportingKcal, 1)) + "千卡的能量");
            }

            if (type == 4) {
                limitTimer.startTimer();
            } else {
                limitTimer.stopTimer();
            }

            LineDataSet defaultSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("heartPlan", true);
            LineDataSet realTimeSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("RealTime", true);

            if (realTimeSet.getEntryCount() >= defaultSet.getEntryCount()) return;

            if (currentTime >= 30 && currentTime % 30 == 15) {
                int section = Math.abs((Key.HRART_SECTION[6] & 0xFF) - (Key.HRART_SECTION[5] & 0xFF));

                RxLogUtils.d("心率区间：" + section);
                //这里当运动比计划心率差值大于20提示用户块（慢）点
                if ((defaultSet.getEntryForIndex(realTimeSet.getEntryCount()).getY() - currentHeart) >= section) {
                    speakAdd("请快一点，保持匀速有节奏的运动才能高效瘦身");
                } else if ((defaultSet.getEntryForIndex(realTimeSet.getEntryCount()).getY() - currentHeart) <= -section) {
                    speakAdd("请慢一点，保持匀速有节奏的运动才能高效瘦身");
                }
            }

        }
    });

    /**
     * 10秒内，未正常连接，提示用户继续运动（运动保持暂停状态）,结束运动，运动结束
     */
    MyTimer connectTimeoutTimer = new MyTimer(() -> {
        finishSporting();
    }, 10000);


    @Override
    public void onDestroy() {
        unregisterReceiver(registerReceiver);
        timer.stopTimer();
        TextSpeakUtils.stop();
        dismissAllDialog();
        connectTimeoutTimer.stopTimer();
        limitTimer.stopTimer();
        super.onDestroy();
    }

    @OnClick(R.id.tv_kcal)
    public void onViewClicked() {

    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
//        finishSporting();
    }


    /**
     * 运动结束
     */
    private void finishSporting() {
        pause = true;
        startOrPauseSport();

        //未开启运动直接结束
        if (currentTime == 0) {
            RxActivityUtils.finishActivity();
        } else if (currentTime < 180) {
            //       用户当前运动时间<3min，提示用户此次记录将不被保存
            sportingShortDialog = new RxDialogSure(mContext)
                    .setTitle("运动提示")
                    .setContent("您当前运动时间过短，此次运动记录将不会被保存")
                    .setSure("确定")
                    .setSureListener(v -> RxActivityUtils.finishActivity());
            sportingShortDialog.show();
        } else {
            if (BleTools.getInstance().isConnect()) {
                //用户运动到达合理时间，想要退出，提示用户是否结束当前运动
                new RxDialogSureCancel(mContext)
                        .setContent("检测到您正在运动，是否结束当前运动？")
                        .setSure("结束并保存")
                        .setSureListener(v -> {
                            sportingFinish(false);
                        }).show();
            } else {
                sportingFinish(false);
            }
        }
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
                        mHeartRateUtil.clearData(mHeartRateBean);
                        RxLogUtils.d("添加心率：保存成功删除本地缓存：");
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

    private void speakAdd(String text) {
        if (mSwMusic.isOpened() && isVisibility())
            TextSpeakUtils.speakAdd(text);
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
                        connectTimeoutTimer.startTimer();
                    })
                    .setSure("结束运动")
                    .setSureListener(v -> finishSporting());
            connectFailDialog.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
