package com.alpaca.components.authmgr.service.impl;

import com.alpaca.components.authmgr.entity.User;
import com.alpaca.components.authmgr.mapper.UserDao;
import com.alpaca.components.authmgr.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

}
