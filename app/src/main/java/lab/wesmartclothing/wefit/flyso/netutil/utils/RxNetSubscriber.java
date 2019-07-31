package lab.wesmartclothing.wefit.flyso.netutil.utils;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
        _onError(new RxHttpsException().handleResponseError(e),
                e instanceof ExplainException ? ((ExplainException) e).getCode() : -1);

    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ");
    }

    @Override
    public void onNext(T t) {
        Log.e(TAG, "onNext线程: " + Thread.currentThread().getName());
        _onNext(t);
    }

    protected abstract void _onNext(T t);

    protected void _onError(String error, int code) {

    }


}
