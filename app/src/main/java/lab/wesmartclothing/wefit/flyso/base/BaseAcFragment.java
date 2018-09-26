package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.vondear.rxtools.utils.RxKeyboardUtils;
import com.vondear.rxtools.utils.RxLogUtils;

import io.reactivex.subjects.BehaviorSubject;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;
import lab.wesmartclothing.wefit.netlib.utils.LifeCycleEvent;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseAcFragment extends QMUIFragment {
    public Activity mActivity;
    public Context mContext;
    public String TGA = "";
    protected final BehaviorSubject<LifeCycleEvent> lifecycleSubject = BehaviorSubject.create();

    public BaseAcFragment() {
    }

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(getContext(), 100);
    }


    @Override
    public void onAttach(Context context) {
        RxLogUtils.i(TGA + "：onAttach");
        lifecycleSubject.onNext(LifeCycleEvent.ATTACH);
        super.onAttach(context);
        mContext = mActivity = getActivity();
        initDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        RxLogUtils.i(TGA + "：onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    public TipDialog tipDialog;


    public void setTGA(String TGA) {
        this.TGA = TGA;
    }

    private void initDialog() {

        tipDialog = new TipDialog(mActivity);
    }


    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    @Override
    public void onPause() {
        RxLogUtils.i(TGA + "：onPause");
        lifecycleSubject.onNext(LifeCycleEvent.STOP);
        super.onPause();
    }


    @Override
    public void onStop() {
        RxLogUtils.i(TGA + "：onStop");
        lifecycleSubject.onNext(LifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    public void onResume() {
        RxLogUtils.i(TGA + "：onResume");
        lifecycleSubject.onNext(LifeCycleEvent.RESUME);
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        RxLogUtils.i(TGA + "：onDestroyView");
        lifecycleSubject.onNext(LifeCycleEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        RxLogUtils.i(TGA + "：onDestroy");
        lifecycleSubject.onNext(LifeCycleEvent.DESTROY);
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            RefWatcher refWatcher = LeakCanary.installedRefWatcher();
// We expect schrodingerCat to be gone soon (or not), let's watch it.
            refWatcher.watch(this);
        }
    }

    @Override
    public void onDetach() {
        RxLogUtils.i(TGA + "：onDetach");
        lifecycleSubject.onNext(LifeCycleEvent.DETACH);
        tipDialog.dismiss();
        RxKeyboardUtils.hideSoftInput(mActivity);
        super.onDetach();
        mActivity = null;
    }


    @Override
    public void onStart() {
        lifecycleSubject.onNext(LifeCycleEvent.START);
        RxLogUtils.i(TGA + "：onStart");
        super.onStart();
    }

    protected void onVisible() {
    }

    protected void onInvisible() {
    }

    @Override
    protected boolean canDragBack() {
        return true;
    }


    ///////////////////////////////////////////////////////////////////////////
    // 解决页面重叠
    ///////////////////////////////////////////////////////////////////////////


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxLogUtils.i(TGA + "：onCreate");
        if (savedInstanceState != null) {

            boolean aBoolean = savedInstanceState.getBoolean("STATE_SAVE_IS_HIDDEN");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!aBoolean) {
                transaction.hide(this);
            } else {
                transaction.show(this);
            }
            transaction.commit();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("STATE_SAVE_IS_HIDDEN", isHidden());
    }


}
