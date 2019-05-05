package lab.wesmartclothing.wefit.flyso.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxLogUtils;

import lab.wesmartclothing.wefit.flyso.view.TipDialog;

/**
 * Created icon_hide_password vondear
 * on 2015/11/21.
 * <p>
 * 若需要采用Lazy方式加载的Fragment，初始化内容放到initData实现
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 * <p>
 * 注意事项 1:
 * 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 * <p>
 * 注意事项 2:
 * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 * QMUIFragment.UI
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 是否可见状态
     */
    private boolean isVisible;

    /**
     * 标志位，View已经初始化完成。
     */

    /**
     * 是否第一次加载
     */
    boolean isLoad = false;

    public FragmentActivity mActivity;


    ///////////////////////////////////////////////////////////////////////////
    // 解决页面重叠
    ///////////////////////////////////////////////////////////////////////////

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxLogUtils.d("加载Fragment:" + savedInstanceState);
        if (savedInstanceState != null) {
            boolean aBoolean = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!aBoolean) {
                transaction.hide(this);
            } else {
                transaction.show(this);
            }
            transaction.commit();
        }
    }

    public BaseFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        initDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLoad = true;
        onVisible();
    }

    public TipDialog tipDialog;


    private void initDialog() {

        tipDialog = new TipDialog(mActivity);
    }


//        @Override
//    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
//        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
//        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
//        // 导致initData反复执行,所以这里注释掉
//        // isFirstLoad = true;
//
//        // 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
//        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
//        // 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
//            mActivity = getActivity();
//
//        isFirstLoad = true;
//        View view = initViews(layoutInflater, viewGroup, savedInstanceState);
//        isPrepared = true;
//        lazyLoad();
//        return view;
//    }


    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
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
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
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
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();

    }


    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
//        RxLogUtils.d("isVisible:" + isVisible);
//        RxLogUtils.d("isLoad：" + isLoad);
        if (!isVisible || !isLoad) {
            return;
        }
//        isLoad = false;
        initData();
    }


    public abstract void initData();

    public abstract void initView();

}
