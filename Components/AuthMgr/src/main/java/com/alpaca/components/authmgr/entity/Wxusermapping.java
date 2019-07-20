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
 * 
 * </p>
 *
 * @author lichenw
 * @since 2019-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_wxusermapping")
@ApiModel(value="Wxusermapping对象", description="")
public class Wxusermapping extends Model<Wxusermapping> {

    private static final long serialVersionUID = 1L;

    @TableId("mid")
    private String mid;

    @TableField("openid")
    private String openid;

    @TableField("userId")
    private String userId;

    @TableField("usertype")
    private String usertype;


    @Override
    protected Serializable pkVal() {
        return this.mid;
    }

}
