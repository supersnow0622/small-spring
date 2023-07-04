package com.wlx.springframework.test;

import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.wlx.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();

        UserService userServiceCache = (UserService) beanFactory.getBean("userService");
        userServiceCache.queryUserInfo();
    }
}
