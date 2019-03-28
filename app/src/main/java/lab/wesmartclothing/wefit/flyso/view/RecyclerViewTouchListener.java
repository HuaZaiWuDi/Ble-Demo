package lab.wesmartclothing.wefit.flyso.view;

import android.view.MotionEvent;
import android.view.View;

import com.vondear.rxtools.utils.RxUtils;

public class RecyclerViewTouchListener implements View.OnTouchListener {


    private float oldY;
    private float oldX;
    private float newY;
    private float newX;
    private boolean moveTransverse = false;//是否横向滑动

    private View.OnClickListener mOnClickListener;


    public RecyclerViewTouchListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public RecyclerViewTouchListener() {
    }

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                newX = ev.getX();
                newY = ev.getY();
                view.getParent().requestDisallowInterceptTouchEvent(true);
                RxUtils.isFastClick(100);
                moveTransverse = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //手指滑动同时判断滑动方向，一旦滑动方向大于+-60便调用
                //getParent().getParent().requestDisallowInterceptTouchEvent(false);
                //将滑动事件交给RecyclerView来处理
                oldX = newX;
                oldY = newY;
                newX = ev.getX();
                newY = ev.getY();

                float moveX = Math.abs(oldX - newX);
                float moveY = Math.abs(oldY - newY);

//                RxLogUtils.d("webView：moveX:" + moveX + "----moveY:" + moveY);
                if (moveX > moveY) {
                    moveTransverse = true;
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }

//                //moveX * 1.73 < moveY  ,判断左右滑动范围为+-60度
//                if (moveX * 1.73 < moveY) {
//                    view.getParent().requestDisallowInterceptTouchEvent(false);
//                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (RxUtils.isFastClick(100) && mOnClickListener != null && !moveTransverse) {
                    mOnClickListener.onClick(view);
                }
                view.getParent().requestDisallowInterceptTouchEvent(false);

                break;
        }
        return false;
    }
}
