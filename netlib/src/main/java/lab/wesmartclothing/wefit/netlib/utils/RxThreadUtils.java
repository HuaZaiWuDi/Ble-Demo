package lab.wesmartclothing.wefit.netlib.utils;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;

import com.zchu.rxcache.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 项目名称：MyProject
 * 类描述：使用方法compose（），此操作符是操作整个流，给我一个observable，还给你一个同样的observable
 * 创建人：oden
 * 创建时间：2018/4/17
 */
public class RxThreadUtils {


    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return FlowableTransformer
     */
    public static <T> FlowableTransformer<T, T> rxThreaHelper() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> flowable) {
                return flowable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> rxThreadHelper() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> rxIoThreadHelper() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.single());
            }
        };
    }

    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> showDialog(final Dialog dialog) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog.show();
                    }
                }).doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                });
            }
        };
    }

    /**
     * 得到 Observable
     *
     * @param <T> 指定的泛型类型
     * @return Observable
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    Log.e("createData", "subscribe: ", e);
                }
            }
        });
    }

    /**
     * 绑定生命周期，在AC和Fragment在销毁时结束网络请求
     *
     * @param <T> 指定的泛型类型
     * @return Observable
     * <p>
     * takeUtil，很显然，observable.takeUtil(condition)，当condition == true时终止，且包含临界条件的item
     */
    public static <T> ObservableTransformer<T, T> bindLife(final BehaviorSubject<LifeCycleEvent> subject) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.takeUntil(subject.skipWhile(new Predicate<LifeCycleEvent>() {
                    @Override
                    public boolean test(LifeCycleEvent activityLifeCycleEvent) throws Exception {
                        return activityLifeCycleEvent != LifeCycleEvent.DESTROY && activityLifeCycleEvent != LifeCycleEvent.DETACH;
                    }
                }));
            }
        };
    }


//    /**
//     * 对结果进行预处理
//     *
//     * @param <T>
//     * @return
//     */
//    public static <T> ObservableTransformer<T, T> handleResult() {
//        return new ObservableTransformer<T, T>() {
//            @Override
//            public ObservableSource<T> apply(Observable<T> upstream) {
//                return upstream.flatMap(new Function<T, ObservableSource<T>>() {
//                    @Override
//                    public ObservableSource<T> apply(T t) throws Exception {
//                        if (t instanceof String) {
//                            JSONObject object = null;
//                            try {
//                                object = new JSONObject((String) t);
//                                int code = object.getInt("code");
//                                String msg = object.getString("msg");
//                                if (code == 0) {
//                                    String data = object.getString("data");
//                                    return createData((T) data);
//                                } else {
//                                    return Observable.error(new Throwable(msg));
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                return Observable.error(new Throwable("数据异常"));
//                            }
//                        }
//                        return Observable.error(new Throwable("数据异常"));
//                    }
//                });
//            }
//        };
//    }

    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> handleResult() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.flatMap(new Function<T, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(T t) throws Exception {
                        if (t instanceof String) {
                            JSONObject object = null;

                            try {
                                object = new JSONObject((String) t);
                                int code = object.getInt("code");
                                String msg = object.getString("msg");
                                if (code == 0) {
                                    String data = object.getString("data");
                                    if (TextUtils.isEmpty(data)) {
                                        return Observable.error(new Throwable("数据异常"));
                                    }
                                    return createData((T) data);
                                } else {
                                    return Observable.error(new ExplainException(msg, code));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return Observable.error(new Throwable("数据异常"));
                            }
                        }
                        return Observable.error(new Throwable("数据异常"));
                    }
                });
            }
        };
    }


    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
//    public static <T> ObservableTransformer<HttpResult<T>, T> handleResult2() {
//        return new ObservableTransformer<HttpResult<T>, T>() {
//            @Override
//            public ObservableSource<T> apply(Observable<HttpResult<T>> upstream) {
//                return upstream.map(new Function<HttpResult<T>, T>() {
//                    @Override
//                    public T apply(HttpResult<T> tHttpResult) throws Exception {
//                        if (tHttpResult.getCode() != 0) {
//                            throw new ExplainException(tHttpResult.getMessage(), tHttpResult.getMessage(), tHttpResult.getCode());
//                        }
//                        return tHttpResult.getData();
//                    }
//                });
//            }
//        };
//    }

    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<HttpResult<T>, T> handleResult2() {
        return new ObservableTransformer<HttpResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<HttpResult<T>> upstream) {
                return upstream.flatMap(new Function<HttpResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(HttpResult<T> t) throws Exception {
                        LogUtils.debug(t.toString());
                        if (t.getCode() == 0) {
                            return createData(t.getData());
                        } else {
                            return Observable.error(new ExplainException(t.getMessage(), t.getCode()));
                        }
                    }
                });
            }
        };
    }

}
