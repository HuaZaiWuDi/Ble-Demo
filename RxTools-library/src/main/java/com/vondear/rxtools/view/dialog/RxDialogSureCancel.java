package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.view.layout.RxTextView;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogSureCancel extends RxDialog {

    private TextView mTvTitle;
    private TextView mTvContent;
    private RxTextView mTvSure;
    private RxTextView mTvCancel;
    private ImageView img_close;

    public RxDialogSureCancel setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public RxDialogSureCancel setContent(String content) {
        this.mTvContent.setText(content);
        return this;
    }

    public TextView getTvContent() {
        return mTvContent;
    }

    public TextView getTvSure() {
        return mTvSure;
    }

    public RxDialogSureCancel setSure(String strSure) {
        this.mTvSure.setText(strSure);
        return this;
    }

    public RxDialogSureCancel setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
        return this;
    }

    public RxDialogSureCancel setSureBgColor(@ColorInt int colorSure) {
        mTvSure.getHelper().setBorderColorNormal(colorSure);
        return this;
    }

    public RxDialogSureCancel setCancelBgColor(@ColorInt int colorCancel) {
        mTvCancel.getHelper().setBorderColorNormal(colorCancel);
        return this;
    }

    public RxDialogSureCancel showImgClose(boolean isShow) {
        img_close.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public TextView getTvCancel() {
        return mTvCancel;
    }


    public ImageView getImg_close() {
        return img_close;
    }

    public RxDialogSureCancel setSureListener(final View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureListener != null) {
                    sureListener.onClick(v);
                    dismiss();
                }
            }
        });
        return this;
    }

    public RxDialogSureCancel setCancelListener(final View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                    dismiss();
                }
            }
        });
        return this;
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure_false, null);
        mTvSure = dialog_view.findViewById(R.id.tv_sure);
        mTvCancel = dialog_view.findViewById(R.id.tv_cancel);
        mTvContent = dialog_view.findViewById(R.id.tv_content);
        mTvContent.setTextIsSelectable(true);
        setCanceledOnTouchOutside(false);
        mTvTitle = dialog_view.findViewById(R.id.tv_title);
        img_close = dialog_view.findViewById(R.id.iv_close);
        setContentView(dialog_view);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public RxDialogSureCancel(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSureCancel(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSureCancel(Context context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel(Activity context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }


}
