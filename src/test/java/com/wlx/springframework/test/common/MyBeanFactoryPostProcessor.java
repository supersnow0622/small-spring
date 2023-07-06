package com.wlx.springframework.test.common;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor  {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition userService = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = userService.getPropertyValues();
        propertyValues.addProperty(new PropertyValue("company", "字节跳动"));
    }
}
