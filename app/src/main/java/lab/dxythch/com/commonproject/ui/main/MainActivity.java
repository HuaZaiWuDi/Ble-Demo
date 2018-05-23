package lab.dxythch.com.commonproject.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseActivity;
import lab.dxythch.com.commonproject.base.MyAPP;
import lab.dxythch.com.commonproject.entity.BottomTabItem;
import lab.dxythch.com.commonproject.entity.SaveUserInfo;
import lab.dxythch.com.commonproject.netserivce.RetrofitService;
import lab.dxythch.com.commonproject.prefs.Prefs_;
import lab.dxythch.com.commonproject.tools.Key;
import lab.dxythch.com.commonproject.ui.login.AddDeviceActivity_;
import lab.dxythch.com.commonproject.ui.login.LoginActivity_;
import lab.dxythch.com.commonproject.ui.main.find.FindFragment;
import lab.dxythch.com.commonproject.ui.main.mine.MineFragment;
import lab.dxythch.com.commonproject.ui.main.slimming.SlimmingFragment;
import lab.dxythch.com.commonproject.ui.main.store.StoreFragment;
import lab.dxythch.com.commonproject.utils.StatusBarUtils;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById
    CommonTabLayout mCommonTabLayout;

    @Pref
    Prefs_ mPrefs;

    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    @AfterViews
    public void initView() {
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();

        initBottomTab();
        initMyViewPager();
        setDefaultFragment();
        initData();

        NetManager.getInstance().setUserIdToken(mPrefs.UserId().get(), mPrefs.token().get());
    }

    //重传在无网络情况下未上传的数据
    private void initData() {
        if (!RxNetUtils.isAvailable(mContext.getApplicationContext())) {
            //没有网络直接返回
            return;
        }
        saveUserInfo();
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
                    RxActivityUtils.skipActivity(mContext, LoginActivity_.class);
                } else if (position == 1) {
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


    //重传用户信息
    private void saveUserInfo() {
        SaveUserInfo mUserInfo = (SaveUserInfo) MyAPP.getACache().getAsObject(Key.CACHE_USER_INFO);
        if (mUserInfo == null) return;
        String s = new Gson().toJson(mUserInfo, SaveUserInfo.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.saveUserInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        MyAPP.getACache().remove(Key.CACHE_USER_INFO);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


}
