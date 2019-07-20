package com.alpaca.infrastructure.core.cache;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author lichenw
 * @Created 2019/7/1 15:22
 */
public abstract class SysCache<T> {
    @Resource(name = "CacheProvider")
    ICacheProvider<T> cacheProvider;

    public T put(String key, T value) {
        return cacheProvider.put(key, value);
    }

    public T get(String key) {
        return cacheProvider.get(key);
    }

    public T del(String key) {
        return cacheProvider.del(key);
    }

    public List<T> getAll(String pattern) {
        return cacheProvider.getAll(pattern);
    }

}
