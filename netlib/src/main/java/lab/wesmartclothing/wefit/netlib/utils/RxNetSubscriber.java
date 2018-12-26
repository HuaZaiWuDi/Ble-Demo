package lab.wesmartclothing.wefit.netlib.utils;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lab.wesmartclothing.wefit.netlib.utils.ExplainException;
import lab.wesmartclothing.wefit.netlib.utils.RxHttpsException;

/**
 * 项目名称：MyProject
 * 类描述：
 * 创建人：oden
 * 创建时间：2018/4/14
 */
public abstract class RxNetSubscriber<T> implements Observer<T> {
    String TAG = "【RxNetSubscriber】";

    @Override
    public void onSubscribe(Disposable d) {
        Log.e(TAG, "onSubscribe: ");
    }

    public RxNetSubscriber() {
    }


    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.toString());
//        if (BuildConfig.DEBUG)
//            Toast.makeText(RxManager.getInstance().getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        if (e instanceof ExplainException)
            _onError(((ExplainException) e).getMsg(), ((ExplainException) e).getCode());
        _onError(new RxHttpsException().handleResponseError(e));
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ");
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    protected abstract void _onNext(T t);


    protected void _onError(String error) {

    }

    protected void _onError(String error, int code) {

    }


}
