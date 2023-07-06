package com.wlx.springframework.beans.factory;

import java.util.Map;

// 扩展 Bean 工厂接口的接口
public interface ListableBeanFactory extends BeanFactory {

    <T> Map<String, T> getBeansOfType(Class<T> type);

    String[] getBeanDefinitionNames();
}
