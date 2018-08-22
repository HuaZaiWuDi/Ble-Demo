package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.vondear.rxtools.aboutByte.ByteUtil;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AboutUpdateDialog;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/8/11.
 */
public class AboutFragment extends BaseAcFragment {

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

    public static QMUIFragment getInstance() {
        return new AboutFragment();
    }

    private String updateURL = "";
    private AboutUpdateDialog dialog;
    private String currentVersion = "";
    private String newVersion = "";

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_about, null);
        unbinder = ButterKnife.bind(this, view);

        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        BleAPI.readDeviceInfo(null);
        super.onDestroyView();
    }

    private void initView() {
        readBLEVersion();
        initTopBar();
        RxTextUtils.getBuilder("智裳科技 ")
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
                popBackStack();
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
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getUpgradeInfo(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取固件版本：" + s);

                        FirmwareVersionUpdate firmwareVersionUpdate = MyAPP.getGson().fromJson(s, FirmwareVersionUpdate.class);
                        if (firmwareVersionUpdate.isHasNewVersion()) {
                            RxLogUtils.d("有最新的版本");
                            updateURL = firmwareVersionUpdate.getFileUrl();
                            newVersion = firmwareVersionUpdate.getFirmwareVersion();

                            Drawable drawable = getResources().getDrawable(R.mipmap.icon_recommend);
                            //一定要加这行！！！！！！！！！！！
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            mTvClothingVersion.setCompoundDrawables(null, null, drawable, null);
                            QMUIRoundButtonDrawable buttonDrawable = (QMUIRoundButtonDrawable) mBtnUpdate.getBackground();
                            buttonDrawable.setBgData(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                            buttonDrawable.setStroke(1, getResources().getColor(R.color.red));
                            mBtnUpdate.setEnabled(true);
                        } else {
                            RxToast.normal("当前固件版本 v" + currentVersion + " 已经是最新版本，");
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    @Override
    protected void popBackStack() {
        if (dialog == null || !dialog.isShowing()) {
            super.popBackStack();
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
                        mTvClothingVersion.setText("固件版本号 v" + newVersion);
                        mTvClothingVersion.setCompoundDrawables(null, null, null, null);
                        QMUIRoundButtonDrawable buttonDrawable = (QMUIRoundButtonDrawable) mBtnUpdate.getBackground();
                        buttonDrawable.setBgData(ColorStateList.valueOf(getResources().getColor(R.color.GrayWrite)));
                        buttonDrawable.setStroke(1, getResources().getColor(R.color.GrayWrite));
                        mBtnUpdate.setEnabled(false);
                    }
                    @Override
                    public void fail() {
                        mLayoutUpdateFail.setVisibility(View.VISIBLE);
                    }
                });
                //set进度值
                dialog.show();
                //后续再做进度判断，如果到达100，
                break;
            case R.id.tv_tip:
                //服务协议
                Bundle bundle = new Bundle();
                bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Term_Service);
                bundle.putString(Key.BUNDLE_TITLE, getString(R.string.ServiceAgreement));
                RxActivityUtils.skipActivity(mActivity, WebTitleActivity.class, bundle);
                break;
            case R.id.btn_reUpdate:
                mLayoutUpdateFail.setVisibility(View.GONE);
                break;
        }
    }
}
