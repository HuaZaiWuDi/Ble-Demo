package com.vondear.rxtools.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.vondear.rxtools.view.layout.helper.RxBaseHelper;
import com.vondear.rxtools.view.layout.iface.RxHelper;

/**
 * RView
 *
 * @author ZhongDaFeng
 */
public class RxView extends View implements RxHelper<RxBaseHelper> {


    private RxBaseHelper mHelper;

    public RxView(Context context) {
        this(context, null);
    }

    public RxView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = new RxBaseHelper(context, this, attrs);
        setClickable(true);
    }

    @Override
    public RxBaseHelper getHelper() {
        return mHelper;
    }
}
