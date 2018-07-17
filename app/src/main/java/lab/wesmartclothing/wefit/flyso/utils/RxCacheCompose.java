package lab.wesmartclothing.wefit.flyso.utils;

import com.vondear.rxtools.model.cache.ACache;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by jk on 2018/7/17.
 */
public class RxCacheCompose {

    public ACache mACache;


    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> cacheAndRemote(final ACache mACache, final String key) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }).doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {

                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        observable.next()
                    }
                }).doAfterNext(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        if (mACache != null)
                            mACache.put(key, (String) t);
                    }
                });
            }
        };
    }

}
