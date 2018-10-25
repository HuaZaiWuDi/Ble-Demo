package lab.wesmartclothing.wefit.flyso.ui.main.record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;

/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.main.record
 * @FileName SlimmingFragment
 * @Date 2018/10/22 16:50
 * @Author JACK
 * @Describe TODO记录界面
 * @Project Android_WeFit_2.0
 */
public class SlimmingFragment extends BaseAcFragment {


    public static SlimmingFragment newInstance() {
        Bundle args = new Bundle();
        SlimmingFragment fragment = new SlimmingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_slimming, null);
        return view;
    }
}
