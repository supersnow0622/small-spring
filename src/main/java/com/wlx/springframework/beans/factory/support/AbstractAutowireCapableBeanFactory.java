package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.BeanException;
import com.wlx.springframework.beans.factory.config.BeanDefinition;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        try {
            Object object = beanDefinition.getBeanClass().newInstance();
            addSingleton(beanName, object);
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanException("创建对象失败", e);
        }
    }
}
