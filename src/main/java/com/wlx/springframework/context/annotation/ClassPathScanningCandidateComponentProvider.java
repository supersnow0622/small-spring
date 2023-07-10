package com.wlx.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.wlx.springframework.beans.factory.config.BeanDefinition;
import com.wlx.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new HashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }
        return candidates;
    }
}
