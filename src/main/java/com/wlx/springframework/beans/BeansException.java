package com.wlx.springframework.beans;

public class BeansException extends RuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable e) {
        super(msg, e);
    }
}
