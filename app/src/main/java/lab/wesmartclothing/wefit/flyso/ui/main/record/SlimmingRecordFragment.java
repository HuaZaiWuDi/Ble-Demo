package lab.wesmartclothing.wefit.flyso.ui.main.record;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.aboutCarmera.RxImageTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.model.tool.RxQRCode;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.chart.bar.BarVerticalChart;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxLinearLayout;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.waveview.RxWaveHelper;
import com.vondear.rxtools.view.waveview.RxWaveView;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.SlimmingRecordBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.NetWorkType;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.energy.EnergyActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.FoodRecommend;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SmartClothingFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightRecordFragment;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.HealthLevelView;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.main.record
 * @FileName SlimmingRecordFragment
 * @Date 2018/10/31 15:50
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class SlimmingRecordFragment extends BaseAcFragment {


    @BindView(R.id.tv_continuousRecord)
    TextView mTvContinuousRecord;
    @BindView(R.id.tv_progress)
    TextView mTvProgress;
    @BindView(R.id.img_head)
    ImageView mImgHead;
    @BindView(R.id.img_email)
    ImageView mImgEmail;
    @BindView(R.id.pro_complete)
    RxWaveView mProComplete;
    @BindView(R.id.img_breakfast)
    RxImageView mImgBreakfast;
    @BindView(R.id.img_lunch)
    RxImageView mImgLunch;
    @BindView(R.id.img_dinner)
    RxImageView mImgDinner;
    @BindView(R.id.img_sporting)
    RxImageView mImgSporting;
    @BindView(R.id.img_weigh)
    RxImageView mImgWeigh;
    @BindView(R.id.img_userImg)
    RxImageView mImgUserImg;
    @BindView(R.id.iv_Healthy)
    ImageView mIvHealthy;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.layout_Health_title)
    RelativeLayout mLayoutHealthTitle;
    @BindView(R.id.tv_weightInfo)
    TextView mTvWeightInfo;
    @BindView(R.id.tv_healthScore)
    TextView mTvHealthScore;
    @BindView(R.id.tv_healthDate)
    TextView mTvHealthDate;
    @BindView(R.id.tv_healthInfo)
    TextView mTvHealthInfo;
    @BindView(R.id.Healthy_level)
    HealthLevelView mHealthyLevel;
    @BindView(R.id.layout_title)
    RelativeLayout mLayoutTitle;
    @BindView(R.id.iv_weight)
    ImageView mIvWeight;
    @BindView(R.id.layout_weight_title)
    RxLinearLayout mLayoutWeightTitle;
    @BindView(R.id.mLineChart)
    LineChart mMLineChart;
    @BindView(R.id.tv_currentWeight)
    TextView mTvCurrentWeight;
    @BindView(R.id.tv_weight_chart_1)
    TextView mTvWeightChart1;
    @BindView(R.id.tv_weight_chart_2)
    TextView mTvWeightChart2;
    @BindView(R.id.tv_weight_chart_3)
    TextView mTvWeightChart3;
    @BindView(R.id.tv_weight_chart_4)
    TextView mTvWeightChart4;
    @BindView(R.id.tv_weight_chart_5)
    TextView mTvWeightChart5;
    @BindView(R.id.tv_weight_chart_6)
    TextView mTvWeightChart6;
    @BindView(R.id.tv_weight_chart_7)
    RxTextView mTvWeightChart7;
    @BindView(R.id.iv_diet)
    ImageView mIvDiet;
    @BindView(R.id.layout_diet_title)
    RxLinearLayout mLayoutDietTitle;
    @BindView(R.id.tv_currentDiet)
    TextView mTvCurrentDiet;
    @BindView(R.id.dietProgress_1)
    BarVerticalChart mDietProgress1;
    @BindView(R.id.dietProgress_2)
    BarVerticalChart mDietProgress2;
    @BindView(R.id.dietProgress_3)
    BarVerticalChart mDietProgress3;
    @BindView(R.id.dietProgress_4)
    BarVerticalChart mDietProgress4;
    @BindView(R.id.dietProgress_5)
    BarVerticalChart mDietProgress5;
    @BindView(R.id.dietProgress_6)
    BarVerticalChart mDietProgress6;
    @BindView(R.id.dietProgress_7)
    BarVerticalChart mDietProgress7;
    @BindView(R.id.layout_dietChart)
    LinearLayout mLayoutDietChart;
    @BindView(R.id.line_chart_diet)
    View mLineChartDiet;
    @BindView(R.id.tv_diet_chart_1)
    TextView mTvDietChart1;
    @BindView(R.id.tv_diet_chart_2)
    TextView mTvDietChart2;
    @BindView(R.id.tv_diet_chart_3)
    TextView mTvDietChart3;
    @BindView(R.id.tv_diet_chart_4)
    TextView mTvDietChart4;
    @BindView(R.id.tv_diet_chart_5)
    TextView mTvDietChart5;
    @BindView(R.id.tv_diet_chart_6)
    TextView mTvDietChart6;
    @BindView(R.id.tv_diet_chart_7)
    RxTextView mTvDietChart7;
    @BindView(R.id.iv_sports)
    ImageView mIvSports;
    @BindView(R.id.layout_sports_title)
    RxLinearLayout mLayoutSportsTitle;
    @BindView(R.id.tv_currentKcal)
    TextView mTvCurrentKcal;
    @BindView(R.id.sportingProgress_1)
    BarVerticalChart mSportingProgress1;
    @BindView(R.id.sportingProgress_2)
    BarVerticalChart mSportingProgress2;
    @BindView(R.id.sportingProgress_3)
    BarVerticalChart mSportingProgress3;
    @BindView(R.id.sportingProgress_4)
    BarVerticalChart mSportingProgress4;
    @BindView(R.id.sportingProgress_5)
    BarVerticalChart mSportingProgress5;
    @BindView(R.id.sportingProgress_6)
    BarVerticalChart mSportingProgress6;
    @BindView(R.id.sportingProgress_7)
    BarVerticalChart mSportingProgress7;
    @BindView(R.id.layout_sportingChart)
    LinearLayout mLayoutSportingChart;
    @BindView(R.id.line_chart)
    View mLineChart;
    @BindView(R.id.tv_sporting_chart_1)
    TextView mTvSportingChart1;
    @BindView(R.id.tv_sporting_chart_2)
    TextView mTvSportingChart2;
    @BindView(R.id.tv_sporting_chart_3)
    TextView mTvSportingChart3;
    @BindView(R.id.tv_sporting_chart_4)
    TextView mTvSportingChart4;
    @BindView(R.id.tv_sporting_chart_5)
    TextView mTvSportingChart5;
    @BindView(R.id.tv_sporting_chart_6)
    TextView mTvSportingChart6;
    @BindView(R.id.tv_sporting_chart_7)
    RxTextView mTvSportingChart7;
    @BindView(R.id.iv_energy)
    ImageView mIvEnergy;
    @BindView(R.id.layout_energy_title)
    RxLinearLayout mLayoutEnergyTitle;
    @BindView(R.id.tv_currentEnergy)
    TextView mTvCurrentEnergy;
    @BindView(R.id.energyProgress_1)
    BarVerticalChart mEnergyProgress1;
    @BindView(R.id.energyProgress_2)
    BarVerticalChart mEnergyProgress2;
    @BindView(R.id.energyProgress_3)
    BarVerticalChart mEnergyProgress3;
    @BindView(R.id.energyProgress_4)
    BarVerticalChart mEnergyProgress4;
    @BindView(R.id.energyProgress_5)
    BarVerticalChart mEnergyProgress5;
    @BindView(R.id.energyProgress_6)
    BarVerticalChart mEnergyProgress6;
    @BindView(R.id.energyProgress_7)
    BarVerticalChart mEnergyProgress7;
    @BindView(R.id.layout_energyChart)
    LinearLayout mLayoutEnergyChart;
    @BindView(R.id.line_chart_energy)
    View mLineChartEnergy;
    @BindView(R.id.tv_energy_chart_1)
    TextView mTvEnergyChart1;
    @BindView(R.id.tv_energy_chart_2)
    TextView mTvEnergyChart2;
    @BindView(R.id.tv_energy_chart_3)
    TextView mTvEnergyChart3;
    @BindView(R.id.tv_energy_chart_4)
    TextView mTvEnergyChart4;
    @BindView(R.id.tv_energy_chart_5)
    TextView mTvEnergyChart5;
    @BindView(R.id.tv_energy_chart_6)
    TextView mTvEnergyChart6;
    @BindView(R.id.tv_energy_chart_7)
    RxTextView mTvEnergyChart7;
    @BindView(R.id.scroll)
    NestedScrollView mScroll;
    @BindView(R.id.prant)
    LinearLayout mPrant;
    @BindView(R.id.layout_complete)
    RelativeLayout mLayoutComplete;
    @BindView(R.id.tv_appVersion)
    TextView mTvAppVersion;
    @BindView(R.id.tv_continuousRecord2)
    TextView mTvContinuousRecord2;
    @BindView(R.id.pro_complete2)
    RxWaveView mProComplete2;
    @BindView(R.id.tv_progress2)
    TextView mTvProgress2;
    @BindView(R.id.layout_complete2)
    RelativeLayout mLayoutComplete2;
    @BindView(R.id.img_breakfast2)
    RxImageView mImgBreakfast2;
    @BindView(R.id.img_lunch2)
    RxImageView mImgLunch2;
    @BindView(R.id.img_dinner2)
    RxImageView mImgDinner2;
    @BindView(R.id.img_sporting2)
    RxImageView mImgSporting2;
    @BindView(R.id.img_weigh2)
    RxImageView mImgWeigh2;
    @BindView(R.id.img_userImg2)
    RxImageView mImgUserImg2;
    @BindView(R.id.layout_shareHead)
    RelativeLayout mLayoutShareHead;
    @BindView(R.id.img_userImg3)
    RxImageView mImgUserImg3;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.layout_shareTitle)
    RelativeLayout mLayoutShareTitle;
    @BindView(R.id.img_QRcode)
    ImageView mImgQRcode;

    private RxWaveHelper mWaveHelper;
    private int[] colors = {R.color.BrightGray, R.color.BrightGray, R.color.BrightGray,
            R.color.BrightGray, R.color.BrightGray, R.color.BrightGray, R.color.green_61D97F};
    private List<String> dates = new ArrayList<>();
    private List<Integer> item = new ArrayList<>();


    public static SlimmingRecordFragment newInstance() {
        Bundle args = new Bundle();
        SlimmingRecordFragment fragment = new SlimmingRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_slimming_record;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initWareView();
        initTextTypeface();
        initChart(mMLineChart);

    }

    private void initTextTypeface() {
        mTvContinuousRecord.setTypeface(MyAPP.typeface);
        mTvContinuousRecord2.setTypeface(MyAPP.typeface);
        mTvProgress.setTypeface(MyAPP.typeface);
        mTvProgress2.setTypeface(MyAPP.typeface);
        mTvWeightInfo.setTypeface(MyAPP.typeface);
        mTvHealthScore.setTypeface(MyAPP.typeface);
        mTvCurrentWeight.setTypeface(MyAPP.typeface);
        mTvCurrentDiet.setTypeface(MyAPP.typeface);
        mTvCurrentKcal.setTypeface(MyAPP.typeface);
        mTvCurrentEnergy.setTypeface(MyAPP.typeface);
    }

    private void initWareView() {
        mProComplete.setBorder(1, ContextCompat.getColor(mContext, R.color.GrayWrite));
        mProComplete.setWaveColor(Color.parseColor("#7DB8FFDC"), Color.parseColor("#FFB8FFDC"));
        mProComplete2.setBorder(1, ContextCompat.getColor(mContext, R.color.GrayWrite));
        mProComplete2.setWaveColor(Color.parseColor("#7DB8FFDC"), Color.parseColor("#FFB8FFDC"));
        mProComplete.setWaterLevelRatio(0f);
//        mProComplete.setWaveColor(Color.parseColor("#6ABEBD"), Color.parseColor("#B8FFDC"));
        mProComplete.setShowWave(true);

    }


    @Override
    protected void initRxBus() {
        RxBus.getInstance().register2(RefreshSlimming.class)
                .compose(RxComposeUtils.<RefreshSlimming>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<RefreshSlimming>() {
                    @Override
                    protected void _onNext(RefreshSlimming refreshSlimming) {
                        initNetData();
                    }
                });
        //只有在显示时才会网络请求
        RxBus.getInstance().register2(NetWorkType.class)
                .compose(RxComposeUtils.<NetWorkType>bindLifeResume(lifecycleSubject))
                .subscribe(new RxSubscriber<NetWorkType>() {
                    @Override
                    protected void _onNext(NetWorkType netWorkType) {
                        if (netWorkType.isBoolean())
                            initNetData();
                    }
                });

    }


    @Override
    protected void initNetData() {
        super.initNetData();
        initUserInfo();
        getData();
    }


    private void initUserInfo() {
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = MyAPP.getGson().fromJson(string, UserInfo.class);
        MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg);
    }

    private void getData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().indexInfo(1, 7))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("indexInfo", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        SlimmingRecordBean bean = MyAPP.getGson().fromJson(s, SlimmingRecordBean.class);
                        updateUI(bean);
                    }

                    @Override
                    protected void _onError(String error) {
                        super._onError(error);
                        RxToast.normal(error);
                    }
                });
    }

    private void updateUI(SlimmingRecordBean bean) {
        slimmingTarget(bean);
        healthScore(bean);
        weightRecord(bean);
        dietRecord(bean.getDietList());
        sportingRecord(bean.getAthleticsInfoList());
        energyRecord(bean.getDataList());
    }

    /**
     * 食材记录
     *
     * @param list
     */
    private void dietRecord(List<SlimmingRecordBean.DietListBean> list) {
        Calendar calendar = Calendar.getInstance();
        float dietMax = 0;
        dates.clear();
        item.clear();
        if (list != null && !list.isEmpty()) {
            calendar.setTimeInMillis(list.get(list.size() - 1).getHeatDate());
            int size = list.size();
            for (int i = 0; i < 7; i++) {
                if (i < size) {
                    SlimmingRecordBean.DietListBean bean = list.get(i);
                    dates.add(RxFormat.setFormatDate(bean.getHeatDate(), "MM/dd"));
                    item.add(bean.getCalorie());
                    dietMax = Math.max(dietMax, bean.getCalorie());
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                    item.add(0);
                }
            }

            SlimmingRecordBean.DietListBean bean = list.get(0);

            RxTextUtils.getBuilder(bean.getCalorie() + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentDiet);

        } else {
            RxTextUtils.getBuilder(0 + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentDiet);

            for (int i = 0; i < 7; i++) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                item.add(i, 0);
            }
        }


        mTvDietChart7.setText(dates.get(0));
        mTvDietChart6.setText(dates.get(1));
        mTvDietChart5.setText(dates.get(2));
        mTvDietChart4.setText(dates.get(3));
        mTvDietChart3.setText(dates.get(4));
        mTvDietChart2.setText(dates.get(5));
        mTvDietChart1.setText(dates.get(6));

        mDietProgress7.setProgress((int) (item.get(0) * 100 / dietMax), false);
        mDietProgress6.setProgress((int) (item.get(1) * 100 / dietMax), false);
        mDietProgress5.setProgress((int) (item.get(2) * 100 / dietMax), false);
        mDietProgress4.setProgress((int) (item.get(3) * 100 / dietMax), false);
        mDietProgress3.setProgress((int) (item.get(4) * 100 / dietMax), false);
        mDietProgress2.setProgress((int) (item.get(5) * 100 / dietMax), false);
        mDietProgress1.setProgress((int) (item.get(6) * 100 / dietMax), false);
    }

    /**
     * 能量记录
     *
     * @param list
     */
    private void energyRecord(List<SlimmingRecordBean.DataListBean> list) {
        Calendar calendar = Calendar.getInstance();
        float max = 0;
        dates.clear();
        item.clear();

        if (list != null && !list.isEmpty()) {
            calendar.setTimeInMillis(list.get(list.size() - 1).getRecordDate());
            int size = list.size();
            for (int i = 0; i < 7; i++) {
                if (i < size) {
                    SlimmingRecordBean.DataListBean bean = list.get(i);
                    dates.add(RxFormat.setFormatDate(bean.getRecordDate(), "MM/dd"));
                    int value = bean.getAthlCalorie() + bean.getBasalCalorie() - bean.getHeatCalorie();
                    max = Math.max(max, Math.abs(value));
                    item.add(Math.abs(value));
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                    item.add(0);
                }
            }

            SlimmingRecordBean.DataListBean bean = list.get(0);
            int value = bean.getAthlCalorie() + bean.getBasalCalorie() - bean.getHeatCalorie();

            mEnergyProgress7.setColor(ContextCompat.getColor(mContext, value < 0 ? R.color.red : R.color.orange_FF7200));
            mTvCurrentEnergy.setTextColor(ContextCompat.getColor(mContext, value < 0 ? R.color.red : R.color.orange_FF7200));
            mTvEnergyChart7.getHelper().setBackgroundColorNormal(ContextCompat.getColor(mContext, value < 0 ? R.color.red : R.color.orange_FF7200));
            RxTextUtils.getBuilder(Math.abs(value) + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentEnergy);
        } else {
            RxTextUtils.getBuilder(0 + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentEnergy);
            for (int i = 0; i < 7; i++) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                item.add(i, 0);
            }
        }


        mEnergyProgress7.setProgress((int) (item.get(0) * 100 / max), false);
        mEnergyProgress6.setProgress((int) (item.get(1) * 100 / max), false);
        mEnergyProgress5.setProgress((int) (item.get(2) * 100 / max), false);
        mEnergyProgress4.setProgress((int) (item.get(3) * 100 / max), false);
        mEnergyProgress3.setProgress((int) (item.get(4) * 100 / max), false);
        mEnergyProgress2.setProgress((int) (item.get(5) * 100 / max), false);
        mEnergyProgress1.setProgress((int) (item.get(6) * 100 / max), false);


        mTvEnergyChart7.setText(dates.get(0));
        mTvEnergyChart6.setText(dates.get(1));
        mTvEnergyChart5.setText(dates.get(2));
        mTvEnergyChart4.setText(dates.get(3));
        mTvEnergyChart3.setText(dates.get(4));
        mTvEnergyChart2.setText(dates.get(5));
        mTvEnergyChart1.setText(dates.get(6));

    }

    /**
     * 运动记录
     *
     * @param list
     */
    private void sportingRecord(List<SlimmingRecordBean.AthleticsInfoListBean> list) {
        Calendar calendar = Calendar.getInstance();
        float max = 0;
        dates.clear();
        item.clear();
        if (list != null && !list.isEmpty()) {
            calendar.setTimeInMillis(list.get(list.size() - 1).getAthlDate());
            int size = list.size();
            for (int i = 0; i < 7; i++) {
                if (i < size) {
                    dates.add(RxFormat.setFormatDate(list.get(i).getAthlDate(), "MM/dd"));
                    max = Math.max(max, list.get(i).getCalorie());
                    item.add(list.get(i).getCalorie());
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                    item.add(0);
                }
            }

            RxTextUtils.getBuilder(list.get(0).getCalorie() + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentKcal);
        } else {
            RxTextUtils.getBuilder(0 + "")
                    .append("\tkcal").setProportion(0.5f)
                    .into(mTvCurrentKcal);

            for (int i = 0; i < 7; i++) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                item.add(i, 0);
            }
        }


        mSportingProgress7.setProgress((int) (item.get(0) * 100 / max), false);
        mSportingProgress6.setProgress((int) (item.get(1) * 100 / max), false);
        mSportingProgress5.setProgress((int) (item.get(2) * 100 / max), false);
        mSportingProgress4.setProgress((int) (item.get(3) * 100 / max), false);
        mSportingProgress3.setProgress((int) (item.get(4) * 100 / max), false);
        mSportingProgress2.setProgress((int) (item.get(5) * 100 / max), false);
        mSportingProgress1.setProgress((int) (item.get(6) * 100 / max), false);

        mTvSportingChart7.setText(dates.get(0));
        mTvSportingChart6.setText(dates.get(1));
        mTvSportingChart5.setText(dates.get(2));
        mTvSportingChart4.setText(dates.get(3));
        mTvSportingChart3.setText(dates.get(4));
        mTvSportingChart2.setText(dates.get(5));
        mTvSportingChart1.setText(dates.get(6));
    }


    /**
     * 体重记录
     *
     * @param bean
     */
    private void weightRecord(SlimmingRecordBean bean) {
        setLineChartData(bean);
        if (bean.getNormWeight() != 0)
            addLimitLine2Y(mMLineChart, (float) bean.getNormWeight(), bean.getNormWeight() + "kg");
    }


    private void initChart(BarLineChartBase lineChartBase) {
        lineChartBase.setEnabled(false);
        lineChartBase.setTouchEnabled(false);//可以点击
        lineChartBase.setMaxVisibleValueCount(60);
        lineChartBase.getLegend().setEnabled(false);
        lineChartBase.getDescription().setEnabled(false);
        lineChartBase.setDrawGridBackground(false);
        XAxis xAxis = lineChartBase.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);

        YAxis leftAxis = lineChartBase.getAxisLeft();
        YAxis rightAxis = lineChartBase.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        mMLineChart.setData(new LineData());
        createSet();
    }

    private void setLineChartData(SlimmingRecordBean bean) {
        dates.clear();
        List<SlimmingRecordBean.WeightInfoBean> list = bean.getWeightInfoList();
        Calendar calendar = Calendar.getInstance();
        ArrayList<Entry> lineEntry = new ArrayList<>();
        float max = 0;
        float min = 100;

        if (list != null && !list.isEmpty()) {
            calendar.setTimeInMillis(list.get(list.size() - 1).getWeightDate());
            int size = list.size();
            for (int i = 0; i < 7; i++) {
                if (i < size) {
                    float weight = (float) list.get(i).getWeight();
                    lineEntry.add(new Entry(6 - i, weight));
                    dates.add(RxFormat.setFormatDate(list.get(i).getWeightDate(), "MM/dd"));
                    max = Math.max(max, weight);
                    min = Math.min(min, weight);
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                    lineEntry.add(new Entry(6 - i, 0));
                    min = Math.min(min, 0);
                }
            }
            RxTextUtils.getBuilder(list.get(0).getWeight() + "")
                    .append("\tkg").setProportion(0.5f)
                    .into(mTvCurrentWeight);
        } else {
            RxTextUtils.getBuilder(0 + "")
                    .append("\tkg").setProportion(0.5f)
                    .into(mTvCurrentWeight);

            for (int i = 0; i < 7; i++) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                lineEntry.add(new Entry(i, 0));
            }
        }

        YAxis yAxis = mMLineChart.getAxisLeft();
        max *= 1.2f;
        max = (float) Math.max(max, bean.getNormWeight());
        min = (float) Math.min(min, bean.getNormWeight());
        yAxis.setAxisMaximum(max);
        yAxis.setAxisMinimum(min);

        LineDataSet set = (LineDataSet) mMLineChart.getData().getDataSetByIndex(0);
        Collections.reverse(lineEntry);
