package com.wlx.springframework.beans.factory.support;

import com.wlx.springframework.beans.factory.FactoryBean;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanPostProcessor;
import com.wlx.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.wlx.springframework.utils.ClassUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return doGetBean(beanName, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) {
        return (T) getBean(beanName);
    }

    protected <T> T doGetBean(String beanName, Object... args) {
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null) {
            return (T) getObjectFromBeanInstance(sharedInstance, beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        Object bean = createBean(beanName, beanDefinition, args);
        return (T) getObjectFromBeanInstance(bean, beanName);
    }

    public abstract BeanDefinition getBeanDefinition(String beanName);

    public abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object... args);

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessorList.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessorList() {
        return beanPostProcessorList;
    }

    public ClassLoader getBeanClassLoader() {
        return ClassUtils.getDefaultClassLoader();
    }

    private Object getObjectFromBeanInstance(Object bean, String beanName) {
        if (!(bean instanceof FactoryBean)) {
            return bean;
        }
        FactoryBean<?> factoryBean = (FactoryBean<?>) bean;
        return getObjectFromFactoryBean(factoryBean, beanName);
    }
}
