package lab.wesmartclothing.wefit.flyso.ui.main;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.ActivityBaseLocation;
import lab.wesmartclothing.wefit.flyso.base.FragmentKeyDown;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.entity.DeviceLink;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.login.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.ui.main.find.FindFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MineFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.SlimmingFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.store.StoreFragment;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActivityBaseLocation {

    @ViewById
    CommonTabLayout mCommonTabLayout;
    @ViewById
    RelativeLayout bottom_tab;

    @Pref
    Prefs_ mPrefs;

    @Receiver(actions = Key.ACTION_SWITCH_BOTTOM_TAB)
    void switchBottomTab(@Receiver.Extra(Key.EXTRA_SWITCH_BOTTOM_TAB) boolean isVisible) {
        bottom_tab.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    @AfterViews
    public void initView() {
        StatusBarUtils.from(this).setHindStatusBar(true).process();

        initBottomTab();
        initMyViewPager();
        setDefaultFragment();


    }



    private void initMyViewPager() {
        mFragments.clear();
        mFragments.add(SlimmingFragment.getInstance());
        mFragments.add(FindFragment.getInstance());
        mFragments.add(StoreFragment.getInstance());
        mFragments.add(MineFragment.getInstance());
    }

    private void initBottomTab() {
        String[] tab_text = getResources().getStringArray(R.array.tab_text);
        int[] imgs_unselect = {R.mipmap.slimming_unselect, R.mipmap.found_normal_icon1,
                R.mipmap.market_normal_icon1, R.mipmap.my_normal_icon1};
        int[] imgs_select = {R.mipmap.fit_click_icon1, R.mipmap.found_click_icon,
                R.mipmap.market_click_icon, R.mipmap.my_click_icon1};

        for (int i = 0; i < tab_text.length; i++) {
            mBottomTabItems.add(new BottomTabItem(imgs_select[i], imgs_unselect[i], tab_text[i]));
        }

        mCommonTabLayout.setTabData(mBottomTabItems);

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchFragment(mFragments.get(position));
                if (position == 3) {
                    RxActivityUtils.skipActivity(mContext, AddDeviceActivity_.class);
                }
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

        FragmentKeyDown mStoreFragment = (StoreFragment) mFragments.get(2);
        FragmentKeyDown mFindFragment = (FindFragment) mFragments.get(1);

        if (mStoreFragment != null) {
            if (mStoreFragment.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                if (!RxUtils.isFastClick(2000)) {
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
    public void setGpsInfo(Location location) {

    }

    @Override
    protected void onDestroy() {
        initDevice();
        super.onDestroy();
    }

    //数据统计接口
    private void initDevice() {
        DeviceLink deviceLink = new DeviceLink();
        deviceLink.setMacAddr(RxDeviceUtils.getAndroidId(this.getApplicationContext()));//AndroidID
        deviceLink.setDeviceName(RxDeviceUtils.getBuildMANUFACTURER());//设备厂商名字，如：小米
        deviceLink.setLinkStatus(0);
        deviceLink.deviceLink(deviceLink);
    }

}
