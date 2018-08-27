package lab.wesmartclothing.wefit.flyso.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.rxbus.GoToFind;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.find.FindFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MeFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MessageFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.Slimming2Fragment;
import lab.wesmartclothing.wefit.flyso.ui.main.store.StoreFragment;
import lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

/**
 * Created by jk on 2018/8/10.
 */
public class MainFragment extends BaseAcFragment {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.mCommonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.bottom_tab)
    RelativeLayout mBottomTab;
    @BindView(R.id.parent)
    RelativeLayout mParent;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new MainFragment();
    }

    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    protected boolean canDragBack() {
        return false;
    }

    private void initView() {
        RxLogUtils.i("MainFragment 创建");
        initMyViewPager();
        initBottomTab();
        initRxBus();
        mBottomTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initRxBus() {
        Disposable register = RxBus.getInstance().register(String.class, new Consumer<String>() {
            @Override
            public void accept(String openTarget) throws Exception {
                RxLogUtils.d("点击通知栏执行操作：" + openTarget);
                openActivity(openTarget);
            }
        });
        Disposable goToFind = RxBus.getInstance().register(GoToFind.class, new Consumer<GoToFind>() {
            @Override
            public void accept(GoToFind goToFind) throws Exception {
                mViewpager.setCurrentItem(1, true);
            }
        });
        RxBus.getInstance().addSubscription(this, register, goToFind);
    }

    private void openActivity(String openTarget) {
        switch (openTarget) {
            case MyJpushReceiver.ACTIVITY_SLIM:
                mViewpager.setCurrentItem(0, true);
                break;
            case MyJpushReceiver.ACTIVITY_FIND:
                mViewpager.setCurrentItem(1, true);
                break;
            case MyJpushReceiver.ACTIVITY_SHOP:
                mViewpager.setCurrentItem(2, true);
                break;
            case MyJpushReceiver.ACTIVITY_USER:
                mViewpager.setCurrentItem(3, true);
                break;
            case MyJpushReceiver.ACTIVITY_MESSAGE:
                //跳转消息通知
                startFragment(MessageFragment.getInstance());
                break;
        }
    }


    private void initBottomTab() {
        String[] tab_text = getResources().getStringArray(R.array.tab_text);
        int[] imgs_unselect = {R.mipmap.icon_slimming_unselect, R.mipmap.icon_find_unselect,
                R.mipmap.icon_shopping_unselect, R.mipmap.icon_mine_unselect};
        int[] imgs_select = {R.mipmap.icon_slimming_select, R.mipmap.icon_find_select,
                R.mipmap.icon_shopping_select, R.mipmap.icon_mine_select};
        mBottomTabItems.clear();
        for (int i = 0; i < tab_text.length; i++) {
            mBottomTabItems.add(new BottomTabItem(imgs_select[i], imgs_unselect[i], tab_text[i]));
        }

        mCommonTabLayout.setTextSelectColor(getResources().getColor(R.color.Gray));
        mCommonTabLayout.setTextUnselectColor(getResources().getColor(R.color.GrayWrite));
        mCommonTabLayout.setTabData(mBottomTabItems);

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewpager.setCurrentItem(position, true);
            }

            @Override
            public void onTabReselect(int position) {
                //双击或三击我的按钮，出现切换网络界面，同时需要退出重新登录
                if (position == 3 && RxUtils.isFastClick(1000) && BuildConfig.DEBUG) {
                    new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                            .addItem("http://10.10.11.192:15112")
                            .addItem("http://10.10.11.208:15112")
                            .addItem("http://119.23.225.125:15112")
                            .addItem("http://10.10.11.208:15101/mix/")
                            .setTitle("修改网络需要重启应用，提示网络错误，需要重新登录")
                            .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    dialog.dismiss();
                                    SPUtils.put(SPKey.SP_BSER_URL, tag);
                                    ServiceAPI.switchURL(tag);
                                }
                            })
                            .build()
                            .show();
//                    showLocalNotify();

                }
            }
        });
    }

    private void showLocalNotify() {
        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(1);
        ln.setContent("hhhfff");
        ln.setTitle("lnfff");
        ln.setNotificationId(11111111);
        ln.setBroadcastTime(System.currentTimeMillis() + 1000 * 60 * 10);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "jpush");
        map.put("test", "111");
        JSONObject json = new JSONObject(map);
        ln.setExtras(json.toString());
        JPushInterface.addLocalNotification(mContext.getApplicationContext(), ln);
    }


    private void initMyViewPager() {
        mFragments.clear();
//        mFragments.add(SlimmingFragment.getInstance());
        mFragments.add(Slimming2Fragment.getInstance());
        mFragments.add(FindFragment.getInstance());
        mFragments.add(StoreFragment.getInstance());
        mFragments.add(MeFragment.getInstance());

        mViewpager.setOffscreenPageLimit(4);
        mViewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCommonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

//    private FragmentManager fm;
//    private Fragment mFragmentNow;

//    private void setDefaultFragment() {
//        fm = getChildFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.add(R.id.mFrameLayout, mFragments.get(0));
//        transaction.commit();
//        mFragmentNow = mFragments.get(0);
//    }
//
//
//    private void switchFragment(Fragment to) {
//        if (mFragmentNow != to) {
//            FragmentTransaction transaction = fm.beginTransaction();
//            if (!to.isAdded()) {    // 先判断是否被add过
//                transaction.hide(mFragmentNow).add(R.id.mFrameLayout, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
//            } else {
//                transaction.hide(mFragmentNow).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
//            }
//            mFragmentNow = to;
//        }
//    }

    @Override
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();

    }
}
