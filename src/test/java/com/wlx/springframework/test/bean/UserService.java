package com.wlx.springframework.test.bean;

public class UserService {

    private UserDao userDao;

    private String uId = "xxx";

    public void queryUserInfo() {
        System.out.println("查询用户信息:" + userDao.queryUserName(uId));
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
