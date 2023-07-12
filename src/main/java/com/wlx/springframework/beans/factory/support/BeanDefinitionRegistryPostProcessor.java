package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.config.BeanFactoryPostProcessor;

public interface BeanDefinitionRegistryPostProcessor  extends BeanFactoryPostProcessor {

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
