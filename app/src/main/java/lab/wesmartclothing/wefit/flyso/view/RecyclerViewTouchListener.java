package lab.wesmartclothing.wefit.flyso.view;

import android.view.MotionEvent;
import android.view.View;

public class RecyclerViewTouchListener implements View.OnTouchListener {


    public float oldY;
    public float oldX;
    public float newY;
    public float newX;

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                newX = ev.getX();
                newY = ev.getY();
                view.getParent().requestDisallowInterceptTouchEvent(true);
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
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }

//                //moveX * 1.73 < moveY  ,判断左右滑动范围为+-60度
//                if (moveX * 1.73 < moveY) {
//                    view.getParent().requestDisallowInterceptTouchEvent(false);
//                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }
}
