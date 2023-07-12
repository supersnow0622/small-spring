package com.wlx.springframework.test.bean;

import com.wlx.springframework.beans.factory.DisposableBean;
import com.wlx.springframework.beans.factory.InitializingBean;
import com.wlx.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class UserDao implements InitializingBean, DisposableBean {

    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "小傅哥");
        hashMap.put("10002", "八杯水");
        hashMap.put("10003", "阿毛");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init：" + UserDao.class.getSimpleName());
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy:" + UserDao.class.getSimpleName());
    }
}
