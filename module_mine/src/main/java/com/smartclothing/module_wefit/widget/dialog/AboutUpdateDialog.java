package com.smartclothing.module_wefit.widget.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.widget.progressbar.UpdateProgressBar;

/**
 * @author zzp
 */
public class AboutUpdateDialog extends BaseDialog implements View.OnClickListener {
    private QMUIRelativeLayout frContent;
    private LinearLayout ll_close;
    private TextView tv_update_tip;
    private UpdateProgressBar progressbar;

    public AboutUpdateDialog(Context context, double p) {
        super(context);
        super.setContentView(R.layout.dialog_recharge_tip);

        Window window = getWindow();
        WindowManager.LayoutParams attributesParams = window.getAttributes();
        attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributesParams.dimAmount = 0.4f;
        setCanceledOnTouchOutside(false);
        int screenWidth = window.getWindowManager().getDefaultDisplay().getWidth();
        int windowWidth = (int) (screenWidth * p);
        window.setLayout(windowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        ll_close = findViewById(R.id.ll_close);
        frContent = findViewById(R.id.fr_content);
        tv_update_tip = findViewById(R.id.tv_update_tip);
        progressbar = findViewById(R.id.progressbar);

        progressbar.setProgress(100f);
        ll_close.setOnClickListener(this);
    }

    @Override
    public void setContentView(View view) {
        frContent.removeAllViews();
        frContent.addView(view, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams lp) {
        frContent.removeAllViews();
        frContent.addView(view, lp);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(getContext(), layoutResID, null);
        setContentView(view);
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ll_close) {
            dismiss();

        } else {
        }
    }

    public void setProgressWithFloat(float progress) {
        progressbar.setProgressWithoutAnim(progress);
    }

    public void setTip(String tip) {
        tv_update_tip.setText(tip);
    }

    public void setProgress() {
        progressbar.setProgress(100);
    }

    public UpdateProgressBar getProgressBar() {
        return progressbar;
    }

}