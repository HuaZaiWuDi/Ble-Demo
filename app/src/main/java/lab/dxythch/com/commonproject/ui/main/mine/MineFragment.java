package lab.dxythch.com.commonproject.ui.main.mine;

import com.vondear.rxtools.fragment.FragmentLazy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import lab.dxythch.com.commonproject.R;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_mine)
public class MineFragment extends FragmentLazy {

    public static MineFragment getInstance() {
        return new MineFragment_();
    }


    @Override
    public void initData() {

    }

    @AfterViews
    void initView() {
    }

}
