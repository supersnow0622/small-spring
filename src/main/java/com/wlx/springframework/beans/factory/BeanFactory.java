package com.wlx.springframework.beans.factory;

import com.wlx.springframework.beans.BeansException;

public interface BeanFactory {

    Object getBean(String beanName);

    Object getBean(String beanName, Object... args);

    <T> T getBean(String beanName, Class<T> requiredType);

    <T> T getBean(Class<T> requiredType) throws BeansException;
}
