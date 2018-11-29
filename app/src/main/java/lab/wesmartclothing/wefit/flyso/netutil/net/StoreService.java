package lab.wesmartclothing.wefit.flyso.netutil.net;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jk on 2018/5/24.
 */
public interface StoreService {
        String BASE_URL = "https://dev.wesmartclothing.com/system/";
//    String BASE_URL = "http://10.10.11.208:15320/";


    ///////////////////////////////////////////////////////////////////////////
    // 商城
    ///////////////////////////////////////////////////////////////////////////
//    @FormUrlEncoded
    @GET("getMallAddress")
    Observable<String> getMallAddress();

    //订单地址
    @GET("getOrderUrl")
    Observable<String> getOrderUrl();

    //购物车地址
    @GET("getShoppingAddress")
    Observable<String> getShoppingAddress();

    //系统配置
    @GET("smart/getSystemConfig")
    Observable<String> getSystemConfig();


}
