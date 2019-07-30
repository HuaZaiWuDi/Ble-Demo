package lab.wesmartclothing.wefit.flyso.entity;

import android.support.v4.app.Fragment;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public class BottomTabItem implements CustomTabEntity {

    int select_img;
    int unselect_img;
    String text;
    Fragment mFragment;

    @Override
    public String getTabTitle() {
        return text;
    }

    @Override
    public int getTabSelectedIcon() {
        return select_img;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unselect_img;
    }


    public Fragment getFragment() {
        return mFragment;
    }

    public BottomTabItem(String text) {
        this.text = text;
    }

    public BottomTabItem(int select_img, int unselect_img, String text) {
        this.select_img = select_img;
        this.unselect_img = unselect_img;
        this.text = text;
    }

    public BottomTabItem(int select_img, int unselect_img, String text, Fragment fragment) {
        this.select_img = select_img;
        this.unselect_img = unselect_img;
        this.text = text;
        mFragment = fragment;
    }
}
