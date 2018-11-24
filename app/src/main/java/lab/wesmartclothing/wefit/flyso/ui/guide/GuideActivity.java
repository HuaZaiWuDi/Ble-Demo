package lab.wesmartclothing.wefit.flyso.ui.guide;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.BleService;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.btn_goto)
    QMUIRoundButton mBtnGoto;

    private ArrayList<Integer> mImageItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        mImageItems.clear();
        for (int i = 0; i < 3; i++) {
            mImageItems.add(R.drawable.guide_1);
            mImageItems.add(R.drawable.guide_2);
            mImageItems.add(R.drawable.guide_3);
        }
        mViewpager.setOffscreenPageLimit(2);
        mViewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
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
                return photoView;
            }
        });
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBtnGoto.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.btn_goto)
    public void onViewClicked() {
        initPermissions();
    }

    private void initPermissions() {
        new RxPermissions(mActivity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        startService(new Intent(mContext, BleService.class));
                        RxActivityUtils.skipActivityAndFinish(mContext, LoginRegisterActivity.class);
                    }
                });
    }

}
