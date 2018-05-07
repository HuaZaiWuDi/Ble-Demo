package lab.dxythch.com.commonproject.ui.main.store;

import com.vondear.rxtools.fragment.FragmentLazy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import lab.dxythch.com.commonproject.R;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_shore)
public class StoreFragment extends FragmentLazy {

    public static StoreFragment getInstance() {
        return new StoreFragment_();
    }


    @Override
    @AfterViews
    public void initData() {

    }
}
