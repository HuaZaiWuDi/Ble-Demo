package lab.wesmartclothing.wefit.netlib.utils;

import android.util.Log;

import io.reactivex.functions.Function;

public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

    @Override
    public T apply(HttpResult<T> tHttpResult) throws Exception {
        Log.e("error", tHttpResult.getData().toString() + "");
        if (tHttpResult.getCode() != 0) {
            throw new Exception(tHttpResult.getMessage());
        }
        return tHttpResult.getData();
    }
}
