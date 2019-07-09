package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AlthDataBean;
import lab.wesmartclothing.wefit.flyso.entity.AlthDataListBean;
import lab.wesmartclothing.wefit.flyso.entity.DataListBean;
import lab.wesmartclothing.wefit.flyso.entity.GroupDataListBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.GroupType;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.line.LineBean;
import lab.wesmartclothing.wefit.flyso.view.line.SuitLines;
import lab.wesmartclothing.wefit.flyso.view.line.Unit;

/**
 * Created by jk on 2018/7/18.
 */
public class SmartClothingFragment extends BaseActivity {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.btn_strongTip)
    QMUIRoundButton mBtnStrongTip;
    @BindView(R.id.layout_StrongTip)
    RelativeLayout mLayoutStrongTip;
    @BindView(R.id.suitlines)
    SuitLines mSuitlines;
    @BindView(R.id.iv_sports)
    ImageView mIvSports;
    @BindView(R.id.tv_sportDate)
    TextView mTvSportDate;
    @BindView(R.id.layout_sports)
    RelativeLayout mLayoutSports;
    @BindView(R.id.iv_heatKacl)
    ImageView mIvHeatKacl;
    @BindView(R.id.tv_heatTitlle)
    TextView mTvHeatTitlle;
    @BindView(R.id.tv_Heat_Kcal)
    TextView mTvHeatKcal;
    @BindView(R.id.iv_sports_time)
    ImageView mIvSportsTime;
    @BindView(R.id.tv_sports_time_title)
    TextView mTvSportsTimeTitle;
    @BindView(R.id.tv_Sports_Time)
    TextView mTvSportsTime;
    @BindView(R.id.recycler_Sporting)
    RecyclerView mRecyclerSporting;
    Unbinder unbinder;
    @BindView(R.id.img_switchDate)
    ImageView mImgSwitchDate;


    private List<DataListBean> list;
    private BaseQuickAdapter adapter;
    private int pageNum = 1;
    private @GroupType
    String groupType = GroupType.TYPE_DAYS;
    private boolean needRefresh = true;//通过flag判断是否需要刷新数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_smart_clothing);
        unbinder = ButterKnife.bind(this);

        StatusBarUtils.from(mActivity)
                .setStatusBarColor(getResources().getColor(R.color.red))
                .setLightStatusBar(true)
                .process();


        initView();
    }


    private void initView() {
        initTopBar();
        Typeface typeface = MyAPP.typeface;
        mTvHeatKcal.setTypeface(typeface);
        mTvSportsTime.setTypeface(typeface);
        mTvSportDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_CH));
        initData();
        initSportingList();
    }

    @Override
    protected void initNetData() {
        super.initNetData();

        RxBus.getInstance().register2(RefreshSlimming.class)
                .compose(RxComposeUtils.<RefreshSlimming>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<RefreshSlimming>() {
                    @Override
                    protected void _onNext(RefreshSlimming refreshSlimming) {
                        RxLogUtils.d("刷新数据");
                        needRefresh = true;
                        pageNum = 1;
                        initData();
                    }
                });
    }

    private void initSportingList() {
        mRecyclerSporting.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerSporting.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter = new BaseQuickAdapter<AlthDataBean, BaseViewHolder>(R.layout.item_sporting_list) {
            @Override
            protected void convert(BaseViewHolder helper, AlthDataBean item) {
                String timeSection = "";
                if (GroupType.TYPE_DAYS.equals(groupType)) {
                    timeSection = RxFormat.setFormatDate(item.getStartTime(), RxFormat.Date_Time) + "-"
                            + RxFormat.setFormatDate(item.getEndTime(), RxFormat.Date_Time);
                } else {
                    timeSection = RxFormat.setFormatDate(item.getStartTime(), RxFormat.Date);
                }

                SpannableStringBuilder timeBuilder = RxTextUtils.getBuilder(GroupType.TYPE_DAYS.equals(groupType) ?
                        getString(R.string.timeSlot) + "\n" : "运动日期" + "\n")
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                        .append(timeSection)
                        .create();

                SpannableStringBuilder kcalBuilder = RxTextUtils.getBuilder(getString(R.string.consumeCalorie) + "\n")
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                        .append(String.format("%.1f", item.getCalorie()))
                        .append("\tkcal")
                        .create();

                //getPlanFlag():0是自由运动，1是课程运动
                helper.setText(R.id.tv_sportingType, item.getPlanFlag() != 1 ? getString(R.string.freeRun) : getString(R.string.planRun))
                        .setText(R.id.tv_sportingTime, timeBuilder)
                        .setTypeface(MyAPP.typeface, R.id.tv_sportingTime, R.id.tv_sportingKcal)
                        .setText(R.id.tv_sportingKcal, kcalBuilder);
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (GroupType.TYPE_DAYS.equals(groupType)) {
                Bundle bundle = new Bundle();
                AlthDataBean item = (AlthDataBean) adapter.getItem(position % adapter.getData().size());
                if (item == null) return;
                bundle.putString(Key.BUNDLE_DATA_GID, item.getGid());
                bundle.putBoolean(Key.BUNDLE_SPORTING_PLAN, item.getPlanFlag() == 1);
                bundle.putBoolean(Key.BUNDLE_GO_BCAK, true);
                RxActivityUtils.skipActivity(mContext, SportsDetailsFragment.class, bundle);
            }
        });
        mRecyclerSporting.setAdapter(adapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        NetManager.getApiService().athlFetchGroupTypeRecordList(groupType, pageNum, 10)
                .compose(RxComposeUtils.handleResult())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("fetchAthleticsListDetail" + pageNum + groupType,
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

    private void fetchDayOrMonthData(long recordDate) {
        NetManager.getApiService().athlFetchDaysOrMonthRecordList(groupType, recordDate, 1, 31)
                .compose(RxComposeUtils.handleResult())
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().transformObservable("athlFetchDaysOrMonthRecordList" + recordDate + groupType,
                        String.class, needRefresh ? CacheStrategy.firstRemote() : CacheStrategy.firstCacheTimeout(60 * 1000)))
                .map(new CacheResult.MapFunc())
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        needRefresh = false;
                        AlthDataListBean bean = new Gson().fromJson(s, AlthDataListBean.class);
                        adapter.setNewData(bean.getList());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                        adapter.setNewData(null);
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

                String date = RxFormat.setFormatDate(bean.getRecordDate(), GroupType.TYPE_DAYS.equals(groupType) ? "MM/dd" : "yyyy/MM");
                int color = bean.getAthlCalorie() < bean.getAthlPlan() ? 0x87FFFFFF : 0xFFFFFFFF;
                lines_Heat.add(new Unit((float) bean.getAthlCalorie(), date, color));
                lines_Base.add(new Unit((float) bean.getAthlPlan(), ""));
            }
            mSuitlines.addDataChart(Arrays.asList(lines_Heat, lines_Base));
            pageNum++;
        }
    }

    private void initLineChart() {
        if (this.list == null) return;
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Base = new ArrayList<>();
        Collections.reverse(this.list);//
        for (int i = 0; i < this.list.size(); i++) {
            DataListBean bean = this.list.get(i);

            String date = RxFormat.setFormatDate(bean.getRecordDate(), GroupType.TYPE_DAYS.equals(groupType) ? "MM/dd" : "yyyy/MM");
            int color = bean.getAthlCalorie() < bean.getAthlPlan() ? 0x87FFFFFF : 0xFFFFFFFF;
            lines_Heat.add(new Unit((float) bean.getAthlCalorie(), date, color));
            lines_Base.add(new Unit((float) bean.getAthlPlan(), ""));
            RxLogUtils.d("运动标准：" + bean.getAthlPlan());
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
            if (!this.list.isEmpty()) {
                DataListBean bean = this.list.get(valueX);
                mTvSportDate.setText(RxFormat.setFormatDate(bean.getRecordDate(), RxFormat.Date_CH));
                mTvHeatKcal.setText(RxFormatValue.fromat4S5R(bean.getAthlCalorie(), 1));
                mTvSportsTime.setText(RxFormatValue.fromatUp(bean.getDuration() < 60 ? 1 : bean.getDuration() / 60, 0));
            }
        });

        mSuitlines.setLineChartStopItemListener(valueX -> {
            RxLogUtils.e("滑动停止：" + valueX);
            if (!this.list.isEmpty()) {
                fetchDayOrMonthData(list.get(valueX).getRecordDate());
            }
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

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle(R.string.runRecord);
//        btn_Connect = mQMUIAppBarLayout.addRightTextButton(
//                getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)) ? R.string.unBind : BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected), R.id.tv_connect);
//        btn_Connect.setTextColor(Color.WHITE);
//        btn_Connect.setTextSize(13);
    }


    @OnClick(R.id.img_switchDate)
    public void onViewClicked() {
        if (RxUtils.isFastClick(800))
            return;
        if (groupType.equals(GroupType.TYPE_DAYS)) {
            groupType = GroupType.TYPE_MONTHS;
            mImgSwitchDate.setImageResource(R.mipmap.ic_select_month);
        } else {
            groupType = GroupType.TYPE_DAYS;
            mImgSwitchDate.setImageResource(R.mipmap.ic_select_day);
        }
        pageNum = 1;
        initData();
    }
}
