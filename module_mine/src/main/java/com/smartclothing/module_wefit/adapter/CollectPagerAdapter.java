package com.smartclothing.module_wefit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smartclothing.module_wefit.fragment.CollectPagerItemFragment;

import java.util.List;

public class CollectPagerAdapter extends FragmentPagerAdapter {

    List<CollectPagerItemFragment> fragments;

    private final String[] TITLES = {"课程", "资讯"};

    public CollectPagerAdapter(FragmentManager fm, List<CollectPagerItemFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
