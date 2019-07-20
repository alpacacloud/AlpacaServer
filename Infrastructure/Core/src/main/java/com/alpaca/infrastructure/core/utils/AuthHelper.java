package com.alpaca.infrastructure.core.utils;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lichenw
 * Created on 2019/7/9
 */
public final class AuthHelper {

    public static final String AUTH_TOKEN = "SXAUTHID";

    /**
     * 获取当前请求用户的AuthID
     *
     * @return
     */
    public static String getAuthId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ticket = request.getHeader(AUTH_TOKEN);
        return ticket;
    }

    public static void setData(String key, String data) {

        RequestContextHolder.currentRequestAttributes().setAttribute(key, data, 0);
    }

    public static String getData(String key) {

        Object attribute = RequestContextHolder.currentRequestAttributes().getAttribute(key, 0);
        if (attribute != null) {
            return attribute.toString();
        }
        return null;
    }
}
