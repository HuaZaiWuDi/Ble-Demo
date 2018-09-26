package com.vondear.rxtools.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.vondear.rxtools.view.layout.helper.RxBaseHelper;
import com.vondear.rxtools.view.layout.iface.RxHelper;

/**
 * RRelativeLayout
 *
 * @author ZhongDaFeng
 */
public class RxRelativeLayout extends RelativeLayout implements RxHelper<RxBaseHelper> {

    private RxBaseHelper mHelper;

    public RxRelativeLayout(Context context) {
        this(context, null);
    }

    public RxRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = new RxBaseHelper(context, this, attrs);
        setClickable(true);
    }

    @Override
    public RxBaseHelper getHelper() {
        return mHelper;
    }
}
