package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
        try {
            Object object = null;
            Class<?> beanClass = beanDefinition.getBeanClass();
            Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
            for (Constructor<?> constructor : declaredConstructors) {
                if (null != args && constructor.getParameters().length == args.length) {
                    object = instantiationStrategy.instantiate(beanDefinition, constructor, args);
                    break;
                }
            }
            addSingleton(beanName, object);
            return object;
        } catch (Exception e) {
            throw new BeansException("创建对象失败", e);
        }
    }
}
