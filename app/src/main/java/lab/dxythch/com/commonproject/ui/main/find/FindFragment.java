package lab.dxythch.com.commonproject.ui.main.find;

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
@EFragment(R.layout.fragment_find)
public class FindFragment extends BaseWebFragment {

    public static FindFragment getInstance() {
        return new FindFragment_();
    }

    @Override
    public void initData() {

    }


    @ViewById
    RelativeLayout parent;


    @Override
    @AfterViews
    public void initView() {
        initWebView(parent, ServiceAPI.FIND_Addr);

    }




}
