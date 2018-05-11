package lab.dxythch.com.commonproject.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.dxythch.com.commonproject.R;

/**
 * Created by 华 on 2017/5/2.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;

    public QMUITipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RxActivityUtils.addActivity(this);


        initDialog();
    }

    private void initDialog() {
        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
    }

    public void loadCricle(String img_url, @NonNull ImageView img) {
        Glide.with(mContext)
                .load(img_url)
                .error(R.mipmap.ic_launcher)
                .bitmapTransform(new CropCircleTransformation(mContext))//圆角图片
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img);
    }


    public abstract void initView();

    @Override
    protected void onDestroy() {
        tipDialog.dismiss();
        tipDialog = null;
        super.onDestroy();
        mContext = null;
    }

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
