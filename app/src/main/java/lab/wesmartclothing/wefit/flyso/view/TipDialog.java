package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.os.Handler;

import com.kongzue.dialog.v2.WaitDialog;
import com.vondear.rxtools.activity.RxActivityUtils;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created icon_hide_password jk on 2018/5/28.
 */
public class TipDialog {
    private final long defaultTimeout = 15 * 1000;
    private WaitDialog dialog;
    private Context mContext;
    private Handler timeoutHandler = new Handler();

    public TipDialog(Context mContext) {
        this.mContext = mContext;
    }

    public TipDialog() {
        this.mContext = RxActivityUtils.currentActivity();
    }

    public WaitDialog getTipDialog() {
        return dialog;
    }


    public void setDuration(long timeOut) {
        if (timeOut <= 0) return;
        timeoutHandler.removeCallbacksAndMessages(null);
        timeoutHandler.postDelayed(() -> {
            if (dialog != null) {
                dialog.doDismiss();
            }
        }, timeOut);
    }

    public void show(long timeOut) {
        dismiss();
        dialog = WaitDialog.show(mContext, mContext.getString(R.string.tv_loading));
        setDuration(timeOut);
    }

    public void show(String text, long timeOut) {
        dismiss();
        dialog = WaitDialog.show(mContext, text);
        setDuration(timeOut);
    }

    public void show() {
        dismiss();
        dialog = WaitDialog.show(mContext, mContext.getString(R.string.tv_loading));
        setDuration(defaultTimeout);
    }

    public void showSuccess(String content) {
        com.kongzue.dialog.v2.TipDialog.show(mContext, content, com.kongzue.dialog.v2.TipDialog.TYPE_FINISH);

    }

    public void showFail(String content, long timeOut) {
        com.kongzue.dialog.v2.TipDialog.show(mContext, content, (int) timeOut, com.kongzue.dialog.v2.TipDialog.TYPE_ERROR);
    }

    public void showInfo(String content, long timeOut) {
        com.kongzue.dialog.v2.TipDialog.show(mContext, content, (int) timeOut, com.kongzue.dialog.v2.TipDialog.TYPE_WARNING);
    }

    public void dismiss() {
        timeoutHandler.removeCallbacksAndMessages(null);
        if (dialog != null)
            dialog.doDismiss();
    }

}
