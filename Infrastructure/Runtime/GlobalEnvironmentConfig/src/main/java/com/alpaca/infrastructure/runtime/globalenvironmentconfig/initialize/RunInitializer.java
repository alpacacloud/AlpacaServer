package com.alpaca.infrastructure.runtime.globalenvironmentconfig.initialize;

import com.alpaca.infrastructure.core.Initializer;
import com.alpaca.infrastructure.core.utils.SpringUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ：lichenw
 * @Date ：Created in 20:57 2019/3/13
 * @Description：
 * @Modified By：
 */
@Configuration
public class RunInitializer implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String[] beanNames = SpringUtil.getApplicationContext().getBeanDefinitionNames();
        for(String beanName: beanNames){
            Class<?> beanType = SpringUtil.getApplicationContext().getType(beanName);

            if (null != beanType){
                if (Initializer.class.isAssignableFrom(beanType)){
                    Initializer initLoad = (Initializer) SpringUtil.getBean(beanName);
                    new Thread(()->initLoad.initialize()).start();
                }
            }
        }


    }
}