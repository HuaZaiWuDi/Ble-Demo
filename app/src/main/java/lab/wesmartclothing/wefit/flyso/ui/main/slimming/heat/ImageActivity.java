package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat;

import android.os.Bundle;
import android.widget.ImageView;

import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;


public class ImageActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.img_bg)
    ImageView mImgBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_image;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(view -> onBackPressed());
    }
}
