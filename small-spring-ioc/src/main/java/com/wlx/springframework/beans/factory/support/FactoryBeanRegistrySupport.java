package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        Object object;
        if (factoryBean.isSingleton()) {
            object = factoryBeanObjectCache.get(beanName);
            if (object == null) {
                object = doGetObjectFromFactoryBean(factoryBean, beanName);
                factoryBeanObjectCache.put(beanName, (object != null ? object : NULL_OBJECT));
            }
        } else {
            object = doGetObjectFromFactoryBean(factoryBean, beanName);
        }
        return object != NULL_OBJECT ? object : null;
    }

    protected Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, final String beanName) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
        }
    }
}
