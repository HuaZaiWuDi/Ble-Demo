package lab.wesmartclothing.wefit.netlib.rx;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lab.wesmartclothing.wefit.netlib.utils.RxHttpException;

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
        _onError(new RxHttpException().handleResponseError(e));
        _onError(new RxHttpException().handleResponseError(e), -1);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        if (t instanceof String) {
            JSONObject object = null;
            try {
                object = new JSONObject((String) t);
                String retCode = object.getString("code");
                String msg = object.getString("msg");
                int status = Integer.parseInt(retCode);
                if (status == 0) {
                    String data = object.getString("data");
                    if (data != null)
                        _onNext((T) data);
                    else {
                        _onNext((T) msg);
                    }
                } else {
                    _onError(msg);
                    _onError(msg, status);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                onError(e);
            }
        }
        Log.v(TAG, "onNext: " + t);
    }

    protected abstract void _onNext(T t);


    protected void _onError(String error) {

    }

    protected void _onError(String error, int code) {

    }
}
