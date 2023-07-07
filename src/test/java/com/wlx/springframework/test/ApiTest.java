package com.wlx.springframework.test;

import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanReference;
import com.wlx.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.wlx.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.wlx.springframework.context.support.ClassPathXmlApplicationContext;
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

    @Test
    public void testContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");

        UserService userService = context.getBean("userService", UserService.class);
        userService.queryUserInfo();
    }

    @Test
    public void testInitAndDestroy() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        context.registerShutdownHook();

        UserService userService = context.getBean("userService", UserService.class);
        userService.queryUserInfo();
    }

    @Test
    public void testPrototype() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        UserDao userDao1 = applicationContext.getBean("userDao", UserDao.class);
        UserDao userDao2 = applicationContext.getBean("userDao", UserDao.class);

        System.out.println(userDao1);
        System.out.println(userDao2);
    }
}
