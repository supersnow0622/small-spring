package com.wlx.springframework.test.event;

import com.wlx.springframework.context.ApplicationListener;
import com.wlx.springframework.context.event.ContextClosedEvent;
import com.wlx.springframework.stereotype.Component;

@Component
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        System.out.println("关闭事件，来源：" + contextClosedEvent.getSource().getClass());
    }
}
