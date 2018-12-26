package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.layout.RxTextView;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogSure extends RxDialog {

    private TextView mTvTitle;
    private TextView mTvContent;
    private RxTextView mTvSure;
    private ImageView iv_close;


    public ImageView getIv_close() {
        return iv_close;
    }


    public TextView getTvTitle() {
        return mTvTitle;
    }

    public TextView getTvSure() {
        return mTvSure;
    }

    public TextView getTvContent() {
        return mTvContent;
    }


    public RxDialogSure setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public RxDialogSure setSureBgColor(@ColorInt int colorSure) {
        mTvSure.getHelper().setBorderColorNormal(colorSure);
        return this;
    }

    public RxDialogSure setSure(String content) {
        mTvSure.setText(content);
        return this;
    }

    public RxDialogSure setSureListener(final View.OnClickListener sureListener) {
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

    public RxDialogSure setContent(String str) {
        if (RxRegUtils.isURL(str)) {
            // 响应点击事件的话必须设置以下属性
            mTvContent.setMovementMethod(LinkMovementMethod.getInstance());
            mTvContent.setText(RxTextUtils.getBuilder("").setBold().append(str).setUrl(str).create());//当内容为网址的时候，内容变为可点击
        } else {
            mTvContent.setText(str);
        }
        return this;
    }


    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure, null);
        mTvSure = dialog_view.findViewById(R.id.tv_sure);
        mTvTitle = (TextView) dialog_view.findViewById(R.id.tv_title);
        mTvTitle.setTextIsSelectable(true);
        mTvContent = (TextView) dialog_view.findViewById(R.id.tv_content);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvContent.setTextIsSelectable(true);
        iv_close = dialog_view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(dialog_view);
    }

    public RxDialogSure(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSure(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSure(Context context) {
        super(context);
        initView();
    }

    public RxDialogSure(Activity context) {
        super(context);
        initView();
    }

    public RxDialogSure(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

}
