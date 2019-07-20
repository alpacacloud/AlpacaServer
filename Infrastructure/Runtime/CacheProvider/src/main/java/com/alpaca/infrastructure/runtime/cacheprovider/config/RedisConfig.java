package com.alpaca.infrastructure.runtime.cacheprovider.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alpaca.infrastructure.runtime.cacheprovider.serializer.MyRedisSerializer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author lichenw
 * @Created 2019/7/1 15:33
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String , Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        MyRedisSerializer myRedisSerializer = new MyRedisSerializer();

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        template.setKeySerializer(myRedisSerializer);
        template.setHashKeySerializer(myRedisSerializer);
        template.setDefaultSerializer(myRedisSerializer);

        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

}
