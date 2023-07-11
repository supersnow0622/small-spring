package com.wlx.springframework.aop.framework.autoproxy;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.wlx.springframework.aop.*;
import com.wlx.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.wlx.springframework.aop.framework.ProxyFactory;
import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.BeanFactory;
import com.wlx.springframework.beans.factory.BeanFactoryAware;
import com.wlx.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.wlx.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;
import java.util.Set;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    private Set<Object> earlyProxyReferences = new ConcurrentHashSet<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (!earlyProxyReferences.contains(beanName)) {
            // 没有经过AOP的对象
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    @Override
    public Object postBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        return pvs;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    private Object wrapIfNecessary(Object bean, String beanName) {
        if (isInfrastructureClass(bean.getClass())) return bean;

        Collection<AspectJExpressionPointcutAdvisor> pointcutAdvisors =
                beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor advisor : pointcutAdvisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.match(bean.getClass())) continue;

            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = new TargetSource(bean);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(true);

            return new ProxyFactory(advisedSupport).getProxy();
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass)
                || Advisor.class.isAssignableFrom(beanClass);
    }
}
