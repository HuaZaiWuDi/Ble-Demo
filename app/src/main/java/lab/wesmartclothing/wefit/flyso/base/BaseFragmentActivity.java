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

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseFragmentActivity extends QMUIFragmentActivity {


    public Context mContext;
    public Activity mActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        //屏幕沉浸
//        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        super.onCreate(savedInstanceState);

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mContext = this;
        mActivity = this;
        RxActivityUtils.addActivity(this);

        ScreenAdapter.setCustomDensity(this);

    }

    @Override
    protected void onDestroy() {
        RxActivityUtils.removeActivity(this);
        super.onDestroy();
        mContext = null;
        mActivity = null;
    }


}
