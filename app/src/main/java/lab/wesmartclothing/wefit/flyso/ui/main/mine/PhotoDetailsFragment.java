package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lzy.imagepicker.bean.ImageItem;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.view.scaleimage.ImageSource;
import com.vondear.rxtools.view.scaleimage.RxScaleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/8/16.
 */
public class PhotoDetailsFragment extends BaseAcFragment {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    public static QMUIFragment getInstance() {
        return new PhotoDetailsFragment();
    }

    private List<RxScaleImageView> mViews = new ArrayList<>();


    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_photo_details, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();

        Bundle bundle = getArguments();
        ArrayList<ImageItem> list = bundle.getParcelableArrayList(Key.BUNDLE_DATA);

        for (int i = 0; i < list.size(); i++) {
            RxScaleImageView view = new RxScaleImageView(mContext);
            LinearLayout.MarginLayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.MarginLayoutParams.MATCH_PARENT,
                    LinearLayout.MarginLayoutParams.MATCH_PARENT
            );
            view.setLayoutParams(params);
            view.setImage(ImageSource.uri(list.get(i).path));
        }
        mViewPager.setAdapter(imageAdapter);

    }


    final PagerAdapter imageAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RxScaleImageView view = mViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

    };


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("1/3");
        mQMUIAppBarLayout.addRightImageButton(R.mipmap.icon_delete_write, R.id.tv_delete)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }

}
