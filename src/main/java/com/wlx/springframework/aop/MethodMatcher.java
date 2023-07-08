package com.wlx.springframework.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    boolean match(Method method, Class<?> clazz);
}
