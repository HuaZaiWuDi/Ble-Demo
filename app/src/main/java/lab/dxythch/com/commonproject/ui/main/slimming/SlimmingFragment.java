package lab.dxythch.com.commonproject.ui.main.slimming;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.vondear.rxtools.fragment.FragmentLazy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.ui.main.slimming.heat.HeatFragment;
import lab.dxythch.com.commonproject.ui.main.slimming.sports.SportsFragment;
import lab.dxythch.com.commonproject.ui.main.slimming.weight.WeightFragment;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_slimming)
public class SlimmingFragment extends FragmentLazy {

    public static SlimmingFragment getInstance() {
        return new SlimmingFragment_();
    }

    //    @ViewById
//    MagicIndicator magicIndicator;
    @ViewById
    ViewPager mViewPager;

    @ViewById
    TextView tv_heat;
    @ViewById
    TextView tv_weight;
    @ViewById
    TextView tv_sports;


    @Click
    void tv_heat() {
        initTitle(0);
        mViewPager.setCurrentItem(0);
    }

    @Click
    void tv_weight() {
        initTitle(1);
        mViewPager.setCurrentItem(1);
    }

    @Click
    void tv_sports() {
        initTitle(2);
        mViewPager.setCurrentItem(2);
    }


    private int titleIndex = 0;


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] titleStrings;

    @Override
    public void initData() {

    }

    @AfterViews
    void initView() {
        initTitleTab();
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

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTitle(int index) {
        tv_heat.setBackgroundResource(index == 0 ? R.mipmap.button_bg : 0);
        tv_heat.setTextColor(getResources().getColor(index == 0 ? R.color.colorTheme : R.color.white));
        tv_weight.setBackgroundResource(index == 1 ? R.mipmap.button_bg : 0);
        tv_weight.setTextColor(getResources().getColor(index == 1 ? R.color.colorTheme : R.color.white));
        tv_sports.setBackgroundResource(index == 2 ? R.mipmap.button_bg : 0);
        tv_sports.setTextColor(getResources().getColor(index == 2 ? R.color.colorTheme : R.color.white));

    }


}
