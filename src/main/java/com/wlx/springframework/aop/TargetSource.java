package com.wlx.springframework.aop;

import com.wlx.springframework.utils.ClassUtils;

public class TargetSource {

    private Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?>[] getTargetClass() {
        return this.target.getClass().getInterfaces();
    }
}
