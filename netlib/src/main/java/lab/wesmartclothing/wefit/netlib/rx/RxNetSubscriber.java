package lab.wesmartclothing.wefit.netlib.rx;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 项目名称：MyProject
 * 类描述：
 * 创建人：oden
 * 创建时间：2018/4/14
 */
public abstract class RxNetSubscriber<T> implements Observer<T> {
    private Context mContext;
    String TAG = "【RxNetSubscriber】";

    @Override
    public void onSubscribe(Disposable d) {
    }

    public RxNetSubscriber() {
    }

    public RxNetSubscriber(Context context) {
        mContext = context;
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.toString());
        Toast.makeText(RxManager.getInstance().getApplication(), e.toString(), Toast.LENGTH_LONG).show();

//        _onError(new RxHttpException().handleResponseError(e));
//        _onError(new RxHttpException().handleResponseError(e), -1);
    }

    @Override
    public void onComplete() {

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
