package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.shiro;

import com.alpaca.infrastructure.core.utils.AuthHelper;
import com.alpaca.infrastructure.core.utils.DataUtil;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @Author ：lichenw
 * @Date ：Created in 16:33 2019/3/31
 * @Description：
 * @Modified By：
 */
public class SessionManager extends DefaultWebSessionManager {


    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String token = AuthHelper.getAuthId();
        if (DataUtil.isNotEmpty(token)) {
            return token;
        }
        return super.getSessionId(request, response);
    }
}
