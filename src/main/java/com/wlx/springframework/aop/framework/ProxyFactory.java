package com.wlx.springframework.aop.framework;

import com.wlx.springframework.aop.AdvisedSupport;

public class ProxyFactory implements AopProxy {

    private AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        if (advisedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advisedSupport).getProxy();
        }
        return new JdkDynamicAopProxy(advisedSupport).getProxy();
    }

}
