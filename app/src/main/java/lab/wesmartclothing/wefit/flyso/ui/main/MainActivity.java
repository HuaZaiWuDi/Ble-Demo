package lab.wesmartclothing.wefit.flyso.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.smartclothing.module_wefit.widget.dialog.AboutUpdateDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseALocationActivity;
import lab.wesmartclothing.wefit.flyso.base.FragmentKeyDown;
import lab.wesmartclothing.wefit.flyso.ble.BleService_;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.rxbus.SlimmingTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.find.FindFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MineFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.Slimming2Fragment;
import lab.wesmartclothing.wefit.flyso.ui.main.store.StoreFragment;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseALocationActivity {

    @ViewById
    CommonTabLayout mCommonTabLayout;
    @ViewById
    RelativeLayout bottom_tab;


    private Intent bleIntent;


    @Receiver(actions = Key.ACTION_SWITCH_BOTTOM_TAB)
    void switchBottomTab(@Receiver.Extra(Key.EXTRA_SWITCH_BOTTOM_TAB) boolean isVisible) {
        bottom_tab.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
    }

    @Override
    @AfterViews
    public void initView() {

        initBottomTab();
        initMyViewPager();
        setDefaultFragment();
        initRxBus();

        startLocation(null);
        NetManager.getInstance().setUserIdToken(SPUtils.getString(SPKey.SP_UserId), SPUtils.getString(SPKey.SP_token));

        RxLogUtils.d("手机MAC地址" + RxDeviceUtils.getMacAddress(mContext));
        RxLogUtils.d("手机信息" + RxDeviceUtils.getAndroidId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        bleIntent = new Intent(mContext, BleService_.class);
        startService(bleIntent);
    }

    private void initRxBus() {
        Disposable register = RxBus.getInstance().register(SlimmingTab.class, new Consumer<SlimmingTab>() {
            @Override
            public void accept(SlimmingTab slimmingTab) throws Exception {
                switchFragment(mFragments.get(0));
                mCommonTabLayout.setCurrentTab(0);
            }
        });
        Disposable disposable = RxBus.getInstance().register(FirmwareVersionUpdate.class, new Consumer<FirmwareVersionUpdate>() {
            @Override
            public void accept(final FirmwareVersionUpdate firmwareVersionUpdate) throws Exception {
                final boolean isMust = firmwareVersionUpdate.getMustUpgrade() != 0;
                //差值大于2kg，体重数据不合理
                final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
                dialog.getTvTitle().setBackgroundResource(R.mipmap.slice);
                dialog.getTvContent().setText("是否升级到最新的版本");
                dialog.setCancel("升级");
                dialog.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        AboutUpdateDialog updatedialog = new AboutUpdateDialog(mActivity, firmwareVersionUpdate.getFileUrl(), firmwareVersionUpdate.getMustUpgrade() == 0);
                        updatedialog.show();
                    }
                });
                dialog.setSure(isMust ? "退出" : "取消");
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (isMust) {
                            RxActivityUtils.AppExit(mContext);
                            finish();
                        }
                    }
                });

            }
        });

        RxBus.getInstance().addSubscription(this, register, disposable);
    }

    private void initMyViewPager() {
        mFragments.clear();
//        mFragments.add(SlimmingFragment.getInstance());
        mFragments.add(Slimming2Fragment.getInstance());
        mFragments.add(FindFragment.getInstance());
        mFragments.add(StoreFragment.getInstance());
        mFragments.add(MineFragment.getInstance());
    }

    private void initBottomTab() {
        String[] tab_text = getResources().getStringArray(R.array.tab_text);
        int[] imgs_unselect = {R.mipmap.icon_slimming_unselect, R.mipmap.icon_find_unselect,
                R.mipmap.icon_shopping_unselect, R.mipmap.icon_mine_unselect};
        int[] imgs_select = {R.mipmap.icon_slimming_select, R.mipmap.icon_find_select,
                R.mipmap.icon_shopping_select, R.mipmap.icon_mine_select};

        for (int i = 0; i < tab_text.length; i++) {
            mBottomTabItems.add(new BottomTabItem(imgs_select[i], imgs_unselect[i], tab_text[i]));
        }

        mCommonTabLayout.setTextSelectColor(getResources().getColor(R.color.Gray));
        mCommonTabLayout.setTextUnselectColor(getResources().getColor(R.color.GrayWrite));
        mCommonTabLayout.setTabData(mBottomTabItems);

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchFragment(mFragments.get(position));
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    private FragmentManager fm;
    private Fragment mFragmentNow;

    private void setDefaultFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.mFrameLayout, mFragments.get(0));
        transaction.commit();
        mFragmentNow = mFragments.get(0);
    }


    private void switchFragment(Fragment to) {
        if (mFragmentNow != to) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(mFragmentNow).add(R.id.mFrameLayout, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mFragmentNow).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            mFragmentNow = to;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) return super.onKeyDown(keyCode, event);
        FragmentKeyDown mStoreFragment = (StoreFragment) mFragments.get(2);
        FragmentKeyDown mFindFragment = (FindFragment) mFragments.get(1);

        if (mStoreFragment != null) {
            if (mStoreFragment.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                if (!RxUtils.isFastClick(2000)) {
                    RxLogUtils.d("mStoreFragment:再按一次");
                    RxToast.success("再按一次退出");
                    return true;
                }
            }
        }
        if (mFindFragment != null) {
            if (mFindFragment.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                if (!RxUtils.isFastClick(2000)) {
                    RxLogUtils.d("mFindFragment:再按一次");
                    RxToast.success("再按一次退出");
                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    //不退出app，而是隐藏当前的app
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        stopService(bleIntent);
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();
    }


}
