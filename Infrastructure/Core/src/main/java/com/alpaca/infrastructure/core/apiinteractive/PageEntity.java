package com.alpaca.infrastructure.core.apiinteractive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ：lichenw
 * @Date ：Created in 0:42 2019/3/27
 * @Description：
 * @Modified By：
 */
@Data
public class PageEntity<T>  {
    @ApiModelProperty(value = "混合参数", name = "conditions", example = "XXX", required = true)
    protected T parameter;

    @ApiModelProperty(value = "第几页", name = "pageNo", example = "1", required = true)
    protected int pageNo;

    @ApiModelProperty(value = "每页显示几条", name = "pageSize", example = "10", required = true)
    protected int pageSize;

    @ApiModelProperty(value = "排序字段", name = "orderBy", example = "create_date", required = false)
    private String orderBy = "";

    @ApiModelProperty(value = "1、升序 0、降序", name = "isAsc", example = "0", required = false)
    private String isAsc = "";


}
