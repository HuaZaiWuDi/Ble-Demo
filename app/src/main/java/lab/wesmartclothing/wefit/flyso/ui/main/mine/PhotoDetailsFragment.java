package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.imagepicker.bean.ImageItem;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.view.recyclerview.banner.BannerLayoutManager;
import com.vondear.rxtools.view.scaleimage.ImageSource;
import com.vondear.rxtools.view.scaleimage.RxScaleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/8/16.
 */
public class PhotoDetailsFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.banner)
    RecyclerView banner;


    private BaseQuickAdapter adapter;
    private ArrayList<ImageItem> list;
    private int current = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo_details);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        initTopBar();
        initRecycler();
    }

    private void initRecycler() {
        Bundle bundle = getIntent().getExtras();
        list = bundle.getParcelableArrayList(Key.BUNDLE_DATA);

        mQMUIAppBarLayout.setTitle(1 + "/" + list.size());
        BannerLayoutManager bannerLayoutManager = new BannerLayoutManager(mContext, banner, false, list.size());
        banner.setLayoutManager(bannerLayoutManager);
        adapter = new BaseQuickAdapter<ImageItem, BaseViewHolder>(R.layout.item_photo, list) {
            @Override
            protected void convert(BaseViewHolder helper, ImageItem item) {
                ((RxScaleImageView) helper.getView(R.id.iv_scale)).setImage(ImageSource.uri(item.path));
            }
        };
        banner.setAdapter(adapter);
        bannerLayoutManager.setOnSelectedViewListener(new BannerLayoutManager.OnSelectedViewListener() {
            @Override
            public void onSelectedView(View view, int position) {
                mQMUIAppBarLayout.setTitle((position + 1) + "/" + list.size());
                current = position;
            }
        });
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                bundle.putParcelableArrayList(Key.BUNDLE_DATA, list);
                intent.putExtras(bundle);
                setResult(UserInfofragment.RESULT_CODE, intent);
                finish();
            }
        });

        mQMUIAppBarLayout.addRightImageButton(R.mipmap.icon_delete_write, R.id.tv_delete)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.remove(current);
                        mQMUIAppBarLayout.setTitle((current == 0 ? current + 1 : current) + "/" + list.size());
                        if (adapter.getData().size() == 0) {
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent();
                            bundle.putParcelableArrayList(Key.BUNDLE_DATA, list);
                            intent.putExtras(bundle);
                            setResult(UserInfofragment.RESULT_CODE, intent);
                            finish();
                        }
                    }
                });
    }

}
