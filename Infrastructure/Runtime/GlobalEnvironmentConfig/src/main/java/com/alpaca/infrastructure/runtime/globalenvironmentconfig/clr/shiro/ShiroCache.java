package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.shiro;

import com.alpaca.infrastructure.core.cache.SysCache;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author lichenw
 * @Created 2019/7/1 16:04
 */
@Component
public class ShiroCache extends SysCache<Session> {

    static final String SHIRO_SESSION_CACHE_KEY_PREFIX = "SHIROSESSION";

    @Override
    public List<Session> getAll(String pattern) {
        return super.getAll(SHIRO_SESSION_CACHE_KEY_PREFIX + pattern);
    }

    @Override
    public Session del(String key) {
        return super.del(SHIRO_SESSION_CACHE_KEY_PREFIX + key);
    }

    @Override
    public Session get(String key) {
        return super.get(SHIRO_SESSION_CACHE_KEY_PREFIX + key);
    }

    @Override
    public Session put(String key, Session value) {
        return super.put(SHIRO_SESSION_CACHE_KEY_PREFIX + key, value);
    }

    public List<Session> getAll() {
        return getAll("*");
    }
}
