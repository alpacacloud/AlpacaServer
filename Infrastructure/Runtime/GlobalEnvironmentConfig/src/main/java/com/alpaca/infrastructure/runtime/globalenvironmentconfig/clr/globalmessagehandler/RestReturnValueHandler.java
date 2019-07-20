package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.globalmessagehandler;

import com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.globalmessagehandler.GlobalMessage;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:05 2019/3/13
 * @Description：
 * @Modified By：
 */
public class RestReturnValueHandler implements HandlerMethodReturnValueHandler {
    private HandlerMethodReturnValueHandler proxy;

    public RestReturnValueHandler(HandlerMethodReturnValueHandler handlerMethodReturnValueHandler){
        proxy = handlerMethodReturnValueHandler;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return proxy.supportsReturnType(methodParameter);
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        GlobalMessage globalMessage = new GlobalMessage();
        globalMessage.setData(o);
        globalMessage.setStatusCode(GlobalMessage.SUCESS_CODE);
        proxy.handleReturnValue(globalMessage, methodParameter, modelAndViewContainer, nativeWebRequest);
    }
}
