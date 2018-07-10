package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.LayoutRes;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created icon_hide_password jk on 2018/5/28.
 */
public class TipDialog {
    private final long defaultTimeout = 10 * 1000;
    private QMUITipDialog tipDialog;
    private Handler mHandler = new Handler();
    private Context mContext;
    private String Content;

    public TipDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void setContent(String content) {
        Content = content;
    }


    public void setDuration(long timeOut) {
        dismiss();
        if (timeOut <= 0) return;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
            }
        }, timeOut);
    }

    public void show(long timeOut) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(mContext.getString(R.string.tv_loading))
                .create();
        setDuration(timeOut);
        tipDialog.show();
    }

    public void show() {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(mContext.getString(R.string.tv_loading))
                .create();
        setDuration(defaultTimeout);
        tipDialog.show();
    }

    public void showSuccess(String content) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(content)
                .create();
        setDuration(defaultTimeout);
        tipDialog.show();
    }

    public void showFail(String content) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(content)
                .create();
        setDuration(defaultTimeout);
        tipDialog.show();
    }

    public void showInfo(String content) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(content)
                .create();
        setDuration(defaultTimeout);
        tipDialog.show();
    }

    public void showCustom(@LayoutRes int resId) {
        tipDialog = new QMUITipDialog.CustomBuilder(mContext)
                .setContent(resId)
                .create();
        setDuration(defaultTimeout);
        tipDialog.show();
    }

    public void dismiss() {
        if (tipDialog == null) return;
        mHandler.removeCallbacksAndMessages(null);
        if (tipDialog.isShowing())
            tipDialog.dismiss();
    }

}
