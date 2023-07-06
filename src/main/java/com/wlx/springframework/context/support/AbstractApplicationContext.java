package com.wlx.springframework.context.support;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.wlx.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.wlx.springframework.beans.factory.config.BeanPostProcessor;
import com.wlx.springframework.context.ApplicationContextAwareProcessor;
import com.wlx.springframework.context.ConfigurableApplicationContext;
import com.wlx.springframework.core.io.DefaultResourceLoader;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    @Override
    public void refresh() throws BeansException {
        // 1.创建工厂，并加载bean定义
        refreshBeanFactory();

        // 2.获取工厂
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3.注册ApplicationContextAware
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 4.执行自定义配置BeanFactoryPostProcessor的实现类
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5.添加自定义配置BeanPostProcessor的实现类
        registerBeanPostProcessors(beanFactory);

        // 6.实例化单例Bean对象
        beanFactory.preInstantiateSingletons();
    }

    protected abstract void refreshBeanFactory();

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = getBeansOfType(BeanFactoryPostProcessor.class);
        beanFactoryPostProcessorMap.values().forEach(x -> x.postProcessBeanFactory(beanFactory));
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = getBeansOfType(BeanPostProcessor.class);
        beanPostProcessorMap.values().forEach(beanFactory::addBeanPostProcessor);
    }

    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return getBeanFactory().getBean(beanName, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) {
        return getBeanFactory().getBean(beanName, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }
}
