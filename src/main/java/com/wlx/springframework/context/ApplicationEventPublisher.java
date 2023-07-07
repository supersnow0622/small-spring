package com.wlx.springframework.context;

public interface ApplicationEventPublisher {

    void publicEvent(ApplicationEvent event);
}
