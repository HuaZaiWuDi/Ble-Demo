package lab.wesmartclothing.wefit.flyso.netutil.utils;


import android.app.Application;

import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.IObservableStrategy;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

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

    public Application getApplication() {
        return application;
    }


    public <T> Observable<T> dofunSubscribe(Observable<T> observable) {
        return observable
                .throttleFirst(1, TimeUnit.SECONDS)  //两秒只发生第一时间
                .timeout(1, TimeUnit.SECONDS)   //两秒超时重新发送
                .retry(2)  //重试两次
                .compose(RxComposeUtils.<T>rxThreadHelper());
    }


    public <T> Observable<T> doNetSubscribe(Observable<T> observable) {
        return observable
                .compose(RxComposeUtils.<T>handleResult())
                .compose(RxComposeUtils.<T>rxThreadHelper())
                ;
    }

    public <T> Observable<T> doNetSubscribe(Observable<HttpResult<T>> observable,
                                            BehaviorSubject<LifeCycleEvent> lifecycleSubject,
                                            String cacheKey,
                                            Class type,
                                            IObservableStrategy strategy
    ) {
        return observable
                .compose(RxComposeUtils.<T>handleResult2())
                .compose(RxComposeUtils.<T>rxThreadHelper())
                .compose(RxComposeUtils.<T>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<T>transformObservable(cacheKey, type, strategy))
                .map(new CacheResult.MapFunc<T>())
                ;
    }

    public <T> Observable<T> doNetSubscribe(Observable<HttpResult<T>> observable,
                                            BehaviorSubject<LifeCycleEvent> lifecycleSubject
    ) {
        return observable
                .compose(RxComposeUtils.<T>handleResult2())
                .compose(RxComposeUtils.<T>rxThreadHelper())
                .compose(RxComposeUtils.<T>bindLife(lifecycleSubject));
    }


    public <T> Observable<T> doLoadDownSubscribe(Observable<T> observable) {
        return observable
                .compose(RxComposeUtils.<T>rxThreadHelper());
    }

}
