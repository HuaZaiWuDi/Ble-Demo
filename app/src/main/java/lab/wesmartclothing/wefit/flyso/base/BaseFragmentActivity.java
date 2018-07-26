package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.vondear.rxtools.activity.RxActivityUtils;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.HeatFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SmartClothingFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightRecordFragment;

/**
 * Created by cgspine on 2018/1/7.
 */

public class BaseFragmentActivity extends QMUIFragmentActivity {


    public Context mContext;
    public Activity mActivity;


    @Override
    protected int getContextViewId() {
        return R.id.qmuidemo;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //屏幕沉浸
//        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏

        mContext = this;
        mActivity = this;
        RxActivityUtils.addActivity(this);

        ScreenAdapter.setCustomDensity(this);


        if (savedInstanceState == null) {
            String fragmentName = getIntent().getStringExtra(Key.BUNDLE_FRAGMENT);

            if (fragmentName.equals(HeatFragment.class.getSimpleName()))
                startFragment(HeatFragment.getInstance());
            else if (fragmentName.equals(WeightRecordFragment.class.getSimpleName()))
                startFragment(WeightRecordFragment.getInstance());
            else if (fragmentName.equals(SmartClothingFragment.class.getSimpleName()))
                startFragment(SmartClothingFragment.getInstance());
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
