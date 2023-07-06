package com.wlx.springframework.context.support;

import com.wlx.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.wlx.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] locations = getConfigLocations();
        reader.loadBeanDefinitions(locations);
    }

    protected abstract String[] getConfigLocations();
}
