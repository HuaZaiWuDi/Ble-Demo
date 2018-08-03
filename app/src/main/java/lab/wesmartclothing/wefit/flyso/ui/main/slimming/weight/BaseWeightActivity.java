package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.os.Bundle;
import android.support.annotation.Nullable;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseFragmentActivity;

/**
 * Created by jk on 2018/8/2.
 */
public class BaseWeightActivity extends BaseFragmentActivity {
    @Override
    protected int getContextViewId() {
        return R.id.qmuidemo;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            startFragment(WeightRecordFragment.getInstance());
        }
    }
}
