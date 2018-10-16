package com.vondear.rxtools.model.state;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * Created by ${Hankkin} on 2018/10/12.
 */

public class PageLayout extends FrameLayout {

    enum State {
        EMPTY_TYPE,
        LOADING_TYPE,
        ERROR_TYPE,
        CONTENT_TYPE,
        CUSTOM_TYPE
    }

    private View mLoading;
    private View mEmpty;
    private View mError;
    private View mContent;
    private View mCustom;
    private Context mContext;
    private State mCurrentState;

    public PageLayout(@NonNull Context context) {
        super(context);
    }

    public PageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void showView(final State state) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            changeView(state);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    changeView(state);
                }
            });
        }
    }

    private void changeView(State state) {
        mCurrentState = state;
        setViewGone();
        switch (state) {
            case LOADING_TYPE:
                if (mLoading != null)
                    mLoading.setVisibility(VISIBLE);
                break;
            case EMPTY_TYPE:
                if (mEmpty != null)
                    mEmpty.setVisibility(VISIBLE);
                break;
            case ERROR_TYPE:
                if (mError != null)
                    mError.setVisibility(VISIBLE);
                break;
            case CUSTOM_TYPE:
                if (mCustom != null)
                    mCustom.setVisibility(VISIBLE);
                break;
            case CONTENT_TYPE:
                mContent.setVisibility(VISIBLE);
                break;
        }
    }

    private void setViewGone() {
        if (mLoading != null)
            mLoading.setVisibility(GONE);
        if (mEmpty != null)
            mEmpty.setVisibility(GONE);
        if (mError != null)
            mError.setVisibility(GONE);
        mContent.setVisibility(GONE);
        if (mCustom != null) {
            mCustom.setVisibility(GONE);
        }
    }

    public View showLoading() {
        showView(State.LOADING_TYPE);
        return this.mLoading;
    }

    public View showError() {
        showView(State.ERROR_TYPE);
        return this.mError;
    }

    public View showEmpty() {
        showView(State.EMPTY_TYPE);
        return this.mEmpty;
    }

    public View hide() {
        showView(State.CONTENT_TYPE);
        return this.mContent;
    }

    public View showCustom() {
        showView(State.CUSTOM_TYPE);
        return this.mCustom;
    }


    public static class Builder {
        private PageLayout mPageLayout;
        private LayoutInflater mInflater;
        private Context mContext;
        private TextView mTvError;
        private OnRetryClickListener mOnRetryClickListener;

        public Builder(Context context) {
            mContext = context;
            this.mPageLayout = new PageLayout(mContext);
            this.mInflater = LayoutInflater.from(mContext);
        }


        /**
         * 设置loading布局
         */
        public Builder setLoading(int loadingId) {
            View loading = mInflater.inflate(loadingId, mPageLayout, false);
            mPageLayout.mLoading = loading;
            mPageLayout.addView(loading);
            return this;
        }

        /**
         * 设置loading布局
         */
        public Builder setLoading(View loadingView) {
            mPageLayout.mLoading = loadingView;
            mPageLayout.addView(loadingView);
            return this;
        }

        /**
         * 自定义错误布局
         * 默认样式，传入错误文案ID 如果不传入点击ID，默认点击全屏重新加载，及点击回调
         */
        public Builder setError(int errorId, int errorClickId, final OnRetryClickListener onRetryClickListener) {
            View error = mInflater.inflate(errorId, mPageLayout, false);
            mPageLayout.mError = error;
            mPageLayout.addView(error);
            if (errorClickId == 0) {
                error.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onRetryClickListener != null)
                            onRetryClickListener.onRetry();
                    }
                });
            } else {
                mTvError = error.findViewById(errorClickId);
                mTvError.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onRetryClickListener != null)
                            onRetryClickListener.onRetry();
                    }
                });
            }
            return this;
        }

        /**
         * 自定义错误布局
         * 设置前需手动初始化好View中各个事件
         */
        public Builder setError(View error) {
            mPageLayout.mError = error;
            mPageLayout.addView(error);
            return this;
        }

        /**
         * 自定义空布局
         */
        public Builder setEmpty(int empty) {
            View view = mInflater.inflate(empty, mPageLayout, false);
            mPageLayout.mEmpty = view;
            mPageLayout.addView(view);
            return this;
        }

        /**
         * 自定义空布局
         */
        public Builder setEmpty(View empty) {
            mPageLayout.mEmpty = empty;
            mPageLayout.addView(empty);
            return this;
        }

        /**
         * 自定义布局
         */
        public Builder setCustomView(View view) {
            mPageLayout.mCustom = view;
            mPageLayout.addView(view);
            return this;
        }

        /**
         * 自定义布局
         */
        public Builder setCustomView(int customId) {
            View view = mInflater.inflate(customId, mPageLayout, false);
            mPageLayout.mCustom = view;
            mPageLayout.addView(view);
            return this;
        }


        public Builder initPage(Object targetView) {
            ViewGroup content = null;
            if (targetView instanceof Activity) {
                mContext = (Context) targetView;
                content = ((Activity) targetView).findViewById(android.R.id.content);
            } else if (targetView instanceof Fragment) {
                mContext = ((Fragment) targetView).getActivity();
                content = (ViewGroup) ((Fragment) targetView).getView().getParent();
            } else if (targetView instanceof View) {
                mContext = ((View) targetView).getContext();
                content = (ViewGroup) ((View) targetView).getParent();
            }
            if (content != null) {
                int childCount = content.getChildCount();
                int index = 0;
                View oldContent = null;
                if (targetView instanceof View) {
                    oldContent = (View) targetView;
                    for (int i = 0; i < childCount; i++) {
                        index = i;
                        break;
                    }
                } else {
                    oldContent = content.getChildAt(0);
                }
                mPageLayout.mContent = oldContent;
                mPageLayout.removeAllViews();
                content.removeView(oldContent);
                ViewGroup.LayoutParams params = oldContent.getLayoutParams();
                content.addView(mPageLayout, index, params);
                mPageLayout.addView(oldContent);
            }
            return this;
        }

        public Builder setOnRetryListener(OnRetryClickListener onRetryListener) {
            this.mOnRetryClickListener = onRetryListener;
            return this;
        }

        public PageLayout create() {
            mPageLayout.hide();
            return mPageLayout;
        }

    }

    public interface OnRetryClickListener {
        void onRetry();
    }
}