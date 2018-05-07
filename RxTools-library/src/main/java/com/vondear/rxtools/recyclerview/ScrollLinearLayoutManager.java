package com.vondear.rxtools.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * 解决scrollView与recyclerView的滑动嵌套问题
 * <p>
 * 官方的BUG
 * 解决 IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter
 * <p>
 * <p>
 * 其中recyclerView 外面需要嵌套一层RelativeLayout
 **/

public class ScrollLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = false;//是否停止recycler的滑动

    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public ScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}