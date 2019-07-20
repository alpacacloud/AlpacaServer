package com.alpaca.infrastructure.core.exception;

import lombok.Data;

/**
 * @author Lichenw
 * Created on 2019/6/1
 */
@Data
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 6890849143085620167L;

    /**
     * 错误状态码（业务）
     */
    private int code = 500;

    public ServiceException() {
        super();
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Exception e) {
        super(msg, e);
    }

}

