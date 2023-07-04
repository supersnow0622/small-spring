package com.wlx.springframework.test.bean;

public class UserService {

    private UserDao userDao;

    private String name = "xxx";

    public UserService() {
    }

    public UserService(String name) {
        this.name = name;
    }

    public void queryUserInfo() {
        System.out.println("查询用户信息:" + userDao.queryUserName(name));
    }

}
