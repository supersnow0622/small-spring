package com.wlx.springframework.aop;

public interface PointcutAdvisor extends Advisor{

    Pointcut getPointcut();
}
