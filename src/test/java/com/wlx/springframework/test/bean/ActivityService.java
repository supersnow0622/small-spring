package com.wlx.springframework.test.bean;

import com.wlx.springframework.beans.factory.annotation.Value;
import com.wlx.springframework.stereotype.Component;

import java.util.Random;

@Component("activityService")
public class ActivityService implements IActivityService {

    @Value("${token}")
    private String token;

    @Override
    public String queryActivityInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "小傅哥，100001，深圳";
    }

    @Override
    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

    @Override
    public String toString() {
        return "ActivityService{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
