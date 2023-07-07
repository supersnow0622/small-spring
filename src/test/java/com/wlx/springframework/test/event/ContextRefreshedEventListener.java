package com.wlx.springframework.test.event;

import com.wlx.springframework.context.ApplicationListener;
import com.wlx.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("刷新事件, 来源：" + contextRefreshedEvent.getSource().getClass());
    }
}
