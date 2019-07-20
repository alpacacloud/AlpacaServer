package com.alpaca.infrastructure.runtime.cacheprovider.service;

import com.alpaca.infrastructure.core.cache.ICacheProvider;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author lichenw
 * @Created 2019/7/1 15:33
 */
@Repository("CacheProvider")
public class CacheProvider<T> implements ICacheProvider<T> {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public T put(String key, T value) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
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
