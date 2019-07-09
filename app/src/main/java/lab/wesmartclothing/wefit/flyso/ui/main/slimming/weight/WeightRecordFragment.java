package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
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
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HealthInfoBean;
import lab.wesmartclothing.wefit.flyso.entity.WeightGroupListBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleHistoryData;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.GroupType;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.line.LineBean;
import lab.wesmartclothing.wefit.flyso.view.line.SuitLines;
import lab.wesmartclothing.wefit.flyso.view.line.Unit;

/**
 * Created by jk on 2018/7/26.
 */
public class WeightRecordFragment extends BaseActivity {

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
    @BindView(R.id.tv_curWeight)
    TextView mTvCurWeight;
    @BindView(R.id.iv_bodyFat)
    ImageView mIvBodyFat;
    @BindView(R.id.tv_bodyFatTitle)
    TextView mTvBodyFatTitle;
    @BindView(R.id.tv_bodyFat)
    TextView mTvBodyFat;
    @BindView(R.id.iv_bmi)
    ImageView mIvBmi;
    @BindView(R.id.tv_bmi_title)
    TextView mTvBmiTitle;
    @BindView(R.id.tv_bmi)
    TextView mTvBmi;
    @BindView(R.id.iv_muscle)
    ImageView mIvMuscle;
    @BindView(R.id.tv_muscle_title)
    TextView mTvMuscleTitle;
    @BindView(R.id.tv_muscle)
    TextView mTvMuscle;
    @BindView(R.id.recycler_historyWeight)
    RecyclerView mRecyclerHistoryWeight;
    @BindView(R.id.tv_dataContrast)
    TextView mTvDataContrast;
    @BindView(R.id.img_switch)
    ImageView mImgSwitch;
    @BindView(R.id.img_switchDate)
    ImageView mImgSwitchDate;


    private HealthInfoBean currentWeightInfo;
    //传递数据给我目标详情界面
    private List<HealthInfoBean> list;
    private Bundle bundle = new Bundle();
    private int pageNum = 1;
    private BaseQuickAdapter dayWeightAdapter;
    private @GroupType
    String groupType = GroupType.TYPE_DAYS;
    private boolean mIsWeight = true;
    private boolean needRefresh = true;//通过flag判断是否需要刷新数据

    @Override
    protected int layoutId() {
        return R.layout.fragment_weight_record;
    }

    @Override
    protected int statusBarColor() {
        return getResources().getColor(R.color.green_61D97F);
    }

