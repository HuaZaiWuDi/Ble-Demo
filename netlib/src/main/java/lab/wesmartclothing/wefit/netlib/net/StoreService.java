package lab.wesmartclothing.wefit.netlib.net;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jk on 2018/5/24.
 */
public interface StoreService {
    String BASE_URL = ServiceAPI.BASE_URL;


    ///////////////////////////////////////////////////////////////////////////
    // 商城
    ///////////////////////////////////////////////////////////////////////////
//    @FormUrlEncoded
    @GET("system/getMallAddress")
    Observable<String> getMallAddress();

    //订单地址
    @GET("system/getOrderUrl")
    Observable<String> getOrderUrl();

    //购物车地址
    @GET("system/getShoppingAddress")
    Observable<String> getShoppingAddress();


}
