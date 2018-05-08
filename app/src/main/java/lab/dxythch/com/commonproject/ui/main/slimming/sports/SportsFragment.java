package lab.dxythch.com.commonproject.ui.main.slimming.sports;

import com.vondear.rxtools.fragment.FragmentLazy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import lab.dxythch.com.commonproject.R;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_sports)
public class SportsFragment extends FragmentLazy {


    public static SportsFragment getInstance() {
        return new SportsFragment_();
    }

    @Override
    @AfterViews
    public void initData() {

    }

    @AfterViews
    void initView() {
    }

}
