package com.wlx.springframework.test;

import com.wlx.springframework.aop.AdvisedSupport;
import com.wlx.springframework.aop.MethodMatcher;
import com.wlx.springframework.aop.TargetSource;
import com.wlx.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.wlx.springframework.aop.framework.Cglib2AopProxy;
import com.wlx.springframework.aop.framework.JdkDynamicAopProxy;
import com.wlx.springframework.aop.framework.ReflectiveMethodInvocation;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanReference;
import com.wlx.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.wlx.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.wlx.springframework.context.support.ClassPathXmlApplicationContext;
import com.wlx.springframework.test.bean.*;
import com.wlx.springframework.test.event.CustomEvent;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    @Test
    public void testFactoryBean() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        context.registerShutdownHook();;

        UserService userService = context.getBean("userService", UserService.class);
        userService.queryBodyInfo();
    }

    @Test
    public void testEvent() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.publicEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));
        applicationContext.registerShutdownHook();
    }

    @Test
    public void testProxyMethod() {
        Object targetObj = new ActivityService();

        IActivityService proxy = (IActivityService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                targetObj.getClass().getInterfaces(), new InvocationHandler() {

                    MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* com.wlx.springframework.test.bean.IActivityService.*(..))");

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (methodMatcher.match(method, targetObj.getClass())) {
                            MethodInterceptor methodInterceptor = new MethodInterceptor() {
                                @Override
                                public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                                    long start = System.currentTimeMillis();
                                    try {
                                        return methodInvocation.proceed();
                                    } finally {
                                        System.out.println("监控 - Begin By AOP");
                                        System.out.println("方法名称：" + methodInvocation.getMethod().getName());
                                        System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
                                        System.out.println("监控 - End\r\n");
                                    }
                                }
                            };
                            methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObj, method, args));
                        }

                        return method.invoke(targetObj, args);
                    }
                });
        System.out.println(proxy.queryActivityInfo());
    }

    @Test
    public void test_dynamic() {
        // 目标对象
        IActivityService activityService = new ActivityService();
        // 组装代理信息
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(activityService));
        advisedSupport.setMethodInterceptor(new ActivityServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.wlx.springframework.test.bean.IActivityService.*(..))"));

        // 代理对象(JdkDynamicAopProxy)
        IActivityService proxy_jdk = (IActivityService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        // 测试调用
        System.out.println("测试结果：" + proxy_jdk.queryActivityInfo());

        // 代理对象(Cglib2AopProxy)
        IActivityService proxy_cglib = (IActivityService) new Cglib2AopProxy(advisedSupport).getProxy();
        // 测试调用
        System.out.println("测试结果：" + proxy_cglib.register("花花"));
    }

    @Test
    public void testAop() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");

        IActivityService activityService = context.getBean("activityService", IActivityService.class);
        System.out.println(activityService.queryActivityInfo());
    }

    @Test
    public void testPropertyPlaceholder() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-property.xml");
        ActivityService activityService = (ActivityService) context.getBean("activityService");
        System.out.println(activityService);
    }
}
