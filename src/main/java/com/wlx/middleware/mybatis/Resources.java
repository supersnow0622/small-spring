package com.wlx.middleware.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Resources {

    public static Reader getResourceAsReader(String resource) throws IOException {
        ClassLoader[] classLoaders = new ClassLoader[]{ClassLoader.getSystemClassLoader(),
                Thread.currentThread().getContextClassLoader()};
        for (ClassLoader classLoader : classLoaders) {
            InputStream inputStream = classLoader.getResourceAsStream(resource);
            if (inputStream != null) {
                return new InputStreamReader(inputStream);
            }
        }
        throw new IOException("Could not find resource " + resource);
    }
}
