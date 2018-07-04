package lab.wesmartclothing.wefit.flyso.ui.main.slimming;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;

import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.UnScrollableViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseFragment;
import lab.wesmartclothing.wefit.flyso.rxbus.SlimmingTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.HeatFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SportsFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightFragment;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_slimming)
public class SlimmingFragment extends BaseFragment {

    public static SlimmingFragment getInstance() {
        return new SlimmingFragment_();
    }

    //    @ViewById
//    MagicIndicator magicIndicator;
    @ViewById
    UnScrollableViewPager mViewPager;

    @ViewById
    TextView tv_heat;
    @ViewById
    TextView tv_weight;
    @ViewById
    TextView tv_sports;

    private boolean isComplete = false;
    private int position = 1;

    @Click
    void tv_heat() {
        position = 0;
        initTitle(position);
        mViewPager.setCurrentItem(0);
    }

    @Click
    void tv_weight() {
        position = 1;
        initTitle(position);
        mViewPager.setCurrentItem(1);
    }

    @Click
    void tv_sports() {
        position = 2;
        initTitle(position);
        mViewPager.setCurrentItem(2);
    }

    @Receiver(actions = Key.ACTION_SWITCH_THEME)
    void switchTheme(@Receiver.Extra(Key.EXTRA_SWITCH_THEME) boolean isComplete) {
        this.isComplete = isComplete;
        initTitle(position);
    }


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] titleStrings;

    @Override
    public void initData() {
        RxLogUtils.d("加载：【SlimmingFragment】");
    }

    @AfterViews
    public void initView() {
        initTitleTab();
        initRxBus();
    }

    private void initRxBus() {
        RxBus.getInstance().register(SlimmingTab.class, new Consumer<SlimmingTab>() {
            @Override
            public void accept(SlimmingTab slimmingTab) throws Exception {
                position = slimmingTab.getTab();
                initTitle(position);
                mViewPager.setCurrentItem(position);
            }
        });
    }

    private void initTitleTab() {
        mFragments.clear();
        titleStrings = mActivity.getResources().getStringArray(R.array.tab_Slimming);
        mFragments.add(HeatFragment.getInstance());
        mFragments.add(WeightFragment.getInstance());
        mFragments.add(SportsFragment.getInstance());

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new FragmentPagerAdapter(mActivity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        position = 1;
        initTitle(position);
        mViewPager.setCurrentItem(1);
//        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                initTitle(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    private void initTitle(int index) {
        tv_heat.setBackgroundResource(index == 0 ? R.mipmap.button_bg : 0);
        tv_heat.setTextColor(getResources().getColor(index == 0 ? isComplete ? R.color.colorRed : R.color.colorTheme : R.color.white));
        tv_weight.setBackgroundResource(index == 1 ? R.mipmap.button_bg : 0);
        tv_weight.setTextColor(getResources().getColor(index == 1 ? R.color.colorTheme : R.color.white));
        tv_sports.setBackgroundResource(index == 2 ? R.mipmap.button_bg : 0);
        tv_sports.setTextColor(getResources().getColor(index == 2 ? R.color.colorTheme : R.color.white));

    }

}
