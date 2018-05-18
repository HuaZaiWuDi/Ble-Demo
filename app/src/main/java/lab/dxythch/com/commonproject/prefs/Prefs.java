package lab.dxythch.com.commonproject.prefs;

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
    String QN_mac();

    @DefaultInt(175)
    int height();

    @DefaultInt(60)
    int weight();

    @DefaultString("male")
    String sex();

    @DefaultLong(0)
    long birthDayMillis();


}