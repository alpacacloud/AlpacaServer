package com.alpaca.components.authmgr.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色信息
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value="Role对象", description="角色信息")
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID")
    @TableId("id")
    private String id;

    @ApiModelProperty(value = "角色名称")
    @TableField("RoleName")
    private String RoleName;

    @ApiModelProperty(value = "排序码")
    @TableField("irank")
    private Integer irank;

    @ApiModelProperty(value = "状态码")
    @TableField("LogicStatus")
    @TableLogic
    private Integer LogicStatus;

    @ApiModelProperty(value = "标识")
    @TableField("tag")
    private String tag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
