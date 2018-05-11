package lab.dxythch.com.commonproject.prefs;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by jk on 2018/5/10.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Prefs {


    @DefaultString("")
    String UserId();

}
