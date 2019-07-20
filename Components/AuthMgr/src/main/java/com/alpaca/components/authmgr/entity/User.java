package com.alpaca.components.authmgr.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * 用户表
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="User对象", description="用户表")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("Id")
    private String Id;

    @ApiModelProperty(value = "姓名")
    @TableField("PersonName")
    private String PersonName;

    @ApiModelProperty(value = "登录名")
    @TableField("LoginName")
    private String LoginName;

    @ApiModelProperty(value = "登录密码")
    @TableField("LoginPwd")
    private String LoginPwd;

    @ApiModelProperty(value = "登录次数")
    @TableField("LoginCount")
    private Integer LoginCount;

    @ApiModelProperty(value = "最后登录时间")
    @TableField("LastLoginTime")
    private LocalDateTime LastLoginTime;

    @ApiModelProperty(value = "状态")
    @TableField("LogicStatus")
    @TableLogic
    private Integer LogicStatus;


    @Override
    protected Serializable pkVal() {
        return this.Id;
    }

}
