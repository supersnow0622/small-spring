package com.wlx.springframework.test.dao;


import com.wlx.springframework.test.po.User;

public interface IUserDao {

     User queryUserInfoById(Long id);

}