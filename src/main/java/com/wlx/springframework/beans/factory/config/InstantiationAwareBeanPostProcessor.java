package com.wlx.springframework.beans.factory.config;

import com.wlx.springframework.beans.BeansException;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    Object postBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

}
