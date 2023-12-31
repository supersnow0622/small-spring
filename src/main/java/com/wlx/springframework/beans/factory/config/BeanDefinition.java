package com.wlx.springframework.beans.factory.config;

import com.wlx.springframework.beans.PropertyValues;

public class BeanDefinition {

    private Class beanClass;

    private PropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    private String scope = ConfigurableBeanFactory.SCOPE_SINGLETON;

    private boolean singleton = true;

    private boolean prototype = false;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues;
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues, String initMethodName, String destroyMethodName) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = scope.equals(ConfigurableBeanFactory.SCOPE_SINGLETON);
        this.prototype = scope.equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
    }

    public boolean isSingleton() {
        return singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }
}
