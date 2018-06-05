package com.smartclothing.module_wefit.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.adapter.CollectPagerAdapter;
import com.smartclothing.module_wefit.fragment.CollectPagerItemFragment;
import com.smartclothing.module_wefit.widget.NoScrollViewPager;
import com.smartclothing.module_wefit.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/*我的收藏*/

public class CollectActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout iv_back;

    PagerSlidingTabStrip slider_collect;
    NoScrollViewPager pager_collect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collect);

        initView();

    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);

        slider_collect = findViewById(R.id.slider_collect);
        pager_collect = findViewById(R.id.pager_collect);

        List<CollectPagerItemFragment> fragments = new ArrayList<>();
        CollectPagerItemFragment fragmentLessen = new CollectPagerItemFragment();
        fragmentLessen.setInfoType(1);
        CollectPagerItemFragment fragmentNews = new CollectPagerItemFragment();
        fragmentNews.setInfoType(2);
        fragments.add(fragmentLessen);
        fragments.add(fragmentNews);
        pager_collect.setAdapter(new CollectPagerAdapter(getSupportFragmentManager(), fragments));
        pager_collect.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        slider_collect.setViewPager(pager_collect);

        listener();
    }

    private void listener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            onBackPressed();

        }
    }
}
