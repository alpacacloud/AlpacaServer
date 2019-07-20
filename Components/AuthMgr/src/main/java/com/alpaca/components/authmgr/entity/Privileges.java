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
 * 权限列表
 * </p>
 *
 * @author lichenw
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_privileges")
@ApiModel(value="Privileges对象", description="权限列表")
public class Privileges extends Model<Privileges> {

    private static final long serialVersionUID = 1L;

    @TableId("Id")
    private String Id;

    @TableField("PriName")
    private String PriName;

    @TableField("tag")
    private String tag;

    @TableField("irank")
    private Integer irank;

    @TableField("LogicStatus")
    @TableLogic
    private Integer LogicStatus;


    @Override
    protected Serializable pkVal() {
        return this.Id;
    }

}
