package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.Manifest;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.entity.DeviceVersionBean;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
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
    @BindView(R.id.tv_appName)
    TextView mTvAppName;


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
        super.onDestroy();
    }

    private void initView() {
        initTopBar();
        RxTextUtils.getBuilder(getString(R.string.companyName))
                .append(getString(R.string.clause))
                .setForegroundColor(getResources().getColor(R.color.red))
                .setUnderline()
                .into(mTvTip);
        mTvAppVersion.setText(getString(R.string.softwareVersion, RxDeviceUtils.getAppVersionName()));
        mTvClothingVersion.setText(getString(R.string.firmwareVersion, "--"));
        mTvAppName.setText(getString(R.string.appName) + "APP");

        RxBus.getInstance().registerSticky(DeviceVersionBean.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(versionBean -> {
                    RxLogUtils.d("硬件版本号");
                    currentVersion = versionBean.getFirmwareVersion();
                    mTvClothingVersion.setText(getString(R.string.firmwareVersion, currentVersion));
                    checkFirmwareVersion(versionBean);
                });
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle(R.string.aboutUs);
    }


    private void checkFirmwareVersion(final DeviceVersionBean object) {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .getUpgradeInfo(NetManager.fetchRequest(new Gson().toJson(object))))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取固件版本：" + s);
                        FirmwareVersionUpdate firmwareVersionUpdate = JSON.parseObject(s, FirmwareVersionUpdate.class);
                        if (firmwareVersionUpdate.isHasNewVersion()) {
                            updateURL = firmwareVersionUpdate.getFileUrl();
                            newVersion = firmwareVersionUpdate.getFirmwareVersion();

                            RxTextUtils.getBuilder(getString(R.string.firmwareVersion, currentVersion))
                                    .append("-> v" + newVersion)
                                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.red))
                                    .into(mTvClothingVersion);

                            checkState(true);
                        } else {
                            RxToast.normal(getString(R.string.firmwareVersion, currentVersion) + getString(R.string.newest));
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
                //判断是否有权限
                new RxPermissions((FragmentActivity) mActivity)
                        .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .compose(RxComposeUtils.bindLife(lifecycleSubject))
                        .subscribe(new RxSubscriber<Permission>() {
                            @Override
                            protected void _onNext(Permission aBoolean) {
                                RxLogUtils.e("是否开启了权限：" + aBoolean);
                                if (aBoolean.granted) {
                                    dialog = new AboutUpdateDialog(mContext, updateURL, false);
                                    dialog.setBLEUpdateListener(new AboutUpdateDialog.BLEUpdateListener() {
                                        @Override
                                        public void success() {
                                        }

                                        @Override
                                        public void fail() {
                                            mLayoutUpdateFail.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    //set进度值
                                    dialog.show();
                                }
                            }
                        });
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
