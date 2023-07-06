package com.wlx.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanPostProcessor;
import com.wlx.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
        try {
            Object bean = createBeanInstance(beanName, beanDefinition, args);
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行bean的初始化方法和BeanPostProcessor的前置和后置方法
            bean = initializeBean(bean, beanName, beanDefinition);
            addSingleton(beanName, bean);
            return bean;
        } catch (Exception e) {
            throw new BeansException(beanName + " be created failed", e);
        }
    }

    private Object initializeBean(Object bean, String beanName, BeanDefinition beanDefinition) {
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        invokeInitMethods(beanName, wrappedBean, beanDefinition);
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {
    }

    private Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object... args) {
        Constructor<?>[] declaredConstructors = beanDefinition.getBeanClass().getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            if (null != args && constructor.getParameters().length == args.length) {
                return instantiationStrategy.instantiate(beanDefinition, constructor, args);
            }
        }
        return instantiationStrategy.instantiate(beanDefinition, null, null);
    }

    private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if (value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }

                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values：" + beanName);
        }
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object currentBean = existingBean;
        List<BeanPostProcessor> beanPostProcessorList = getBeanPostProcessorList();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            currentBean = beanPostProcessor.postProcessBeforeInitialization(existingBean, beanName);
            if (currentBean == null) return existingBean;
        }
        return currentBean;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object currentBean = existingBean;
        List<BeanPostProcessor> beanPostProcessorList = getBeanPostProcessorList();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            currentBean = beanPostProcessor.postProcessAfterInitialization(existingBean, beanName);
            if (currentBean == null) return existingBean;
        }
        return currentBean;
    }
}
