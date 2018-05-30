package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.os.Handler;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Created by jk on 2018/5/28.
 */
public class TipDialog extends QMUITipDialog {

    private Handler mHandler = new Handler();

    public TipDialog(Context context) {
        super(context);

    }

    public TipDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setDuration(long timeOut) {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isShowing()) {
                    dismiss();
                }
            }
        }, timeOut);
    }

}
