package com.vondear.rxtools.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vondear.rxtools.view.layout.helper.RxBaseHelper;
import com.vondear.rxtools.view.layout.iface.RxHelper;

/**
 * RLinearLayout
 *
 * @author ZhongDaFeng
 */
public class RxLinearLayout extends LinearLayout implements RxHelper<RxBaseHelper> {

    private RxBaseHelper mHelper;

    public RxLinearLayout(Context context) {
        this(context, null);
    }

    public RxLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = new RxBaseHelper(context, this, attrs);
        setClickable(true);
    }

    @Override
    public RxBaseHelper getHelper() {
        return mHelper;
    }
}
