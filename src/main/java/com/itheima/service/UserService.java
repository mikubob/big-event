package com.itheima.service;

import com.itheima.pojo.User;

public interface UserService {
    /**
     * 用户注册
     * @param username
     * @param password
     */
    void register(String username, String password);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User findByUserName(String username);

    /**
     * 修改用户信息
     * @param user
     */
    void update(User user);
}