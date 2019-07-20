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
 * 用户角色映射表
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_userrolemapping")
@ApiModel(value="Userrolemapping对象", description="用户角色映射表")
public class Userrolemapping extends Model<Userrolemapping> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户角色映射表")
    @TableId("id")
    private String id;

    @ApiModelProperty(value = "用户Id")
    @TableField("UserId")
    private String UserId;

    @ApiModelProperty(value = "角色Id")
    @TableField("RoleId")
    private String RoleId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
