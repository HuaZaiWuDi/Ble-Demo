package lab.wesmartclothing.wefit.flyso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jk on 2018/8/2.
 */
public class OverlapLayoutManager extends RecyclerView.LayoutManager {
    private Context mContext;
    private float SCALE_GAP = 20f;//向右偏移的角度百分百

    public OverlapLayoutManager(Context context) {
        mContext = context;
        SCALE_GAP = dp2px(25);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() < 0 || state.isPreLayout()) return;
        //1.如何实现层叠效果--cardView.layout(l,t,r,b)
        //2.如何让8个条目中的4个展示在RecylerView里面
        //1在布局layout之前，将所有的子View先全部detach掉，然后放到Scrap集合里面缓存。
        detachAndScrapAttachedViews(recycler);
        //2)只将最上面4个view添加到RecylerView容器里面

        int itemCount = getItemCount();//10个

        for (int i = 0; i < itemCount; i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);

            //摆放View
            layoutDecorated(view,
                    0,
                    0,
                    getDecoratedMeasuredWidth(view),
                    getDecoratedMeasuredHeight(view));

            //层叠效果--Scale+TranslationY
            //层级的位置关系1/2/3/4
            view.setTranslationX(SCALE_GAP * i);
        }
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                mContext.getResources().getDisplayMetrics());
    }
}
