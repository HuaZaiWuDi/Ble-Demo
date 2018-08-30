package lab.wesmartclothing.wefit.netlib.utils;

import android.net.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * 项目名称：MyProject
 * 类描述：展示网络错误信息。
 * 创建人：oden
 * 创建时间：2018/4/18
 */
public class RxHttpException {

    /**
     * INVALID_PHONE("10000", "无效的手机号"),
     * <p>
     * // 用户注册
     * PHONE_IS_REGISTER("10001", "您输入的手机号已经被注册"),
     * PHONE_NOT_REGISTER("10002", "您输入的手机号未注册"),
     * AUTH_CHECK_FAIL("10003", "权限校验失败"),
     * USER_NOT_EXIST("10004", "找不到对应的用户信息"),
     * <p>
     * //前台用户登录
     * CODE_NOT_EXISTS("10010", "此手机号对应的验证码不存在"),
     * ERROR_CODE("10011", "验证码不正确"),
     * <p>
     * // app用户密码登录
     * INVALID_ACCOUNT("10020", "无效账号（手机号不存在）"),
     * PASSWORD_NOT_SET("10021", "未设置登录密码"),
     * PASSWORD_ERROR("10022", "密码错误"),
     * <p>
     * // 第三方登录
     * INVALID_USER_TYPE("10030", "无效登录类型"),
     * USER_TYPE_IS_BIND("10031", "您输入的手机号已经被绑定"),
     * <p>
     * //用户模块
     * USER_ID_EMPTY("10021", "用户ID不能为空"),
     * <p>
     * //10000-19999 管理员登录
     * ADMIN_NOT_EXISTS("10100", "管理员账号不存在"),
     * ADMIN_ERROR_PASSWORD("10101", "密码有误");
     */


    public String handleResponseError(Throwable t) {
        //这里不光是只能打印错误,还可以根据不同的错误作出不同的逻辑处理
        String msg = "请求网络失败";
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        } else if (t instanceof ConnectException) {
            msg = "连接服务器失败";
        }
//        if (!TextUtils.isEmpty(t.getMessage())) {
//            msg = t.getMessage();
//        }
        if ("timeout".equals(t.getMessage())) {
            msg = "请求网络超时";
        }
//        if ("Failed".startsWith(t.getMessage())) {
//            msg = "请求网络失败";
//        }
        return msg;
    }

    private String convertStatusCode(HttpException httpException) {

        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

}
