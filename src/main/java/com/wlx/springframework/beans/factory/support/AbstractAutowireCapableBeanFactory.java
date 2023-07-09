package com.wlx.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.wlx.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.*;
import com.wlx.springframework.beans.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
        try {
            // 判断是否有代理类
            Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
            if (null != bean)
                return bean;
            bean = createBeanInstance(beanName, beanDefinition, args);
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行bean的初始化方法和BeanPostProcessor的前置和后置方法
            bean = initializeBean(bean, beanName, beanDefinition);

            // 注册销毁方法
            registerDisposableBeanIfNecessary(bean, beanName, beanDefinition);

            if (beanDefinition.isSingleton()) {
                registerSingleton(beanName, bean);
            }
            return bean;
        } catch (Exception e) {
            throw new BeansException(beanName + " be created failed", e);
        }
    }

    private Object initializeBean(Object bean, String beanName, BeanDefinition beanDefinition) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }

            if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }

            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }

        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("initializing bean failed", e);
        }
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) throws Exception {
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet(wrappedBean, beanName);
        }

        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName)) {
            Method method = beanDefinition.getBeanClass().getMethod(initMethodName);
            if (method == null) {
                throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            }
            method.invoke(wrappedBean);
        }
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

    private void registerDisposableBeanIfNecessary(Object bean, String beanName, BeanDefinition beanDefinition) {
        if (beanDefinition.isPrototype()) {
            return;
        }

        if ((bean instanceof DisposableBean) || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInitialization(beanDefinition.getBeanClass(), beanName);
        if (null != bean) {
            return applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    public Object applyBeanPostProcessorsBeforeInitialization(Class<?> clazz, String beanName) {
        List<BeanPostProcessor> beanPostProcessorList = getBeanPostProcessorList();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object bean = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postBeforeInstantiation(clazz, beanName);
                if (null != bean) return bean;
            }
        }
        return null;
    }


}
