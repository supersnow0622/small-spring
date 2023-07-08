package com.wlx.springframework.context.event;

import com.wlx.springframework.context.ApplicationEvent;
import com.wlx.springframework.context.ApplicationListener;

public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event);
}
