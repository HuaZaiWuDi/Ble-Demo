package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created icon_hide_password jk on 2018/5/28.
 */
public class TipDialog {
    private final long defaultTimeout = 10 * 1000;
    private QMUITipDialog tipDialog;
    private Context mContext;

    public TipDialog(Context mContext) {
        this.mContext = mContext;
    }


    public QMUITipDialog getTipDialog() {
        return tipDialog;
    }

    private MyTimer timeoutTimer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            if (tipDialog != null && tipDialog.isShowing()) {
                tipDialog.dismiss();
            }
        }
    });

    public void setDuration(long timeOut) {
        if (timeOut <= 0) return;
        timeoutTimer.setPeriodAndDelay(0, timeOut);
        timeoutTimer.startTimer();
    }

    public void show(long timeOut) {
        dismiss();
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(mContext.getString(R.string.tv_loading))
                .create();
        setDuration(timeOut);
        tipDialog.show();
    }

    public void show(String text, long timeOut) {
        dismiss();
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(text)
                .create();
        setDuration(timeOut);
        tipDialog.show();
    }

    public void show() {
        dismiss();
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(mContext.getString(R.string.tv_loading))
                .create();
        setDuration(defaultTimeout);
        tipDialog.show();
    }

    public void showSuccess(String content, long timeOut) {
        dismiss();
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(content)
                .create();
        tipDialog.setCanceledOnTouchOutside(true);
        setDuration(timeOut);
        tipDialog.show();
    }

    public void showFail(String content, long timeOut) {
        dismiss();
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(content)
                .create();
        tipDialog.setCanceledOnTouchOutside(true);
        setDuration(timeOut);
        tipDialog.show();
    }

    public void showInfo(String content, long timeOut) {
        dismiss();
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(content)
                .create();
        tipDialog.setCanceledOnTouchOutside(true);
        setDuration(timeOut);
        tipDialog.show();
    }

    public void showCustom(@LayoutRes int resId) {
        dismiss();
        tipDialog = new QMUITipDialog.CustomBuilder(mContext)
                .setContent(resId)
                .create();
        tipDialog.setCanceledOnTouchOutside(true);
        setDuration(defaultTimeout);
        tipDialog.show();
    }

    public void dismiss() {
        if (tipDialog == null) return;
        if (timeoutTimer != null) timeoutTimer.stopTimer();
        if (tipDialog.isShowing())
            tipDialog.dismiss();
    }

}
