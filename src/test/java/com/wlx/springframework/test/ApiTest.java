package com.wlx.springframework.test;

import com.wlx.springframework.beans.factory.PropertyValue;
import com.wlx.springframework.beans.factory.PropertyValues;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanReference;
import com.wlx.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.wlx.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.wlx.springframework.test.bean.UserDao;
import com.wlx.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_BeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        BeanDefinition userDao = new BeanDefinition(UserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDao);

        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addProperty(new PropertyValue("uId", "10001"));
        propertyValues.addProperty(new PropertyValue("userDao", new BeanReference("userDao")));

        BeanDefinition userService = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", userService);

        UserService userServiceCache = (UserService) beanFactory.getBean("userService");
        userServiceCache.queryUserInfo();
    }

    @Test
    public void test_xml() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        UserService userService = (UserService) beanFactory.getBean("userService", UserService.class);
        userService.queryUserInfo();
    }
}
