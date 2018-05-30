package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.smartclothing.blelibrary.BleTools;
import com.tbruyelle.rxpermissions2.Permission;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
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
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseFragment;
import lab.wesmartclothing.wefit.flyso.entity.SportsListBean;
import lab.wesmartclothing.wefit.flyso.entity.WeightInfoItem;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.login.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.utils.ChartManager;
import lab.wesmartclothing.wefit.flyso.view.SportsMarkerView;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import me.dkzwm.widget.srl.RefreshingListenerAdapter;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.config.Constants;
import me.dkzwm.widget.srl.extra.header.MaterialHeader;
import me.dkzwm.widget.srl.utils.PixelUtl;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_sports)
public class SportsFragment extends BaseFragment {


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

    @Pref
    Prefs_ mPrefs;

    @Click
    void tv_details() {
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
            tv_connectTip.setVisibility(View.GONE);
            initBle();
        }
    }

    private BaseQuickAdapter adapter;
    private List<WeightInfoItem> weightLists = new ArrayList<>();
    private List<SportsListBean.ListBean> sportsBeans = new ArrayList<>();
    private SportsListBean.ListBean bean;
    private boolean refreshOnce = false;//解决多次刷新的问题
    private boolean notMore = false;//解决多次刷新的问题
    private int pageNum = 1;//页码

    @Override
    public void initData() {
//        setData(null, null, null);
        getSportsData();
        initBle();
        if (!mPrefs.clothingBind().get()) {
            initDeviceConnectTip(1);
        }
    }

    private void initBle() {
        if (!BleTools.getBleManager().isBlueEnable()) {
            RxLogUtils.v("蓝牙未开启:");
            initDeviceConnectTip(0);
            return;
        }

        checkLocation(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    BleTools.getInstance().configScan();
                    BleTools.getBleManager().scanAndConnect(new BleScanAndConnectCallback() {
                        @Override
                        public void onScanFinished(BleDevice scanResult) {

                        }

                        @Override
                        public void onStartConnect() {

                        }

                        @Override
                        public void onConnectFail(BleDevice bleDevice, BleException exception) {

                        }

                        @Override
                        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                            tv_connectDevice.setText(R.string.connected);
                        }

                        @Override
                        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                            tv_connectDevice.setText(R.string.disConnected);
                        }

                        @Override
                        public void onScanStarted(boolean success) {

                        }

                        @Override
                        public void onScanning(BleDevice bleDevice) {

                        }
                    });
                }
            }
        });
    }

    @AfterViews
    public void initView() {
        initRecycler();
        initWeightInfo();
        initChart();
        initHRefresh();
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

        String[] data = {"--h--min", "--" + getString(R.string.unit_kcal), "--bpm", "--bpm"};

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
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getAthleticsList(pageNum, 20))
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
                        RxLogUtils.d("获取运动数据：" + s);
                        SportsListBean bean = new Gson().fromJson(s, SportsListBean.class);
                        List<SportsListBean.ListBean> list = bean.getList();

                        if (list.size() > 0) {
                            sportsBeans.addAll(0, list);
                            syncChart(sportsBeans);
                        }
                        if (mRefresh.isRefreshing()) {
                            mRefresh.refreshComplete();
                        }
                        if (!bean.isHasNextPage()) {
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

    private void syncChart(final List<SportsListBean.ListBean> list) {
        List<String> days = new ArrayList<>();
        ArrayList<Entry> heats = new ArrayList<>();
        ArrayList<Entry> sportsTimes = new ArrayList<>();

        synWeightData(list.get(list.size() - 1));


        for (int i = 0; i < list.size(); i++) {
            heats.add(new Entry(i, (float) list.get(i).getCalorie()));
            sportsTimes.add(new Entry(i, (float) list.get(i).getDuration()));
            days.add(RxFormat.setFormatDate(list.get(i).getAthlDate(), "MM/dd"));
        }

        chartManager.setData(heats, sportsTimes, days);

        mLineChart.moveViewToX(heats.size() - (pageNum - 1) * 20 - 4);
        mLineChart.invalidate();

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

    private void synWeightData(SportsListBean.ListBean bean) {

        if (bean == null) return;
        this.bean = bean;

        int min = bean.getDuration() / 60;

        weightLists.get(0).setData_left(min / 60 + "h" + min % 60 + "min");
        weightLists.get(0).setData_right(bean.getCalorie() + getString(R.string.unit_kcal));
        weightLists.get(1).setData_left(bean.getAvgHeart() + "bpm");
        weightLists.get(1).setData_right(bean.getMaxHeart() + "bpm");

        adapter.setNewData(weightLists);

        tv_weight_date.setText(RxFormat.setFormatDate(bean.getAthlDate(), RxFormat.Date));
    }


    ChartManager chartManager;

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
            case 3:
                tv_connectTip.setVisibility(View.GONE);
                dialog_not_connect.setVisibility(View.VISIBLE);
                break;
        }
    }
}
