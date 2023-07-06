package com.wlx.springframework.context.support;

import com.wlx.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.wlx.springframework.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;
    @Override
    protected void refreshBeanFactory() {
        beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
