package com.wlx.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.DisposableBean;
import com.wlx.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {

    private Object bean;
    private String beanName;
    private BeanDefinition beanDefinition;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.beanDefinition = beanDefinition;
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if (StrUtil.isNotBlank(destroyMethodName)) {
            Method method = beanDefinition.getBeanClass().getMethod(destroyMethodName);
            if (method == null) {
                throw new BeansException("Could not find an Destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
            }
            method.invoke(bean);
        }
    }
}
