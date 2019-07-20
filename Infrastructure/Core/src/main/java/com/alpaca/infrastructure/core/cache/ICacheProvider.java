package com.alpaca.infrastructure.core.cache;

import java.util.List;

/**
 * @Author lichenw
 * @Created 2019/7/1 15:01
 */
public interface ICacheProvider<T> {

    T put(String key, T value);

    T get(String key);

    T del(String key);

    List<T> getAll(String pattern);
}
