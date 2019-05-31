package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineDataSet;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.SportInterface;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.HeartRateChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.utils.Number2Chinese;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

/**
 * Created by jk on 2018/7/19.
 */
public class SportingActivity extends BaseSportActivity implements SportInterface {


    @Override
    public void sportFinish() {
//        if (mChartHeartRate.getData().getEntryCount() < 90) {
//            //       用户当前运动时间<3min，提示用户此次记录将不被保存
//            sportingShortDialog = new RxDialogSure(mContext)
//                    .setTitle(getString(R.string.sportTip))
//                    .setContent("您当前运动时间过短，此次运动记录将不会被保存")
//                    .setSure(getString(R.string.ok))
//                    .setSureListener(v -> {
//                        RxActivityUtils.finishActivity();
//                    });
//            sportingShortDialog.show();
//        } else
        {
            timeTimer.stopTimer();
            speakAdd(getString(R.string.speech_freeSportFinish,
                    Number2Chinese.number2Chinese(currentTime / 60 + "") + "分钟" +
                            (currentTime % 60 == 0 ? "" : Number2Chinese.number2Chinese(currentTime % 60 + "") + "秒"),
                    Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(currentKcal, 1))));
            //如果检测到运动已经结束，直接保存数据并进入详情页
            uploadData();
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_sportsing;
    }


    @Override
    protected void initViews() {
        mHeartRateBean.setPlanFlag(0);
        currentMode = getString(R.string.freeSporting);
        super.initViews();
        XAxis xAxis = mChartHeartRate.getXAxis();
        xAxis.setAxisMaximum(90);
        mChartHeartRate.invalidate();

        RxTextUtils.getBuilder("0.0")
                .append("\tkcal").setProportion(0.3f)
                .into(mTvKcal);

        speakAdd(getString(R.string.speech_sprotStart));

        timeTimer = new MyTimer(() -> {
            currentTime++;

            mTvSportsTime.setText(RxFormat.setSec2MS(currentTime));
            mTvCurrentTime.setText(RxFormat.setSec2MS(currentTime));
            if (currentTime % (60 * 3) == 0) {
                speakAdd(getString(R.string.speech_currentKcal, mTvSportsStatus.getText().toString()));
            }
        });
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
                                    Number2Chinese.number2Chinese(RxFormatValue.fromat4S5R(kilometre, 2)),
                                    Number2Chinese.number2Chinese((int) currentKcal + ""),
                                    Number2Chinese.number2Chinese(stepSpeed / 60 + "") + "分钟"
                                            + Number2Chinese.number2Chinese(stepSpeed % 60 + "") + "秒",
                                    Number2Chinese.number2Chinese(currentTime / 60 + "") + "分钟" +
                                            (currentTime % 60 == 0 ? "" : Number2Chinese.number2Chinese(currentTime % 60 + "") + "秒")
                            ));
                        }

                        lineChartUtils.setRealTimeData(sportsDataTab.getReversePace());
                        freeTextSpeak(sportsDataTab.getReversePace());
                        guideLineMove();
                        saveData(sportsDataTab);

                        //撤销最大显示范围，让一屏显示所有数据
                        if (mChartHeartRate.getData().getEntryCount() > 90) {
                            XAxis xAxis = mChartHeartRate.getXAxis();
                            xAxis.resetAxisMaximum();
                        }
                    }
                });
    }

    /**
     * 图上指导线移动
     */
    private void guideLineMove() {
        LineDataSet realTimeSet = (LineDataSet) mChartHeartRate.getData().getDataSetByLabel("RealTime", true);
        int count = realTimeSet.getEntryCount();

        int width = RxUtils.dp2px(325) / 90 * count;
        if (width < RxUtils.dp2px(24)) {
            width = RxUtils.dp2px(30);
        } else if (width > RxUtils.dp2px(325)) {
            width = RxUtils.dp2px(320);
        }

        ViewGroup.LayoutParams layoutParams = mTvCurrentTime.getLayoutParams();
        layoutParams.width = width;
        mTvCurrentTime.setLayoutParams(layoutParams);
    }

}
