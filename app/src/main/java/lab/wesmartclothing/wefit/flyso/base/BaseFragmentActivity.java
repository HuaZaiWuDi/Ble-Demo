package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.vondear.rxtools.activity.RxActivityUtils;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.HeatFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SportsFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightFragment;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;

/**
 * Created by cgspine on 2018/1/7.
 */

public class BaseFragmentActivity extends QMUIFragmentActivity {


    public Context mContext;
    public Activity mActivity;


    public static BaseFragmentActivity getInstance() {
        return new BaseFragmentActivity();
    }

    @Override
    protected int getContextViewId() {
        return R.id.qmuidemo;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //屏幕沉浸
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;
        RxActivityUtils.addActivity(this);

        ScreenAdapter.setCustomDensity(this);


        if (savedInstanceState == null) {
            String fragmentName = getIntent().getStringExtra(Key.BUNDLE_FRAGMENT);

            if (fragmentName.equals(HeatFragment.class.getSimpleName()))
                startFragment(HeatFragment.getInstance());
            else if (fragmentName.equals(WeightFragment.class.getSimpleName()))
                startFragment(WeightFragment.getInstance());
            else if (fragmentName.equals(SportsFragment.class.getSimpleName()))
                startFragment(SportsFragment.getInstance());
        }
    }


    @Override
    protected void onDestroy() {
        RxActivityUtils.removeActivity(this);
        super.onDestroy();
        mContext = null;
        mActivity = null;
    }


}
