package lab.dxythch.com.netlib.net;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 项目名称：Bracelet
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/5/24
 */
public interface RetrofitService {
    String BASE_URL = ServiceAPI.BASE_URL;


    ///////////////////////////////////////////////////////////////////////////
    // 瘦身（热量）
    ///////////////////////////////////////////////////////////////////////////

    //    @FormUrlEncoded
//    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/slim/getSlimHistoryInfo")
    Observable<String> getHeatHistory(@Body RequestBody body);


    @POST("/slim/getFoodInfo")
    Observable<String> getFoodInfo(@Body RequestBody body);


    @POST("/slim/searchFoodInfo")
    Observable<String> searchFoodInfo(@Body RequestBody body);

    @POST("/slim/getKeyWord")
    Observable<String> getKeyWord(@Body RequestBody body);

    @POST("/slim/addHeatInfo")
    Observable<String> addHeatInfo(@Body RequestBody body);

    @POST("/slim/removeHeatInfo")
    Observable<String> removeHeatInfo(@Body RequestBody body);


    ///////////////////////////////////////////////////////////////////////////
    // 体重
    ///////////////////////////////////////////////////////////////////////////

    @POST("/slim/getWeightInfo")
    Observable<String> getWeightInfo(@Body RequestBody body);

    @POST("/slim/getWeightList")
    Observable<String> getWeightList(@Body RequestBody body);

    @POST("/slim/addWeightInfo")
    Observable<String> addWeightInfo(@Body RequestBody body);

    @POST("/slim/getAthleticsInfo")
    Observable<String> getAthleticsInfo(@Body RequestBody body);

    @POST("/slim/getAthleticsList")
    Observable<String> getAthleticsList(@Body RequestBody body);

    @POST("/slim/addAthleticsInfo")
    Observable<String> addAthleticsInfo(@Body RequestBody body);


}
