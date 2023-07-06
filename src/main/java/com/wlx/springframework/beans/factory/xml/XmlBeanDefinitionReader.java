package com.wlx.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.wlx.springframework.beans.BeansException;
import com.wlx.springframework.beans.PropertyValue;
import com.wlx.springframework.beans.PropertyValues;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.config.BeanReference;
import com.wlx.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.wlx.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.wlx.springframework.core.io.Resource;
import com.wlx.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

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
        } catch (IOException | ClassNotFoundException e) {
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

    public void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList beanNodes = root.getChildNodes();

        for (int i = 0; i < beanNodes.getLength(); i++) {
            Node beanNode = beanNodes.item(i);
            if (!(beanNode instanceof Element)) {
                continue;
            }
            if (!"bean".equals(beanNode.getNodeName())) {
                continue;
            }

            Element beanElement = (Element) beanNode;
            String id = beanElement.getAttribute("id");
            String beanName = beanElement.getAttribute("name");
            String className = beanElement.getAttribute("class");

            beanName = StrUtil.isEmpty(id) ? beanName : id;
            Class<?> clazz = Class.forName(className);

            NodeList propertyNodes = beanElement.getChildNodes();
            PropertyValues propertyValues = new PropertyValues();
            for (int j = 0; j < propertyNodes.getLength(); j++) {
                Node propertyNode = propertyNodes.item(j);
                if (!(propertyNode instanceof Element)) {
                    continue;
                }
                if (!"property".equals(propertyNode.getNodeName())) {
                    continue;
                }
                Element propertyElement = (Element) propertyNode;
                String propertyName = propertyElement.getAttribute("name");
                String value = propertyElement.getAttribute("value");
                String ref = propertyElement.getAttribute("ref");
                Object obj = StrUtil.isEmpty(ref) ? value : new BeanReference(ref);
                propertyValues.addProperty(new PropertyValue(propertyName, obj));
            }

            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            BeanDefinition beanDefinition = new BeanDefinition(clazz, propertyValues);
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
