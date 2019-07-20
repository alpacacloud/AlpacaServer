package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.shiro;

import com.alpaca.infrastructure.core.utils.AuthHelper;
import com.alpaca.infrastructure.core.utils.DataUtil;
import com.alpaca.infrastructure.core.utils.SpringUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Author lichenw
 * @Created 2019/7/1 16:31
 */
@Component
public class RedisShiroSessionDao extends EnterpriseCacheSessionDAO {

    private ShiroCache _shiroCache;

    private ShiroCache shiroCache(){
        if(_shiroCache == null){
            _shiroCache = SpringUtil.getBean(ShiroCache.class);
        }
        return _shiroCache;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        super.update(session);
        shiroCache().put(session.getId().toString(), session);
    }

    @Override
    public void delete(Session session) {
        super.delete(session);
        shiroCache().del(session.getId().toString());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return shiroCache().getAll();
    }

    @Override
    protected Serializable doCreate(Session session) {

        Serializable sessionid = super.doCreate(session);
        String topenid = AuthHelper.getData("TOPENID");
        if(DataUtil.isNotEmpty(topenid)){
            sessionid = topenid;
            ((SimpleSession)session).setId(sessionid);
        }
        shiroCache().put(sessionid.toString(), session);
        return sessionid;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (DataUtil.isEmpty(sessionId)) {
            return null;
        }
        Session session = super.doReadSession(sessionId);

        if (session == null) {
            session = shiroCache().get(sessionId.toString());
        }
        return session;
    }
}
