package com.wlx.springframework.aop;

public interface ClassFilter {

    boolean match(Class<?> clazz);
}
