package com.wlx.springframework.mybatis;


import cn.hutool.core.lang.ClassScanner;
import com.wlx.middleware.mybatis.SqlSessionFactory;
import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.wlx.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Set;

/**
 * 博  客：http://bugstack.cn
 * 公众号：bugstack虫洞栈 | 沉淀、分享、成长，让自己和他人都能有所收获！
 * create by 小傅哥
 */
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            Set<Class<?>> classes = ClassScanner.scanPackage(basePackage);
            for (Class<?> clazz : classes) {
                PropertyValues propertyValues = new PropertyValues();
                propertyValues.addProperty(new PropertyValue("mapperInterface", clazz));
                propertyValues.addProperty(new PropertyValue("sqlSessionFactory", sqlSessionFactory));
                BeanDefinition beanDefinition = new BeanDefinition(MapperFactoryBean.class, propertyValues);

                registry.registerBeanDefinition(clazz.getSimpleName(), beanDefinition);
            }
        } catch (Exception e) {
            throw new BeansException("scan dao interface failed", e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // left intentionally blank
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
