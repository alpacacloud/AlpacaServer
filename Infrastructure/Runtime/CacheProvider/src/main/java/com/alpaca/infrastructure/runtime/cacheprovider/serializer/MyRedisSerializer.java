package com.alpaca.infrastructure.runtime.cacheprovider.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import java.io.*;

/**
 * @Author lichenw
 * @Created 2019/7/1 15:51
 */
public class MyRedisSerializer implements RedisSerializer {
    @Nullable
    @Override
    public byte[] serialize(@Nullable Object o) throws SerializationException {
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(byteout);
            outputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteout.toByteArray();
    }

    @Nullable
    @Override
    public Object deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream;
        Object object;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            object = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return object;
    }
}
