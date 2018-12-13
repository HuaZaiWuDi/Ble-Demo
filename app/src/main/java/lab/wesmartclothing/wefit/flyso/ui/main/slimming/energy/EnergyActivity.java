package lab.wesmartclothing.wefit.flyso.ui.main.slimming.energy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.chart.LineBean;
import com.vondear.rxtools.view.chart.SuitLines;
import com.vondear.rxtools.view.chart.Unit;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.EnergyBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.FoodRecommend;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SmartClothingFragment;
import lab.wesmartclothing.wefit.flyso.utils.EnergyUtil;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class EnergyActivity extends BaseActivity {


    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.suitlines)
    SuitLines mSuitlines;
    @BindView(R.id.layout_lenged)
    LinearLayout mLayoutLenged;
    @BindView(R.id.iv_sports)
    ImageView mIvSports;
    @BindView(R.id.tv_sportDate)
    TextView mTvSportDate;
    @BindView(R.id.tv_surplusHeat)
    TextView mTvSurplusHeat;
    @BindView(R.id.layout_energy_title)
    RelativeLayout mLayoutEnergyTitle;
    @BindView(R.id.img_eat)
    ImageView mImgEat;
    @BindView(R.id.layout_eatEnergy)
    RxRelativeLayout mLayoutEatEnergy;
    @BindView(R.id.img_sporting)
    ImageView mImgSporting;
    @BindView(R.id.layout_sportingEnergy)
    RxRelativeLayout mLayoutSportingEnergy;
    @BindView(R.id.tv_eatKcal)
    TextView mTvEatKcal;
    @BindView(R.id.tv_sportingKcal)
    TextView mTvSportingKcal;


    private long currentDate = System.currentTimeMillis();

    @Override
    protected int layoutId() {
        return R.layout.activity_energy;
    }


    @Override
    protected void initViews() {
        super.initViews();
        //屏幕沉浸
        StatusBarUtils.from(mActivity)
                .setStatusBarColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                .setLightStatusBar(false)
                .process();
        initTopBar();
    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTopBar.setTitle("能量记录");
    }

    private void initLineChart(final List<EnergyBean.ListBean> list) {
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Time = new ArrayList<>();
        if (RxDataUtils.isEmpty(list)) return;
        Collections.reverse(list);
        for (int i = 0; i < list.size(); i++) {
            EnergyBean.ListBean bean = list.get(i);
            Unit unit_heat = new Unit(bean.getHeatCalorie(), RxFormat.setFormatDate(bean.getRecordDate(), "MM/dd"));
            Unit unit_time = new Unit(bean.getAthlCalorie());

            lines_Heat.add(unit_heat);
            lines_Time.add(unit_time);
        }

        LineBean heatLine = new LineBean();
        heatLine.setUnits(lines_Heat);
        heatLine.setShowPoint(true);
        heatLine.setLineWidth(RxUtils.dp2px(2));
        heatLine.setColor(Color.parseColor("#FFFFFF"));

        LineBean timeLine = new LineBean();
        timeLine.setShowPoint(true);
        timeLine.setUnits(lines_Time);
        timeLine.setLineWidth(RxUtils.dp2px(2));
        timeLine.setColor(Color.parseColor("#FFFFFF"));
        timeLine.setDashed(true);

        mSuitlines.setSpaceMin(RxUtils.dp2px(4));
        new SuitLines.LineBuilder()
                .add(heatLine)
                .add(timeLine)
                .build(mSuitlines);

        mSuitlines.setLineChartSelectItemListener(new SuitLines.LineChartSelectItemListener() {
            @Override
            public void selectItem(int valueX) {
                EnergyBean.ListBean bean = list.get(valueX);
                currentDate = bean.getRecordDate();
                mTvSportDate.setText(RxFormat.setFormatDate(bean.getRecordDate(), RxFormat.Date_CH));

//                int surplusHeat = bean.getAthlCalorie() + bean.getBasalCalorie() - bean.getHeatCalorie();

                int surplusHeat = EnergyUtil.energy(bean.getAthlCalorie(), bean.getHeatCalorie(), bean.getBasalCalorie());

                mTvSurplusHeat.setTextColor(ContextCompat.getColor(mContext, surplusHeat < 0 ? R.color.red : R.color.orange_FF7200));
                RxTextUtils.getBuilder(Math.abs(surplusHeat) + "")
                        .append("\tkacl").setProportion(0.5f)
                        .into(mTvSurplusHeat);

                RxTextUtils.getBuilder("饮食摄入能量\n")
                        .append(bean.getHeatCalorie() + "").setProportion(1.3f)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                        .append("\tkcal").setProportion(0.7f)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                        .into(mTvEatKcal);

                RxTextUtils.getBuilder("运动消耗能量\n")
                        .append(bean.getAthlCalorie() + "").setProportion(1.3f)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                        .append("\tkcal").setProportion(0.7f)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                        .into(mTvSportingKcal);

            }
        });
    }


    @Override
    protected void initNetData() {
        super.initNetData();
        getData();
    }


    private void getData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().fetchHeatList())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchHeatList", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        Logger.json(s);
                        EnergyBean bean = JSON.parseObject(s, EnergyBean.class);
                        updateUI(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }

                });
    }

    private void updateUI(EnergyBean bean) {
        initLineChart(bean.getList());
    }


    @OnClick({R.id.layout_eatEnergy, R.id.layout_sportingEnergy})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.layout_eatEnergy:
                bundle.putLong(Key.BUNDLE_DATE_TIME, currentDate);
                RxActivityUtils.skipActivity(mContext, FoodRecommend.class);
                break;
            case R.id.layout_sportingEnergy:
                RxActivityUtils.skipActivity(mContext, SmartClothingFragment.class);
                break;
        }
    }
}
