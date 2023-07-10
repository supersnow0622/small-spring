package com.wlx.springframework.beans.factory.annotation;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.BeanFactory;
import com.wlx.springframework.beans.factory.BeanFactoryAware;
import com.wlx.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.wlx.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.wlx.springframework.utils.ClassUtils;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        // 1.解析@value注解
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (null != valueAnnotation) {
                String value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(value);
                pvs.addProperty(new PropertyValue(field.getName(), value));
            }
        }

        // 2.解析Autowired
        for (Field field : declaredFields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (null != autowiredAnnotation) {
                Class<?> fieldType = field.getType();
                Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                Object dependentBean = null;
                if (qualifierAnnotation != null) {
                    String qualifierName = qualifierAnnotation.value();
                    dependentBean = beanFactory.getBean(qualifierName, fieldType);
                } else {
                    dependentBean = beanFactory.getBean(fieldType);
                }
                pvs.addProperty(new PropertyValue(field.getName(), dependentBean));
            }
        }
        return bean;
    }
}
