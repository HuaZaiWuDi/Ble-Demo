package lab.wesmartclothing.wefit.flyso.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.find.FindFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MeFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MessageFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.Slimming2Fragment;
import lab.wesmartclothing.wefit.flyso.ui.main.store.StoreFragment;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.ACTIVITY_FIND;
import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.ACTIVITY_MESSAGE;
import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.ACTIVITY_SHOP;
import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.ACTIVITY_SLIM;
import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.ACTIVITY_USER;

/**
 * Created by jk on 2018/8/10.
 */
public class MainFragment extends BaseAcFragment {

    @BindView(R.id.mFrameLayout)
    FrameLayout mMFrameLayout;
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

    private void initView() {
        initMyViewPager();
        initBottomTab();
        setDefaultFragment();
        initRxBus();
    }

    private void initRxBus() {
        Disposable register = RxBus.getInstance().register(String.class, new Consumer<String>() {
            @Override
            public void accept(String openTarget) throws Exception {
                RxLogUtils.d("点击通知栏执行操作：" + openTarget);
                openActivity(openTarget);
            }
        });
        RxBus.getInstance().addSubscription(this, register);
    }

    private void openActivity(String openTarget) {
        switch (openTarget) {
            case ACTIVITY_SLIM:
                mCommonTabLayout.setCurrentTab(0);
                switchFragment(mFragments.get(0));
                break;
            case ACTIVITY_FIND:
                mCommonTabLayout.setCurrentTab(1);
                switchFragment(mFragments.get(1));
                break;
            case ACTIVITY_SHOP:
                mCommonTabLayout.setCurrentTab(2);
                switchFragment(mFragments.get(2));
                break;
            case ACTIVITY_USER:
                mCommonTabLayout.setCurrentTab(3);
                switchFragment(mFragments.get(3));
                break;
            case ACTIVITY_MESSAGE:
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
                }
            }
        });
    }

    private void initMyViewPager() {
        mFragments.clear();
//        mFragments.add(SlimmingFragment.getInstance());
        mFragments.add(Slimming2Fragment.getInstance());
        mFragments.add(FindFragment.getInstance());
        mFragments.add(StoreFragment.getInstance());
        mFragments.add(MeFragment.getInstance());
    }

    private FragmentManager fm;
    private Fragment mFragmentNow;

    private void setDefaultFragment() {
        fm = getChildFragmentManager();
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
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();

    }
}
