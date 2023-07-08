package com.wlx.springframework.beans.factory;

public interface InitializingBean {

    void afterPropertiesSet(Object bean, String beanName) throws Exception;
}
