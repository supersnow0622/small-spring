package com.wlx.springframework.beans.factory;

import com.wlx.springframework.beans.BeansException;

public interface DisposableBean {

    void destroy() throws Exception;
}
