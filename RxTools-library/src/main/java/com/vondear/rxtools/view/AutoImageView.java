package com.vondear.rxtools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 项目名称：CheckInOut
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/12/5
 * <p>
 * 配合RecyclerView使用
 * <p>
 * mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
 *
 * @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
 * super.onScrolled(recyclerView, dx, dy);
 * <p>
 * int fPos = mLinearLayoutManager.findFirstVisibleItemPosition();
 * int lPos = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
 * for (int i = fPos; i <= lPos; i++) {
 * View view = mLinearLayoutManager.findViewByPosition(i);
 * AutoImageView adImageView = (AutoImageView) view.findViewById(R.id.id_iv_ad);
 * if (adImageView.getVisibility() == View.VISIBLE) {
 * Dy = mLinearLayoutManager.getHeight() - view.getTop();
 * adImageView.setDy(Dy);
 * }
 * }
 * }
 * });
 */
public class AutoImageView extends AppCompatImageView {

    private int mMinDy;//最小y轴距离，也就是图片控件高度
    private int mDy;//动态显示图片的top对应Y轴位置


    public AutoImageView(Context context) {
        this(context, null);
    }

    public AutoImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setDy(int dy) {
        if (getDrawable() == null) {
            return;
        }
        mDy = dy - mMinDy;
        if (mDy <= 0) {
            mDy = 0;
        }
        if (mDy > getDrawable().getBounds().height() - mMinDy) {
            mDy = getDrawable().getBounds().height() - mMinDy;
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMinDy = h;
    }

    public int getDy() {
        return mDy;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        int w = getWidth();
        int h = (int) (getWidth() * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, w, h);//绘制充满控件宽高的图片
        canvas.save();
        canvas.translate(0, -getDy());//移动绘制到指定y轴的地方
        super.onDraw(canvas);
        canvas.restore();
    }
}
