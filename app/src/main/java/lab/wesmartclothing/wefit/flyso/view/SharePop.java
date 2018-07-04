package lab.wesmartclothing.wefit.flyso.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created icon_hide_password jk on 2018/5/28.
 */
public class SharePop extends PopupWindow {
    private Activity mContext;


    public static final int SHARE_WX = 0;
    public static final int SHARE_PENGYOUYUAN = 1;

    public SharePop(Activity context) {
        super(context);
        this.mContext = context;
    }

    public void initPop() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_share, null, false);
        LinearLayout textLayout = view.findViewById(R.id.Linear_text);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);//解决popup被输入法挡住的问题
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setContentView(view);
        this.setAnimationStyle(R.style.popAnim);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(mContext, 1.0f);
            }
        });
        LinearLayout wx = view.findViewById(R.id.Linear_wx);
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPop();
                if (mShareLinstener != null)
                    mShareLinstener.shareType(SHARE_WX);
            }
        });

        LinearLayout pyq = view.findViewById(R.id.Linear_pengyouquan);
        pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPop();
                if (mShareLinstener != null)
                    mShareLinstener.shareType(SHARE_PENGYOUYUAN);
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setBackgroundAlpha(mContext, 0.6f);
    }


    public void dismissPop() {
        dismiss();
        setBackgroundAlpha(mContext, 1.0f);
    }


    public interface ShareLinstener {
        void shareType(int type);
    }

    private ShareLinstener mShareLinstener;

    public void setShareLinstener(ShareLinstener shareLinstener) {
        mShareLinstener = shareLinstener;
    }

    //屏幕主题变暗
    private void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    private int sp2px(Context context, int sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.getResources().getDisplayMetrics());
    }
}
