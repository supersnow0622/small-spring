package com.wlx.springframework.beans.factory;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName);

    void preInstantiateSingletons() throws BeansException;
}
