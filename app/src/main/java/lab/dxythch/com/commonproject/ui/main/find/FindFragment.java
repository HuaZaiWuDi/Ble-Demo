package lab.dxythch.com.commonproject.ui.main.find;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseFragment;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_find)
public class FindFragment extends BaseFragment {

    public static FindFragment getInstance() {
        return new FindFragment_();
    }

    @Override
    public void initData() {

    }

    @AfterViews
    public void initView() {

    }
}
