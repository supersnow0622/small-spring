package com.wlx.springframework.test.bean;

import com.wlx.springframework.beans.factory.annotation.Autowired;
import com.wlx.springframework.beans.factory.annotation.Value;
import com.wlx.springframework.stereotype.Component;

@Component
public class BrandService {

    @Autowired
    private UserDao userDao;

    @Value("${token}")
    private String token;

    @Override
    public String toString() {
        return "BrandService{" +
                "userDao=" + userDao +
                ", token='" + token + '\'' +
                '}';
    }
}
