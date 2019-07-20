package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.globalmessagehandler;

import com.alibaba.fastjson.JSONObject;
import com.alpaca.infrastructure.core.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:04 2019/3/13
 * @Description：
 * @Modified By：
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全局异常捕捉处理_500
     *
     * @param
     * @return
     */
    @ExceptionHandler(Exception.class)
    public GlobalMessage exceptionHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Exception e) {
        return processGlobalMessage(servletRequest, servletResponse, e, GlobalMessage.ERROR_CODE, StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "Request processing exception, termination request processing!");

    }


    /**
     * 拦截捕捉IllegalArgumentException异常_501
     *
     * @param
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public GlobalMessage exdasfasdf(HttpServletRequest servletRequest, HttpServletResponse servletResponse, IllegalArgumentException e) {
        return processGlobalMessage(servletRequest, servletResponse, e, "501", "IllegalArgumentException");
    }


    /**
     * 拦截捕捉ConstraintViolationException异常_501
     * Spring boot 方法级参数验证, 在调用方法时如果参数或返回值无法满足对应的限制就无法完成调用(多用于服务间调用)
     *
     * @param servletRequest
     * @param servletResponse
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public GlobalMessage handleApiConstraintViolationException(HttpServletRequest servletRequest, HttpServletResponse servletResponse, ConstraintViolationException e) {
        return processGlobalMessage(servletRequest, servletResponse, e, GlobalMessage.ERROR_CODE, "ConstraintViolationException");
    }

    @ExceptionHandler(ShiroException.class)
    public GlobalMessage handleShiroException(HttpServletRequest servletRequest, HttpServletResponse servletResponse, ShiroException e) {
        return processGlobalMessage(servletRequest, servletResponse, e, "403", "Auth error");
    }


    /**
     * 全局处理自定义异常
     *
     * @param
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public GlobalMessage serviceException(HttpServletRequest servletRequest, HttpServletResponse servletResponse, ServiceException e) {
        return processGlobalMessage(servletRequest, servletResponse, e, GlobalMessage.ERROR_CODE, StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "Request processing exception, termination request processing!");
    }

    public GlobalMessage processGlobalMessage(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Exception e, String errorCode, String message) {
        GlobalMessage globalMessage = new GlobalMessage();
        globalMessage.setStatusCode(String.format("%s[%s]", errorCode, UUID.randomUUID().toString().toUpperCase()));
        globalMessage.setErrorMessage(message);

        ErrorRequestInfo requestInfo = new ErrorRequestInfo();

        requestInfo.setMethod(servletRequest.getMethod());
        requestInfo.setParameter(JSONObject.toJSONString(servletRequest.getParameterMap()));
        requestInfo.setPathInfo(servletRequest.getPathInfo());
        requestInfo.setQueryString(servletRequest.getQueryString());
        requestInfo.setRemoteAddr(servletRequest.getRemoteAddr());
        requestInfo.setRemoteHost(servletRequest.getRemoteHost());
        requestInfo.setRequestURI(servletRequest.getRequestURI());
        requestInfo.setRequestURL(servletRequest.getRequestURL().toString());
        requestInfo.setHeaders(new ArrayList<>());
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (true) {
            String headerName = headerNames.nextElement();
            if (StringUtils.isEmpty(headerName)) {
                break;
            }
            requestInfo.getHeaders().add(String.format("%s:%s", headerName, servletRequest.getHeader(headerName)));
        }


        logger.error(String.format("ErrorCode:%s, RequestInfo:%s", globalMessage.getStatusCode(), JSONObject.toJSONString(requestInfo)), e);
        return globalMessage;

    }


}
