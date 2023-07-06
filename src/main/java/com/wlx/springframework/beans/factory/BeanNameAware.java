package com.wlx.springframework.beans.factory;

public interface BeanNameAware extends Aware {

    void setBeanName(String beanName);
}