    @Override
    protected void initViews() {
        super.initViews();

        Typeface typeface = MyAPP.typeface;
        mTvCurWeight.setTypeface(typeface);
        mTvBodyFat.setTypeface(typeface);
        mTvMuscle.setTypeface(typeface);
        mTvBmi.setTypeface(typeface);

        initTopBar();
//        checkStatus();
        initRecyclerHistoryWeight();

        mTvSportDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_CH));

        showHistoryWeightData(BleService.historyWeightData);

        RxTextUtils.getBuilder(getString(R.string.dataChange))
                .append(getString(R.string.dataContrast))
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .into(mTvDataContrast);
    }

    private void initRecyclerHistoryWeight() {
        mRecyclerHistoryWeight.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        dayWeightAdapter = new BaseQuickAdapter<HealthInfoBean, BaseViewHolder>(R.layout.item_today_weight) {

            @Override
            protected void convert(BaseViewHolder helper, HealthInfoBean item) {
                TextView view = helper.getView(R.id.tv_weight);
                RxTextUtils.getBuilder(String.format("%.1f", item.getWeight()))
                        .append(" kg").setProportion(0.5f)
                        .into(view);
                helper.setText(R.id.tv_weightTime, RxFormat.setFormatDate(item.getCreateTime(), "HH:mm"))
                        .setGone(R.id.img_weightFlag, item.getHealthScore() != 0);
            }
        };
        mRecyclerHistoryWeight.setAdapter(dayWeightAdapter);
        dayWeightAdapter.setOnItemClickListener((adapter, view, position) -> {
            HealthInfoBean weightInfoBean = (HealthInfoBean) adapter.getItem(position);
            if (weightInfoBean != null)
                BodyDataFragment.start(mContext, weightInfoBean.getGid(), null, true);
        });
    }


    @Override
    protected void initNetData() {
        super.initNetData();
        initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initRxBus2() {
        super.initRxBus2();
        //体重历史数据
        RxBus.getInstance().register2(ScaleHistoryData.class)
                .compose(RxComposeUtils.<ScaleHistoryData>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<ScaleHistoryData>() {
                    @Override
                    protected void _onNext(ScaleHistoryData data) {
                        final List<QNScaleStoreData> mList = data.getList();
                        showHistoryWeightData(mList);
                    }
                });

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

    private void showHistoryWeightData(final List<QNScaleStoreData> mList) {
        if (!RxDataUtils.isEmpty(mList)) {
            mLayoutStrongTip.setVisibility(View.VISIBLE);
            String checkSporting = getString(R.string.checkHistoryWeight);
            SpannableStringBuilder builder = RxTextUtils.getBuilder(checkSporting)
                    .setForegroundColor(getResources().getColor(R.color.green_61D97F))
                    .setLength(9, checkSporting.length());
            mBtnStrongTip.setText(builder);
            mBtnStrongTip.setOnClickListener(v -> {
                mLayoutStrongTip.setVisibility(View.GONE);
                RxActivityUtils.skipActivity(mActivity, WeightDataActivity.class);
            });
        }
    }

    private void initData() {
        NetManager.getApiService().fetchGroupTypeRecordList(groupType, pageNum, 10)
                .compose(RxComposeUtils.handleResult())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("fetchGroupTypeRecordList" + pageNum + groupType, String.class,
                        CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        WeightGroupListBean bean = new Gson().fromJson(s, WeightGroupListBean.class);
                        if (RxDataUtils.isEmpty(bean.getList())) {
                            return;
                        }
                        updateUI(bean.getList());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    //当日多次体重详情
    private void fetchOneDayWeightList(long currentDate) {
        NetManager.getApiService().fetchDaysOrMonthRecordList(groupType, currentDate, 1, 31)
                .compose(RxComposeUtils.handleResult())
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().transformObservable("fetchDaysOrMonthRecordList" + currentDate + groupType,
                        String.class, needRefresh ? CacheStrategy.firstRemote() : CacheStrategy.firstCacheTimeout(60 * 1000)))
                .map(new CacheResult.MapFunc())
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        needRefresh = false;
                        WeightGroupListBean bean = new Gson().fromJson(s, WeightGroupListBean.class);
                        dayWeightAdapter.setNewData(bean.getList());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                        dayWeightAdapter.setNewData(null);
                    }
                });
    }

    private void updateUI(List<HealthInfoBean> infoBeanList) {
        if (pageNum == 1) {
            list = infoBeanList;
            Collections.reverse(list);
            initLineChart();
            pageNum++;
        } else {
            Collections.reverse(infoBeanList);
            list.addAll(0, infoBeanList);

            List<Unit> lines_weight = new ArrayList<>();
            List<Unit> lines_bodyFat = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                HealthInfoBean itemBean = list.get(i);

                String date = RxFormat.setFormatDate(itemBean.getDateNo(), GroupType.TYPE_DAYS.equals(groupType) ? "MM/dd" : "yyyy/MM");
                Unit unit_weight = new Unit((float) itemBean.getWeight(), date);
                Unit unit_bodyFat = new Unit((float) itemBean.getBodyFatRate(), date);
                lines_weight.add(unit_weight);
                lines_bodyFat.add(unit_bodyFat);
            }
            mSuitlines.addDataChart(Arrays.asList(mIsWeight ? lines_weight : lines_bodyFat));
            pageNum++;
        }
    }


    private void initLineChart() {
        if (RxDataUtils.isEmpty(list)) return;
        List<Unit> lines_weight = new ArrayList<>();
        List<Unit> lines_bodyFat = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            HealthInfoBean itemBean = list.get(i);
            String date = RxFormat.setFormatDate(itemBean.getDateNo(), GroupType.TYPE_DAYS.equals(groupType) ? "MM/dd" : "yyyy/MM");
            Unit unit_weight = new Unit((float) itemBean.getWeight(), date);
            Unit unit_bodyFat = new Unit((float) itemBean.getBodyFatRate(), date);
            lines_weight.add(unit_weight);
            lines_bodyFat.add(unit_bodyFat);
        }

        LineBean weightLine = new LineBean();
        weightLine.setUnits(mIsWeight ? lines_weight : lines_bodyFat);
        weightLine.setShowPoint(true);
        weightLine.setFill(true);
        weightLine.setLineWidth(RxUtils.dp2px(2));
        weightLine.setColor(0x7fffffff, 0x00ffffff);

        mSuitlines.setYSpace(1f, 0);
        new SuitLines.LineBuilder()
                .add(weightLine)
                .build(mSuitlines);

        mSuitlines.setLineChartSelectItemListener(valueX -> {
            if (!list.isEmpty()) {
                mTvCurWeight.setText(String.format("%.1f", list.get(valueX).getWeight()));
                mTvBodyFat.setText(String.format("%.1f", list.get(valueX).getBodyFatRate()));
                mTvMuscle.setText(String.format("%.1f", (list.get(valueX).getMuscleMass() / list.get(valueX).getWeight() * 100f)));
                mTvBmi.setText(String.format("%.1f", list.get(valueX).getBmi()));
                mTvSportDate.setText(RxFormat.setFormatDate(list.get(valueX).getDateNo(), RxFormat.Date_CH));
                currentWeightInfo = list.get(valueX);

                fetchOneDayWeightList(list.get(valueX).getDateNo());
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


    @Override
    protected void onDestroy() {
        if (list != null) {
            list.clear();
            list = null;
        }
        super.onDestroy();

    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle(R.string.weightRecord);
//        btn_Connect = mQMUIAppBarLayout.addRightTextButton(
//                getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC)) ? R.string.unBind :
//                        mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected), R.id.tv_connect);
//        btn_Connect.setTextColor(Color.WHITE);
//        btn_Connect.setTextSize(13);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.img_switch, R.id.img_switchDate, R.id.layout_sports, R.id.tv_dataContrast})
    public void onViewClicked(View view) {
        if (RxUtils.isFastClick(800))
            return;
        switch (view.getId()) {
            case R.id.img_switch:
                if (mIsWeight) {
                    mIsWeight = false;
                    mImgSwitch.setImageResource(R.mipmap.ic_weight_close);
                } else {
                    mIsWeight = true;
                    mImgSwitch.setImageResource(R.mipmap.ic_weight_open);
                }
                initLineChart();
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
            case R.id.layout_sports:
                if (currentWeightInfo == null || RxDataUtils.isEmpty(list)) return;
                bundle.putSerializable(Key.BUNDLE_DATA, currentWeightInfo);
                RxActivityUtils.skipActivity(mContext, HealthyAssessActivity.class, bundle);
                break;
            case R.id.tv_dataContrast:
                RxActivityUtils.skipActivity(mContext, WeightContrastActivity.class);
                break;
            default:
        }
    }
}
