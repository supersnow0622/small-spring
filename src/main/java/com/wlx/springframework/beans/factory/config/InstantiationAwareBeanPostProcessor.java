package com.wlx.springframework.beans.factory.config;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    Object postBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    Object postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName);

    default Object getEarlyBeanReference(Object bean, String beanName) {
        return bean;
    }
}
