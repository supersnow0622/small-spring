package com.wlx.springframework.beans.factory;

public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
