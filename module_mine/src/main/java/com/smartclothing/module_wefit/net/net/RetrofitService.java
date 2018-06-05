package com.smartclothing.module_wefit.net.net;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 项目名称：Bracelet
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/5/24
 */
public interface RetrofitService {
    String BASE_URL = ServiceAPI.BASE_URL;


    /*我的模块：获取用户信息*/
    @POST("user/userCenter")
    Observable<String> userCenter();


    /*获取用户个人资料*/
    @POST("user/userInfo")
    Observable<String> userInfo();

    /*保存用户信息*/
    @POST("user/saveUserInfo")
    Observable<String> saveUserInfo(@Body RequestBody body);

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

    /*退出登录*/
    @POST("login/logout")
    Observable<String> logout();


}