//        Logger.d("体重数据：" + lineEntry.size());
        set.setValues(lineEntry);

        mMLineChart.getData().notifyDataChanged();
        mMLineChart.notifyDataSetChanged();
        mMLineChart.invalidate();
        mMLineChart.setVisibleXRangeMaximum(7);
        mMLineChart.animateX(1000);

        mTvWeightChart1.setText(dates.get(6));
        mTvWeightChart2.setText(dates.get(5));
        mTvWeightChart3.setText(dates.get(4));
        mTvWeightChart4.setText(dates.get(3));
        mTvWeightChart5.setText(dates.get(2));
        mTvWeightChart6.setText(dates.get(1));
        mTvWeightChart7.setText(dates.get(0));
    }

    private void createSet() {
        LineDataSet set1 = new LineDataSet(null, "weight");
        set1.setDrawIcons(false);
        set1.setDrawValues(false);
        set1.setColor(getResources().getColor(R.color.BrightGray));
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(true);
        set1.setCircleRadius(5f);
        set1.setLineWidth(1f);
        set1.setCircleColors(colors, mActivity);
        mMLineChart.getData().addDataSet(set1);
        mMLineChart.invalidate();
    }

    //添加提示线
    public void addLimitLine2Y(BarLineChartBase lineChartBase, float value, String label) {
        YAxis y = lineChartBase.getAxisLeft();
        LimitLine.LimitLabelPosition pos = LimitLine.LimitLabelPosition.LEFT_TOP;
        if (y.mAxisMaximum - value < 5) {
            pos = LimitLine.LimitLabelPosition.LEFT_BOTTOM;
        }
        //提示线，
        LimitLine ll = new LimitLine(value, label);//线条颜色宽度等
        ll.setLineColor(getResources().getColor(R.color.gray_ECEBF0));
        ll.setLineWidth(1f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(pos);//文字颜色、大小
        ll.setTextColor(getResources().getColor(R.color.GrayWrite));
        ll.setTextSize(10f);

        y.removeAllLimitLines();
        //加入到 mXAxis 或 mYAxis
        y.addLimitLine(ll);

    }

    /**
     * 健康分数
     *
     * @param bean
     */
    private void healthScore(SlimmingRecordBean bean) {
        SlimmingRecordBean.WeightInfoBean weightInfo = bean.getWeightInfo();
        if (weightInfo == null) {
            RxTextUtils.getBuilder("--")
                    .append("\t体重(kg)\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\tBMI\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\t体脂率(%)\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\t基础代谢(kcal)\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvWeightInfo);

            RxTextUtils.getBuilder("0.0")
                    .append("\t分").setProportion(0.5f)
                    .into(mTvHealthScore);


            RxTextUtils.getBuilder("身材：\t").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("未知")
                    .append("\t\t\t患病风险：").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("未知")
                    .into(mTvHealthInfo);

            mTvHealthDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), "yyyy/MM/dd"));
            return;
        }

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(weightInfo.getWeight(), 1))
                .append("\t体重(kg)\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(RxFormatValue.fromat4S5R(weightInfo.getBmi(), 1))
                .append("\tBMI\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(RxFormatValue.fromat4S5R(weightInfo.getBodyFat(), 1))
                .append("\t体脂率(%)\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(RxFormatValue.fromat4S5R(weightInfo.getBmr(), 1))
                .append("\t基础代谢(kcal)\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvWeightInfo);

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(weightInfo.getHealthScore(), 1))
                .append("\t分").setProportion(0.5f)
                .into(mTvHealthScore);

        RxTextUtils.getBuilder("身材：\t").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.getBodyType())
                .append("\t\t\t患病风险：").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(bean.getLevelDesc())
                .into(mTvHealthInfo);

        mHealthyLevel.switchLevel(bean.getSickLevel());
        mTvHealthDate.setText(RxFormat.setFormatDate(weightInfo.getWeightDate(), "yyyy/MM/dd"));
    }

    /**
     * 今日完成目标
     *
     * @param bean
     */
    private void slimmingTarget(SlimmingRecordBean bean) {
        mImgBreakfast.getHelper().setIconNormal(bean.isBreakfastDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgLunch.getHelper().setIconNormal(bean.isLunchDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgDinner.getHelper().setIconNormal(bean.isDinnerDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgSporting.getHelper().setIconNormal(bean.isAthlDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgWeigh.getHelper().setIconNormal(bean.isWeightDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));

        mImgBreakfast2.getHelper().setIconNormal(bean.isBreakfastDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgLunch2.getHelper().setIconNormal(bean.isLunchDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgDinner2.getHelper().setIconNormal(bean.isDinnerDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgSporting2.getHelper().setIconNormal(bean.isAthlDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));
        mImgWeigh2.getHelper().setIconNormal(bean.isWeightDone()
                ? ContextCompat.getDrawable(mContext, R.mipmap.icon_food_complete)
                : new ColorDrawable(ContextCompat.getColor(mContext, R.color.Gray_ECEBF0)));

        int complete = 0;
        if (bean.isBreakfastDone()) complete += 20;
        if (bean.isLunchDone()) complete += 20;
        if (bean.isDinnerDone()) complete += 20;
        if (bean.isAthlDone()) complete += 20;
        if (bean.isWeightDone()) complete += 20;

        mProComplete.setWaterLevelRatio(complete / 100f);
        mProComplete2.setWaterLevelRatio(complete / 100f);
//        mProComplete.setWaterLevelRatio(0.8f);
        if (complete / 100f > 0) {
            mWaveHelper = new RxWaveHelper(mProComplete);
            mWaveHelper.start();
        }
        RxTextUtils.getBuilder(complete + "")
                .append("\t%").setProportion(0.5f)
                .into(mTvProgress);
        RxTextUtils.getBuilder(complete + "")
                .append("\t%").setProportion(0.5f)
                .into(mTvProgress2);


        RxTextUtils.getBuilder("总共记录\t")
                .append(bean.getTotalCompleteDays() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("\t天").into(mTvContinuousRecord);
        RxTextUtils.getBuilder("总共记录\t")
                .append(bean.getTotalCompleteDays() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("\t天").into(mTvContinuousRecord2);

    }


    @Override
    protected void onInvisible() {
        super.onInvisible();
        if (mWaveHelper != null)
            mWaveHelper.cancel();
        //屏幕沉浸
        StatusBarUtils.from(this)
                .setStatusBarColor(ContextCompat.getColor(mContext, R.color.Gray))
                .setLightStatusBar(false)
                .process();
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        if (mWaveHelper != null)
            mWaveHelper.start();
        //屏幕沉浸
        StatusBarUtils.from(this)
                .setStatusBarColor(Color.parseColor("#ABCBE0"))
                .setLightStatusBar(false)
                .process();
    }


    @Override
    public void onStart() {
        super.onStart();
        ShareUtil.recycle();
    }

    @OnClick({
            R.id.img_email,
            R.id.layout_weight_title,
            R.id.layout_diet_title,
            R.id.layout_sports_title,
            R.id.layout_energy_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_email:
                //分享
                share(true);
                break;
            case R.id.layout_weight_title:
                RxActivityUtils.skipActivity(mContext, WeightRecordFragment.class);
                break;
            case R.id.layout_diet_title:
                RxActivityUtils.skipActivity(mContext, FoodRecommend.class);
                break;
            case R.id.layout_sports_title:
                RxActivityUtils.skipActivity(mContext, SmartClothingFragment.class);
                break;
            case R.id.layout_energy_title:
                RxActivityUtils.skipActivity(mContext, EnergyActivity.class);
                break;
        }
    }

    private void share(boolean isStart) {
        mLayoutShareHead.setVisibility(isStart ? View.VISIBLE : View.INVISIBLE);
        mLayoutShareTitle.setVisibility(isStart ? View.VISIBLE : View.GONE);

        if (isStart) {
            tipDialog.show("正在分享...", 3000);
            RxQRCode.builder(ServiceAPI.APP_DOWN_LOAD_URL)
                    .codeSide(800)
                    .logoBitmap(R.mipmap.icon_app_round, getResources())
                    .into(mImgQRcode);

            UserInfo info = MyAPP.getGson().fromJson(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);
            MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg2);
            MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg3);


            RxTextUtils.getBuilder(info.getUserName() + "\n")
                    .append(getString(R.string.appDays, getString(R.string.appName), info.getRegisterTime())).setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvUserName);
            mTvDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd"));

            RxTextUtils.getBuilder("让 你 健 康 瘦\n")
                    .append(getString(R.string.appName) + "v" + RxDeviceUtils.getAppVersionName())
                    .setProportion(0.5f)
                    .into(mTvAppVersion);
            mProComplete2.setShowWave(true);
            //延迟500毫秒，需要等到控件显示
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //控件转图片
                    Bitmap bitmap = RxImageTools.view2Bitmap(mPrant, ContextCompat.getColor(mContext, R.color.white));
                    //微信分享图片最大尺寸32KB
//                    bitmap = RxImageUtils.compressByScale(bitmap, 0.5f, 0.5f);
                    showSimpleBottomSheetGrid(bitmap);
                    share(false);
                }
            }, 500);
        }
    }

    private void showSimpleBottomSheetGrid(final Bitmap imgUrl) {
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(mContext);
        builder.addItem(R.mipmap.wechat, "微信好友", 1, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.fr, "朋友圈", 2, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.weib, "新浪微博", 3, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.qq, "QQ好友", 4, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
//                .addItem(R.mipmap.zone, "QQ空间", 5, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case 1:
                                ShareUtil.shareImage(mActivity, SharePlatform.WX, imgUrl, shareListener);
                                break;
                            case 2:
                                ShareUtil.shareImage(mActivity, SharePlatform.WX_TIMELINE, imgUrl, shareListener);
                                break;
                            case 3:
                                ShareUtil.shareImage(mActivity, SharePlatform.WEIBO, imgUrl, shareListener);
                                break;
                            case 4:
                                ShareUtil.shareImage(mActivity, SharePlatform.QQ, imgUrl, shareListener);
                                break;
                            case 5:
                                ShareUtil.shareImage(mActivity, SharePlatform.QZONE, imgUrl, shareListener);
                                break;

                        }
                    }
                }).build().show();
    }


    ShareListener shareListener = new ShareListener() {
        @Override
        public void shareSuccess() {
            RxLogUtils.d("分享成功");
            RxToast.normal("分享成功");
            tipDialog.dismiss();
        }

        @Override
        public void shareFailure(Exception e) {
            RxLogUtils.d("分享失败");
            RxToast.normal("分享失败");
            tipDialog.dismiss();
        }

        @Override
        public void shareCancel() {
            RxLogUtils.d("分享关闭");
            RxToast.normal("分享关闭");
            tipDialog.dismiss();
        }
    };

}
