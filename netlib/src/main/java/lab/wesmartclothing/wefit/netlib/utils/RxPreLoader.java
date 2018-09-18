package lab.wesmartclothing.wefit.netlib.utils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Tony Shen on 2017/6/2.
 */

public class RxPreLoader<T> {

    //能够缓存订阅之前的最新数据
    private BehaviorSubject<T> mData;
    private Disposable disposable;

    public RxPreLoader(T defaultValue) {

        mData = BehaviorSubject.createDefault(defaultValue);
    }

    /**
     * 发送事件
     *
     * @param object
     */
    public void publish(T object) {
        mData.onNext(object);
    }

    /**
     * 订阅事件
     *
     * @param onNext
     * @return
     */
    public Disposable subscribe(Consumer onNext) {
        disposable = mData.subscribe(onNext);
        return disposable;
    }

    /**
     * 反订阅
     */
    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    /**
     * 获取缓存数据的Subject
     *
     * @return
     */
    public BehaviorSubject<T> getCacheDataSubject() {
        return mData;
    }

    /**
     * 直接获取最近的一个数据
     *
     * @return
     */
    public T getLastCacheData() {
        return mData.getValue();
    }
}
