package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseFragmentActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.MainFragment;

/**
 * Created by jk on 2018/8/2.
 */
public class BaseMeActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getContextViewId() {
        return R.id.main_container;
    }


}