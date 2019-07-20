package com.alpaca.components.authmgr.mapper;

import com.alpaca.components.authmgr.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色信息 Mapper 接口
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
public interface RoleDao extends BaseMapper<Role> {
    @Select("select t.* from sys_role t where exists (SELECT * from sys_userrolemapping where RoleId = t.id and UserId = #{userId})")
    List<Role> queryRoleByUserId(String userId);
}
