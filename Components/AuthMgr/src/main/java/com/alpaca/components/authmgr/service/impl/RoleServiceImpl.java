package com.alpaca.components.authmgr.service.impl;

import com.alpaca.components.authmgr.entity.Role;
import com.alpaca.components.authmgr.mapper.RoleDao;
import com.alpaca.components.authmgr.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

}
