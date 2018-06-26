package lab.wesmartclothing.wefit.flyso.ui.main.store;

import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.vondear.rxtools.utils.RxLogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebFragment;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_shore)
public class StoreFragment extends BaseWebFragment {

    public static StoreFragment getInstance() {
        return new StoreFragment_();
    }


    @ViewById
    RelativeLayout parent;

    @Override
    public void initData() {
        RxLogUtils.d("加载：【StoreFragment】");
    }


    @AfterViews
    public void initView() {
        initWebView(parent);
    }


    @Nullable
    @Override
    protected String getUrl() {
        return ServiceAPI.Store_Addr;
    }


}
