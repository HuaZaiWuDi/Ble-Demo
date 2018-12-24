package lab.wesmartclothing.wefit.flyso.ui.toolui;

import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.view.layout.RxTextView;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;

public class Main2Activity extends BaseActivity {


    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.text)
    RxTextView mText;
    @BindView(R.id.container)
    LinearLayout mContainer;



    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main2;
    }


    private boolean f;

    @Override
    protected void initViews() {
        super.initViews();
//        RxTextUtils.getBuilder(getString(R.string.plan))
//                .setBackgroundColor(R.drawable.fade_write)
//                .into(mText);
////        mText.setTextColor(R.drawable.fade_write);
        mText.setMovementMethod(ScrollingMovementMethod.getInstance());


        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        mTopBar.setTitle(getString(R.string.appName) + " 健康报告");

        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f = !f;
                mText.getHelper().setTextGradientOpen(f);
            }
        });

    }

}
