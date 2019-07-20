package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.globalmessagehandler;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:04 2019/3/13
 * @Description：
 * @Modified By：
 */
@Data
public class GlobalMessage<T> implements Serializable {
    private String errorMessage;
    private String statusCode;
    private T data;
    private String version;

    /**
     * 结果正确消息码
     */
    public static final String SUCESS_CODE="200";
    /**
     * 默认错误码
     */
    public static final String ERROR_CODE="500";
}


