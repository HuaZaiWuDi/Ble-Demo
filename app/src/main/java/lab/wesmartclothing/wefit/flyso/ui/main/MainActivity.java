package lab.wesmartclothing.wefit.flyso.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseALocationActivity;
import lab.wesmartclothing.wefit.flyso.base.FragmentKeyDown;
import lab.wesmartclothing.wefit.flyso.ble.BleService_;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.flyso.rxbus.SlimmingTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginActivity_;
import lab.wesmartclothing.wefit.flyso.ui.login.UserInfoActivity_;
import lab.wesmartclothing.wefit.flyso.ui.main.find.FindFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MineFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.SlimmingFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.store.StoreFragment;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseALocationActivity {

    @ViewById
    CommonTabLayout mCommonTabLayout;
    @ViewById
    RelativeLayout bottom_tab;

    @Pref
    Prefs_ mPrefs;

    private Intent bleIntent;


    @Receiver(actions = Key.ACTION_SWITCH_BOTTOM_TAB)
    void switchBottomTab(@Receiver.Extra(Key.EXTRA_SWITCH_BOTTOM_TAB) boolean isVisible) {
        bottom_tab.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    @AfterViews
    public void initView() {
        //屏幕沉浸
        StatusBarUtils.from(this).setStatusBarColor(getResources().getColor(R.color.colorTheme)).process();
        NetManager.getInstance().setUserIdToken(mPrefs.UserId().get(), mPrefs.token().get());
        initBottomTab();
        initMyViewPager();
        setDefaultFragment();
        initRxBus();

        startLocation(null);

//        RxActivityUtils.skipActivity(mContext, TempActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bleIntent = new Intent(mContext, BleService_.class);
        startService(bleIntent);
    }

    private void initRxBus() {
        RxBus.getInstance().register(SlimmingTab.class, new Consumer<SlimmingTab>() {
            @Override
            public void accept(SlimmingTab slimmingTab) throws Exception {
                switchFragment(mFragments.get(0));
                mCommonTabLayout.setCurrentTab(0);
            }
        });
    }

    private void initMyViewPager() {
        mFragments.clear();
        mFragments.add(SlimmingFragment.getInstance());
        mFragments.add(FindFragment.getInstance());
        mFragments.add(StoreFragment.getInstance());
        mFragments.add(MineFragment.getInstance());
//        mFragments.add(Mine.getInstance());
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
            }

            @Override
            public void onTabReselect(int position) {
                if (BuildConfig.DEBUG)
                    if (position == 0) {
                        final EditText editText = new EditText(mContext);
                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setTitle("修改用户ID")
                                .setView(editText)
                                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String s = editText.getText().toString();
                                        if (RxDataUtils.isNullString(s))
                                            s = "";
                                        RxActivityUtils.skipActivity(mContext, MainActivity_.class);
                                        finish();
                                        mPrefs.UserId().put(s);
                                        NetManager.getInstance().setUserIdToken(mPrefs.UserId().get(), mPrefs.token().get());
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else if (position == 3) {
                        RxActivityUtils.skipActivity(mContext, LoginActivity_.class);
                    } else if (position == 2) {
                        RxActivityUtils.skipActivity(mContext, UserInfoActivity_.class);
                    }
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
    protected void onDestroy() {
        stopService(bleIntent);
        super.onDestroy();
    }


}
