package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
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
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.chart.line.LineBean;
import com.vondear.rxtools.view.chart.line.SuitLines;
import com.vondear.rxtools.view.chart.line.Unit;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.BleTools;
import lab.wesmartclothing.wefit.flyso.entity.AthleticsInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.HeartRateChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

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
    @BindView(R.id.layout_lenged)
    LinearLayout mLayoutLenged;
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


    private Button btn_Connect;
    private List<AthleticsInfo.ListBean> list;
    private BaseQuickAdapter adapter;
    private int pageNum = 1;


    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //监听瘦身衣连接情况
            if (Key.ACTION_CLOTHING_CONNECT.equals(intent.getAction())) {
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_CLOTHING_CONNECT, false);
                if (state) {
                    btn_Connect.setText(R.string.connected);
                } else {
                    btn_Connect.setText(R.string.disConnected);
                    mLayoutStrongTip.setVisibility(View.GONE);
                }
            }
            //瘦身衣运动结束
            else if (Key.ACTION_CLOTHING_STOP.equals(intent.getAction())) {
                mLayoutStrongTip.setVisibility(View.GONE);
                //监听系统蓝牙
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_smart_clothing);
        unbinder = ButterKnife.bind(this);

        StatusBarUtils.from(mActivity)
                .setStatusBarColor(getResources().getColor(R.color.red))
                .setLightStatusBar(true)
                .process();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Key.ACTION_CLOTHING_CONNECT);
        filter.addAction(Key.ACTION_CLOTHING_STOP);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(registerReceiver, filter);

        initView();
    }


    private void initView() {
        initMRxBus();
        initTopBar();
        Typeface typeface = MyAPP.typeface;
        mTvHeatKcal.setTypeface(typeface);
        mTvSportsTime.setTypeface(typeface);
        mTvSportDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_CH));
        checkStatus();
        initData();
        initSportingList();
    }

    private void initSportingList() {
        mRecyclerSporting.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerSporting.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter = new BaseQuickAdapter<AthleticsInfo.ListBean.AthlListBean, BaseViewHolder>(R.layout.item_sporting_list) {
            @Override
            protected void convert(BaseViewHolder helper, AthleticsInfo.ListBean.AthlListBean item) {
                String timeSection = RxFormat.setFormatDate(item.getStartTime(), RxFormat.Date_Time) + "-"
                        + RxFormat.setFormatDate(item.getEndTime(), RxFormat.Date_Time);

                SpannableStringBuilder timeBuilder = RxTextUtils.getBuilder("运动时间段\n")
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                        .append(timeSection)
                        .create();

                SpannableStringBuilder kcalBuilder = RxTextUtils.getBuilder("消耗热量\n")
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                        .append(item.getCalorie() + "")
                        .append("\tkcal")
                        .create();

                //getPlanFlag():0是自由运动，1是课程运动
                helper.setText(R.id.tv_sportingType, item.getPlanFlag() != 1 ? "自由运动" : "课程运动")
                        .setText(R.id.tv_sportingTime, timeBuilder)
                        .setTypeface(MyAPP.typeface, R.id.tv_sportingTime, R.id.tv_sportingKcal)
                        .setText(R.id.tv_sportingKcal, kcalBuilder);
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            AthleticsInfo.ListBean.AthlListBean item = (AthleticsInfo.ListBean.AthlListBean) adapter.getItem(position % adapter.getData().size());
            if (item == null) return;
            bundle.putString(Key.BUNDLE_DATA_GID, item.getGid());
            bundle.putBoolean(Key.BUNDLE_SPORTING_PLAN, item.getPlanFlag() == 1);
            bundle.putBoolean(Key.BUNDLE_GO_BCAK, true);
            RxActivityUtils.skipActivity(mContext, SportsDetailsFragment.class, bundle);

        });
        mRecyclerSporting.setAdapter(adapter);
    }

    private void initMRxBus() {
        //后台上传心率数据成功，刷新界面
        RxBus.getInstance().register2(RefreshSlimming.class)
                .compose(RxComposeUtils.<RefreshSlimming>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<RefreshSlimming>() {
                    @Override
                    protected void _onNext(RefreshSlimming hearRateUpload) {
                        pageNum = 1;
                        initData();
                    }
                });

        RxBus.getInstance().register2(HeartRateChangeBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<HeartRateChangeBus>() {
                    @Override
                    protected void _onNext(HeartRateChangeBus sportsDataTab) {
                        if (mLayoutStrongTip != null && mLayoutStrongTip.getVisibility() == View.GONE) {
                            mLayoutStrongTip.setVisibility(View.VISIBLE);
                            String checkSporting = getString(R.string.checkSporting);
                            SpannableStringBuilder builder = RxTextUtils.getBuilder(checkSporting)
                                    .setForegroundColor(getResources().getColor(R.color.red))
                                    .setLength(9, checkSporting.length());
                            mBtnStrongTip.setText(builder);
                            mBtnStrongTip.setOnClickListener(v -> {
                                mLayoutStrongTip.setVisibility(View.GONE);
                                RxActivityUtils.skipActivity(mActivity, SportingActivity.class);
                            });
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(registerReceiver);
        super.onDestroy();
    }

    private void initData() {
        btn_Connect.setText(getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)) ?
                R.string.unBind : BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected));
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().fetchAthleticsListDetail(pageNum, 10))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchAthleticsListDetail" + pageNum, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        AthleticsInfo bean = JSON.parseObject(s, AthleticsInfo.class);
                        updateUI(bean);

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void updateUI(AthleticsInfo bean) {
        if (pageNum == 1) {
            list = bean.getList();
            initLineChart(list);
            pageNum++;
        } else {
            if (RxDataUtils.isEmpty(bean.getList())) return;
            Collections.reverse(bean.getList());//
            list.addAll(0, bean.getList());

            List<Unit> lines_Heat = new ArrayList<>();
            List<Unit> lines_Time = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                AthleticsInfo.ListBean.DayAthlBean athlBean = list.get(i).getDayAthl();
                Unit unit_heat = new Unit((float) athlBean.getCalorie(), RxFormat.setFormatDate(athlBean.getAthlDate(), "MM/dd"));
                Unit unit_time = new Unit(athlBean.getDuration() < 60 ? 1 : athlBean.getDuration() / 60, "");

                lines_Heat.add(unit_heat);
                lines_Time.add(unit_time);
            }
            mSuitlines.addDataChart(Arrays.asList(lines_Heat, lines_Time));
            pageNum++;
        }

    }


    private void checkStatus() {
        if (!BleTools.getBleManager().isBlueEnable()) {
            mLayoutStrongTip.setVisibility(View.VISIBLE);
            String tipOpenBlueTooth = getString(R.string.tipOpenBlueTooth);
            SpannableStringBuilder builder = RxTextUtils.getBuilder(tipOpenBlueTooth)
                    .setForegroundColor(getResources().getColor(R.color.red))
                    .setLength(12, tipOpenBlueTooth.length());
            mBtnStrongTip.setText(builder);
            mBtnStrongTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BleTools.getBleManager().enableBluetooth();
                }
            });
        }
    }

    private void initLineChart(final List<AthleticsInfo.ListBean> list) {
        if (list == null) return;
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Time = new ArrayList<>();
        Collections.reverse(list);//
        for (int i = 0; i < list.size(); i++) {
            AthleticsInfo.ListBean.DayAthlBean bean = list.get(i).getDayAthl();
            Unit unit_heat = new Unit((float) bean.getCalorie(), RxFormat.setFormatDate(bean.getAthlDate(), "MM/dd"));
            Unit unit_time = new Unit(bean.getDuration() < 60 ? 1 : bean.getDuration() / 60, "");

            lines_Heat.add(unit_heat);
            lines_Time.add(unit_time);
        }

        LineBean heatLine = new LineBean();
        heatLine.setUnits(lines_Heat);
        heatLine.setShowPoint(true);
        heatLine.setLineWidth(RxUtils.dp2px(2));
        heatLine.setColor(Color.parseColor("#F2A49C"));

        LineBean timeLine = new LineBean();
        timeLine.setShowPoint(true);
        timeLine.setUnits(lines_Time);
        timeLine.setLineWidth(RxUtils.dp2px(2));
        timeLine.setColor(Color.parseColor("#F2A49C"));
        timeLine.setDashed(true);

        mSuitlines.setSpaceMin(RxUtils.dp2px(4));
        new SuitLines.LineBuilder()
                .add(heatLine)
                .add(timeLine)
                .build(mSuitlines);

        mSuitlines.setLineChartSelectItemListener(new SuitLines.LineChartSelectItemListener() {
            @Override
            public void selectItem(int valueX) {
                AthleticsInfo.ListBean.DayAthlBean bean = list.get(valueX).getDayAthl();
                mTvSportDate.setText(RxFormat.setFormatDate(bean.getAthlDate(), RxFormat.Date_CH));
                mTvHeatKcal.setText(RxFormatValue.fromat4S5R(bean.getCalorie(), 1));
                mTvSportsTime.setText(RxFormatValue.fromatUp(bean.getDuration() < 60 ? 1 : bean.getDuration() / 60, 0));
            }
        });

        mSuitlines.setLineChartStopItemListener(new SuitLines.LineChartStopItemListener() {
            @Override
            public void stopItem(int valueX) {
                RxLogUtils.e("滑动停止：" + valueX);
                adapter.setNewData(list.get(valueX).getAthlList());
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
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("运动记录");
        btn_Connect = mQMUIAppBarLayout.addRightTextButton(
                getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)) ? R.string.unBind : BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected), R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
    }


}
