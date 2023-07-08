package com.wlx.springframework.test.common;

import com.wlx.springframework.beans.factory.config.BeanPostProcessor;
import com.wlx.springframework.test.bean.UserService;

public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setuId("10002");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setAddress("北京");
        }
        return bean;
    }
}
