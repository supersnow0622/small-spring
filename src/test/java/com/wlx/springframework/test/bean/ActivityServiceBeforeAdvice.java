package com.wlx.springframework.test.bean;


import com.wlx.springframework.aop.MethodBeforeAdvice;
import com.wlx.springframework.stereotype.Component;

import java.lang.reflect.Method;

public class ActivityServiceBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public Object before(Method method, Object[] args, Object target) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return method.invoke(target, args);
        } finally {
            System.out.println("监控 - Begin By AOP");
            System.out.println("方法名称：" + method.getName());
            System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
            System.out.println("监控 - End\r\n");
        }
    }
}
