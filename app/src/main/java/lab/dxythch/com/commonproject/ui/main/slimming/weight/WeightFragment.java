package lab.dxythch.com.commonproject.ui.main.slimming.weight;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
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
import org.json.JSONException;
import org.json.JSONObject;

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
import lab.dxythch.com.commonproject.utils.MyXFormatter;
import lab.dxythch.com.commonproject.view.MyMarkerView;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
import lab.dxythch.com.netlib.utils.RxBus;
import me.dkzwm.widget.srl.RefreshingListenerAdapter;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.header.MaterialHeader;
import me.dkzwm.widget.srl.utils.PixelUtl;
import okhttp3.RequestBody;

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
        initQNBle();
    }

    private List<WeightInfoItem> weightLists = new ArrayList<>();

    private ArrayList<Entry> yVals = new ArrayList<Entry>();
    private BaseQuickAdapter adapter;

    private List<QNScaleData> QNDatas = new ArrayList<>();

    @Override
    public void initData() {
        getWeightData();
    }

    private void initHRefresh() {
        MaterialHeader header = new MaterialHeader(mActivity);
        header.setColorSchemeColors(new int[]{Color.parseColor("#00C5CC"), Color.parseColor("#62DFBA")});
        header.setPadding(PixelUtl.dp2px(mActivity, 25), 0, PixelUtl.dp2px(mActivity, 25), 0);
        mRefresh.setHeaderView(header);

        mRefresh.setDisableLoadMore(true);
        mRefresh.setDisableLoadMore(true);
        mRefresh.setEnableOverScroll(false);
        mRefresh.setNestedScrollingEnabled(false);
        mRefresh.setEnablePinContentView(true);
        mRefresh.setEnablePinRefreshViewWhileLoading(true);

        mRefresh.setOnRefreshListener(new RefreshingListenerAdapter() {
            @Override
            public void onRefreshBegin(boolean isRefresh) {
                mRefresh.refreshComplete(5000);
            }
        });

    }

    private void initRxBus() {
        Disposable register = RxBus.getInstance().register(WeightIsUpdate.class, new Consumer<WeightIsUpdate>() {
            @Override
            public void accept(WeightIsUpdate weightIsUpdate) throws Exception {
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
                if ("Scale".equals(device.getName()))
                    mQNBleTools.connectDevice(device);
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
                tv_connectDevice.setCompoundDrawables(getResources().getDrawable(R.mipmap.unbound_icon), null, null, null);
                RxTextUtils.getBuilder(getString(R.string.unbindDevice))
                        .append(getString(R.string.goBind)).setForegroundColor(getResources().getColor(R.color.colorTheme))
                        .into(tv_connectTip);
                tv_connectTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_connectTip.setVisibility(View.GONE);
                        //TODO 去绑定设备
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

    //初始化图表
    private void initChart() {
        // no description text
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setTouchEnabled(true);//可以点击
        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);//X，Y轴缩放

        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setEnabled(false);//关闭图例
        mLineChart.setAutoScaleMinMaxEnabled(false);
        mLineChart.setNoDataText("");//没有数据时显示

        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
        mv.setChartView(mLineChart); // For bounds control
        mLineChart.setMarker(mv); // Set the marker to the chart

        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();

    }


    private void setData(ArrayList<Entry> yVals, List<String> xVals) {
        // 阴影线
        ArrayList<Entry> yVals_2 = new ArrayList<Entry>();

        for (int i = 0; i < yVals.size() + 3; i++) {
            yVals_2.add(new Entry(i, 42));
        }

        XAxis x = mLineChart.getXAxis();
        x.setLabelCount(7, false);
        x.setTextColor(Color.WHITE);
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setGridColor(getResources().getColor(R.color.lineColor));
        x.setAxisLineColor(Color.WHITE);
        x.setDrawAxisLine(true);
        x.setDrawLabels(true);
        x.setValueFormatter(new MyXFormatter(xVals));


        YAxis y = mLineChart.getAxisLeft();
        y.setDrawLimitLinesBehindData(true);
        y.setLabelCount(13, false);
        y.setTextColor(Color.WHITE);
        y.setDrawGridLines(true);
        y.setGridColor(getResources().getColor(R.color.lineColor));
        y.setAxisLineColor(Color.WHITE);
//        y.setGranularity(2f);// //设置最小间隔，防止当放大时出现重复标签
        y.setDrawAxisLine(false);
        y.setAxisMaximum(150f);
        y.setAxisMinimum(20f);

        //提示线，
        LimitLine ll = new LimitLine(42, "标准");//线条颜色宽度等
        ll.setLineColor(getResources().getColor(R.color.colorTheme));
        ll.setLineWidth(1f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);//文字颜色、大小
        ll.setTextColor(getResources().getColor(R.color.white));
        ll.setTextSize(12f);

        //加入到 mXAxis 或 mYAxis
        y.addLimitLine(ll);

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setLineWidth(3f);

        set1.setDrawCircles(false);//是否显示节点圆心

        set1.setColor(Color.WHITE);
        set1.setDrawVerticalHighlightIndicator(false);
        set1.setDrawHorizontalHighlightIndicator(false);


        //阴影线
        LineDataSet set2 = new LineDataSet(yVals_2, "DataSet 2");
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setLineWidth(1f);
        set2.setColor(getResources().getColor(R.color.colorTheme));
        set2.setDrawCircles(false);//是否显示节点圆心
        set2.setDrawVerticalHighlightIndicator(false);
        set2.setDrawHorizontalHighlightIndicator(false);
        set2.setHighlightEnabled(false);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextSize(9f);
        data.setDrawValues(false);


        // set data
        mLineChart.setData(data);
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();

        // do not forget to refresh the chart
        mLineChart.animateX(500);

        mLineChart.centerViewToY(50f, YAxis.AxisDependency.LEFT);

        mLineChart.moveViewToX(9);

        mLineChart.setVisibleXRangeMaximum(7);

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
        mRecyclerView.setAdapter(adapter);
    }


    private void getWeightData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", "testuser");
            jsonObject.put("pageNum", 1);
            jsonObject.put("pageSize", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getWeightList(1, 10))
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

                        if (list.size() > 0)
                            syncChart(list);

                        tv_idealWeight.setText(bean.getIdealWeight() + "kg");
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void syncChart(final List<WeightDataBean.WeightListBean.ListBean> list) {
        List<String> days = new ArrayList<>();
        synWeightData(list.get(list.size() - 1));

        for (int i = 0; i < list.size(); i++) {
            yVals.add(new Entry(i, (float) list.get(i).getWeight()));
            days.add(RxFormat.setFormatDate(list.get(i).getWeightDate(), "MM/dd"));
        }

        setData(yVals, days);

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
