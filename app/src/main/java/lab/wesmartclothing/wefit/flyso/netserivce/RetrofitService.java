package lab.wesmartclothing.wefit.flyso.netserivce;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @POST("slim/getSlimHistoryInfo")
    Observable<String> getHeatHistory(@Body RequestBody body);

    @POST("slim/getFoodInfo")
    Observable<String> getFoodInfo(@Body RequestBody body);

    @FormUrlEncoded
    @POST("slim/searchFoodInfo")
    Observable<String> searchFoodInfo(@Field("foodName") String foodName);

    @POST("slim/getKeyWord")
    Observable<String> getKeyWord(@Body RequestBody body);

    @POST("slim/addHeatInfo")
    Observable<String> addHeatInfo(@Body RequestBody body);

    @POST("slim/removeHeatInfo")
    Observable<String> removeHeatInfo(@Body RequestBody body);

    @POST("slim/getAddedHeatInfo")
    Observable<String> getAddedHeatInfo(@Body RequestBody body);

    ///////////////////////////////////////////////////////////////////////////
    // 体重
    ///////////////////////////////////////////////////////////////////////////

    @POST("slim/getWeightInfo")
    Observable<String> getWeightInfo(@Body RequestBody body);

//    @POST("slim/getWeightList")
//    Observable<String> getWeightList(@Body RequestBody body);

    @FormUrlEncoded
    @POST("slim/getWeightList")
    Observable<String> getWeightList(@Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    @POST("slim/addWeightInfo")
    Observable<String> addWeightInfo(@Body RequestBody body);


    ///////////////////////////////////////////////////////////////////////////
    // 运动
    ///////////////////////////////////////////////////////////////////////////


    @POST("slim/getAthleticsInfo")
    Observable<String> getAthleticsInfo(@Body RequestBody body);

    @FormUrlEncoded
    @POST("slim/getAthleticsList")
    Observable<String> getAthleticsList(@Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    @POST("slim/addAthleticsInfo")
    Observable<String> addAthleticsInfo(@Body RequestBody body);

    ///////////////////////////////////////////////////////////////////////////
    // 登录
    ///////////////////////////////////////////////////////////////////////////

    @FormUrlEncoded
    @POST("login")
    Observable<String> login(@Field("phone") String phone, @Field("code") String code);

    //    @FormUrlEncoded
    @POST("login/logout")
    Observable<String> logout();

    @GET("login/sendCode")
    Observable<String> sendCode(@Query("phone") String phone);

    ///////////////////////////////////////////////////////////////////////////
    // 我的
    ///////////////////////////////////////////////////////////////////////////

    @POST("user/saveUserInfo")
    Observable<String> saveUserInfo(@Body RequestBody body);

    @POST("user/addBindDevice")
    Observable<String> addBindDevice(@Body RequestBody body);

    @FormUrlEncoded
    @POST("user/isBindDevice")
    Observable<String> isBindDevice(@Field("macAddr") String phone);

    @POST("user/userInfo")
    Observable<String> userInfo();


    ///////////////////////////////////////////////////////////////////////////
    // 数据统计
    ///////////////////////////////////////////////////////////////////////////
    @POST("appData/deviceLink")
    Observable<String> deviceLink(@Body RequestBody body);


}
