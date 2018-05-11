package lab.dxythch.com.netlib.rx;


import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.reflect.Field;

import lab.dxythch.com.netlib.BuildConfig;
import lab.dxythch.com.netlib.net.ServiceAPI;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 项目名称：BleCar
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/30 11:57
 */
public class NetManager {
    private static NetManager netManager = null;
    private static Retrofit retrofit = null;

    private NetManager() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ServiceAPI.BASE_URL)
                .build();
    }

    public synchronized static NetManager getInstance() {
        if (netManager == null) {
            netManager = new NetManager();
        }
        return netManager;
    }

    public <S> S create(Class<S> service) {
        return retrofit.create(service);
    }

    /**
     * 返回原始的值
     *
     * @param service
     * @param <S>
     * @return
     */
    public <S> S createString(Class<S> service) {

        Interceptor NetInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request.header("Content-Type: application/json");
                request.header("Accept: application/json");
                return chain.proceed(request);
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            //日志显示级别
            HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BASIC;
            //新建log拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.w("【NetManager】", message);
                }
            });
            loggingInterceptor.setLevel(level);
            //OkHttp进行添加拦截器loggingInterceptor
            builder.addInterceptor(loggingInterceptor);
        }
        builder.addInterceptor(NetInterceptor);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .baseUrl(getBaseUrl(service))
                .build();
        return retrofit.create(service);
    }

    /**
     * URL不一致时调用
     *
     * @param service
     * @param <S>
     * @return
     */
    public <S> S createWithUrl(Class<S> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getBaseUrl(service))
                .build();
        return retrofit.create(service);
    }

    /**
     * 解析接口中的BASE_URL，解决BASE_URL不一致的问题
     *
     * @param service
     * @param <S>
     * @return
     */
    private <S> String getBaseUrl(Class<S> service) {
        try {
            Field field = service.getField("BASE_URL");
            return (String) field.get(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
