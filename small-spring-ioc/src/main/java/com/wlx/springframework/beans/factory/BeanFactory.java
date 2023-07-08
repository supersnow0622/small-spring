package com.wlx.springframework.beans.factory;

public interface BeanFactory {

    Object getBean(String beanName);

    Object getBean(String beanName, Object... args);

    <T> T getBean(String beanName, Class<T> requiredType);
}
