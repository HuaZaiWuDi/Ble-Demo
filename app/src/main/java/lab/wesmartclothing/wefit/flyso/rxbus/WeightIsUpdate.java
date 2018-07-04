package lab.wesmartclothing.wefit.flyso.rxbus;

/**
 * Created icon_hide_password jk on 2018/5/17.
 */
public class WeightIsUpdate {

    boolean isUpdate;

    public WeightIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
