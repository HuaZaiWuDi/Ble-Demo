package lab.wesmartclothing.wefit.flyso.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;

public class WebTitleActivity extends BaseWebActivity {
    @BindView(R.id.parent)
    RelativeLayout mParent;
    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_title);
        ButterKnife.bind(this);

        initWebView(mParent);

        initTopBar();
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityUtils.finishActivity();
            }
        });
        mQMUIAppBarLayout.setTitle(getIntent().getStringExtra(Key.BUNDLE_TITLE));
    }


    @Nullable
    @Override
    protected String getUrl() {
        return getIntent().getStringExtra(Key.BUNDLE_WEB_URL);
    }
}
