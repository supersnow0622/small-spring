package com.wlx.springframework.beans.factory;

public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);
}
