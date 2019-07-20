package com.alpaca.infrastructure.core.auth;

/**
 * @Author lichenw
 * @Created 2019/5/10 9:10
 */
public interface IAuthUserService {
    /**
     * 获取当前登陆用户
     * @return
     */
    GlobalUserInfo getCurrentLoginUser();
}
