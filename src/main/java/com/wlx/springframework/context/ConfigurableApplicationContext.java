package com.wlx.springframework.context;

import com.wlx.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void refresh() throws BeansException;
}
