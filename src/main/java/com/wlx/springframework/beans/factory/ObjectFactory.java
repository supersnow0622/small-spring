package com.wlx.springframework.beans.factory;

import com.wlx.springframework.beans.BeansException;

public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}
