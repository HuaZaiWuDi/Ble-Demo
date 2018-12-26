package lab.wesmartclothing.wefit.flyso.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;

public class WebTitleActivity extends BaseWebActivity {
    @BindView(R.id.parent)
    RelativeLayout mParent;
    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;


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
        mQMUIAppBarLayout.setTitle(getIntent().getStringExtra(Key.BUNDLE_TITLE));
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityUtils.finishActivity();
            }
        });
    }


    @Nullable
    @Override
    protected String getUrl() {
        return getIntent().getStringExtra(Key.BUNDLE_WEB_URL);
    }
}
