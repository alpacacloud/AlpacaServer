package com.alpaca.infrastructure.core.auth;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

public interface IAuthService {

    SimpleAuthorizationInfo getSimpleAuthorizationInfo(PrincipalCollection principalCollection);

    SimpleAuthenticationInfo getSimpleAuthenticationInfo(AuthenticationToken authenticationToken);
}
