package com.alpaca.infrastructure.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @author Lichenw
 * Created on 2019/7/9
 */
@Configuration
public class RequestContextListenerConfig {

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
}
