package lab.dxythch.com.commonproject.ui.main.find;

import com.vondear.rxtools.fragment.FragmentLazy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import lab.dxythch.com.commonproject.R;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_find)
public class FindFragment extends FragmentLazy {

    public static FindFragment getInstance() {
        return new FindFragment_();
    }

    @Override
    @AfterViews
    public void initData() {

    }
}
