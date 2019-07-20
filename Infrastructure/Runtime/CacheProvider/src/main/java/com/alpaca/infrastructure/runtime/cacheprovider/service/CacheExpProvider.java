package com.alpaca.infrastructure.runtime.cacheprovider.service;

import com.alpaca.infrastructure.core.cache.ICacheExpireProvider;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author lichenw
 * @Created 2019/7/1 15:33
 */
@Repository("CacheExpProvider")
public class CacheExpProvider<T> implements ICacheExpireProvider<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    @Override
    public T put(String key, T value, long expMillisecond) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expMillisecond, TimeUnit.MILLISECONDS);
        return value;
    }

    @Override
    public T put(String key, T value) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, 30 * 60 * 1000, TimeUnit.MILLISECONDS);
        return value;
    }

    @Override
    public T get(String key) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    @Override
    public T del(String key) {
        T result = get(key);
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        RedisOperations<String, T> operations = valueOperations.getOperations();
        operations.delete(key);
        return result;
    }

    @Override
    public List<T> getAll(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.multiGet(keys);
    }
}
