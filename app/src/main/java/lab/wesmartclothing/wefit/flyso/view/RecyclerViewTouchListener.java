package lab.wesmartclothing.wefit.flyso.view;

import android.view.MotionEvent;
import android.view.View;

import com.vondear.rxtools.utils.RxUtils;

public class RecyclerViewTouchListener implements View.OnTouchListener {


    private float startY, oldY, newY;
    private float startX, oldX, newX;
    private boolean moveTransverse = false;//是否横向滑动

    private final int FAST_CLICK_TIME = 80;

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
                startX = newX = ev.getX();
                startY = newY = ev.getY();
                view.getParent().requestDisallowInterceptTouchEvent(true);
                RxUtils.isFastClick(FAST_CLICK_TIME);
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
                //判断滑动距离小于值，则为点击
                boolean notMove = Math.abs(startX - ev.getX()) < 50 && Math.abs(startY - ev.getY()) < 50;
                if (RxUtils.isFastClick(FAST_CLICK_TIME) && mOnClickListener != null && !moveTransverse && notMove) {
                    mOnClickListener.onClick(view);
                }
                view.getParent().requestDisallowInterceptTouchEvent(false);

                break;
        }
        return false;
    }
}
