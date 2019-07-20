package com.alpaca.components.authmgr.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色权限映射表
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_roleprivilegemapping")
@ApiModel(value="Roleprivilegemapping对象", description="角色权限映射表")
public class Roleprivilegemapping extends Model<Roleprivilegemapping> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "映射Id")
    @TableId("id")
    private String id;

    @ApiModelProperty(value = "角色ID")
    @TableField("RoleId")
    private String RoleId;

    @ApiModelProperty(value = "权限Id")
    @TableField("PrivilegeId")
    private String PrivilegeId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
