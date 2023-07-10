package com.wlx.springframework.beans.factory;

import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.wlx.springframework.core.io.DefaultResourceLoader;
import com.wlx.springframework.core.io.Resource;
import com.wlx.springframework.utils.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                PropertyValue[] propertyValues = beanDefinition.getPropertyValues().getPropertyValues();
                for (PropertyValue propertyValue : propertyValues) {
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String)) {
                        continue;
                    }
                    value = resolveStringValue((String) value, properties);
                    beanDefinition.getPropertyValues().addProperty(new PropertyValue(propertyValue.getName(), value));
                }
            }

            StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(valueResolver);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveStringValue(String strValue, Properties properties) {
        StringBuilder buffer = new StringBuilder(strValue);
        int startIndex = strValue.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int endIndex = strValue.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            String propKey = strValue.substring(startIndex + 2, endIndex);
            String proVal = properties.getProperty(propKey);
            buffer.replace(startIndex, endIndex + 1, proVal);
            return proVal.toString();
        }
        return null;
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strValue) {
            return PropertyPlaceholderConfigurer.this.resolveStringValue(strValue, properties);
        }
    }
}
