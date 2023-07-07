package com.wlx.springframework.test.bean;

import com.wlx.springframework.beans.factory.BeanClassLoaderAware;
import com.wlx.springframework.beans.factory.BeanFactory;
import com.wlx.springframework.beans.factory.BeanFactoryAware;
import com.wlx.springframework.beans.factory.BeanNameAware;
import com.wlx.springframework.context.ApplicationContext;
import com.wlx.springframework.context.ApplicationContextAware;

public class UserService implements BeanNameAware, BeanFactoryAware, BeanClassLoaderAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    private UserDao userDao;

    private String uId;

    private String company;

    private String address;

    private IBodyDao bodyDao;

    public void queryUserInfo() {
        System.out.println("查询用户信息:" + this.toString());
        System.out.println(bodyDao.getBodyInfo(uId));
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserService{" +
                "uId='" + uId + '\'' +
                ", company='" + company + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public void init() {
        System.out.println("init:" + UserService.class.getSimpleName());
    }

    public void destroy() {
        System.out.println("destroy:" + UserService.class.getSimpleName());
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("classLoader is:" + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        System.out.println("beanFactory is:" + beanFactory);
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("beanName is:" + beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        System.out.println("applicationContext is:" + applicationContext);
    }
}
