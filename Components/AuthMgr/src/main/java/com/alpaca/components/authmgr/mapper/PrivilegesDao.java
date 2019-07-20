package com.alpaca.components.authmgr.mapper;

import com.alpaca.components.authmgr.entity.Privileges;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 权限列表 Mapper 接口
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
public interface PrivilegesDao extends BaseMapper<Privileges> {

    @Select("select * from sys_privileges t where exists (SELECT 1 from sys_roleprivilegemapping rpm where rpm.PrivilegeId = t.id and exists( SELECT 1 from sys_userrolemapping urm where urm.RoleId = rpm.RoleId and urm.userid=#{userId}))")
    List<Privileges> queryPrivilegesByUserId(String userId);

}
