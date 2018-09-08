package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
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
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.WeightDataBean;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleHistoryData;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

/**
 * Created by jk on 2018/7/26.
 */
@EActivity
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
    @BindView(R.id.tv_details)
    TextView mTvDetails;

    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
            checkStatus();
        } else if (state == BluetoothAdapter.STATE_ON) {
            mLayoutStrongTip.setVisibility(View.GONE);
        }
    }

    //体脂称连接状态
    @Receiver(actions = Key.ACTION_SCALE_CONNECT)
    void scaleIsConnect(@Receiver.Extra(Key.EXTRA_SCALE_CONNECT) boolean state) {
        if (BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC)))
            if (btn_Connect != null)
                btn_Connect.setText(state ? R.string.connected : R.string.disConnected);
    }

    @Bean
    QNBleTools mQNBleTools;


    private Button btn_Connect;
    private String currentGid;
    private boolean isSettingTargetWeight = false;
    //传递数据给我目标详情界面
    private double stillNeed = 0;
    private int hasDays = 0;
    private double lastWeight = 0;//最后一条体重数据
    private List<WeightDataBean.WeightListBean.ListBean> list;
    private Bundle bundle = new Bundle();
    private boolean isVisite = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_weight_record);
        unbinder = ButterKnife.bind(this);
        StatusBarUtils.from(this)
                .setStatusBarColor(getResources().getColor(R.color.green_61D97F))
                .setLightStatusBar(true)
                .process();
        initView();
    }


    @Override
    public void onStart() {
        isVisite = true;
        initData();
        super.onStart();
    }

    @Override
    public void onStop() {
        isVisite = false;
        super.onStop();
    }


    private void initView() {
        Typeface typeface = MyAPP.typeface;
        mTvCurWeight.setTypeface(typeface);
        mTvBodyFat.setTypeface(typeface);
        mTvMuscle.setTypeface(typeface);
        mTvBmi.setTypeface(typeface);
        tvTarget.setTypeface(typeface);
        tvStartWeight.setTypeface(typeface);
        tvEndWeight.setTypeface(typeface);

        initTopBar();
        checkStatus();
        initRxBus();
        mTvSportDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_CH));
    }

    private void initRxBus() {

        //体重历史数据
        RxBus.getInstance().register2(ScaleHistoryData.class)
                .compose(RxComposeUtils.<ScaleHistoryData>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<ScaleHistoryData>() {
                    @Override
                    protected void _onNext(ScaleHistoryData data) {
                        final List<QNScaleStoreData> mList = data.getList();
                        if (mList.size() > 0) {
                            mLayoutStrongTip.setVisibility(View.VISIBLE);
                            String checkSporting = getString(R.string.checkHistoryWeight);
                            SpannableStringBuilder builder = RxTextUtils.getBuilder(checkSporting)
                                    .setForegroundColor(getResources().getColor(R.color.green_61D97F))
                                    .setLength(9, checkSporting.length());
                            mBtnStrongTip.setText(builder);
                            mBtnStrongTip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mLayoutStrongTip.setVisibility(View.GONE);
                                    String s = MyAPP.getGson().toJson(mList);
                                    RxLogUtils.d("体重信息:" + s);
                                    bundle.putString(Key.BUNDLE_WEIGHT_HISTORY, s);
                                    RxActivityUtils.skipActivity(mActivity, WeightDataActivity_.class, bundle);
                                }
                            });
                        }
                    }
                });
    }


    private void initData() {
        btn_Connect.setText(getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC)) ? R.string.unBind :
                mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected));

        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.fetchWeightInfo(1, 100))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchWeightInfo", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("加载数据：" + s);
                        WeightDataBean bean = MyAPP.getGson().fromJson(s, WeightDataBean.class);
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
        hasDays = bean.getHasDays();
        stillNeed = bean.getStillNeed();
        lastWeight = bean.getWeight();

        SPUtils.put(SPKey.SP_realWeight, (float) lastWeight);
        RxLogUtils.d("是否有目标体重：" + bean.isTargetSet());
        //是否录入体重
        mLayoutTips.setVisibility(bean.isTargetSet() ? View.GONE : View.VISIBLE);
        mTvSettingTarget.setVisibility(bean.isTargetSet() ? View.GONE : View.VISIBLE);
        mLayoutWeight.setVisibility(bean.isTargetSet() ? View.VISIBLE : View.GONE);

        if (stillNeed <= 0) {
            mTvDetails.setText("您的目标已完成，请前往设置新目标");
            stillNeed = 0;
        } else if (hasDays <= 0) {
            mTvDetails.setText("坚持锻炼并设置新目标");
        } else if (stillNeed > 0 && hasDays > 0) {
            mTvDetails.setText("目标就在眼前，加油！");
        }

        RxTextUtils.getBuilder("需减 ")
                .append(RxFormatValue.fromat4S5R(stillNeed, 1) + "")
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
        list = bean.getWeightList().getList();
        SuitLines.LineBuilder builder = new SuitLines.LineBuilder();
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Time = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            WeightDataBean.WeightListBean.ListBean itemBean = list.get(i);
            Unit unit_weight = new Unit((float) itemBean.getWeight(), RxFormat.setFormatDate(itemBean.getWeightDate(), "MM/dd"));
            Unit unit_bodyFat = new Unit((float) itemBean.getBodyFat(), RxFormat.setFormatDate(itemBean.getWeightDate(), "MM/dd"));
            unit_weight.setShowPoint(true);

            unit_bodyFat.setShowPoint(list.size() == 1);
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
                mTvMuscle.setText((float) (list.get(valueX).getSinew() / list.get(valueX).getWeight() * 100) + "");
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


    @OnClick({R.id.layout_sportTip, R.id.tv_settingTarget, R.id.layout_sports})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.layout_sports) {
            if (list == null || list.size() == 0) return;
            bundle.putString(Key.BUNDLE_WEIGHT_GID, currentGid);
            RxActivityUtils.skipActivity(mContext, BodyDataFragment.class, bundle);
        } else {
            //上一次体重为0则表示用户没有上称
            if (lastWeight == 0) {
                RxToast.normal("您还未录入初始体重\n请上称！！", 3000);
                return;
            }
            if (isSettingTargetWeight) {
                //跳转初始体重详情
                RxActivityUtils.skipActivity(mContext, TargetDetailsFragment.class, bundle);
            } else {
                //传递初始体重信息
                RxActivityUtils.skipActivity(mContext, SettingTargetFragment.class, bundle);
            }
        }
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("体重记录");
        btn_Connect = mQMUIAppBarLayout.addRightTextButton(
                getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC)) ? R.string.unBind :
                        mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected), R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
    }

}
