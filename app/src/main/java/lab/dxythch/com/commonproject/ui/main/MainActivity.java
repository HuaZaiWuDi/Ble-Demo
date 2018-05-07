package lab.dxythch.com.commonproject.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.vondear.rxtools.view.UnScrollableViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseActivity;
import lab.dxythch.com.commonproject.entity.BottomTabItem;
import lab.dxythch.com.commonproject.ui.main.find.FindFragment;
import lab.dxythch.com.commonproject.ui.main.mine.MineFragment;
import lab.dxythch.com.commonproject.ui.main.slimming.SlimmingFragment;
import lab.dxythch.com.commonproject.ui.main.store.StoreFragment;
import lab.dxythch.com.commonproject.utils.StatusBarUtils;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById
    CommonTabLayout mCommonTabLayout;
    @ViewById
    UnScrollableViewPager mUnScrollableViewPager;

    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    @AfterViews
    public void initView() {
//        RxBarUtils.hideStatusBar(this);
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();

        initBottomTab();
        initMyViewPager();
    }

    private void initMyViewPager() {
        mFragments.add(SlimmingFragment.getInstance());
        mFragments.add(FindFragment.getInstance());
        mFragments.add(StoreFragment.getInstance());
        mFragments.add(MineFragment.getInstance());

        mUnScrollableViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
    }

    private void initBottomTab() {
        String[] tab_text = getResources().getStringArray(R.array.tab_text);
        int[] imgs_unselect = {R.mipmap.slimming_unselect, R.mipmap.found_normal_icon1,
                R.mipmap.market_normal_icon1, R.mipmap.my_normal_icon1};
        int[] imgs_select = {R.mipmap.fit_click_icon1, R.mipmap.found_click_icon1,
                R.mipmap.market_click_icon1, R.mipmap.my_click_icon1};

        for (int i = 0; i < tab_text.length; i++) {
            mBottomTabItems.add(new BottomTabItem(imgs_select[i], imgs_unselect[i], tab_text[i]));
        }

        mCommonTabLayout.setTabData(mBottomTabItems);

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mUnScrollableViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }


}
