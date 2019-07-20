package com.alpaca.infrastructure.runtime.corssdomain;

import com.alpaca.infrastructure.core.config.FileSystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:07 2019/3/13
 * @Description：
 * @Modified By：
 */
@Configuration
public class DefaultWebMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    private CorssDomainInterceptor corssDomainInterceptor;

    @Autowired
    private FileSystemConfig fileSystemConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corssDomainInterceptor)
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/fs/gl/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/fs/gl/**").addResourceLocations("file:"+fileSystemConfig.getPath());
    }

}
