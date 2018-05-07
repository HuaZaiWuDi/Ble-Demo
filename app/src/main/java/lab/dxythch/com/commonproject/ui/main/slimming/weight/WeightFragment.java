package lab.dxythch.com.commonproject.ui.main.slimming.weight;

import com.vondear.rxtools.fragment.FragmentLazy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import lab.dxythch.com.commonproject.R;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_weight)
public class WeightFragment extends FragmentLazy {

    public static WeightFragment getInstance() {
        return new WeightFragment_();
    }

    @Override
    @AfterViews
    public void initData() {

    }
}
