package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.factory.BeanFactory;
import com.wlx.springframework.beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String beanName) {
        Object beanObject = getSingleton(beanName);
        if (beanObject != null) {
            return beanObject;
        }
        BeanDefinition beanDefinition = getDefinition(beanName);
        return createBean(beanName, beanDefinition);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        Object beanObject = getSingleton(beanName);
        if (beanObject != null) {
            return beanObject;
        }
        BeanDefinition beanDefinition = getDefinition(beanName);
        return createBean(beanName, beanDefinition, args);
    }

    public abstract BeanDefinition getDefinition(String beanName);

    public abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object... args);

}
