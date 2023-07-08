package com.wlx.springframework.beans.factory.config;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.BeanFactory;

// 自动化处理Bean工厂配置的接口
public interface AutowireCapableBeanFactory extends BeanFactory {

    // 执行 BeanPostProcessors 接口实现类的 postProcessBeforeInitialization 方法
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
