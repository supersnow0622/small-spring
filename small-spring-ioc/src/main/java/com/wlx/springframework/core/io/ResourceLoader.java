package com.wlx.springframework.core.io;

public interface ResourceLoader {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String location);
}
