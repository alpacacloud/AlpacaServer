package com.alpaca.components.authmgr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.alpaca.infrastructure.core.auth.IAuthService;
import com.alpaca.infrastructure.core.utils.GuidHelper;
import com.alpaca.infrastructure.core.utils.LocalDateHelper;
import com.alpaca.infrastructure.core.utils.encrypt.Sha;
import com.alpaca.infrastructure.core.auth.UserType;
import com.alpaca.components.authmgr.domain.login.LoginUserInfo;
import com.alpaca.components.authmgr.entity.Role;
import com.alpaca.components.authmgr.entity.User;
import com.alpaca.components.authmgr.entity.Userrolemapping;
import com.alpaca.components.authmgr.entity.Wxusermapping;
import com.alpaca.components.authmgr.mapper.PrivilegesDao;
import com.alpaca.components.authmgr.mapper.RoleDao;
import com.alpaca.components.authmgr.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hero
 * @date
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegesDao privilegesDao;

    @Autowired
    private WxusermappingService wxusermappingService;

    @Autowired
    private UserrolemappingService userrolemappingService;

    @Override
    public SimpleAuthorizationInfo getSimpleAuthorizationInfo(PrincipalCollection principalCollection) {
        Session session = SecurityUtils.getSubject().getSession();
        SimpleAuthorizationInfo simpleAuthorizationInfo = (SimpleAuthorizationInfo) session.getAttribute("SIMPLEAUTHENTICATIONINFO");
        return simpleAuthorizationInfo;
    }

    @Override
    public SimpleAuthenticationInfo getSimpleAuthenticationInfo(AuthenticationToken authenticationToken) {
        Session session = SecurityUtils.getSubject().getSession();

        UserType ut = (UserType) session.getAttribute("UT");
        User user = null;
        if (UserType.WEB.equals(ut)) {
            user = webUserLogin(authenticationToken);
        } else if (UserType.WXXCX.equals(ut)) {
            user = xcxUserLogin(authenticationToken, ut);
        } else if (UserType.WXGZH.equals(ut)) {
            user = xcxUserLogin(authenticationToken, ut);
        }
        LoginUserInfo userInfo = new LoginUserInfo();

        userInfo.setPersionName(user.getPersonName());

        userInfo.setRoles(roleDao.queryRoleByUserId(user.getId()));

        userInfo.setPrivileges(privilegesDao.queryPrivilegesByUserId(user.getId()));

        session.setAttribute("USERINFO", userInfo);
        session.setAttribute("ACAULUSER", user);

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), authenticationToken.getCredentials(), "DefaultRo");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        userInfo.getRoles().forEach(t -> simpleAuthorizationInfo.addRole(t.getTag()));
        userInfo.getPrivileges().forEach(t -> simpleAuthorizationInfo.addStringPermission(t.getTag()));
        session.setAttribute("SIMPLEAUTHENTICATIONINFO", simpleAuthorizationInfo);

        return simpleAuthenticationInfo;
    }

    private User webUserLogin(AuthenticationToken authenticationToken) {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("LoginName", username);
        List<User> list = userService.list(userQueryWrapper);

        if (list.size() < 1) {
            throw new ShiroException("用户不存在");
        }

        String encPwd = "";
        try {
            encPwd = Sha.encryptSHA(password);
        } catch (Exception e) {
            throw new ShiroException(e);
        }

        User user = list.get(0);

        if (!encPwd.equals(user.getLoginPwd())) {
            throw new ShiroException("用户名密码错误");
        }
        return user;
    }

    private User xcxUserLogin(AuthenticationToken authenticationToken, UserType userType) {
        String openid = (String) authenticationToken.getPrincipal();
        Wxusermapping wxusermapping = wxusermappingService.getOne(new QueryWrapper<Wxusermapping>().eq("openid", openid).eq("usertype", userType.toString()), false);
        if (wxusermapping == null) {
            initUser(openid, userType);
            wxusermapping = wxusermappingService.getOne(new QueryWrapper<Wxusermapping>().eq("openid", openid).eq("usertype", userType.toString()));
        }
        return userService.getById(wxusermapping.getUserId());
    }

    private void initUser(String openId, UserType userType) {
        User user = new User();
        user.setId(GuidHelper.getGuid());
        user.setLoginName("wxuser" + user.getId());
        user.setLoginPwd("");
        user.setPersonName("用户" + user.getId());
        user.setLogicStatus(0);
        user.setLastLoginTime(LocalDateHelper.getCurrent());

        Role xcxRole = roleService.getOne(new QueryWrapper<Role>().eq("tag", userType.toString()), false);
        Userrolemapping userrolemapping = new Userrolemapping();
        userrolemapping.setId(GuidHelper.getGuid());
        userrolemapping.setUserId(user.getId());
        userrolemapping.setRoleId(xcxRole.getId());

        Wxusermapping wxusermapping = new Wxusermapping();
        wxusermapping.setMid(GuidHelper.getGuid());
        wxusermapping.setOpenid(openId);
        wxusermapping.setUserId(user.getId());
        wxusermapping.setUsertype(userType.toString());

        userService.saveOrUpdate(user);
        userrolemappingService.saveOrUpdate(userrolemapping);
        wxusermappingService.saveOrUpdate(wxusermapping);

    }
}
