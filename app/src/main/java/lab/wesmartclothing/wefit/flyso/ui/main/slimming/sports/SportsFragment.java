package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
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
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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
import lab.wesmartclothing.wefit.flyso.entity.SportsListBean;
import lab.wesmartclothing.wefit.flyso.entity.WeightInfoItem;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.utils.ChartManager;
import lab.wesmartclothing.wefit.flyso.view.SportsMarkerView;
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

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
@EFragment()
public class SportsFragment extends BaseAcFragment {


    public static SportsFragment getInstance() {
        return new SportsFragment_();
    }

    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    TextView tv_weight_date;
    @ViewById
    TextView tv_connectDevice;
    @ViewById
    TextView tv_connectTip;
    @ViewById
    SmoothRefreshLayout mRefresh;
    @ViewById
    LineChart mLineChart;
    @ViewById
    LinearLayout dialog_not_connect;


    @Click
    void tv_details() {
        if (bean == null) return;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Key.BUNDLE_SPORTS_INFO, bean);
        RxActivityUtils.skipActivity(mActivity, SportsDetailsActivity_.class, bundle);
    }

    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
            initDeviceConnectTip(0);
        } else if (state == BluetoothAdapter.STATE_ON) {
            initDeviceConnectTip(3);
        }
    }

    //监听瘦身衣连接情况
    @Receiver(actions = Key.ACTION_CLOTHING_CONNECT)
    void clothingConnectStatus(@Receiver.Extra(Key.EXTRA_CLOTHING_CONNECT) boolean state) {
        if (state) {
            tv_connectDevice.setText(R.string.connected);
        } else {
            tv_connectDevice.setText(R.string.disConnected);
            initDeviceConnectTip(3);
        }
    }

    //心率
    @Receiver(actions = Key.ACTION_HEART_RATE_CHANGED)
    void myHeartRate(@Receiver.Extra(Key.EXTRA_HEART_RATE_CHANGED) byte[] heartRate) {
        initDeviceConnectTip(2);
    }

    //运动停止
    @Receiver(actions = Key.ACTION_CLOTHING_STOP)
    void sportsStop() {
        initDeviceConnectTip(3);
    }

    private BaseQuickAdapter adapter;
    private List<WeightInfoItem> weightLists = new ArrayList<>();
    private List<SportsListBean.ListBean> sportsBeans = new ArrayList<>();
    private SportsListBean.ListBean bean;
    private boolean refreshOnce = false;//解决多次刷新的问题
    private boolean notMore = false;//解决多次刷新的问题
    private int pageNum = 1;//页码
    ChartManager chartManager; //chart管理类
    Highlight mHighlight; //记录当前高亮的位置


    @Override
    public void onStart() {
        super.onStart();
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
            initDeviceConnectTip(1);
        } else {
            tv_connectDevice.setText(BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected);
            getSportsData();
        }
    }

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sports, null);
        return rootView;
    }

    private void initBle() {
        if (!BleTools.getBleManager().isBlueEnable()) {
            RxLogUtils.v("蓝牙未开启:");
            initDeviceConnectTip(0);
            return;
        }
    }

    @AfterViews
    public void initView() {
        initRecycler();
        initWeightInfo();
        initChart();
        initHRefresh();
        initRxBus();
        initBle();
    }


    private void initRxBus() {
        //解除绑定，展示栏数据恢复初始状态
        Disposable device = RxBus.getInstance().register(Device.class, new Consumer<Device>() {
            @Override
            public void accept(Device device) throws Exception {
                if (BleKey.TYPE_CLOTHING.equals(device.getDeviceNo())) {
//                    mPrefs.clothing().put("");
                    SPUtils.put(SPKey.SP_clothingMAC, "");
                    initDeviceConnectTip(1);
                    BleTools.getInstance().disConnect();
                    initWeightInfo();
                }
            }
        });
        RxBus.getInstance().addSubscription(this, device);
    }


    @Override
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new BaseQuickAdapter<WeightInfoItem, BaseViewHolder>(R.layout.item_sports_body_info) {
            @Override
            protected void convert(BaseViewHolder helper, WeightInfoItem item) {
                helper.setText(R.id.tv_left, item.getData_left());
                helper.setText(R.id.tv_right, item.getData_right());
                helper.setText(R.id.title_left, item.getTitle_left())
                        .setTextColor(R.id.title_left, getResources().getColor(R.color.textColor));
                helper.setText(R.id.title_right, item.getTitle_right())
                        .setTextColor(R.id.title_right, getResources().getColor(R.color.textColor));
                helper.setImageResource(R.id.img_left, item.getImg_left());
                helper.setImageResource(R.id.img_right, item.getImg_right());
            }
        };
        adapter.bindToRecyclerView(mRecyclerView);
    }

    private void initWeightInfo() {
        String[] sports_title = getResources().getStringArray(R.array.sports_title);
        int[] img_icons = {R.mipmap.time_icon, R.mipmap.energy_iocn, R.mipmap.average_icon, R.mipmap.max_icon};

        String[] data = {"--h--min", "--" + getString(R.string.unit_k), "--bpm", "--bpm"};

        for (int i = 0; i < sports_title.length; i = i + 2) {
            WeightInfoItem item = new WeightInfoItem();
            item.setTitle_left(sports_title[i]);
            item.setTitle_right(sports_title[i + 1]);
            item.setImg_left(img_icons[i]);
            item.setImg_right(img_icons[i + 1]);
            item.setData_left(data[i]);
            item.setData_right(data[i + 1]);
            weightLists.add(item);
        }
        adapter.setNewData(weightLists);
        tv_weight_date.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date));
    }

    private void getSportsData() {
        if (pageNum == 1) sportsBeans.clear();
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getAthleticsList(pageNum, 20))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (mActivity != null) {
                            mRefresh.refreshComplete();
                        }
                    }
                })
                .compose(MyAPP.getRxCache().<String>transformObservable("getAthleticsList", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取运动数据：" + s);
                        SportsListBean bean = new Gson().fromJson(s, SportsListBean.class);
                        List<SportsListBean.ListBean> list = bean.getList();

                        if (list.size() <= 0) {
                            if (!bean.isHasNextPage()) {
                                notMore = true;
                                mRefresh.setEnableAutoRefresh(false);
                                mRefresh.setDisableRefresh(true);
                            }

                            return;
                        }
                        sportsBeans.addAll(0, list);
                        syncChart(sportsBeans);
                        if (sportsBeans.size() > 0)
                            synWeightData(sportsBeans.get(sportsBeans.size() - 1));
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    private void syncChart(final List<SportsListBean.ListBean> list) {
        Calendar calendar = Calendar.getInstance();
        ArrayList<Entry> heats = new ArrayList<>();
        ArrayList<Entry> sportsTimes = new ArrayList<>();

        final List<String> days = new ArrayList<>();
        List<String> days_d = new ArrayList<>();//真实数据
        List<String> days_f = new ArrayList<>();//头
        List<String> days_l = new ArrayList<>();//尾


        if (list.size() > 0)
            calendar.setTimeInMillis(list.get(0).getAthlDate());


        //左边添加3条数据
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        days_l.add(RxFormat.setFormatDate(calendar, "MM/dd"));
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        days_l.add(RxFormat.setFormatDate(calendar, "MM/dd"));
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        days_l.add(RxFormat.setFormatDate(calendar, "MM/dd"));

        //添加真实数据
        for (int i = 0; i < list.size(); i++) {
            heats.add(new Entry(i, (float) list.get(i).getCalorie()));
            sportsTimes.add(new Entry(i, (float) list.get(i).getDuration()));
            days_d.add(RxFormat.setFormatDate(list.get(i).getAthlDate(), "MM/dd"));
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

        chartManager.setData(heats, sportsTimes, days);


        try {
            mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    mHighlight = h;
                    synWeightData(list.get((int) (e.getX()) % list.size()));
                }

                @Override
                public void onNothingSelected() {
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

    private void synWeightData(SportsListBean.ListBean bean) {

        if (bean == null) return;
        this.bean = bean;
        double min = bean.getDuration() / 60f;
        weightLists.get(0).setData_left(min < 60 ? RxFormatValue.fromatUp(min, 0) + "min" : (int) min / 60 + "h" + (int) min % 60 + "min");
        weightLists.get(0).setData_right(bean.getCalorie() + getString(R.string.unit_k));
        weightLists.get(1).setData_left(bean.getAvgHeart() + "bpm");
        weightLists.get(1).setData_right(bean.getMaxHeart() + "bpm");

        adapter.setNewData(weightLists);

        tv_weight_date.setText(RxFormat.setFormatDate(bean.getAthlDate(), RxFormat.Date));
    }

    //初始化图表
    private void initChart() {
        chartManager = new ChartManager(mActivity, mLineChart);
        SportsMarkerView markerView = new SportsMarkerView(mActivity, R.layout.layout_sports_marker);
        chartManager.addMarker(markerView);
    }

    //初始化横向刷新
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
                pageNum++;
                getSportsData();
            }
        });
    }


    private void initDeviceConnectTip(int QN_bleState) {
        tv_connectTip.setVisibility(View.VISIBLE);
//        Drawable drawable = getResources().getDrawable(QN_bleState == 1 ? R.mipmap.unbound_icon : R.mipmap.connect_icon);
//        //一定要加这行！！！！！！！！！！！
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        tv_connectDevice.setCompoundDrawables(drawable, null, null, null);
        switch (QN_bleState) {
            case 0://打开蓝牙
                if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
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
                        bundle.putString(Key.BUNDLE_BIND_TYPE, BleKey.TYPE_CLOTHING);
                        RxActivityUtils.skipActivity(mActivity, AddDeviceActivity_.class, bundle);
                    }
                });
                break;
            case 2://进入运动
                SpannableStringBuilder stringBuilder = RxTextUtils.getBuilder(getString(R.string.sportsToSee))
                        .setForegroundColor(getResources().getColor(R.color.colorTheme))
                        .setLength(9);
                tv_connectTip.setText(stringBuilder);
                tv_connectTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_connectTip.setVisibility(View.GONE);
                        RxActivityUtils.skipActivity(mActivity, SportsDetailsActivity_.class);
                    }
                });
                break;
            case 3://不提示，tip消失
                if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
                    initDeviceConnectTip(1);
                } else
                    tv_connectTip.setVisibility(View.GONE);
                break;
        }
    }
}
