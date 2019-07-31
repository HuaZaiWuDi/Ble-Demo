package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.fragment.FragmentUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseShareActivity;
import lab.wesmartclothing.wefit.flyso.base.BaseWebTFragment;
import lab.wesmartclothing.wefit.flyso.entity.CollectBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;

public class CollectWebActivity extends BaseShareActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.parent)
    RelativeLayout mParent;

    private CollectBean.ListBean bean;


    @Override
    protected int layoutId() {
        return R.layout.activity_web_interactive;
    }


    @Override
    protected void initViews() {
        super.initViews();

        FragmentUtils.replace(getSupportFragmentManager(),
                BaseWebTFragment.getInstance(getIntent().getStringExtra(Key.BUNDLE_WEB_URL)), R.id.parent);

        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getWebView() != null && getWebView().canGoBack()) {
                            getWebView().goBack();
                        } else {
                            onBackPressed();
                        }
                    }
                });


        QMUIAlphaImageButton shareImg = mTopBar.addRightImageButton(R.mipmap.icon_share, R.id.img_share);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
        }
        shareImg.setOnClickListener(v -> {
            if (bean != null) {
                shareURL(bean.getCoverPicture(), bean.getArticleName(),
                        bean.getSummary(), getIntent().getStringExtra(Key.BUNDLE_WEB_URL));
            } else {
                RxToast.normal(getString(R.string.shareFail));
            }
        });
    }


    private WebView getWebView() {
        WebView mWebView = null;
        try {
            View frameLayout = FragmentUtils.findFragment(getSupportFragmentManager(), BaseWebTFragment.class)
                    .getView().findViewWithTag("webView");

            if (frameLayout instanceof WebView) {
                mWebView = (WebView) frameLayout;
            }
            return mWebView;
        } catch (Exception e) {
            RxLogUtils.e(e);
        }
        return mWebView;
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        bean = (CollectBean.ListBean) bundle.getSerializable(Key.BUNDLE_DATA);
        if (bean != null)
            mTopBar.setTitle(bean.getArticleName());
    }


}
