package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.vondear.rxtools.utils.RxKeyboardUtils;

import lab.wesmartclothing.wefit.flyso.view.TipDialog;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseAcFragment extends QMUIFragment {
    public Activity mActivity;


    public BaseAcFragment() {
    }

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(getContext(), 100);
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
    }

    public TipDialog tipDialog;


    private void initDialog() {

        tipDialog = new TipDialog(mActivity);
    }


    @Override
    public void onDetach() {
        RxKeyboardUtils.hideSoftInput(mActivity);
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
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    protected void onVisible() {
    }

    protected void onInvisible() {
    }


    ///////////////////////////////////////////////////////////////////////////
    // 解决页面重叠
    ///////////////////////////////////////////////////////////////////////////


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
