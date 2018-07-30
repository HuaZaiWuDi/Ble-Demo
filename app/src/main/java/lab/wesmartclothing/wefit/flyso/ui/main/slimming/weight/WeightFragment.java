package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.module_wefit.bean.Device;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.yolanda.health.qnblesdk.listen.QNDataListener;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;
import lab.wesmartclothing.wefit.flyso.entity.WeightDataBean;
import lab.wesmartclothing.wefit.flyso.entity.WeightInfoItem;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.utils.ChartManager;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.WeightTools;
import lab.wesmartclothing.wefit.flyso.view.MyMarkerView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import me.dkzwm.widget.srl.RefreshingListenerAdapter;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.config.Constants;
import me.dkzwm.widget.srl.extra.header.MaterialHeader;
import me.dkzwm.widget.srl.utils.PixelUtl;
import okhttp3.RequestBody;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
@EFragment()
public class WeightFragment extends BaseAcFragment {

    public static WeightFragment getInstance() {
        return new WeightFragment_();
    }

    TextView tv_weight_date;
    TextView tv_idealWeight;
    TextView tv_currentWeight;

    @ViewById
    RecyclerView mRecyclerView;
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


    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
            initDeviceConnectTip(0);
        } else if (state == BluetoothAdapter.STATE_ON) {
            initDeviceConnectTip(3);
        }
    }

    //体脂称连接状态
    @Receiver(actions = Key.ACTION_SCALE_CONNECT)
    void scaleIsConnect(@Receiver.Extra(Key.EXTRA_SCALE_CONNECT) boolean state) {
        isConnect = state;

        tv_connectDevice.setText(state ? R.string.connected : R.string.disConnected);
        if (!state)
            initDeviceConnectTip(3);
    }


    @AfterViews
    public void initView() {
        initRecyclerView();
        initRxBus();
        initChart();
        initHRefresh();
        initWeightInfo();
        initQNBle();
        initBleCallBack();

    }


    @Override
    protected void onInvisible() {
        super.onInvisible();
    }


    private List<WeightInfoItem> weightLists = new ArrayList<>();

    private BaseQuickAdapter adapter;
    private List<QNScaleStoreData> QNDatas = new ArrayList<>();
    private List<WeightDataBean.WeightListBean.ListBean> weightDatas = new ArrayList<>();//体重的总集合
    private int pageNum = 1;
    private boolean refreshOnce = false;//解决多次刷新的问题
    private boolean notMore = false;//解决多次刷新的问题
    public static boolean isConnect = false;
    Highlight mHighlight; //记录当前高亮的位置
    ChartManager chartManager; //chart管理类


    @Override
    public void onStart() {
        if (BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
            initDeviceConnectTip(1);
        } else {
            tv_connectDevice.setText(isConnect ? R.string.connected : R.string.disConnected);
            getWeightData();
        }
        super.onStart();
    }

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_weight, null);
        return rootView;
    }


    private void initBleCallBack() {

        MyAPP.QNapi.setDataListener(new QNDataListener() {
            @Override
            public void onGetUnsteadyWeight(QNBleDevice qnBleDevice, double v) {
                RxLogUtils.d("体重秤实时重量：" + v);
            }

            @Override
            public void onGetScaleData(QNBleDevice qnBleDevice, final QNScaleData qnScaleData) {
//                RxLogUtils.d("实时的稳定测量数据是否有效：" + qnScaleData.isValid());
                for (QNScaleItemData item : qnScaleData.getAllItem()) {
                    RxLogUtils.d("实时的稳定测量数据：" + item.getValue());
                    RxLogUtils.d("实时的稳定测量数据：" + item.getName());
                    RxLogUtils.d("实时的稳定测量数据：" + item.getType());
                }

                isWeightValid(qnScaleData);
            }

            @Override
            public void onGetStoredScale(QNBleDevice qnBleDevice, List<QNScaleStoreData> list) {
                RxLogUtils.d("历史数据：" + list.size());
                for (QNScaleStoreData data : list) {
                    RxLogUtils.d("历史数据：" + data.getWeight());
                    QNDatas.add(data);
                    initDeviceConnectTip(2);
                }
            }
        });
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
        Disposable device = RxBus.getInstance().register(Device.class, new Consumer<Device>() {
            @Override
            public void accept(Device device) throws Exception {
                if (BleKey.TYPE_SCALE.equals(device.getDeviceNo())) {
                    //删除绑定
                    RxLogUtils.d("WeightFragment:删除绑定");
                    mQNBleTools.disConnectDevice(device.getMacAddr());
                    SPUtils.put(SPKey.SP_scaleMAC, "");
                    initDeviceConnectTip(1);
                    initWeightInfo();
                }
            }
        });

        RxBus.getInstance().addSubscription(this, device);
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


    private void initQNBle() {

        if (!BleTools.getBleManager().isBlueEnable()) {
            RxLogUtils.v("蓝牙未开启:");
            initDeviceConnectTip(0);
            return;
        }
    }


    @Override
    public void onDestroy() {
        MyAPP.QNapi.setBleConnectionChangeListener(null);
        MyAPP.QNapi.setDataListener(null);
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();
    }


    //初始化图表
    private void initChart() {
        chartManager = new ChartManager(mActivity, mLineChart);
        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
        chartManager.addMarker(mv);

        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setAxisMaximum(110f);
        yAxis.setAxisMinimum(35f);
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
        View inflate = View.inflate(mActivity, R.layout.layout_weight_head, null);
        tv_weight_date = inflate.findViewById(R.id.tv_weight_date);
        tv_idealWeight = inflate.findViewById(R.id.tv_idealWeight);
        tv_currentWeight = inflate.findViewById(R.id.tv_currentWeight);
        adapter.setHeaderView(inflate);

        adapter.bindToRecyclerView(mRecyclerView);
    }


    private void getWeightData() {
        if (pageNum == 1) weightDatas.clear();
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getWeightList(pageNum, 20))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (mActivity != null)
                            mRefresh.refreshComplete();
                    }
                })
                .compose(MyAPP.getRxCache().<String>transformObservable("getWeightList", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取体重数据：" + s);

                        WeightDataBean bean = new Gson().fromJson(s, WeightDataBean.class);
                        if (bean == null) return;

                        List<WeightDataBean.WeightListBean.ListBean> list = bean.getWeightList().getList();

//                        tv_idealWeight.setText(bean.getTargetWeight() + "kg");

                        if (list.size() <= 0) {
                            if (!bean.getWeightList().isHasNextPage()) {
                                notMore = true;
                                mRefresh.setEnableAutoRefresh(false);
                                mRefresh.setDisableRefresh(true);
                            }
                            return;
                        }
                        weightDatas.addAll(0, list);
                        syncChart(weightDatas);

                        if (weightDatas.size() > 0) {
                            tv_currentWeight.setText(weightDatas.get(weightDatas.size() - 1).getWeight() + "kg");
                        }

                        //提示线，
                        chartManager.addLimitLine2Y((float) bean.getNormWeight(), "标准:\n" + (float) bean.getNormWeight() + "kg");//线条颜色宽度等

                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    private void syncChart(final List<WeightDataBean.WeightListBean.ListBean> list) {
        Calendar calendar = Calendar.getInstance();

        synWeightData(list.get(list.size() - 1));
        List<String> days = new ArrayList<>();
        List<String> days_d = new ArrayList<>();//真实数据
        List<String> days_f = new ArrayList<>();//头
        List<String> days_l = new ArrayList<>();//尾
        ArrayList<Entry> yVals = new ArrayList();

        if (list.size() > 0)
            calendar.setTimeInMillis(list.get(0).getWeightDate());

        //左边添加3条数据
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        days_l.add(RxFormat.setFormatDate(calendar, "MM/dd"));
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        days_l.add(RxFormat.setFormatDate(calendar, "MM/dd"));
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        days_l.add(RxFormat.setFormatDate(calendar, "MM/dd"));

        //添加真实数据
        for (int i = 0; i < list.size(); i++) {
            yVals.add(new Entry(i, (float) list.get(i).getWeight()));
            days_d.add(RxFormat.setFormatDate(list.get(i).getWeightDate(), "MM/dd"));
        }

        //右边添加3填数据
        days_f.add("--/--");
        days_f.add("--/--");
        days_f.add("--/--");

        days.addAll(days_l);
        days.addAll(days_d);
        days.addAll(days_f);

        XAxis x = mLineChart.getXAxis();
        x.setSpaceMax(3f);
        x.setSpaceMin(3f);

        chartManager.setData(yVals, days);

        try {
            mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    RxLogUtils.d("点击");
                    mHighlight = h;
                    synWeightData(list.get((int) e.getX() % list.size()));
                }

                @Override
                public void onNothingSelected() {
                    RxLogUtils.d("什么都没点击");
                    if (mHighlight != null)
                        mLineChart.highlightValue(mHighlight);
                }
            });
        } catch (Exception e) {
            RxLogUtils.e("发生异常了！！！" + e.getMessage());
        }


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
//                RxLogUtils.d("最小显示的X轴：" + visibleX);
                if (visibleX != -3) {
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


    private void addWeightData(final QNScaleData qnScaleData) {
        WeightAddBean bean = new WeightAddBean();
        bean.setUserId(SPUtils.getString(SPKey.SP_UserId));
        bean.setMeasureTime(System.currentTimeMillis() + "");
        for (QNScaleItemData item : qnScaleData.getAllItem()) {
            WeightTools.ble2Backstage(item, bean);
            WeightDataBean.WeightListBean.ListBean listBean = WeightTools.ble2Backstage2(item);
            synWeightData(listBean);
        }

        String s = new Gson().toJson(bean, WeightAddBean.class);

        SPUtils.put(SPKey.SP_realWeight, (float) qnScaleData.getItem(1).getValue());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addWeightInfo(body))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加体重：");
                        pageNum = 1;
                        getWeightData();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
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
    }

    //验证体重是否合理
    private void isWeightValid(final QNScaleData qnScaleData) {
        if (weightDatas.size() == 0) {
            addWeightData(qnScaleData);
        } else {
            double weight_1 = weightDatas.get(weightDatas.size() - 1).getWeight();//网络数据
            double weight_2 = SPUtils.getFloat(SPKey.SP_realWeight, (float) weightDatas.get(weightDatas.size() - 1).getWeight());
            double weight_3 = qnScaleData.getAllItem().get(0).getValue();//当前数据
            if (Math.abs(weight_1 - weight_3) >= 2) {
                showDialog(qnScaleData);
            } else if (Math.abs(weight_2 - weight_3) >= 2) {
                showDialog(qnScaleData);
            } else {
                addWeightData(qnScaleData);
            }
        }
    }

    private void showDialog(final QNScaleData qnScaleData) {
        //差值大于2kg，体重数据不合理
        final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
        dialog.getTvTitle().setBackgroundResource(R.mipmap.slice);
        dialog.getTvContent().setText("请确认这是您的体重吗？");
        dialog.setCancel("不是我");
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setSure("就是我");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addWeightData(qnScaleData);
            }
        });
    }

    //界面提示状态
    private void initDeviceConnectTip(int QN_bleState) {
        tv_connectTip.setVisibility(View.VISIBLE);
//        Drawable drawable = getResources().getDrawable(QN_bleState == 1 ? R.mipmap.unbound_icon : R.mipmap.connect_icon);
//        //一定要加这行！！！！！！！！！！！
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        tv_connectDevice.setCompoundDrawables(drawable, null, null, null);
        switch (QN_bleState) {
            case 0://打开蓝牙
                if (BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
                    initDeviceConnectTip(1);
                } else {
                    RxTextUtils.getBuilder(getString(R.string.connectBle))
                            .append(getString(R.string.phoneBle)).setForegroundColor(getResources().getColor(R.color.colorTheme))
                            .into(tv_connectTip);
                    tv_connectTip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!BleTools.getBleManager().isBlueEnable())
                                BleTools.getBleManager().enableBluetooth();
                        }
                    });
                }
                break;
            case 1://绑定设备
                mLineChart.clear();
                mLineChart.invalidate();
                tv_connectDevice.setText(R.string.unBind);
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
                        bundle.putString(Key.BUNDLE_BIND_TYPE, BleKey.TYPE_SCALE);
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
                if (BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
                    initDeviceConnectTip(1);
                } else
                    tv_connectTip.setVisibility(View.GONE);
                break;
        }
    }

}
