package com.alpaca.infrastructure.core.cache;

/**
 * @Author lichenw
 * @Created 2019/7/1 15:02
 */
public interface ICacheExpireProvider <T> extends ICacheProvider<T>{

    T put(String key, T value , long expMillisecond);
}
