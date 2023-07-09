package com.wlx.springframework.aop.framework.adapter;

import com.wlx.springframework.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    private MethodBeforeAdvice methodBeforeAdvice;

    public MethodBeforeAdvice getMethodBeforeAdvice() {
        return methodBeforeAdvice;
    }

    public void setMethodBeforeAdvice(MethodBeforeAdvice methodBeforeAdvice) {
        this.methodBeforeAdvice = methodBeforeAdvice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        methodBeforeAdvice.before(methodInvocation.getMethod(), methodInvocation.getArguments(),
                methodInvocation.getThis());
        return methodInvocation.proceed();
    }
}
