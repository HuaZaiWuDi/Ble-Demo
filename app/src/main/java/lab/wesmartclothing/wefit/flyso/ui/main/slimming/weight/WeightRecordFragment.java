package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.chart.line.LineBean;
import com.vondear.rxtools.view.chart.line.SuitLines;
import com.vondear.rxtools.view.chart.line.Unit;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.HealthyInfoBean;
import lab.wesmartclothing.wefit.flyso.entity.WeightDataBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleHistoryData;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

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
    @BindView(R.id.layout_lenged)
    LinearLayout mLayoutLenged;
    @BindView(R.id.recycler_historyWeight)
    RecyclerView mRecyclerHistoryWeight;
    @BindView(R.id.tv_dataContrast)
    TextView mTvDataContrast;

    private BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //体脂称连接状态
            if (Key.ACTION_SCALE_CONNECT.equals(intent.getAction())) {
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_SCALE_CONNECT, false);
                if (btn_Connect != null)
                    btn_Connect.setText(state ? R.string.connected : R.string.disConnected);
                //系统蓝牙监听
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                if (state == BluetoothAdapter.STATE_OFF) {
                    checkStatus();
                } else if (state == BluetoothAdapter.STATE_ON) {
                    mLayoutStrongTip.setVisibility(View.GONE);
                }
            }
        }
    };

    private QNBleTools mQNBleTools = QNBleTools.getInstance();
    private Button btn_Connect;
    private HealthyInfoBean currentWeightInfo;
    //传递数据给我目标详情界面
    private double lastWeight = 0;//最后一条体重数据
    private List<HealthyInfoBean> list;
    private Bundle bundle = new Bundle();
    private int pageNum = 1;
    private BaseQuickAdapter dayWeightAdapter;

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
        IntentFilter filter = new IntentFilter();
        filter.addAction(Key.ACTION_SCALE_CONNECT);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(registerReceiver, filter);

        Typeface typeface = MyAPP.typeface;
        mTvCurWeight.setTypeface(typeface);
        mTvBodyFat.setTypeface(typeface);
        mTvMuscle.setTypeface(typeface);
        mTvBmi.setTypeface(typeface);

        initTopBar();
        checkStatus();
        initRecyclerHistoryWeight();

        mTvSportDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_CH));

        showHistoryWeightData(BleService.historyWeightData);

        RxTextUtils.getBuilder("了解详细数据变化趋势？请进行")
                .append("数据对比")
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .into(mTvDataContrast);
    }

    private void initRecyclerHistoryWeight() {
        mRecyclerHistoryWeight.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        dayWeightAdapter = new BaseQuickAdapter<HealthyInfoBean, BaseViewHolder>(R.layout.item_today_weight) {

            @Override
            protected void convert(BaseViewHolder helper, HealthyInfoBean item) {
                TextView view = helper.getView(R.id.tv_weight);
                RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(item.getWeight(), 1))
                        .append(" kg").setProportion(0.5f)
                        .into(view);
                helper.setText(R.id.tv_weightTime, RxFormat.setFormatDate(item.getMeasureTime(), "HH:mm"))
                        .setGone(R.id.img_weightFlag, item.getHealthScore() != 0);

                //TODO 通过分数判断体重是否合理

            }
        };
        mRecyclerHistoryWeight.setAdapter(dayWeightAdapter);
        dayWeightAdapter.setOnItemClickListener((adapter, view, position) -> {
            HealthyInfoBean weightInfoBean = (HealthyInfoBean) adapter.getItem(position);
            bundle.putBoolean(Key.BUNDLE_GO_BCAK, true);
            bundle.putSerializable(Key.BUNDLE_DATA, weightInfoBean);
            RxActivityUtils.skipActivity(mContext, BodyDataFragment.class, bundle);
        });

    }


    @Override
    protected void initNetData() {
        super.initNetData();
        initData();
    }

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
        btn_Connect.setText(getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC)) ? R.string.unBind :
                mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected));
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().fetchWeightInfo(pageNum, 10))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchWeightInfo" + pageNum, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        WeightDataBean bean = JSON.parseObject(s, WeightDataBean.class);
                        updateUI(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void fetchOneDayWeightList(String currentGid) {
        //当日多次体重详情
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().fetchOneDateWeightList(currentGid))
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().transformObservable("fetchOneDateWeightList" + currentGid, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        Type type = new TypeToken<List<HealthyInfoBean>>() {
                        }.getType();

                        List<HealthyInfoBean> beanList = JSON.parseObject(s, type);
                        dayWeightAdapter.setNewData(beanList);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


    private void updateUI(WeightDataBean bean) {
        lastWeight = bean.getWeight();

        SPUtils.put(SPKey.SP_realWeight, (float) lastWeight);

        if (bean.getWeightList() == null) {
            return;
        }

        Map<Float, String> map = new HashMap<>();
        map.put((float) bean.getNormWeight(), (float) bean.getNormWeight() + "kg");
        mSuitlines.setlimitLabels(map);

        if (pageNum == 1) {
            list = bean.getWeightList().getList();
            initLineChart();
            pageNum++;
        } else {
            if (RxDataUtils.isEmpty(bean.getWeightList().getList()))
                return;
            Collections.reverse(bean.getWeightList().getList());//
            list.addAll(0, bean.getWeightList().getList());

            List<Unit> lines_weight = new ArrayList<>();
            List<Unit> lines_bodyFat = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                HealthyInfoBean itemBean = list.get(i);
                Unit unit_weight = new Unit((float) itemBean.getWeight(), RxFormat.setFormatDate(itemBean.getWeightDate(), "MM/dd"));
                Unit unit_bodyFat = new Unit((float) itemBean.getBodyFat(), RxFormat.setFormatDate(itemBean.getWeightDate(), "MM/dd"));
                lines_weight.add(unit_weight);
                lines_bodyFat.add(unit_bodyFat);
            }
            mSuitlines.addDataChart(Arrays.asList(lines_weight, lines_bodyFat));
            pageNum++;
        }
    }


    private void initLineChart() {
        Collections.reverse(list);
        List<Unit> lines_weight = new ArrayList<>();
        List<Unit> lines_bodyFat = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            HealthyInfoBean itemBean = list.get(i);
            Unit unit_weight = new Unit((float) itemBean.getWeight(), RxFormat.setFormatDate(itemBean.getWeightDate(), "MM/dd"));
            Unit unit_bodyFat = new Unit((float) itemBean.getBodyFat(), RxFormat.setFormatDate(itemBean.getWeightDate(), "MM/dd"));
            lines_weight.add(unit_weight);
            lines_bodyFat.add(unit_bodyFat);
        }
        LineBean weightLine = new LineBean();
        weightLine.setUnits(lines_weight);
        weightLine.setShowPoint(true);
        weightLine.setLineWidth(RxUtils.dp2px(2));
        weightLine.setColor(0x7fffffff);


        LineBean bodyFatLine = new LineBean();
        bodyFatLine.setFill(true);
        bodyFatLine.setShowPoint(list.size() == 1);
        bodyFatLine.setUnits(lines_bodyFat);
        bodyFatLine.setColor(0x7fffffff);


        new SuitLines.LineBuilder()
                .add(weightLine)
                .add(bodyFatLine)
                .build(mSuitlines);

        mSuitlines.setLineChartSelectItemListener(valueX -> {
            mTvCurWeight.setText(RxFormatValue.fromat4S5R(list.get(valueX).getWeight(), 1));
            mTvBodyFat.setText(RxFormatValue.fromat4S5R(list.get(valueX).getBodyFat(), 1));
            mTvMuscle.setText(RxFormatValue.fromat4S5R((list.get(valueX).getSinew() / list.get(valueX).getWeight() * 100f), 1));
            mTvBmi.setText(RxFormatValue.fromat4S5R(list.get(valueX).getBmi(), 1));
            mTvSportDate.setText(RxFormat.setFormatDate(list.get(valueX).getWeightDate(), RxFormat.Date_CH));
            currentWeightInfo = list.get(valueX);

            fetchOneDayWeightList(list.get(valueX).getGid());
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

    private void checkStatus() {
        if (!BleTools.getBleManager().isBlueEnable()) {
            mLayoutStrongTip.setVisibility(View.VISIBLE);
            String tipOpenBlueTooth = getString(R.string.tipOpenBlueTooth);
            SpannableStringBuilder builder = RxTextUtils.getBuilder(tipOpenBlueTooth)
                    .setForegroundColor(getResources().getColor(R.color.green_61D97F))
                    .setLength(12, tipOpenBlueTooth.length());
            mBtnStrongTip.setText(builder);
            mBtnStrongTip.setOnClickListener(v -> BleTools.getBleManager().enableBluetooth());
        }
    }


    @OnClick({R.id.layout_sports, R.id.tv_dataContrast})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.layout_sports) {
            if (currentWeightInfo == null || RxDataUtils.isEmpty(list)) return;
            bundle.putSerializable(Key.BUNDLE_DATA, currentWeightInfo);
            RxActivityUtils.skipActivity(mContext, HealthyAssessActivity.class, bundle);
        } else if (view.getId() == R.id.tv_dataContrast) {
            RxActivityUtils.skipActivity(mContext, WeightContrastActivity.class);
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(registerReceiver);
        super.onDestroy();
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle("体重记录");
        btn_Connect = mQMUIAppBarLayout.addRightTextButton(
                getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC)) ? R.string.unBind :
                        mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected), R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
