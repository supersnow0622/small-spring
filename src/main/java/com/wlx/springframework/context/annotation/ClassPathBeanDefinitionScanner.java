package com.wlx.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.wlx.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.wlx.springframework.stereotype.Component;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidateComponents) {
                String scope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(scope)) {
                    beanDefinition.setScope(scope);
                }
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

        registry.registerBeanDefinition("com.wlx.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor",
                new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope declaredAnnotation = beanClass.getDeclaredAnnotation(Scope.class);
        if (declaredAnnotation != null && StrUtil.isNotBlank(declaredAnnotation.value())) {
            return declaredAnnotation.value();
        }
        return null;
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component declaredAnnotation = beanClass.getDeclaredAnnotation(Component.class);
        if (declaredAnnotation != null && StrUtil.isNotBlank(declaredAnnotation.value())) {
            return declaredAnnotation.value();
        }
        return StrUtil.lowerFirst(beanClass.getSimpleName());
    }
}
