package lab.wesmartclothing.wefit.netlib.utils;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 项目名称：BleCar
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/30 11:27
 */
public abstract class RxSubscriber<T> implements Observer<T> {

    String TAG = "[RxSubscriber]";


    public RxSubscriber() {
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        _onNext(t);
        Log.v(TAG, "_onNext: " + t);

    }


    protected abstract void _onNext(T t);
}
