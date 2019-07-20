package com.alpaca.infrastructure.runtime.globalenvironmentconfig.clr.shiro;

import com.alpaca.infrastructure.core.config.FileSystemConfig;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:25 2019/3/13
 * @Description：
 * @Modified By：
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        webSecurityManager.setRealm(new DefaultRealm());
        webSecurityManager.setSessionManager(getDefaultSessionManager());
        return webSecurityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterMapDefine = new LinkedHashMap<>();

        filterMapDefine.put("/swagger-resources/**", "anon");
        filterMapDefine.put("/webjars/**", "anon");
        filterMapDefine.put("/v2/**", "anon");
        filterMapDefine.put("/swagger-ui.html/**", "anon");
        filterMapDefine.put("/csrf", "anon");
        filterMapDefine.put("/po/wxpay/notify", "anon");
        filterMapDefine.put("/po/exportPoList", "anon");
        filterMapDefine.put("/fs/gl/**","anon");
        filterMapDefine.put("/", "anon");
        filterMapDefine.put("/distributorgzh/isDistributor", "anon");


//        filterMapDefine.put("/auth/login", "anon");
//        filterMapDefine.put("/auth/wxlogin", "anon");
//        filterMapDefine.put("/auth/gzhlogin", "anon");
        filterMapDefine.put("/auth/**", "anon");
        filterMapDefine.put("/**","authc");

        filterFactoryBean.setFilterChainDefinitionMap(filterMapDefine);
        filterFactoryBean.setLoginUrl("/auth/login");
        return filterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean(name = "sessionManager")
    public SessionManager getDefaultSessionManager() {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(new RedisShiroSessionDao());
//        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        return sessionManager;
    }

}
