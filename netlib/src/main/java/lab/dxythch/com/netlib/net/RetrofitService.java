package lab.dxythch.com.netlib.net;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 项目名称：Bracelet
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/5/24
 */
public interface RetrofitService {
    String BASE_URL = ServiceAPI.BASE_URL;


    @FormUrlEncoded
    @POST("CRMShop/skapp_gainUserId.action")
    Observable<String> gainUserId(@Field("merchantNo") String merchantNo, @Field("systemTime") String systemTime, @Field("mapType") String mapType
            , @Field("longitude") String longitude, @Field("latitude") String latitude);


    @FormUrlEncoded
    @POST("CRMShop/skapp_gainAppConfig.action")
    Observable<String> gainAppConfig(@Field("merchantNo") String merchantNo, @Field("userId") String userId,
                                     @Field("systemTime") String systemTime, @Field("appVersionNo") String appVersionNo,
                                     @Field("mapType") String mapType, @Field("longitude") String longitude,
                                     @Field("latitude") String latitude);


    @FormUrlEncoded
    @POST("CRMShop/skapp_bindDevice.action")
    Observable<String> bindDevice(@Field("merchantNo") String merchantNo, @Field("userId") String userId,
                                  @Field("systemTime") String systemTime, @Field("deviceSN") String deviceSN);

    @FormUrlEncoded
    @POST("CRMShop/skapp_refreshHistory.action")
    Observable<String> refreshHistory(@Field("merchantNo") String merchantNo, @Field("userId") String userId,
                                      @Field("systemTime") String systemTime, @Field("deviceSN") String deviceSN,
                                      @Field("historyData") String historyData);

    @FormUrlEncoded
    @POST("CRMShop/skapp_gainFirmwareConfig.action")
    Observable<String> gainFirmwareConfig(@Field("merchantNo") String merchantNo, @Field("userId") String userId,
                                          @Field("systemTime") String systemTime, @Field("deviceSN") String deviceSN,
                                          @Field("firmwareVersionNo") String firmwareVersionNo);

    @FormUrlEncoded
    @POST("CRMShop/skapp_gainUserInfo.action")
    Observable<String> gainUserInfo(@Field("merchantNo") String merchantNo, @Field("userId") String userId,
                                    @Field("systemTime") String systemTime);

    @FormUrlEncoded
    @POST("CRMShop/skapp_updateUserInfo.action")
    Observable<String> updateUserInfo(@Field("merchantNo") String merchantNo, @Field("userId") String userId,
                                      @Field("systemTime") String systemTime, @Field("userName") String userName,
                                      @Field("phoneNumber") String phoneNumber, @Field("email") String email,
                                      @Field("weChatNo") String weChatNo, @Field("qqNo") String qqNo);

    @FormUrlEncoded
    @POST("CRMShop/skapp_updateFamilyUserInfo.action")
    Observable<String> updateFamilyUserInfo(@Field("merchantNo") String merchantNo, @Field("userId") String userId,
                                            @Field("systemTime") String systemTime, @Field("familyUser") String familyUser,
                                            @Field("familyType") String familyType, @Field("familyAction") String familyAction);

}
