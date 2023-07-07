package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected Object NULL_OBJECT = new Object();

    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private Map<String, DisposableBeanAdapter> disposableBeanMap = new ConcurrentHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
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

    public void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    public void registerDisposableBean(String beanName, DisposableBeanAdapter disposableBeanAdapter) {
        disposableBeanMap.put(beanName, disposableBeanAdapter);
    }


}
