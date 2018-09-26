package com.vondear.rxtools.view.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.vondear.rxtools.view.layout.helper.RxImageViewHelper;

/**
 * RImageView
 *
 * @author ZhongDaFeng
 */
public class RxImageView extends android.support.v7.widget.AppCompatImageView {

    private RxImageViewHelper mHelper;

    public RxImageView(Context context) {
        this(context, null);
    }

    public RxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = new RxImageViewHelper(context, this, attrs);
        setClickable(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mHelper.isNormal()) {
            super.onDraw(canvas);
        } else {
            mHelper.onDraw(canvas);
        }
    }

    public RxImageViewHelper getHelper() {
        return mHelper;
    }


}
