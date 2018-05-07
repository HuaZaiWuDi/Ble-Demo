package lab.dxythch.com.commonproject.ui.main.slimming;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.vondear.rxtools.fragment.FragmentLazy;

import org.androidannotations.annotations.AfterViews;
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

    @ViewById
    SlidingTabLayout mSlidingTabLayout;
    @ViewById
    ViewPager mViewPager;


    private ArrayList<Fragment> mFragments = new ArrayList<>();


    @Override
    @AfterViews
    public void initData() {
        initTitleTab();
    }

    private void initTitleTab() {
        String[] titleStrings = mActivity.getResources().getStringArray(R.array.tab_Slimming);
        mFragments.add(HeatFragment.getInstance());
        mFragments.add(WeightFragment.getInstance());
        mFragments.add(SportsFragment.getInstance());
        mSlidingTabLayout.setViewPager(mViewPager, titleStrings, mActivity, mFragments);

    }
}
