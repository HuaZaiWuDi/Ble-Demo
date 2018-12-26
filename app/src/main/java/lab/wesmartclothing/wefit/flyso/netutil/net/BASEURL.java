package lab.wesmartclothing.wefit.flyso.netutil.net;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//自定义注解
@Retention(RetentionPolicy.SOURCE)
@StringDef({ServiceAPI.BASE_URL_125, ServiceAPI.BASE_URL_208, ServiceAPI.BASE_URL_mix, ServiceAPI.BASE_URL_192})
@interface BASEURL {

}