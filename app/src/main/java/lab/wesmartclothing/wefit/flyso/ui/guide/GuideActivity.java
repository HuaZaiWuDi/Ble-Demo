package lab.wesmartclothing.wefit.flyso.ui.guide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.StatusBarUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.parent)
    RelativeLayout mParent;

    private ArrayList<Integer> mImageItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        StatusBarUtils.from(this).setHindStatusBar(true).process();
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        mImageItems.clear();
        mImageItems.add(R.mipmap.guide_1);
        mImageItems.add(R.mipmap.guide_2);
        mImageItems.add(R.mipmap.guide_3);
        if (BuildConfig.Wesmart)
            mImageItems.add(R.mipmap.guide_4);
        mViewpager.setOffscreenPageLimit(2);
        mViewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mImageItems.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView photoView = new ImageView(mActivity);
                RelativeLayout.MarginLayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.MarginLayoutParams.MATCH_PARENT,
                        RelativeLayout.MarginLayoutParams.MATCH_PARENT
                );
                photoView.setLayoutParams(params);
                MyAPP.getImageLoader().displayImage(mActivity, mImageItems.get(position), 0, photoView);
                container.addView(photoView);
                if (position == mImageItems.size() - 1) {
                    photoView.setOnClickListener(view ->
                            RxActivityUtils.skipActivityAndFinish(mContext, LoginRegisterActivity.class));
                }
                return photoView;

            }
        });
    }


    @OnClick(R.id.parent)
    public void onViewClicked() {

    }
}
