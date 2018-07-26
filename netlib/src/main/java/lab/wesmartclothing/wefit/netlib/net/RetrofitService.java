package lab.wesmartclothing.wefit.netlib.net;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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

    @FormUrlEncoded
    @POST("slim/indexInfo")
    Observable<String> indexInfo(@Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    ///////////////////////////////////////////////////////////////////////////
    // 体重
    ///////////////////////////////////////////////////////////////////////////


//    @POST("slim/getWeightList")
//    Observable<String> getWeightList(@Body RequestBody body);

    @FormUrlEncoded
    @POST("weight/fetchWeightList")
    Observable<String> getWeightList(@Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    //移除体重信息
    @POST("weight/removeWeightInfo")
    Observable<String> removeWeightInfo(@Body RequestBody body);


    //获取目标体重信息
    @FormUrlEncoded
    @POST("weight/fetchTargetWeight")
    Observable<String> fetchTargetWeight();

    @POST("weight/addWeightInfo")
    Observable<String> addWeightInfo(@Body RequestBody body);

    //设置目标体重
    @POST("weight/setTargetWeight")
    Observable<String> setTargetWeight(@Body RequestBody body);


    ///////////////////////////////////////////////////////////////////////////
    // 运动
    ///////////////////////////////////////////////////////////////////////////

    //获取运动记录信息(用于左右翻页)
    @FormUrlEncoded
    @POST("athl/fetchAthleticsInfo")
    Observable<String> getAthleticsInfo(@Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    //获取运动列表记录
    @FormUrlEncoded
    @POST("athl/fetchAthleticsList")
    Observable<String> getAthleticsList(@Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    @POST("athl/addAthleticsInfo")
    Observable<String> addAthleticsInfo(@Body RequestBody body);

    @POST("athl/athleticsDetail")
    Observable<String> athleticsDetail(@Body RequestBody body);


    ///////////////////////////////////////////////////////////////////////////
    // 登录
    ///////////////////////////////////////////////////////////////////////////

    @GET("login/sendCode")
    Observable<String> sendCode2Login(@Query("phone") String phone);

    @FormUrlEncoded
    @POST("login")
    Observable<String> login(@Field("phone") String phone, @Field("code") String code);

    @POST("logout")
    Observable<String> logout();

    @FormUrlEncoded
    @POST("sendCode")
    Observable<String> sendCode(@Field("phone") String phone, @Field("verify") String verify);

    @FormUrlEncoded
    @POST("register")
    Observable<String> register(@Field("phone") String phone, @Field("code") String code, @Field("password") String password);


    @FormUrlEncoded
    @POST("outerLogin")
    Observable<String> outerLogin(@Field("outerId") String outerId, @Field("userName") String userName,
                                  @Field("imgUrl") String imgUrl, @Field("userType") String userType);

    @FormUrlEncoded
    @POST("toResetPassword")
    Observable<String> toResetPassword(@Field("phone") String phone, @Field("code") String code);

    @FormUrlEncoded
    @POST("pwdLogin")
    Observable<String> pwdLogin(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("resetPassword")
    Observable<String> resetPassword(@Field("phone") String phone, @Field("password") String password, @Field("code") String code);

    @FormUrlEncoded
    @POST("phoneBind")
    Observable<String> phoneBind(@Field("phone") String phone, @Field("code") String password,
                                 @Field("outerId") String outerId, @Field("userName") String userName,
                                 @Field("imgUrl") String imgUrl, @Field("userType") String userType);

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


    /*我的模块：获取用户信息*/
    @POST("user/userCenter")
    Observable<String> userCenter();


    /*上传用户头像*/
    @Multipart
    @POST("user/uploadUserImg")
    Observable<String> uploadUserImg(@Part MultipartBody.Part file);

    /*我的收藏*/
    @FormUrlEncoded
    @POST("user/collectionList")
    Observable<String> collectionList(@Field("infoType") int infoType, @Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    /*消息*/
    @FormUrlEncoded
    @POST("user/message")
    Observable<String> message(@Field("msgType") int msgType, @Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    /*查看并更新已读*/
    @FormUrlEncoded
    @POST("user/readed")
    Observable<String> readed(@Field("gid") String gid);

    /*全部更新已读*/
    @FormUrlEncoded
    @POST("user/readedAll")
    Observable<String> readedAll(@Field("msgType") int msgType);

    /*获取用户绑定设备信息列表*/
    @POST("user/deviceList")
    Observable<String> deviceList();

    /*问题反馈*/
    @POST("user/feedback")
    Observable<String> feedback(@Body RequestBody body);

    /*问题反馈上传用图像*/
    @Multipart
    @POST("user/feedbackImg")
    Observable<String> feedbackImg(@Part() List<MultipartBody.Part> parts);

    /*删除指定id的设备*/
    @FormUrlEncoded
    @POST("user/removeBind")
    Observable<String> removeBind(@Field("gid") String gid);

    /*删除指定id的收藏*/
    @FormUrlEncoded
    @POST("user/removeCollection")
    Observable<String> removeCollection(@Field("gid") String gid);


    //下载文件
    @Streaming //防止大文件被写进内存
    @GET
    Observable<ResponseBody> downLoadFile(@Url String fileUrl);

    ///////////////////////////////////////////////////////////////////////////
    // 数据统计
    ///////////////////////////////////////////////////////////////////////////
    @POST("appData/deviceLink")
    Observable<String> deviceLink(@Body RequestBody body);

    @POST("appData/addDeviceVersion")
    Observable<String> addDeviceVersion(@Body RequestBody body);


    ///////////////////////////////////////////////////////////////////////////
    // 发现
    ///////////////////////////////////////////////////////////////////////////
    //发现新连接
    @FormUrlEncoded
    @POST("/find/detail.html")
    Observable<String> newsDetail(@Field("gid") String git);


    ///////////////////////////////////////////////////////////////////////////
    // 固件升级模块
    ///////////////////////////////////////////////////////////////////////////
    @POST("upgrade/getUpgradeInfo")
    Observable<String> getUpgradeInfo(@Body RequestBody body);
}
