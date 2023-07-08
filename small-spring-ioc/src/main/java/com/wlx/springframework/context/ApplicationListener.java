package com.wlx.springframework.context;

public interface ApplicationListener<E extends ApplicationEvent> {

    void onApplicationEvent(E e);
}
