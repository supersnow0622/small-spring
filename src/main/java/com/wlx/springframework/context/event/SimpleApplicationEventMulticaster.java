package com.wlx.springframework.context.event;

import com.wlx.springframework.context.ApplicationEvent;
import com.wlx.springframework.context.ApplicationListener;

import java.util.Collection;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {
    @Override
    public void multicastEvent(ApplicationEvent event) {
        Collection<ApplicationListener> applicationListeners = getApplicationListeners(event);
        applicationListeners.forEach(x -> x.onApplicationEvent(event));
    }
}
