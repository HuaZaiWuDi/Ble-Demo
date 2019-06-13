package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.data.LineDataSet;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.wesmarclothing.mylibrary.net.RxBus;

import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.SportInterface;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.entity.PlanBean;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.HeartRateChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.Number2Chinese;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

/**
 * Created by jk on 2018/7/19.
 */
public class PlanSportingActivity extends BaseSportActivity implements SportInterface {

    private int sportUpWarnCount = 0;
    private int sportDownWarnCount = 0;
    private List<AthlPlanListBean> planList;

    @Override
    public void sportFinish() {
        if (mChartHeartRate.getData().getEntryCount() < 90) {
            //       用户当前运动时间<3min，提示用户此次记录将不被保存
            sportingShortDialog = new RxDialogSure(mContext)
                    .setTitle(getString(R.string.sportTip))
                    .setContent("您当前运动时间过短，此次运动记录将不会被保存")
                    .setSure(getString(R.string.ok))
                    .setSureListener(v -> {
                        RxActivityUtils.finishActivity();
                    });
            sportingShortDialog.show();
        } else {
            sportingFinish(false);
        }
    }


    @Override
    protected void initViews() {
        mHeartRateBean.setPlanFlag(1);
        currentMode = getString(R.string.planSporting);
        super.initViews();

        timeTimer = new MyTimer(() -> {
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
        });

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
        mLayoutLegend.setVisibility(View.VISIBLE);

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
                        if (sportsDataTab == null) return;
                        if (sportsDataTab.getHeartLists().size() < 3)
                            return;
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
        timeTimer.stopTimer();
        uploadData();
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


    //当用户极限运动超过 3 分钟
    MyTimer limitTimer = new MyTimer(3 * 60 * 1000, 3 * 60 * 1000, () ->
            speakAdd(getString(R.string.speech_keepLimitSpeak)));

}
