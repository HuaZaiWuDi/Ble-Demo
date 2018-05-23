package lab.dxythch.com.commonproject.ui.main.slimming.weight;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;
import com.yolanda.health.qnblesdk.listen.QNBleConnectionChangeListener;
import com.yolanda.health.qnblesdk.listen.QNBleDeviceDiscoveryListener;
import com.yolanda.health.qnblesdk.listen.QNDataListener;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseFragment;
import lab.dxythch.com.commonproject.base.MyAPP;
import lab.dxythch.com.commonproject.ble.QNBleTools;
import lab.dxythch.com.commonproject.entity.WeightDataBean;
import lab.dxythch.com.commonproject.entity.WeightInfoItem;
import lab.dxythch.com.commonproject.netserivce.RetrofitService;
import lab.dxythch.com.commonproject.prefs.Prefs_;
import lab.dxythch.com.commonproject.rxbus.WeightIsUpdate;
import lab.dxythch.com.commonproject.tools.Key;
import lab.dxythch.com.commonproject.ui.login.AddDeviceActivity_;
import lab.dxythch.com.commonproject.utils.ChartManager;
import lab.dxythch.com.commonproject.view.MyMarkerView;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
import lab.dxythch.com.netlib.utils.RxBus;
import me.dkzwm.widget.srl.RefreshingListenerAdapter;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.config.Constants;
import me.dkzwm.widget.srl.extra.header.MaterialHeader;
import me.dkzwm.widget.srl.utils.PixelUtl;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_weight)
public class WeightFragment extends BaseFragment {

    public static WeightFragment getInstance() {
        return new WeightFragment_();
    }

    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    TextView tv_weight_date;
    @ViewById
    TextView tv_idealWeight;
    @ViewById
    TextView tv_currentWeight;
    @ViewById
    LineChart mLineChart;
    @ViewById
    TextView tv_connectDevice;
    @ViewById
    TextView tv_connectTip;
    @ViewById
    LinearLayout dialog_not_connect;
    @ViewById
    SmoothRefreshLayout mRefresh;


    @Bean
    QNBleTools mQNBleTools;

