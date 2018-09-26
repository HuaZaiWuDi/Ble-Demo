package com.vondear.rxtools.utils;

/**
 * Singleton helper class for lazily initialization.
 * <p>
 * 单例模式
 *
 * @param <T> 泛型
 * @author <a href="http://www.trinea.cn/" target="_blank">Trinea
 */
public abstract class SingletonUtils<T> {

    private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (SingletonUtils.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }
}