package lab.wesmartclothing.wefit.flyso.entity;

/**
 * Created icon_hide_password jk on 2018/5/14.
 */
public class WeightInfoItem {

    private int img_left;
    private int img_right;
    private String title_right;
    private String data_right;
    private String title_left;
    private String data_left;



    public int getImg_left() {
        return img_left;
    }

    public void setImg_left(int img_left) {
        this.img_left = img_left;
    }

    public int getImg_right() {
        return img_right;
    }

    public void setImg_right(int img_right) {
        this.img_right = img_right;
    }

    public String getTitle_right() {
        return title_right;
    }

    public void setTitle_right(String title_right) {
        this.title_right = title_right;
    }

    public String getData_right() {
        return data_right;
    }

    public void setData_right(String data_right) {
        this.data_right = data_right;
    }

    public String getTitle_left() {
        return title_left;
    }

    public void setTitle_left(String title_left) {
        this.title_left = title_left;
    }

    public String getData_left() {
        return data_left;
    }

    public void setData_left(String data_left) {
        this.data_left = data_left;
    }

    @Override
    public String toString() {
        return "WeightInfoItem{" +
                "img_left=" + img_left +
                ", img_right=" + img_right +
                ", title_right='" + title_right + '\'' +
                ", data_right='" + data_right + '\'' +
                ", title_left='" + title_left + '\'' +
                ", data_left='" + data_left + '\'' +
                '}';
    }
}
