package lab.wesmartclothing.wefit.flyso.ui.main.store;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.arch.QMUIFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebFragment;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public class StoreFragment extends BaseWebFragment {

    @BindView(R.id.img_no_data)
    ImageView mImgNoData;
    @BindView(R.id.layout_web_error)
    RelativeLayout mLayoutWebError;
    @BindView(R.id.parent)
    RelativeLayout parent;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new StoreFragment();
    }


    public void initView() {
        initWebView(parent);
    }


    @Nullable
    @Override
    protected String getUrl() {
        return ServiceAPI.Store_Addr;
    }


    @Override
    protected View onCreateView() {
        View view = View.inflate(mContext, R.layout.fragment_shore, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

}
