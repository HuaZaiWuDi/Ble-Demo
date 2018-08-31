package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxFileUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;
import lab.wesmartclothing.wefit.flyso.ui.login.VerificationPhoneActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.TempActivity_;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;

/**
 * Created by jk on 2018/8/9.
 */
public class Settingfragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.tv_logout)
    TextView mTvLogout;
    Unbinder unbinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        initTopBar();
        groupList();

    }

    private void groupList() {
        QMUICommonListItemView changePasswordItem = mGroupListView.createItemView("修改密码");
        changePasswordItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        changePasswordItem.getTextView().setTextColor(getResources().getColor(R.color.Gray));
        changePasswordItem.getDetailTextView().setTextColor(getResources().getColor(R.color.GrayWrite));

        final QMUICommonListItemView clearCacheItem = mGroupListView.createItemView("清除缓存");
        clearCacheItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        clearCacheItem.setDetailText(RxFileUtils.getTotalCacheSize(mContext.getApplicationContext()));
        clearCacheItem.getTextView().setTextColor(getResources().getColor(R.color.Gray));
        clearCacheItem.getDetailTextView().setTextColor(getResources().getColor(R.color.GrayWrite));

        QMUICommonListItemView accountItem = mGroupListView.createItemView("账号管理");
        accountItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        accountItem.getTextView().setTextColor(getResources().getColor(R.color.Gray));
        accountItem.getDetailTextView().setTextColor(getResources().getColor(R.color.GrayWrite));

        QMUICommonListItemView clothing = mGroupListView.createItemView("瘦身衣");
        clothing.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        clothing.getTextView().setTextColor(getResources().getColor(R.color.Gray));
        clothing.getDetailTextView().setTextColor(getResources().getColor(R.color.GrayWrite));

        QMUIGroupListView.newSection(mContext)
                .addItemView(changePasswordItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxActivityUtils.skipActivity(mActivity, VerificationPhoneActivity.class);
                    }
                })
                .addItemView(clearCacheItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
                        dialog.getTvTitle().setVisibility(View.GONE);
                        dialog.getTvContent().setText("清除缓存？");
                        dialog.setCancel("清除");
                        dialog.setCancelListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                RxFileUtils.clearAllCache(mContext.getApplicationContext());
                                clearCacheItem.setDetailText("0MB");
                            }
                        });
                        dialog.setSure("取消");
                        dialog.setSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                })
                .addItemView(accountItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxActivityUtils.skipActivity(mContext, AccountFragment.class);
                    }
                })
                .addItemView(clothing, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxActivityUtils.skipActivity(mContext, TempActivity_.class);
                    }
                })
                .setUseTitleViewForSectionSpace(false)
                .addTo(mGroupListView);

    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("设置");
    }

    @OnClick(R.id.tv_logout)
    public void onViewClicked() {
        final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
        dialog.getTvTitle().setVisibility(View.GONE);
        dialog.getTvContent().setText("退出登录？");
        dialog.setCancel("退出");
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logout();
            }
        });
        dialog.setSure("取消");
        dialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void logout() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.logout())
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        String baseUrl = SPUtils.getString(SPKey.SP_BSER_URL);
                        boolean SP_GUIDE = SPUtils.getBoolean(SPKey.SP_GUIDE);
                        MyAPP.aMapLocation = null;
                        SPUtils.clear();
                        SPUtils.put(SPKey.SP_BSER_URL, baseUrl);
                        SPUtils.put(SPKey.SP_GUIDE, SP_GUIDE);

                        MyAPP.getRxCache().clear()
                                .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                                .subscribe(new RxSubscriber<Boolean>() {
                                    @Override
                                    protected void _onNext(Boolean aBoolean) {
                                        BleTools.getInstance().disConnect();
                                        new QNBleTools().disConnectDevice();
                                        RxActivityUtils.skipActivityAndFinishAll(mActivity, LoginRegisterActivity.class);
                                    }
                                });
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
