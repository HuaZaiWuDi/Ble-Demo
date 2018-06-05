package lab.wesmartclothing.wefit.flyso.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;

public class WebTitleActivity extends BaseWebActivity {
    RelativeLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_title);
        //屏幕沉浸
        StatusBarUtils.from(this).setStatusBarColor(getResources().getColor(R.color.colorTheme)).process();
        TextView tv_title = findViewById(R.id.tv_title);
        LinearLayout iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        parent = findViewById(R.id.parent);

        initWebView(parent);

        String extra = getIntent().getStringExtra(Key.BUNDLE_TITLE);
        if (extra != null)
            tv_title.setText(extra);
    }

    @Override
    public void initView() {

    }

    @Nullable
    @Override
    protected String getUrl() {
        return getIntent().getStringExtra(Key.BUNDLE_WEB_URL);
    }
}
