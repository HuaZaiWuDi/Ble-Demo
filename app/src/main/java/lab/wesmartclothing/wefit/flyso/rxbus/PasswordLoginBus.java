package lab.wesmartclothing.wefit.flyso.rxbus;

/**
 * Created by jk on 2018/7/3.
 */
public class PasswordLoginBus {

    public String phone;
    public String password;

    public PasswordLoginBus(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }
}
