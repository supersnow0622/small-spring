package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, Constructor constructor, Object... args);
}
