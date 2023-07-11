package com.wlx.springframework.aop.framework;

import com.wlx.springframework.aop.AdvisedSupport;
import com.wlx.springframework.utils.ClassUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Cglib2AopProxy implements AopProxy, MethodInterceptor {

    private AdvisedSupport advisedSupport;

    public Cglib2AopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        Class<?> aClass = advisedSupport.getTargetSource().getTarget().getClass();
        aClass = ClassUtils.isCglibProxyClass(aClass) ? aClass.getSuperclass() : aClass;
        enhancer.setSuperclass(aClass);
        enhancer.setInterfaces(advisedSupport.getTargetSource().getTargetClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (advisedSupport.getMethodMatcher().match(method, advisedSupport.getTargetSource().getTarget().getClass())) {
            return advisedSupport.getMethodInterceptor().invoke(new ReflectiveMethodInvocation(
                    advisedSupport.getTargetSource().getTarget(), method, objects));
        }
        return method.invoke(advisedSupport.getTargetSource().getTarget(), objects);
    }
}
