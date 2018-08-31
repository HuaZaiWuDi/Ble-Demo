package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AthleticsInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

/**
 * Created by jk on 2018/7/18.
 */
@EActivity
public class SmartClothingFragment extends BaseActivity {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.layout_StrongTip)
    RelativeLayout mLayoutStrongTip;
    @BindView(R.id.iv_sports)
    ImageView mIvSports;
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
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.line)
    View mLine;
    Unbinder unbinder;
    @BindView(R.id.btn_strongTip)
    QMUIRoundButton mBtnStrongTip;
    @BindView(R.id.suitlines)
    SuitLines mSuitlines;
    @BindView(R.id.layout_sportTip)
    RelativeLayout mLayoutSportTip;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.tv_sportDate)
    TextView mTvSportDate;


    //监听瘦身衣连接情况
    @Receiver(actions = Key.ACTION_CLOTHING_CONNECT)
    void clothingConnectStatus(@Receiver.Extra(Key.EXTRA_CLOTHING_CONNECT) boolean state) {
        if (BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)))
            if (state) {
                btn_Connect.setText(R.string.connected);
            } else {
                btn_Connect.setText(R.string.disConnected);
                mLayoutStrongTip.setVisibility(View.GONE);
            }
    }

    //心率
    @Receiver(actions = Key.ACTION_HEART_RATE_CHANGED)
    void myHeartRate(@Receiver.Extra(Key.EXTRA_HEART_RATE_CHANGED) byte[] heartRate) {
        if (mLayoutStrongTip != null && mLayoutStrongTip.getVisibility() == View.GONE) {
            mLayoutStrongTip.setVisibility(View.VISIBLE);
            String checkSporting = getString(R.string.checkSporting);
            SpannableStringBuilder builder = RxTextUtils.getBuilder(checkSporting)
                    .setForegroundColor(getResources().getColor(R.color.red))
                    .setLength(9, checkSporting.length());
            mBtnStrongTip.setText(builder);
            mBtnStrongTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLayoutStrongTip.setVisibility(View.GONE);
                    RxActivityUtils.skipActivity(mActivity, SportingFragment_.class);
                }
            });
        }
    }

    //监听系统蓝牙开启
    @Receiver(actions = Key.ACTION_CLOTHING_STOP)
    void CLOTHING_STOP() {
        mLayoutStrongTip.setVisibility(View.GONE);
    }

    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
            checkStatus();
        } else if (state == BluetoothAdapter.STATE_ON) {
            mLayoutStrongTip.setVisibility(View.GONE);
        }
    }


    private Button btn_Connect;
    private long currentDate = 0;


    private List<AthleticsInfo.PageInfoBean.ListBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_smart_clothing);

        StatusBarUtils.from(this)
                .setStatusBarColor(getResources().getColor(R.color.red))
                .setLightStatusBar(true)
                .process();
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        initTopBar();
        Typeface typeface = MyAPP.typeface;
        mTvHeatKcal.setTypeface(typeface);
        mTvSportsTime.setTypeface(typeface);
        mTvSportDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_CH));
        checkStatus();
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        btn_Connect.setText(getString(!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)) ?
                R.string.unBind : BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected));

        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getAthleticsInfo(1, 20))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("getAthleticsInfo", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("加载数据：" + s);
                        AthleticsInfo bean = MyAPP.getGson().fromJson(s, AthleticsInfo.class);

                        mLayoutSportTip.setVisibility(!bean.isTargetSet() ? View.GONE : View.VISIBLE);
                        //今日目标是否已经达成
                        mTvTip.setText(bean.getNeedAthl() <= 0 ? getString(R.string.completeDaytarget) : getString(R.string.gotoSporting, RxFormatValue.fromat4S5R(Math.abs(bean.getNeedAthl() / 1000f), 2)));
                        list = bean.getPageInfo().getList();
                        initLineChart(list);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


    private void checkStatus() {
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
            final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getTvTitle().setVisibility(View.GONE);
            dialog.setContent("您还未绑定燃脂衣");
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

    private void initLineChart(final List<AthleticsInfo.PageInfoBean.ListBean> list) {
        SuitLines.LineBuilder builder = new SuitLines.LineBuilder();
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Time = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AthleticsInfo.PageInfoBean.ListBean bean = list.get(i);
//            RxLogUtils.d("体重数据：getCalorie：" + bean.getCalorie() + "----getDuration：" + bean.getDuration());
            Unit unit_heat = new Unit(bean.getCalorie(), RxFormat.setFormatDate(bean.getAthlDate(), "MM/dd"));
            Unit unit_time = new Unit(bean.getDuration() < 60 ? 1 : bean.getDuration() / 60, "");
            unit_time.setLineStyle(SuitLines.DASHED);
            unit_time.setShowPoint(true);
            unit_heat.setShowPoint(true);

            lines_Heat.add(unit_heat);
            lines_Time.add(unit_time);
        }
        builder.add(lines_Heat, Color.parseColor("#F2A49C"));
        builder.add(lines_Time, Color.parseColor("#F2A49C"));

        mSuitlines.setSpaceMaxMin(0.2f, 0);

        builder.build(mSuitlines, false);
        mSuitlines.setLineChartSelectItemListener(new SuitLines.LineChartSelectItemListener() {
            @Override
            public void selectItem(int valueX) {
                AthleticsInfo.PageInfoBean.ListBean bean = list.get(valueX);
                mTvSportDate.setText(RxFormat.setFormatDate(bean.getAthlDate(), RxFormat.Date_CH));
                mTvHeatKcal.setText(RxFormatValue.fromat4S5R(bean.getCalorie(), 1));
                mTvSportsTime.setText(RxFormatValue.fromatUp(bean.getDuration() < 60 ? 1 : bean.getDuration() / 60, 0));
                currentDate = bean.getAthlDate();
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


    @OnClick({R.id.layout_sports})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_sports:
                if (list == null || list.size() == 0) return;
                Bundle args = new Bundle();
                args.putLong(Key.BUNDLE_SPORTING_DATE, currentDate);
                RxActivityUtils.skipActivity(mActivity, SportsDetailsFragment.class, args);
                break;
        }
    }

}
