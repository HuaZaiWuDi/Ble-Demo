package lab.dxythch.com.commonproject.ui.main.store;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseFragment;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_shore)
public class StoreFragment extends BaseFragment {

    public static StoreFragment getInstance() {
        return new StoreFragment_();
    }


    @Override
    public void initData() {

    }

    @Override
    @AfterViews
    public void initView() {

    }


}
