package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.vondear.rxtools.aboutByte.ByteUtil;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AboutUpdateDialog;

/**
 * Created by jk on 2018/8/11.
 */
public class AboutFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_clothingVersion)
    TextView mTvClothingVersion;
    @BindView(R.id.btn_update)
    QMUIRoundButton mBtnUpdate;
    @BindView(R.id.tv_appVersion)
    TextView mTvAppVersion;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    Unbinder unbinder;
    @BindView(R.id.btn_reUpdate)
    QMUIRoundButton mBtnReUpdate;
    @BindView(R.id.layout_updateFail)
    LinearLayout mLayoutUpdateFail;


    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Key.ACTION_CLOTHING_CONNECT.equals(intent.getAction())) {
                //监听瘦身衣连接情况
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_CLOTHING_CONNECT);
                if (state) {
                    readBLEVersion();
                }
            }
        }
    };

    private String updateURL = "";
    private AboutUpdateDialog dialog;
    private String currentVersion = "";
    private String newVersion = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(registerReceiver);
        super.onDestroy();
    }

    private void initView() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Key.ACTION_CLOTHING_CONNECT);
        registerReceiver(registerReceiver, filter);

        readBLEVersion();
        initTopBar();
        RxTextUtils.getBuilder("香港智享瘦国际集团 ")
                .append("服务条款和隐私条款")
                .setForegroundColor(getResources().getColor(R.color.red))
                .setUnderline()
                .into(mTvTip);
        mTvAppVersion.setText("软件版本号 v" + RxDeviceUtils.getAppVersionName());
        mTvClothingVersion.setText("固件版本号 v--");
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("关于我们");
    }


    private void readBLEVersion() {
        BleAPI.readDeviceInfo(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                RxLogUtils.d("读设备信息" + HexUtil.encodeHexStr(data));
                //021309 010203000400050607090a0b0c10111213

                JsonObject object = new JsonObject();
                object.addProperty("category", data[3]);//设备类型
                object.addProperty("modelNo", data[4]);//待定
                object.addProperty("manufacture", ByteUtil.bytesToIntD2(new byte[]{data[5], data[6]}));
                object.addProperty("hwVersion", ByteUtil.bytesToIntD2(new byte[]{data[7], data[8]}));

                currentVersion = data[9] + "." + data[10] + "." + data[11];
                object.addProperty("firmwareVersion", currentVersion);//当前固件版本
                mTvClothingVersion.setText("固件版本号 v" + currentVersion);
                checkFirmwareVersion(object);
            }
        });
    }

    private void checkFirmwareVersion(final JsonObject object) {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .getUpgradeInfo(NetManager.fetchRequest(object.toString())))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取固件版本：" + s);
                        FirmwareVersionUpdate firmwareVersionUpdate = JSON.parseObject(s, FirmwareVersionUpdate.class);
                        if (firmwareVersionUpdate.isHasNewVersion()) {
                            RxLogUtils.d("有最新的版本");
                            updateURL = firmwareVersionUpdate.getFileUrl();
                            newVersion = firmwareVersionUpdate.getFirmwareVersion();

                            checkState(true);
                        } else {
                            RxToast.normal("当前固件版本 v" + currentVersion + " 已经是最新版本，");
                            checkState(false);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                        checkState(false);
                    }

                });
    }

    private void checkState(boolean isComplete) {
        if (isComplete) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_recommend);
            //一定要加这行！！！！！！！！！！！
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvClothingVersion.setCompoundDrawables(null, null, drawable, null);
            QMUIRoundButtonDrawable buttonDrawable = (QMUIRoundButtonDrawable) mBtnUpdate.getBackground();
            buttonDrawable.setBgData(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            buttonDrawable.setStroke(1, getResources().getColor(R.color.red));
            mBtnUpdate.setEnabled(true);
        } else {
            mTvClothingVersion.setCompoundDrawables(null, null, null, null);
            QMUIRoundButtonDrawable buttonDrawable = (QMUIRoundButtonDrawable) mBtnUpdate.getBackground();
            buttonDrawable.setBgData(ColorStateList.valueOf(getResources().getColor(R.color.BrightGray)));
            buttonDrawable.setStroke(1, getResources().getColor(R.color.BrightGray));
            mBtnUpdate.setEnabled(false);
        }
    }


    @OnClick({R.id.btn_update, R.id.tv_tip, R.id.btn_reUpdate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                dialog = new AboutUpdateDialog(mContext, updateURL, false);
                dialog.setBLEUpdateListener(new AboutUpdateDialog.BLEUpdateListener() {
                    @Override
                    public void success() {
                        readBLEVersion();
                    }

                    @Override
                    public void fail() {
                        mLayoutUpdateFail.setVisibility(View.VISIBLE);
                    }
                });
                //set进度值
                dialog.show();
                break;
            case R.id.tv_tip:
                //服务协议
                WebTitleActivity.startWebActivity(mActivity, getString(R.string.ServiceAgreement), ServiceAPI.Term_Service);
                break;
            case R.id.btn_reUpdate:
                mLayoutUpdateFail.setVisibility(View.GONE);
                break;
        }
    }
}
