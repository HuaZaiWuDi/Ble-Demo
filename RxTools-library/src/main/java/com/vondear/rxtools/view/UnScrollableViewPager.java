package com.vondear.rxtools.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <><p/>
 * Created by woxingxiao on 2017-06-20.
 * 不能滑动的ViewPager
 */

public class UnScrollableViewPager extends ViewPager {

    public UnScrollableViewPager(Context context) {
        super(context);
    }

    public UnScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
