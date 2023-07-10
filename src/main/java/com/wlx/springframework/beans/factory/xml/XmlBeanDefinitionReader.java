package com.wlx.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanReference;
import com.wlx.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.wlx.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.wlx.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import com.wlx.springframework.core.io.Resource;
import com.wlx.springframework.core.io.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        super(beanDefinitionRegistry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        super(beanDefinitionRegistry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) {
        try {
            try(InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (IOException | ClassNotFoundException | DocumentException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) {
        Resource resource = getResourceLoader().getResource(location);
        loadBeanDefinitions(resource);
    }

    public void loadBeanDefinitions(String[] locations) {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    public void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element root = document.getRootElement();

        Element componentScan = root.element("component-scan");
        if (componentScan != null) {
            String basePackage = componentScan.attributeValue("base-package");
            if (StrUtil.isEmpty(basePackage)) {
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(basePackage);
        }

        List<Element> beanNodes = root.elements("bean");
        for (int i = 0; i < beanNodes.size(); i++) {
            Element beanElement = beanNodes.get(i);

            String id = beanElement.attributeValue("id");
            String beanName = beanElement.attributeValue("name");
            String className = beanElement.attributeValue("class");
            String initMethodName = beanElement.attributeValue("init-method");
            String destroyMethodName = beanElement.attributeValue("destroy-method");
            String scope = beanElement.attributeValue("scope");

            Class<?> clazz = Class.forName(className);
            beanName = StrUtil.isEmpty(id) ? beanName : id;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            List<Element> propertyNodes = beanElement.elements();
            PropertyValues propertyValues = new PropertyValues();
            for (int j = 0; j < propertyNodes.size(); j++) {
                Element propertyElement = propertyNodes.get(j);

                String propertyName = propertyElement.attributeValue("name");
                String value = propertyElement.attributeValue("value");
                String ref = propertyElement.attributeValue("ref");
                Object obj = StrUtil.isEmpty(ref) ? value : new BeanReference(ref);
                propertyValues.addProperty(new PropertyValue(propertyName, obj));
            }

            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            BeanDefinition beanDefinition = new BeanDefinition(clazz, propertyValues, initMethodName, destroyMethodName);
            if (StrUtil.isNotEmpty(scope)) {
                beanDefinition.setScope(scope);
            }
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }

    private void scanPackage(String basePackage) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackage.split(","));
    }
}
