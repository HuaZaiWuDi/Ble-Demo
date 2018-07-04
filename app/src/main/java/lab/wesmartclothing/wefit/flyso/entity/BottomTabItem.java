package lab.wesmartclothing.wefit.flyso.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public class BottomTabItem implements CustomTabEntity {

    int select_img;
    int unselect_img;
    String text;


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


    public BottomTabItem(String text) {
        this.text = text;
    }

    public BottomTabItem(int select_img, int unselect_img, String text) {
        this.select_img = select_img;
        this.unselect_img = unselect_img;
        this.text = text;
    }
}
