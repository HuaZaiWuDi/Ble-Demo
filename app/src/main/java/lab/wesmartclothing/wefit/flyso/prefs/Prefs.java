package lab.wesmartclothing.wefit.flyso.prefs;

import org.androidannotations.annotations.sharedpreferences.DefaultFloat;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by jk on 2018/5/10.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Prefs {


    @DefaultString("")
    String UserId();

    @DefaultString("")
    String token();

    @DefaultString("")
    String phone();

//    @DefaultString("")
//    String QN_mac();

    @DefaultInt(175)
    int height();

    @DefaultInt(60)
    int weight();

    @DefaultInt(0)
    int sex();

    @DefaultLong(0)
    long birthDayMillis();

    @DefaultString("")
    String scaleIsBind();

    @DefaultString("")
    String clothing();

    //真实体重
    @DefaultFloat(0)
    float realWeight();

}
