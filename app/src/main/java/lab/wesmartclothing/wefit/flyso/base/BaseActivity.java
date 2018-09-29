package lab.wesmartclothing.wefit.flyso.base;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.concurrent.TimeUnit;

import io.reactivex.subjects.BehaviorSubject;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.ble.BleService;
import lab.wesmartclothing.wefit.flyso.rxbus.OpenAddWeight;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SportingFragment_;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightAddFragment;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;
import lab.wesmartclothing.wefit.netlib.utils.LifeCycleEvent;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;

/**
 * Created icon_hide_password 华 on 2017/5/2.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public Activity mActivity;
    protected final BehaviorSubject<LifeCycleEvent> lifecycleSubject = BehaviorSubject.create();
    public TipDialog tipDialog;
    private boolean gotoSporting = false;
    private boolean gotoWeight = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(LifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);
        //设置为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        //华为底部虚拟按键遮挡问题
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        //屏幕沉浸
        StatusBarUtils.from(this)
                .setStatusBarColor(getResources().getColor(R.color.Gray))
                .setLightStatusBar(true)
                .process();

        mContext = this;
        mActivity = this;
        RxActivityUtils.addActivity(this);

        ScreenAdapter.setCustomDensity(this);
        initDialog();
        initRxBus();

    }

    private void initRxBus() {
        RxBus.getInstance().register2(OpenAddWeight.class)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(RxComposeUtils.<OpenAddWeight>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<OpenAddWeight>() {
                    @Override
                    protected void _onNext(OpenAddWeight integer) {
                        RxLogUtils.d("显示：WeightRecordFragment");
                        RxActivityUtils.skipActivity(mContext, WeightAddFragment.class);
                    }
                });

        RxBus.getInstance().register2(SportsDataTab.class)
                .compose(RxComposeUtils.<SportsDataTab>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<SportsDataTab>() {
                    @Override
                    protected void _onNext(SportsDataTab sportsDataTab) {
//                        Logger.d("运动是否结束：" + BleService.clothingFinish);
                        if (BleService.clothingFinish) {
                            final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.getTvTitle().setVisibility(View.GONE);
                            dialog.setContent("运动已开始，是否进入运动界面");
                            dialog.setCancel("进入").setCancelListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    RxActivityUtils.skipActivity(mContext, SportingFragment_.class);
                                }
                            })
                                    .setSure("取消")
                                    .setSureListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                            dialog.show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        lifecycleSubject.onNext(LifeCycleEvent.START);
        super.onStart();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(LifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(LifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        lifecycleSubject.onNext(LifeCycleEvent.RESUME);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        lifecycleSubject.onNext(LifeCycleEvent.RESTART);
        super.onRestart();
    }


    private void initDialog() {
        tipDialog = new TipDialog(mContext);
    }


    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(LifeCycleEvent.DESTROY);
        tipDialog.dismiss();
        tipDialog = null;
        RxActivityUtils.removeActivity(this);
        super.onDestroy();
        mContext = null;
        mActivity = null;
    }

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (LAYOUT_FRAMELAYOUT.equals(name)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (LAYOUT_LINEARLAYOUT.equals(name)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (LAYOUT_RELATIVELAYOUT.equals(name)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }


    @Override
    public void onBackPressed() {
        RxActivityUtils.finishActivity();
    }
}
