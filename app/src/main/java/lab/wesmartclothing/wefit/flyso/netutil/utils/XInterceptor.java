package lab.wesmartclothing.wefit.flyso.netutil.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vondear.rxtools.utils.RxNetUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/4 22:16
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class XInterceptor {

    /**
     * 自定义的，重试N次的拦截器
     * 通过：addInterceptor 设置
     */
    public static class Retry implements Interceptor {

        public int maxRetry;//最大重试次数
        private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

        public Retry(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            Log.i("Retry", "num:" + retryNum);
            while (!response.isSuccessful() && retryNum < maxRetry) {
                retryNum++;
                Log.i("Retry", "num:" + retryNum);
                response = chain.proceed(request);
            }
            return response;
        }
    }

    /**
     * 设置没有网络的情况下，
     * 的缓存时间
     * 通过：addInterceptor 设置
     */
    public static class CommonNoNetCache implements Interceptor {

        private int maxCacheTimeSecond = 0;
        private Context applicationContext;

        public CommonNoNetCache(int maxCacheTimeSecond, Context applicationContext) {
            this.maxCacheTimeSecond = maxCacheTimeSecond;
            this.applicationContext = applicationContext;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            if (!RxNetUtils.isConnected(applicationContext)) {
                CacheControl tempCacheControl = new CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(maxCacheTimeSecond, TimeUnit.SECONDS)
                        .build();
                request = request.newBuilder()
                        .cacheControl(tempCacheControl)
                        .build();
            }
            return chain.proceed(request);
        }
    }

    /**
     * 设置在有网络的情况下的缓存时间
     * 在有网络的时候，会优先获取缓存
     * 通过：addNetworkInterceptor 设置
     */
    public static class CommonNetCache implements Interceptor {

        private int maxCacheTimeSecond = 0;

        public CommonNetCache(int maxCacheTimeSecond) {
            this.maxCacheTimeSecond = maxCacheTimeSecond;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Response originalResponse = chain.proceed(request);
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxCacheTimeSecond)
                    .build();
        }
    }

    /**
     * 设置一个日志打印拦截器
     * 通过：addInterceptor 设置
     */
    public static class CommonLog implements Interceptor {

        //统一的日志输出控制，可以构造方法传入，统一控制日志
        private boolean logOpen = true;
        //log的日志TAG
        private String logTag = "CommonLog";

        public CommonLog() {
        }

        public CommonLog(boolean logOpen) {
            this.logOpen = logOpen;
        }

        public CommonLog(String logTag) {
            this.logTag = logTag;
        }

        public CommonLog(boolean logOpen, String logTag) {
            this.logOpen = logOpen;
            this.logTag = logTag;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Request request = chain.request();
            long t1 = System.currentTimeMillis();//请求发起的时间
            Response response = chain.proceed(request);
            long t2 = System.currentTimeMillis();//收到响应的时间

            if (logOpen) {
                //这里不能直接使用response.body().string()的方式输出日志
                //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
                //个新的response给应用层处理
                ResponseBody responseBody = response.peekBody(1024 * 1024);
                Log.i(logTag, response.request().url() + " , use-timeMs: " + (t2 - t1) + " , data: " + responseBody.string());
            }

            return response;
        }
    }
}