package com.alpaca.infrastructure.runtime.globalenvironmentconfig.config;

import com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.globalmessagehandler.RestReturnValueHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:05 2019/3/13
 * @Description：
 * @Modified By：
 */
@Configuration
public class RestReturnValueHandlerConfigurer implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> regReturnValueHandlers = new ArrayList<>();
        if (returnValueHandlers != null) {
            returnValueHandlers.forEach(handlerMethodReturnValueHandler -> {
                if (handlerMethodReturnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                    RestReturnValueHandler restReturnValueHandler = new RestReturnValueHandler(handlerMethodReturnValueHandler);
                    regReturnValueHandlers.add(restReturnValueHandler);
                } else {
                    regReturnValueHandlers.add(handlerMethodReturnValueHandler);
                }
            });
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(regReturnValueHandlers);
    }
}
