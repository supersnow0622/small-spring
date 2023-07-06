package com.wlx.springframework.context;

import com.wlx.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);
}
