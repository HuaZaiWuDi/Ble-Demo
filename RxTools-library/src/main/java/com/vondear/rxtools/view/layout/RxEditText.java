package com.vondear.rxtools.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.vondear.rxtools.view.layout.helper.RxTextViewHelper;
import com.vondear.rxtools.view.layout.iface.RxHelper;

/**
 * REditText
 *
 * @author ZhongDaFeng
 */
public class RxEditText extends android.support.v7.widget.AppCompatEditText implements RxHelper<RxTextViewHelper> {

    private RxTextViewHelper mHelper;

    public RxEditText(Context context) {
        this(context, null);
    }

    public RxEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = new RxTextViewHelper(context, this, attrs);

        setClickable(true);
    }

    @Override
    public RxTextViewHelper getHelper() {
        return mHelper;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mHelper != null) {
            mHelper.setEnabled(enabled);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mHelper != null) {
            mHelper.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


}
