package lab.wesmartclothing.wefit.flyso.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.fragment.FragmentUtils;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebActivity;
import lab.wesmartclothing.wefit.flyso.base.BaseWebTFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;

public class WebTitleActivity extends BaseWebActivity {
    @BindView(R.id.parent)
    RelativeLayout mParent;
    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;


    public static void startWebActivity(Context context, String title, String url) {
        startWebActivity(context, title, url, false);
    }

    /**
     * @param context
     * @param title
     * @param url
     * @param isCover 是否被title覆盖
     */
    public static void startWebActivity(Context context, String title, String url, boolean isCover) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_TITLE, title);
        bundle.putString(Key.BUNDLE_WEB_URL, url);
        bundle.putBoolean(Key.BUNDLE_DATA, isCover);
        RxActivityUtils.skipActivity(context, WebTitleActivity.class, bundle);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_web_title;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initWebView(mParent);
        initTopBar();
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        mQMUIAppBarLayout.setTitle(bundle.getString(Key.BUNDLE_TITLE));

        boolean isCover = bundle.getBoolean(Key.BUNDLE_DATA);
        if (!isCover) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mParent.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.QMUIAppBarLayout);
            mParent.setLayoutParams(params);
        }
        FragmentUtils.replace(getSupportFragmentManager(), BaseWebTFragment.getInstance(bundle.getString(Key.BUNDLE_WEB_URL)), R.id.parent);
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> {
            RxActivityUtils.finishActivity();
        });
    }
}
