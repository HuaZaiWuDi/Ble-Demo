package com.vondear.rxtools.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * 公司名称：DXY鼎芯
 * 项目名称：DXYBle
 * 类描述：解决GridView高度设置问题，同时给GridView添加天幕线条默认灰色线条
 * 创建人：Jack
 * 创建时间：2017/6/7
 */
public class myGridView extends GridView {
    private int colorLine = Color.parseColor("#F3F3F3");
    private boolean HasLine = false;

    public myGridView(Context context) {
        super(context);
    }

    public myGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public myGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);//自定义设置高度,第一个是值，后面是最大的状态
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!HasLine) return;
        try {
            View localView1 = getChildAt(0);
            int column = getWidth() / localView1.getWidth();
            int childCount = getChildCount();
            Paint localPaint;
            localPaint = new Paint();
            localPaint.setStyle(Paint.Style.STROKE);
//        localPaint.setColor(getContext().getResources().getColor(colorLine));
            localPaint.setColor(colorLine);
            localPaint.setStrokeMiter((float) 1.0);
            for (int i = 0; i < childCount; i++) {
                View cellView = getChildAt(i);
                if ((i + 1) % column == 0) {
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                } else if ((i + 1) > (childCount - (childCount % column))) {
                    canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                } else {
                    canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                }
            }
            if (childCount % column != 0) {
                for (int j = 0; j < (column - childCount % column); j++) {
                    View lastView = getChildAt(childCount - 1);
                    canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public void setColorLine(@ColorRes int colorLine) {
        this.colorLine = colorLine;
        invalidate();
    }

    public void setHasLine(boolean hasLine) {
        HasLine = hasLine;
        invalidate();
    }
}