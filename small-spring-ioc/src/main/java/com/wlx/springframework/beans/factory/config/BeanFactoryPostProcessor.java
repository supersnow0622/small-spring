package com.wlx.springframework.beans.factory.config;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
