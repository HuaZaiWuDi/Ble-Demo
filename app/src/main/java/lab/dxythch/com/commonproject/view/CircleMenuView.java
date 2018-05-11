package lab.dxythch.com.commonproject.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vondear.rxtools.utils.RxLogUtils;

import lab.dxythch.com.commonproject.R;

/**
 * Created by jk on 2018/5/9.
 */
public class CircleMenuView extends RelativeLayout {


    boolean isOpen = false;

    private ImageView menu_add, menu_breakfast, menu_lunch, menu_dinner, menu_midnight, menu_snack;

    private long duration = 500;

    public CircleMenuView(Context context) {
        this(context, null);
    }

    public CircleMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_menu_button, this, true);
        menu_add = view.findViewById(R.id.menu_add);
        menu_breakfast = view.findViewById(R.id.menu_breakfast);
        menu_lunch = view.findViewById(R.id.menu_lunch);
        menu_dinner = view.findViewById(R.id.menu_dinner);
        menu_midnight = view.findViewById(R.id.menu_midnight);
        menu_snack = view.findViewById(R.id.menu_snack);


        menu_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen)
                    closeMenu();
                else
                    openMenu();
                if (mOnMenuClickListener != null)
                    mOnMenuClickListener.menuOpen(isOpen);
            }
        });

        menu_breakfast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
                if (mOnMenuClickListener != null)
                    mOnMenuClickListener.menuClick(0);
            }
        });
        menu_lunch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
                if (mOnMenuClickListener != null)
                    mOnMenuClickListener.menuClick(1);
            }
        });
        menu_dinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
                if (mOnMenuClickListener != null)
                    mOnMenuClickListener.menuClick(2);
            }
        });
        menu_midnight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
                if (mOnMenuClickListener != null)
                    mOnMenuClickListener.menuClick(3);
            }
        });
        menu_snack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
                if (mOnMenuClickListener != null)
                    mOnMenuClickListener.menuClick(4);
            }
        });

        visibleView(false);
        ViewCompat.animate(menu_breakfast)
                .scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
        ViewCompat.animate(menu_lunch)
                .scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
        ViewCompat.animate(menu_dinner)
                .scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
        ViewCompat.animate(menu_midnight)
                .scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
        ViewCompat.animate(menu_snack)
                .scaleX(0f).scaleY(0f).alpha(0).withLayer().start();

        setComplete(false);
    }

    private void visibleView(boolean isShow) {
        menu_breakfast.setEnabled(isShow);
        menu_lunch.setEnabled(isShow);
        menu_dinner.setEnabled(isShow);
        menu_midnight.setEnabled(isShow);
        menu_snack.setEnabled(isShow);
    }


    public void setComplete(boolean isComplete) {
        menu_add.setBackgroundResource(isComplete ? R.mipmap.add_red_normal_icon : R.mipmap.add_green_normal_icon);
    }


    public void openMenu() {
        RxLogUtils.d("开启");

        isOpen = true;
        ViewCompat.animate(menu_breakfast)
                .setDuration(duration).scaleX(1f).scaleY(1f).alpha(1).withLayer().start();
        ViewCompat.animate(menu_lunch)
                .setDuration(duration).scaleX(1f).scaleY(1f).alpha(1).withLayer().start();
        ViewCompat.animate(menu_dinner)
                .setDuration(duration).scaleX(1f).scaleY(1f).alpha(1).withLayer().start();
        ViewCompat.animate(menu_midnight)
                .setDuration(duration).scaleX(1f).scaleY(1f).alpha(1).withLayer().start();
        ViewCompat.animate(menu_snack)
                .setDuration(duration).scaleX(1f).scaleY(1f).alpha(1).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                visibleView(true);
            }

            @Override
            public void onAnimationEnd(View view) {

            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).withLayer().start();

    }

    public void closeMenu() {
        RxLogUtils.d("关闭");
        isOpen = false;
        ViewCompat.animate(menu_add)
                .rotationX(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                visibleView(false);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).withLayer().start();
        ViewCompat.animate(menu_breakfast)
                .setDuration(duration).scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
        ViewCompat.animate(menu_lunch)
                .setDuration(duration).scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
        ViewCompat.animate(menu_dinner)
                .setDuration(duration).scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
        ViewCompat.animate(menu_midnight)
                .setDuration(duration).scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
        ViewCompat.animate(menu_snack)
                .setDuration(duration).scaleX(0f).scaleY(0f).alpha(0).withLayer().start();
    }


    public interface OnMenuClickListener {
        void menuClick(int position);

        void menuOpen(boolean isOpen);
    }

    private OnMenuClickListener mOnMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }
}
