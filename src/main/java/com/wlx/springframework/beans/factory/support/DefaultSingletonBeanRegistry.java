package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.ObjectFactory;
import com.wlx.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected Object NULL_OBJECT = new Object();

    // 一级缓存，存放完整的bean对象
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 二级缓存，存放半成品bean对象，未设置属性的bean对象
    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    // 三级缓存，存放AOP代理对象，不需要AOP时，存放原始对象
    private Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>();

    private Map<String, DisposableBeanAdapter> disposableBeanMap = new ConcurrentHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object bean = singletonObjects.get(beanName);
        if (bean == null) {
            bean = earlySingletonObjects.get(beanName);
            if (bean == null) {
                ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
                if (objectFactory != null) {
                    bean = objectFactory.getObject();
                    earlySingletonObjects.put(beanName, bean);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return bean;
    }

    public void destroySingletons() {
        Object[] disposableBeanNameArray =  disposableBeanMap.keySet().toArray();
        for (int i = 0; i < disposableBeanNameArray.length; i++) {
            String disposableBeanName = (String) disposableBeanNameArray[i];
            DisposableBeanAdapter disposableBeanAdapter = disposableBeanMap.remove(disposableBeanName);
            try {
                disposableBeanAdapter.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + disposableBeanName + "' threw an exception", e);
            }
        }
    }

    public void registerSingleton(String beanName, Object singletonBean) {
        singletonObjects.put(beanName, singletonBean);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    public void registerDisposableBean(String beanName, DisposableBeanAdapter disposableBeanAdapter) {
        disposableBeanMap.put(beanName, disposableBeanAdapter);
    }

    public void addSingletonFactory(String beanName, ObjectFactory<?> objectFactory) {
        if (!singletonObjects.containsKey(beanName)) {
            singletonFactories.put(beanName, objectFactory);
            earlySingletonObjects.remove(beanName);
        }
    }


}
