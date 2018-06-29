package lab.wesmartclothing.wefit.flyso.view;

import android.os.Handler;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Created by jk on 2018/5/28.
 */
public class TipDialog {
    private final long defaultTimeout = 10 * 1000;
    private QMUITipDialog tipDialog;
    private Handler mHandler = new Handler();

    public TipDialog(QMUITipDialog tipDialog) {
        this.tipDialog = tipDialog;
    }


    public void setDuration(long timeOut) {
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
        if (tipDialog == null) return;
        if (timeOut > 0)
            setDuration(timeOut);
        tipDialog.show();
    }

    public void show() {
        if (tipDialog == null) return;
        setDuration(defaultTimeout);
        tipDialog.show();
    }

    public void dismiss() {
        if (tipDialog == null) return;
        mHandler.removeCallbacksAndMessages(null);
        tipDialog.dismiss();
    }

}
