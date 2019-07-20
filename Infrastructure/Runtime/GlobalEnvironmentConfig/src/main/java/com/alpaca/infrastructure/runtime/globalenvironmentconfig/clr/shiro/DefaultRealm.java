package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.shiro;

import com.alpaca.infrastructure.core.auth.IAuthService;
import com.alpaca.infrastructure.core.utils.SpringUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:24 2019/3/13
 * @Description：
 * @Modified By：
 */
public class DefaultRealm extends AuthorizingRealm {
    private IAuthService authService;

    public IAuthService getAuthService() {
        if (authService == null) {
            authService = SpringUtil.getBean(IAuthService.class);
        }
        return authService;
    }

    @Override
    public String getName() {
        return "bootRealm";
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return getAuthService().getSimpleAuthorizationInfo(principalCollection);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return getAuthService().getSimpleAuthenticationInfo(authenticationToken);
    }
}