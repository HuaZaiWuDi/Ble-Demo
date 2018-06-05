package lab.wesmartclothing.wefit.flyso.netserivce;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jk on 2018/5/24.
 */
public interface StoreService {
    String BASE_URL = ServiceAPI.STORE_URL;


    ///////////////////////////////////////////////////////////////////////////
    // 商城
    ///////////////////////////////////////////////////////////////////////////
//    @FormUrlEncoded
    @GET("smart/getMallAddress")
    Observable<String> getMallAddress();

    //订单地址
    @GET("smart/getOrderUrl")
    Observable<String> getOrderUrl();

    //购物车地址
    @GET("smart/getShoppingAddress")
    Observable<String> getShoppingAddress();


}
