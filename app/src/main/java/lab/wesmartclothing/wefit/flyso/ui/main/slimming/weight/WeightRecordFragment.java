package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.yolanda.health.qnblesdk.listen.QNDataListener;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.WeightDataBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity_;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

/**
 * Created by jk on 2018/7/26.
 */
@EFragment
public class WeightRecordFragment extends BaseAcFragment {

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
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.layout_sportTip)
    RelativeLayout mLayoutSportTip;
    Unbinder unbinder;
    @BindView(R.id.tv_settingTarget)
    TextView mTvSettingTarget;
    @BindView(R.id.tv_target)
    TextView tvTarget;
    @BindView(R.id.tv_startWeight)
    TextView tvStartWeight;
    @BindView(R.id.tv_endWeight)
    TextView tvEndWeight;
    @BindView(R.id.pro_limit)
    RxRoundProgressBar proLimit;
    @BindView(R.id.layout_Tips)
    LinearLayout mLayoutTips;
    @BindView(R.id.layout_weight)
    LinearLayout mLayoutWeight;

    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
            checkStatus();
        } else if (state == BluetoothAdapter.STATE_ON) {
            mLayoutStrongTip.setVisibility(View.GONE);
//            //测试使用
//            startFragment(SportingFragment.getInstance());
        }
    }

    //体脂称连接状态
    @Receiver(actions = Key.ACTION_SCALE_CONNECT)
    void scaleIsConnect(@Receiver.Extra(Key.EXTRA_SCALE_CONNECT) boolean state) {
        if (btn_Connect != null)
            btn_Connect.setText(state ? R.string.connected : R.string.disConnected);
    }

    //蓝牙秤状态改变(开始测量)
    @Receiver(actions = Key.ACTION_STATE_START_MEASURE)
    void scaleStartMeasure() {
        Bundle bundle = new Bundle();
        bundle.putDouble(Key.BUNDLE_LAST_WEIGHT, lastWeight);
        QMUIFragment instance = WeightAddFragment.getInstance();
        instance.setArguments(bundle);
        startFragment(instance);
    }

    @Bean
    QNBleTools mQNBleTools;

    public static QMUIFragment getInstance() {
        return new WeightRecordFragment_();
    }

    private Button btn_Connect;
    private String currentGid;
    private boolean isSettingTargetWeight = false;
    //传递数据给我目标详情界面
    private double initialWeight = 0;
    private double stillNeed = 0;
    private double targetWeight = 0;
    private int hasDays = 0;
    private double lastWeight = 0;//最后一条体重数据

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_weight_record, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onStart() {
        initData();
        initBleCallBack();
        super.onStart();
    }

    @Override
    public void onStop() {
        MyAPP.QNapi.setDataListener(null);
        super.onStop();
    }


    private void initView() {
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvCurWeight.setTypeface(typeface);
        mTvBodyFat.setTypeface(typeface);
        mTvMuscle.setTypeface(typeface);
        mTvBmi.setTypeface(typeface);
        tvTarget.setTypeface(typeface);
        tvStartWeight.setTypeface(typeface);
        tvEndWeight.setTypeface(typeface);

        initTopBar();
        checkStatus();
    }


    private void initData() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.fetchWeightInfo(1, 20))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchWeightInfo", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("加载数据：" + s);
                        WeightDataBean bean = new Gson().fromJson(s, WeightDataBean.class);
                        notifyData(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void notifyData(WeightDataBean bean) {
        isSettingTargetWeight = bean.isTargetSet();
        initialWeight = bean.getInitialWeight();
        hasDays = bean.getHasDays();
        stillNeed = bean.getStillNeed();
        targetWeight = bean.getTargetWeight();
        lastWeight = bean.getWeight();
        RxLogUtils.d("是否有目标体重：" + bean.isTargetSet());
        //是否录入体重
        mLayoutTips.setVisibility(bean.isTargetSet() ? View.GONE : View.VISIBLE);
        mTvSettingTarget.setVisibility(bean.isTargetSet() ? View.GONE : View.VISIBLE);
        mLayoutWeight.setVisibility(bean.isTargetSet() ? View.VISIBLE : View.GONE);

        RxTextUtils.getBuilder("需减 ")
                .append((float) bean.getStillNeed() + "")
                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                .setProportion(1.4f)
                .append(" kg,剩余 ")
                .append(bean.getHasDays() + "")
                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                .setProportion(1.4f)
                .append(" 天").into(tvTarget);
        tvStartWeight.setText((float) bean.getInitialWeight() + "kg");
        tvEndWeight.setText((float) bean.getTargetWeight() + "kg");
        proLimit.setProgress((float) (bean.getComplete() * 100));

        Map<Float, String> map = new HashMap<>();
        map.put((float) bean.getNormWeight(), (float) bean.getNormWeight() + "kg");
        mSuitlines.setlimitLabels(map);

        if (bean.getWeightList() != null) {
            initLineChart(bean);
        }
    }


    private void initLineChart(final WeightDataBean bean) {
        final List<WeightDataBean.WeightListBean.ListBean> list = bean.getWeightList().getList();
        SuitLines.LineBuilder builder = new SuitLines.LineBuilder();
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Time = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            WeightDataBean.WeightListBean.ListBean itemBean = list.get(i);
            Unit unit_weight = new Unit((float) itemBean.getWeight(), RxFormat.setFormatDate(itemBean.getWeightDate(), "MM/dd"));
            Unit unit_bodyFat = new Unit((float) itemBean.getBodyFat() * 0.5f, "");
            unit_weight.setShowPoint(true);

            unit_bodyFat.setShowPoint(false);
            unit_bodyFat.setFill(true);
            lines_Heat.add(unit_weight);
            lines_Time.add(unit_bodyFat);
        }
        builder.add(lines_Heat, 0x7fffffff);
        builder.add(lines_Time, 0x7fffffff);

        mSuitlines.setSpaceMaxMin(0.3f, 0f);
        builder.build(mSuitlines, false);
        mSuitlines.setLineChartSelectItemListener(new SuitLines.LineChartSelectItemListener() {
            @Override
            public void selectItem(int valueX) {
                mTvCurWeight.setText((float) list.get(valueX).getWeight() + "");
                mTvBodyFat.setText((float) list.get(valueX).getBodyFat() + "");
                mTvMuscle.setText((float) list.get(valueX).getMuscle() + "");
                mTvBmi.setText((float) list.get(valueX).getBmi() + "");
                mTvSportDate.setText(RxFormat.setFormatDate(list.get(valueX).getWeightDate(), RxFormat.Date_CH));
                currentGid = list.get(valueX).getGid();
            }
        });
    }

    private void checkStatus() {
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
            final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getTvTitle().setVisibility(View.GONE);
            dialog.setContent("您还未绑定体脂称");
            dialog.getTvCancel().setBackgroundColor(getResources().getColor(R.color.green_61D97F));
            dialog.setCancel("去绑定").setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    RxActivityUtils.skipActivity(mActivity, AddDeviceActivity_.class);
                }
            })
                    .setSure("暂不绑定")
                    .setSureListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    }).show();
        }
        if (!BleTools.getBleManager().isBlueEnable()) {
            mLayoutStrongTip.setVisibility(View.VISIBLE);
            String tipOpenBlueTooth = getString(R.string.tipOpenBlueTooth);
            SpannableStringBuilder builder = RxTextUtils.getBuilder(tipOpenBlueTooth)
                    .setForegroundColor(getResources().getColor(R.color.green_61D97F))
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

    //体脂称提取数据回调
    private void initBleCallBack() {
        MyAPP.QNapi.setDataListener(new QNDataListener() {
            @Override
            public void onGetUnsteadyWeight(QNBleDevice qnBleDevice, double v) {
                RxLogUtils.d("体重秤实时重量：" + v);
            }

            @Override
            public void onGetScaleData(QNBleDevice qnBleDevice, final QNScaleData qnScaleData) {
                RxLogUtils.d("实时的稳定测量数据是否有效：");
            }

            @Override
            public void onGetStoredScale(QNBleDevice qnBleDevice, final List<QNScaleStoreData> list) {
                RxLogUtils.d("历史数据：" + list.size());
                if (list.size() > 0) {
                    mLayoutStrongTip.setVisibility(View.VISIBLE);
                    String checkSporting = getString(R.string.checkHistoryWeight);
                    SpannableStringBuilder builder = RxTextUtils.getBuilder(checkSporting)
                            .setForegroundColor(getResources().getColor(R.color.green_61D97F))
                            .setLength(9, checkSporting.length());
                    mBtnStrongTip.setText(builder);
                    mBtnStrongTip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String s = new Gson().toJson(list);
                            RxLogUtils.d("体重信息:" + s);
                            Bundle bundle = new Bundle();
                            bundle.putString(Key.BUNDLE_WEIGHT_HISTORY, s);
                            RxActivityUtils.skipActivity(mActivity, WeightDataActivity_.class, bundle);
                        }
                    });
                }
            }
        });
    }

    @OnClick({R.id.layout_sportTip, R.id.tv_settingTarget, R.id.layout_sports})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.layout_sports) {
            Bundle args = new Bundle();
            args.putString(Key.BUNDLE_WEIGHT_GID, currentGid);
            QMUIFragment fragment = BodyDataFragment.getInstance();
            fragment.setArguments(args);
            startFragment(fragment);

        } else {
            if (initialWeight == 0) {
                RxToast.normal("请上称录入初始体重后再设置目标");
                return;
            }
            if (isSettingTargetWeight) {
                //传递目标体重信息
                Bundle args = new Bundle();
                args.putInt(Key.BUNDLE_HAS_DAYS, hasDays);
                args.putDouble(Key.BUNDLE_INITIAL_WEIGHT, initialWeight);
                args.putDouble(Key.BUNDLE_STILL_NEED, stillNeed);
                args.putDouble(Key.BUNDLE_TARGET_WEIGHT, targetWeight);
                QMUIFragment fragment = TargetDetailsFragment.getInstance();
                fragment.setArguments(args);
                startFragment(fragment);
            } else {
                //传递初始体重信息
                Bundle args = new Bundle();
                args.putDouble(Key.BUNDLE_INITIAL_WEIGHT, lastWeight);
                QMUIFragment fragment = SettingTargetFragment.getInstance();
                fragment.setArguments(args);
                startFragment(fragment);
            }
        }
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("体重记录");
        btn_Connect = mQMUIAppBarLayout.addRightTextButton(getString(mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected), R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
    }

}
