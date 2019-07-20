package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.globalmessagehandler;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:03 2019/3/13
 * @Description：
 * @Modified By：
 */
@Data
public class ErrorRequestInfo implements Serializable {
    private String requestURL;
    private String requestURI;
    private String queryString;
    private String remoteAddr;
    private String remoteHost;
    private String method;
    private String pathInfo;
    private String parameter;
    private List<String> headers;
}
