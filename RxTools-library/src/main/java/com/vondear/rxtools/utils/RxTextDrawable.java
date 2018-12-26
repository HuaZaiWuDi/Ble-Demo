package com.vondear.rxtools.utils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Package com.vondear.rxtools.utils
 * @FileName RxTextDrawable
 * @Date 2018/12/7 15:18
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class RxTextDrawable {

    public static final int O_LEFT = 0;
    public static final int O_TOP = 1;
    public static final int O_RIGHT = 2;
    public static final int O_BOTTOM = 3;


    //自定义注解
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({O_LEFT, O_RIGHT, O_TOP, O_BOTTOM})
    @interface Orientation {

    }


    /**
     * 给drawable添加点击事件
     *
     * @param textView
     * @param orientation
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void addTextDrawableListener(final TextView textView,
                                               @Orientation final int orientation,
                                               final View.OnClickListener mOnClickListener) {
        final Drawable drawable = textView.getCompoundDrawables()[orientation];
        if (drawable == null) {
            RxLogUtils.d("drawable:" + drawable);
            return;
        }
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (orientation) {
                    case O_LEFT:
                        if (event.getX() < textView.getWidth() - drawable.getBounds().width() - textView.getPaddingLeft()) {
                            if (mOnClickListener != null && !RxUtils.isFastClick(500)) {
                                RxLogUtils.d("点击左边");
                                mOnClickListener.onClick(textView);
                            }
                        }
                        break;
                    case O_RIGHT:
                        if (event.getX() > textView.getWidth() - drawable.getBounds().width() - textView.getPaddingRight()) {
                            if (mOnClickListener != null && !RxUtils.isFastClick(500)) {
                                RxLogUtils.d("点击右边");
                                mOnClickListener.onClick(textView);
                            }
                        }
                        break;
                    case O_TOP:
                        if (event.getY() < textView.getHeight() - drawable.getBounds().height() - textView.getPaddingTop()) {
                            if (mOnClickListener != null && !RxUtils.isFastClick(500)) {
                                RxLogUtils.d("点击上边");
                                mOnClickListener.onClick(textView);
                            }
                        }
                        break;
                    case O_BOTTOM:
                        if (event.getY() > textView.getHeight() - drawable.getBounds().height() - textView.getPaddingBottom()) {
                            if (mOnClickListener != null && !RxUtils.isFastClick(500)) {
                                RxLogUtils.d("点击下边");
                                mOnClickListener.onClick(textView);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

}
