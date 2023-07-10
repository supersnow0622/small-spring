package com.wlx.springframework.test.bean;

import com.wlx.springframework.beans.factory.FactoryBean;
import com.wlx.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
@Component("bodyDao")
public class ProxyBeanFactory implements FactoryBean<IBodyDao> {
    @Override
    public IBodyDao getObject() throws Exception {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("10001", "大脑");
                hashMap.put("10002", "胳膊");
                hashMap.put("10003", "大腿");

                return hashMap.get(args[0].toString());
            }
        };
        return (IBodyDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{IBodyDao.class}, handler);
    }

    @Override
    public Class<IBodyDao> getObjectType() {
        return IBodyDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
