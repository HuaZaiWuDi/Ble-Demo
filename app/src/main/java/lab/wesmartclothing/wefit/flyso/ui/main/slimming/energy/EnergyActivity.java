package lab.wesmartclothing.wefit.flyso.ui.main.slimming.energy;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.DataListBean;
import lab.wesmartclothing.wefit.flyso.entity.GroupDataListBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.GroupType;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.FoodRecommend;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SmartClothingFragment;
import lab.wesmartclothing.wefit.flyso.utils.EnergyUtil;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.line.LineBean;
import lab.wesmartclothing.wefit.flyso.view.line.SuitLines;
import lab.wesmartclothing.wefit.flyso.view.line.Unit;

public class EnergyActivity extends BaseActivity {


    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.suitlines)
    SuitLines mSuitlines;
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
    @BindView(R.id.img_switchDate)
    ImageView mImgSwitchDate;
    @BindView(R.id.img_pointer)
    ImageView mImgPointer;
    @BindView(R.id.tv_energyTitle)
    TextView mTvEnergyTitle;


    private long currentDate = System.currentTimeMillis();
    private int pageNum = 1;
    private List<DataListBean> list;
    private @GroupType
    String groupType = GroupType.TYPE_DAYS;

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
        mTopBar.setTitle(R.string.energyRecord);
    }

    private void initLineChart() {
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Base = new ArrayList<>();
        if (RxDataUtils.isEmpty(this.list)) return;
        Collections.reverse(this.list);
        for (int i = 0; i < this.list.size(); i++) {
            DataListBean bean = this.list.get(i);

            double energy = EnergyUtil.energy(bean.getAthlCalorie(), bean.getHeatCalorie(), bean.getBasalCalorie());
            double baseEnergy = EnergyUtil.energy(bean.getAthlPlan(), bean.getDietPlan(), bean.getBasalCalorie());
            baseEnergy = Math.max(0, baseEnergy);
            energy = Math.max(0, energy);
            String date = RxFormat.setFormatDate(bean.getRecordDate(), GroupType.TYPE_DAYS.equals(groupType) ? "MM/dd" : "yyyy/MM");
            int color = energy < baseEnergy ? 0x87FFFFFF : 0xFFFFFFFF;
            lines_Heat.add(new Unit((float) energy, date, color));
            lines_Base.add(new Unit((float) baseEnergy, ""));
        }

        LineBean heatLine = new LineBean();
        heatLine.setUnits(lines_Heat);
        heatLine.setBarWidth(RxUtils.dp2px(10));
        heatLine.setChartType(SuitLines.ChartType.TYPE_BAR);

        LineBean timeLine = new LineBean();
        timeLine.setUnits(lines_Base);
        timeLine.setShowUpText(false);
        timeLine.setLineType(SuitLines.LineType.CURVE);
        timeLine.setLineWidth(RxUtils.dp2px(1));
        timeLine.setDashed(true);
        mSuitlines.setYSpace(1f, 0);

        new SuitLines.LineBuilder()
                .add(heatLine)
                .add(timeLine)
                .build(mSuitlines);

        mSuitlines.setLineChartSelectItemListener(valueX -> {
            if (this.list.isEmpty()) return;
            DataListBean bean = this.list.get(valueX);
            currentDate = bean.getRecordDate();
            mTvSportDate.setText(RxFormat.setFormatDate(bean.getRecordDate(), RxFormat.Date_CH));

            double surplusHeat = EnergyUtil.energy(bean.getAthlCalorie(), bean.getHeatCalorie(), bean.getBasalCalorie());

            mTvSurplusHeat.setTextColor(ContextCompat.getColor(mContext, surplusHeat < 0 ? R.color.red : R.color.orange_FF7200));

            mTvEnergyTitle.setText(surplusHeat < 0 ? "能量盈余" : "综合消耗");

            RxTextUtils.getBuilder(String.format("%.1f", (float) Math.abs(surplusHeat)))
                    .append("\tkacl").setProportion(0.5f)
                    .into(mTvSurplusHeat);

            RxTextUtils.getBuilder(getString(R.string.intakeEnergy) + "\n")
                    .append(String.format("%.1f", (float) Math.abs(bean.getHeatCalorie()))).setProportion(1.3f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                    .append("\tkcal").setProportion(0.7f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                    .into(mTvEatKcal);

            RxTextUtils.getBuilder(getString(R.string.consumeEnergy) + "\n")
                    .append(String.format("%.1f", (float) Math.abs(bean.getAthlCalorie()))).setProportion(1.3f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                    .append("\tkcal").setProportion(0.7f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00))
                    .into(mTvSportingKcal);
        });

        mSuitlines.setLineChartScrollEdgeListener(new SuitLines.LineChartScrollEdgeListener() {
            @Override
            public void leftEdge() {
                initData();
            }

            @Override
            public void rightEdge() {

            }
        });
    }


    @Override
    protected void initNetData() {
        super.initNetData();
        initData();
    }


    private void initData() {
        NetManager.getApiService().heatFetchGroupTypeRecordList(groupType, pageNum, 10)
                .compose(RxComposeUtils.handleResult())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("heatFetchGroupTypeRecordList" + pageNum + groupType,
                        String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        GroupDataListBean bean = JSON.parseObject(s, GroupDataListBean.class);
                        if (!RxDataUtils.isEmpty(bean.getList()))
                            updateUI(bean.getList());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }

                });
    }

    private void updateUI(List<DataListBean> dataListBeanList) {
        if (pageNum == 1) {
            this.list = dataListBeanList;
            initLineChart();
            pageNum++;
        } else {
            Collections.reverse(dataListBeanList);//
            this.list.addAll(0, dataListBeanList);

            List<Unit> lines_Heat = new ArrayList<>();
            List<Unit> lines_Base = new ArrayList<>();
            for (int i = 0; i < this.list.size(); i++) {
                DataListBean bean = this.list.get(i);
                double energy = EnergyUtil.energy(bean.getAthlCalorie(), bean.getHeatCalorie(), bean.getBasalCalorie());
                double baseEnergy = EnergyUtil.energy(bean.getAthlPlan(), bean.getDietPlan(), bean.getBasalCalorie());

                String date = RxFormat.setFormatDate(bean.getRecordDate(), GroupType.TYPE_DAYS.equals(groupType) ? "MM/dd" : "yyyy/MM");
                int color = energy < baseEnergy ? 0x87FFFFFF : 0xFFFFFFFF;
                lines_Heat.add(new Unit((float) energy, date, color));
                lines_Base.add(new Unit((float) baseEnergy, ""));
            }
            mSuitlines.addDataChart(Arrays.asList(lines_Heat, lines_Base));
            pageNum++;
        }
    }


    @OnClick({R.id.layout_eatEnergy, R.id.layout_sportingEnergy, R.id.img_switchDate})
    public void onViewClicked(View view) {
        if (RxUtils.isFastClick(800))
            return;
        switch (view.getId()) {
            case R.id.layout_eatEnergy:
                FoodRecommend.start(mContext, currentDate);
                break;
            case R.id.layout_sportingEnergy:
                RxActivityUtils.skipActivity(mContext, SmartClothingFragment.class);
                break;
            case R.id.img_switchDate:
                if (groupType.equals(GroupType.TYPE_DAYS)) {
                    groupType = GroupType.TYPE_MONTHS;
                    mImgSwitchDate.setImageResource(R.mipmap.ic_select_month);
                } else {
                    groupType = GroupType.TYPE_DAYS;
                    mImgSwitchDate.setImageResource(R.mipmap.ic_select_day);
                }
                pageNum = 1;
                initData();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
