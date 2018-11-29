package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.vondear.rxtools.utils.RxKeyboardUtils;
import com.vondear.rxtools.utils.RxLogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.netutil.utils.LifeCycleEvent;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseAcFragment extends Fragment {
    public Activity mActivity;
    public Context mContext;
    public String TGA = "";
    private Unbinder unbinder;
    protected final BehaviorSubject<LifeCycleEvent> lifecycleSubject = BehaviorSubject.create();

    /**
     * 是否可见状态
     */
    private boolean isVisible;

    /**
     * 标志位，View已经初始化完成。
     */
    private boolean isPrepared;

    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;

    public BaseAcFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        // 导致initData反复执行,所以这里注释掉
        // isFirstLoad = true;

        // 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
        // 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
        isFirstLoad = true;
        View view = LayoutInflater.from(mContext).inflate(layoutId(), null);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        initRxBus();
        Bundle arguments = getArguments();
        if (arguments != null) {
            initBundle(arguments);
        }
        isPrepared = true;
        lazyLoad();
        return view;
    }


    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initNetData();
    }


    protected abstract @LayoutRes
    int layoutId();


    //初始化Bundle数据
    protected void initBundle(Bundle bundle) {

    }

    //初始化布局逻辑
    protected void initViews() {

    }

    //初始化网络数据
    protected void initNetData() {

    }

    protected void initRxBus() {

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
        RxLogUtils.d("setUserVisibleHint：" + isVisibleToUser);
        RxLogUtils.d("isPrepared：" + isPrepared);
        RxLogUtils.d("isFirstLoad：" + isFirstLoad);
        if (getUserVisibleHint()) {
            isVisible = true;
            if (isPrepared) {
                onVisible();
            }
        } else {
            isVisible = false;
            if (isPrepared) {
                onInvisible();
            }
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
        RxLogUtils.d("onHiddenChanged：" + hidden);
        if (!hidden) {
            isVisible = true;
            if (isPrepared) {
                onVisible();
            }
        } else {
            isVisible = false;
            if (isPrepared) {
                onInvisible();
            }
        }
    }

    @Override
    public void onStart() {
        lifecycleSubject.onNext(LifeCycleEvent.START);
        RxLogUtils.i(TGA + "：onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        RxLogUtils.i(TGA + "：onResume");
        lifecycleSubject.onNext(LifeCycleEvent.RESUME);
        super.onResume();
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
        isVisible = false;
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        if (unbinder != null)
            unbinder.unbind();
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


    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    public boolean isVisibled() {
        return isVisible;
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
