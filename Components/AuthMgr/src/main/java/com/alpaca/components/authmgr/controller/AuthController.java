package com.alpaca.components.authmgr.controller;

import com.alpaca.infrastructure.core.utils.HttpHelper;
import com.alpaca.infrastructure.core.config.WxmpConfig;
import com.alpaca.infrastructure.core.auth.UserType;
import com.alpaca.components.authmgr.domain.login.LoginRequest;
import com.alpaca.components.authmgr.domain.login.LoginResult;
import com.alpaca.components.authmgr.domain.login.LoginUserInfo;
import com.alpaca.components.authmgr.domain.wxlogin.WxLoginResult;
import com.alpaca.components.authmgr.domain.wxlogin.wxapi.AccessTokenResult;
import com.alpaca.components.authmgr.domain.wxlogin.wxapi.Code2SessionResult;
import com.alpaca.components.authmgr.domain.wxlogin.WxLoginRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private WxmpConfig wxmpConfig;

    @PostMapping("/login")
    public LoginResult login(@RequestBody LoginRequest request) throws Exception {
        LoginResult result = new LoginResult();

        Subject subject = SecurityUtils.getSubject();

        Session session1 = subject.getSession();
        session1.setAttribute("UT", UserType.WEB);
        String pwd = new String(Base64.decodeBase64(request.getPassword().getBytes("utf-8")), "utf-8");

        UsernamePasswordToken token = new UsernamePasswordToken(request.getLoginName(), pwd);

        try {
            subject.login(token);
        } catch (Exception e) {
            result.setLoginStatus(-1);
            result.setMessage(e.getCause().getMessage());

            return result;
        }

        Session session = SecurityUtils.getSubject().getSession();
        LoginUserInfo userInfo = (LoginUserInfo) session.getAttribute("USERINFO");
        result.setUserInfo(userInfo);
        result.setLoginStatus(1);
        result.setMessage("Login Scuess!");
        result.setToken(session.getId().toString());
        return result;
    }

    @RequiresPermissions("test1")
    @GetMapping("/msg")
    public String getmsg() {
        return UUID.randomUUID().toString();
    }

    @PostMapping("/wxlogin")
    public WxLoginResult wxlogin(WxLoginRequest request) {
        Code2SessionResult code2SessionResult = getCode2SessionResult(request);

        Subject subject = SecurityUtils.getSubject();
        Session session1 = subject.getSession();
        session1.setAttribute("SESSIONKEY", code2SessionResult.getSession_key());
        session1.setAttribute("UT", UserType.WXXCX);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(code2SessionResult.getOpenid(), "");
        try {
            subject.login(usernamePasswordToken);
        } catch (Exception e) {

        }

        WxLoginResult result = new WxLoginResult();
        result.setToken(SecurityUtils.getSubject().getSession().getId().toString());
        return result;
    }

    @PostMapping("/wxgzhlogin")
    public WxLoginResult wxgzhLogin(String openid){
        Subject subject = SecurityUtils.getSubject();
        Session session1 = subject.getSession();
        session1.setAttribute("SESSIONKEY",  openid);
        session1.setAttribute("UT", UserType.WXGZH);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(openid, "");
        try {
            subject.login(usernamePasswordToken);
        } catch (Exception e) {

        }

        WxLoginResult result = new WxLoginResult();
        result.setToken(SecurityUtils.getSubject().getSession().getId().toString());
        return result;
    }

    private AccessTokenResult getAccessTokenResult() {
        String wxAccess_tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", wxmpConfig.getAppid(), wxmpConfig.getSecret());
        return HttpHelper.HttpGet(wxAccess_tokenUrl, AccessTokenResult.class);
    }

    private Code2SessionResult getCode2SessionResult(WxLoginRequest request) {
        String wxCode2SessionUrl = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", wxmpConfig.getAppid(), wxmpConfig.getSecret(), request.getCode());

        return HttpHelper.HttpGet(wxCode2SessionUrl, Code2SessionResult.class);
    }


}
