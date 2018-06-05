package com.smartclothing.module_wefit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smartclothing.module_wefit.fragment.CollectPagerItemFragment;
import com.smartclothing.module_wefit.fragment.MessagePagerItemFragment;

import java.util.List;

public class MessagePagerAdapter extends FragmentPagerAdapter {

    List<MessagePagerItemFragment> fragments;

    private final String[] TITLES = {"提醒", "通知"};

    public MessagePagerAdapter(FragmentManager fm, List<MessagePagerItemFragment> fragments) {
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