    @Pref
    Prefs_ mPrefs;


    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
            initDeviceConnectTip(0);
        } else if (state == BluetoothAdapter.STATE_ON) {
            tv_connectTip.setVisibility(View.GONE);
            initQNBle();
        }
    }

    @Click
    void icon_weight() {
        RxActivityUtils.skipActivity(mActivity, WeightDataActivity_.class);
    }

    @AfterViews
    public void initView() {
        initRecyclerView();
        initRxBus();
        initChart();
        initHRefresh();
        initWeightInfo();

        if (!mPrefs.scaleIsBind().get()) {
            initDeviceConnectTip(1);
        }
    }


    private List<WeightInfoItem> weightLists = new ArrayList<>();


    private BaseQuickAdapter adapter;

    private List<QNScaleData> QNDatas = new ArrayList<>();
    private List<WeightDataBean.WeightListBean.ListBean> weightDatas = new ArrayList<>();//体重的总集合
    private int pageNum = 1;
    private boolean refreshOnce = false;//解决多次刷新的问题
    private boolean notMore = false;//解决多次刷新的问题

    @Override
    public void initData() {
        getWeightData();
        initQNBle();
    }

    private void initHRefresh() {
        MaterialHeader header = new MaterialHeader(mActivity);
        header.setColorSchemeColors(new int[]{Color.parseColor("#00C5CC"), Color.parseColor("#62DFBA")});
        header.setPadding(PixelUtl.dp2px(mActivity, 25), 0, PixelUtl.dp2px(mActivity, 25), 0);
        mRefresh.setHeaderView(header);

        mRefresh.setDisableRefresh(true);
        mRefresh.setDisableLoadMore(true);
        mRefresh.setEnableOverScroll(false);
        mRefresh.setNestedScrollingEnabled(false);
        mRefresh.setEnablePinContentView(true);
        mRefresh.setEnablePinRefreshViewWhileLoading(true);

        mRefresh.setOnRefreshListener(new RefreshingListenerAdapter() {
            @Override
            public void onRefreshBegin(boolean isRefresh) {
                RxLogUtils.d("开始刷新");
                pageNum++;
                getWeightData();
            }
        });
    }

    private void initRxBus() {
        Disposable register = RxBus.getInstance().register(WeightIsUpdate.class, new Consumer<WeightIsUpdate>() {
            @Override
            public void accept(WeightIsUpdate weightIsUpdate) throws Exception {
                pageNum = 1;
                weightDatas.clear();
                getWeightData();
            }
        });
        RxBus.getInstance().addSubscription(this, register);
    }

    private void initWeightInfo() {
        QNDatas.clear();
        String[] weight_title = getResources().getStringArray(R.array.weight_title);
        int[] img_icons = {R.mipmap.ic_age, R.mipmap.ic_bodyfat, R.mipmap.ic_bmr, R.mipmap.ic_habitus,
                R.mipmap.ic_ffm, R.mipmap.ic_weight, R.mipmap.ic_musculoskeletal, R.mipmap.ic_muscle,
                R.mipmap.ic_protein, R.mipmap.ic_water, R.mipmap.ic_subfat, R.mipmap.ic_bone,
                R.mipmap.ic_visfat, R.mipmap.ic_bmi};
        String[] data = {"--" + getString(R.string.bodyAge), "--%", "--kcal", "--", "--kg", "--kg", "--%", "--kg", "--%", "--%", "--%",
                "--kg", "--" + getString(R.string.level), "--"};

        for (int i = 0; i < weight_title.length; i = i + 2) {
            WeightInfoItem item = new WeightInfoItem();
            item.setTitle_left(weight_title[i]);
            item.setTitle_right(weight_title[i + 1]);
            item.setImg_left(img_icons[i]);
            item.setImg_right(img_icons[i + 1]);
            item.setData_left(data[i]);
            item.setData_right(data[i + 1]);
            weightLists.add(item);
        }
        adapter.setNewData(weightLists);
        tv_weight_date.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date));
    }

    @Override
    protected void onVisible() {
        super.onVisible();
    }

    @Override
    protected void onInvisible() {
        super.onVisible();
    }

    private void initQNBle() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            RxLogUtils.v("蓝牙未开启:");
            initDeviceConnectTip(0);
            return;
        }

        MyAPP.QNapi.setBleDeviceDiscoveryListener(new QNBleDeviceDiscoveryListener() {
            @Override
            public void onDeviceDiscover(QNBleDevice device) {
                RxLogUtils.v("扫描的设备:" + device.getName());
                RxLogUtils.v("扫描的设备:" + device.getMac());
//                if ("Scale".equals(device.getName()))
//                    mQNBleTools.connectDevice(device);
            }

            @Override
            public void onStartScan() {

            }

            @Override
            public void onStopScan() {

            }
        });

        mQNBleTools.scanBle();
        MyAPP.QNapi.setBleConnectionChangeListener(new QNBleConnectionChangeListener() {
            @Override
            public void onConnecting(QNBleDevice qnBleDevice) {

            }

            @Override
            public void onConnected(QNBleDevice qnBleDevice) {
                RxLogUtils.e("连接:");
                tv_connectDevice.setText(R.string.connected);
            }

            @Override
            public void onServiceSearchComplete(QNBleDevice qnBleDevice) {

            }

            @Override
            public void onDisconnecting(QNBleDevice qnBleDevice) {

            }

            @Override
            public void onDisconnected(QNBleDevice qnBleDevice) {
                RxLogUtils.e("断开连接:");
                tv_connectDevice.setText(R.string.disConnected);
                mQNBleTools.scanBle();
            }

            @Override
            public void onConnectError(QNBleDevice qnBleDevice, int i) {
                RxLogUtils.d("连接异常：" + i);
            }

            @Override
            public void onScaleStateChange(QNBleDevice qnBleDevice, int i) {
                RxLogUtils.d("体重秤状态变化:" + i);
            }
        });

        MyAPP.QNapi.setDataListener(new QNDataListener() {
            @Override
            public void onGetUnsteadyWeight(QNBleDevice qnBleDevice, double v) {
                RxLogUtils.d("体重秤实时重量：" + v);
            }

            @Override
            public void onGetScaleData(QNBleDevice qnBleDevice, QNScaleData qnScaleData) {
                RxLogUtils.d("实时的稳定测量数据是否有效：" + qnScaleData.isValid());
                for (QNScaleItemData item : qnScaleData.getAllItem()) {
                    RxLogUtils.d("实时的稳定测量数据：" + item.getValue());
                    RxLogUtils.d("实时的稳定测量数据：" + item.getName());
                    RxLogUtils.d("实时的稳定测量数据：" + item.getType());
                }
                if (qnScaleData.isValid()) {
                    QNDatas.add(qnScaleData);
                    initDeviceConnectTip(2);
                }
            }

            @Override
            public void onGetStoredScale(QNBleDevice qnBleDevice, List<QNScaleStoreData> list) {
                RxLogUtils.d("历史数据：" + list.size());
                for (QNScaleStoreData data : list) {
                    RxLogUtils.d("历史数据：" + data.getWeight());
                    QNScaleData qnScaleData = data.generateScaleData();
                    if (qnScaleData != null)
                        if (qnScaleData.isValid()) {
                            QNDatas.add(qnScaleData);
                            initDeviceConnectTip(2);
                        }
                }

            }
        });
    }


    @Override
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        mQNBleTools.stopScan();
        super.onDestroy();
    }


    private void initDeviceConnectTip(int QN_bleState) {
        tv_connectTip.setVisibility(View.VISIBLE);
        switch (QN_bleState) {
            case 0://打开蓝牙
                RxTextUtils.getBuilder(getString(R.string.connectBle))
                        .append(getString(R.string.phoneBle)).setForegroundColor(getResources().getColor(R.color.colorTheme))
                        .into(tv_connectTip);
                tv_connectTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BluetoothAdapter.getDefaultAdapter().enable();
                    }
                });
                break;
            case 1://绑定设备
                tv_connectDevice.setText(R.string.unBind);
                Drawable drawable = getResources().getDrawable(R.mipmap.unbound_icon);
                //一定要加这行！！！！！！！！！！！
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_connectDevice.setCompoundDrawables(drawable, null, null, null);
                RxTextUtils.getBuilder(getString(R.string.unbindDevice))
                        .append(getString(R.string.goBind)).setForegroundColor(getResources().getColor(R.color.colorTheme))
                        .into(tv_connectTip);
                tv_connectTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_connectTip.setVisibility(View.GONE);
                        //TODO 去绑定设备
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Key.BUNDLE_FORCE_BIND, true);
                        RxActivityUtils.skipActivity(mActivity, AddDeviceActivity_.class, bundle);
                    }
                });
                break;
            case 2://领取体重
                RxTextUtils.getBuilder(getString(R.string.newWeight))
                        .append(getString(R.string.waitReceive)).setForegroundColor(getResources().getColor(R.color.colorTheme))
                        .into(tv_connectTip);
                tv_connectTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_connectTip.setVisibility(View.GONE);

                        String s = new Gson().toJson(QNDatas);
                        RxLogUtils.d("体重信息:" + s);
                        Bundle bundle = new Bundle();
                        bundle.putString(Key.BUNDLE_WEIGHT_HISTORY, s);
                        RxActivityUtils.skipActivity(mActivity, WeightDataActivity_.class, bundle);
                        QNDatas.clear();
                    }
                });
                break;
            case 3:
                tv_connectTip.setVisibility(View.GONE);
                dialog_not_connect.setVisibility(View.VISIBLE);
                break;
        }
    }

    ChartManager chartManager;

    //初始化图表
    private void initChart() {
        chartManager = new ChartManager(mActivity, mLineChart);
        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
        chartManager.addMarker(mv);

        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setAxisMaximum(150f);
        yAxis.setAxisMinimum(20f);
        mLineChart.invalidate();

    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new BaseQuickAdapter<WeightInfoItem, BaseViewHolder>(R.layout.item_body_info) {

            @Override
            protected void convert(BaseViewHolder helper, WeightInfoItem item) {
                helper.setText(R.id.tv_left, item.getData_left());
                helper.setText(R.id.tv_right, item.getData_right());
                helper.setText(R.id.title_left, item.getTitle_left());
                helper.setText(R.id.title_right, item.getTitle_right());
                helper.setImageResource(R.id.img_left, item.getImg_left());
                helper.setImageResource(R.id.img_right, item.getImg_right());
            }
        };
        adapter.bindToRecyclerView(mRecyclerView);
    }


    private void getWeightData() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getWeightList(pageNum, 20))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tipDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        tipDialog.dismiss();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {

                        RxLogUtils.d("获取体重数据：" + s);

                        WeightDataBean bean = new Gson().fromJson(s, WeightDataBean.class);

                        List<WeightDataBean.WeightListBean.ListBean> list = bean.getWeightList().getList();

                        if (list.size() > 0) {
                            weightDatas.addAll(0, list);
                            syncChart(weightDatas);
                        }
                        tv_idealWeight.setText(bean.getIdealWeight() + "kg");
                        if (mRefresh.isRefreshing()) {
                            mRefresh.refreshComplete();
                        }
                        for (int i = 0; i < weightDatas.size(); i++) {
                            RxLogUtils.d(i + "---:" + weightDatas.get(i).getWeight());
                        }

                        //提示线，
                        chartManager.addLimitLine2Y((float) bean.getIdealWeight(), "标准");//线条颜色宽度等

                        if (!bean.getWeightList().isHasNextPage()) {
                            RxToast.info("没有更多数据了");
                            notMore = true;
                            mRefresh.setEnableAutoRefresh(false);
                            mRefresh.setDisableRefresh(true);
                        }

                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    private void syncChart(final List<WeightDataBean.WeightListBean.ListBean> list) {
        synWeightData(list.get(list.size() - 1));
        List<String> days = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < list.size(); i++) {
            yVals.add(new Entry(i, (float) list.get(i).getWeight()));
            days.add(RxFormat.setFormatDate(list.get(i).getWeightDate(), "MM/dd"));
        }

        chartManager.setData(yVals, days);

        mLineChart.moveViewToX(yVals.size() - (pageNum - 1) * 20 - 4);

        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                RxLogUtils.e("---->", e.getX() + "  " + e.getY());
                synWeightData(list.get((int) e.getX()));
            }

            @Override
            public void onNothingSelected() {

            }
        });


        mLineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                refreshOnce = true;
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                float visibleX = mLineChart.getLowestVisibleX();
                if (visibleX != 0) {
                    mRefresh.setEnableAutoRefresh(false);
                    mRefresh.setDisableRefresh(true);
                } else {
                    if (refreshOnce && !notMore) {
                        refreshOnce = false;
                        RxLogUtils.d("刷新：");
                        mRefresh.setDisableRefresh(false);
                        mRefresh.setEnableAutoRefresh(true);
                        if (!mRefresh.isRefreshing())
                            mRefresh.autoRefresh(Constants.ACTION_AT_ONCE, false);
                    }
                }
            }
        });
    }


    private void synWeightData(WeightDataBean.WeightListBean.ListBean bean) {
        if (bean == null) return;
        weightLists.get(0).setData_left(bean.getBodyAge() + getString(R.string.bodyAge));
        weightLists.get(0).setData_right(bean.getBodyFat() + "%");
        weightLists.get(1).setData_left(bean.getBmr() + "kcal");
        weightLists.get(1).setData_right(bean.getBodyType());
        weightLists.get(2).setData_left(bean.getBodyFfm() + "kg");
        weightLists.get(2).setData_right(bean.getWeight() + "kg");
        weightLists.get(3).setData_left(bean.getSinew() + "%");
        weightLists.get(3).setData_right(bean.getMuscle() + "kg");
        weightLists.get(4).setData_left(bean.getProtein() + "%");
        weightLists.get(4).setData_right(bean.getWater() + "%");
        weightLists.get(5).setData_left(bean.getSubfat() + "%");
        weightLists.get(5).setData_right(bean.getBone() + "kg");
        weightLists.get(6).setData_left(bean.getVisfat() + getString(R.string.level));
        weightLists.get(6).setData_right(bean.getBmi() + "");

        adapter.setNewData(weightLists);

        tv_weight_date.setText(RxFormat.setFormatDate(bean.getWeightDate(), RxFormat.Date));
        tv_currentWeight.setText(bean.getWeight() + "kg");

    }

}
