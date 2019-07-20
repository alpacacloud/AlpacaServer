package com.alpaca.components.filemgr.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 附件表
 * </p>
 *
 * @author lichenw
 * @since 2019-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_attachment")
@ApiModel(value="Attachment对象", description="附件表")
public class Attachment extends Model<Attachment> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "附件ID")
    @TableId("fid")
    private String fid;

    @ApiModelProperty(value = "业务ID")
    @TableField("bizID")
    private String bizID;

    @ApiModelProperty(value = "实际文件名称")
    @TableField("ActulName")
    private String ActulName;

    @ApiModelProperty(value = "实际文件路径 ")
    @TableField("ActulPath")
    private String ActulPath;

    @ApiModelProperty(value = "扩展名")
    @TableField("Exten")
    private String Exten;

    @TableField("LogicStatus")
    @TableLogic
    private Integer LogicStatus;

    @ApiModelProperty(value = "文件状态")
    @TableField("fileState")
    private Integer fileState;

    @ApiModelProperty(value = "模块")
    @TableField("Module")
    private String Module;

    @ApiModelProperty(value = "创建时间")
    @TableField("CreateTime")
    private LocalDateTime CreateTime;

    @ApiModelProperty(value = "是否为图片")
    @TableField("IsPic")
    private Integer IsPic;

    @ApiModelProperty(value = "缩略图路径")
    @TableField("ThumbPath")
    private String ThumbPath;

    @ApiModelProperty(value = "文件大小")
    @TableField("FileSize")
    private Long FileSize;

    @ApiModelProperty(value = "文件名")
    @TableField("FileName")
    private String FileName;


    @Override
    protected Serializable pkVal() {
        return this.fid;
    }

}
