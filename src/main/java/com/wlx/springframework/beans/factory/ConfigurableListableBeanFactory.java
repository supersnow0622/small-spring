package com.wlx.springframework.beans.factory;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.wlx.springframework.utils.StringValueResolver;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName);

    void preInstantiateSingletons() throws BeansException;

    String resolveEmbeddedValue(String value);

    void addEmbeddedValueResolver(StringValueResolver stringValueResolver);
}
