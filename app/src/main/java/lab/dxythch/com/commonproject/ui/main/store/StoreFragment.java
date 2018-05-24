package lab.dxythch.com.commonproject.ui.main.store;

import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseWebFragment;
import lab.dxythch.com.commonproject.netserivce.ServiceAPI;

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

    }


    @AfterViews
    public void initView() {
        initWebView(parent, ServiceAPI.Store_Addr);
    }

}
