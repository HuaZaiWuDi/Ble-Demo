package lab.wesmartclothing.wefit.flyso.tools;

import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Package lab.wesmartclothing.wefit.flyso.tools
 * @FileName GroupType
 * @Date 2019/7/1 16:34
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@StringDef({"days", "months"})
public @interface GroupType {

    String TYPE_DAYS = "days";
    String TYPE_MONTHS = "months";

}
