package com.alpaca.components.authmgr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.alpaca.infrastructure.core.auth.GlobalUserInfo;
import com.alpaca.infrastructure.core.auth.IAuthUserService;
import com.alpaca.components.authmgr.domain.login.LoginUserInfo;
import com.alpaca.components.authmgr.entity.User;
import com.alpaca.components.authmgr.entity.Wxusermapping;
import com.alpaca.components.authmgr.service.WxusermappingService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author lichenw
 * @Created 2019/5/10 9:12
 */
@Service
public class AuthUserSerivcesImpl implements IAuthUserService {
    @Autowired
    private WxusermappingService wxusermappingService;

    @Override
    public GlobalUserInfo getCurrentLoginUser() {
        Session session = SecurityUtils.getSubject().getSession();
        User userInfo = (User) session.getAttribute("ACAULUSER");
        if (userInfo != null) {
            GlobalUserInfo globalUserInfo = new GlobalUserInfo();
            globalUserInfo.setUserId(userInfo.getId());
            globalUserInfo.setUserName(userInfo.getPersonName());

            Wxusermapping wxusermapping = wxusermappingService.getOne(new QueryWrapper<Wxusermapping>().eq("userId", globalUserInfo.getUserId()), false);

            if (wxusermapping != null) {
                globalUserInfo.setOpenId(wxusermapping.getOpenid());
            }

            return globalUserInfo;
        }
        return null;
    }
}
