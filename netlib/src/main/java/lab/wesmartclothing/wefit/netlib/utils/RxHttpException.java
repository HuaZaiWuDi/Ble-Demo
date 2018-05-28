package lab.wesmartclothing.wefit.netlib.utils;

import android.net.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

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

    public String handleResponseError(Throwable t) {
        //这里不光是只能打印错误,还可以根据不同的错误作出不同的逻辑处理
        String msg = "未知错误";
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
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
