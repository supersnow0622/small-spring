package com.wlx.springframework.utils;

public class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();
        }
        return classLoader;
    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return clazz!= null && clazz.getName().contains("$$");
    }
}
