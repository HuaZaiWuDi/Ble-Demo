package lab.wesmartclothing.wefit.netlib.rx;


import android.app.Application;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lab.wesmartclothing.wefit.netlib.utils.RxThreadUtils;

/**
 * 项目名称：BleCar
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/30 11:06
 */
public class RxManager {
    private static RxManager rxManager = null;

    String TAG = "[RxManager]";

    Application application;

    private RxManager() {

    }

    public synchronized static RxManager getInstance() {
        if (rxManager == null) {
            rxManager = new RxManager();
        }
        return rxManager;
    }

    public void setAPPlication(Application application) {
        this.application = application;
    }


    public void doUnifySubscribe(Observable<HttpResult> observable, RxNetSubscriber<HttpResult> subscriber) {
        observable
                .map(new Function<HttpResult, HttpResult>() {
                    @Override
                    public HttpResult apply(HttpResult httpResult) throws Exception {
                        if (!httpResult.isStatus()) {
//                            L.d("运行异常");
                        }
                        return httpResult;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public <T> Observable<T> dofunSubscribe(Observable<T> observable) {
        return observable
                .throttleFirst(1, TimeUnit.SECONDS)  //两秒只发生第一时间
                .timeout(1, TimeUnit.SECONDS)   //两秒超时重新发送
                .retry(2)  //重试两次
                .compose(RxThreadUtils.<T>rxThreadHelper());
    }



    public <String> Observable<String> doNetSubscribe(Observable<String> observable) {
        return observable
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(RxThreadUtils.<String>rxThreadHelper());
    }


}
