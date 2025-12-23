package com.itheima.service.impl;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param username
     * @param password
     */
    @Override
    public void register(String username, String password) {
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime updateTime = LocalDateTime.now();
        userMapper.register(username, password, createTime, updateTime);
    }

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return user;
    }

    /**
     * 修改用户信息
     * @param user
     */
    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    /**
     * 用户修改头像
     * @param avatarUrl
     */
    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map= ThreadLocalUtil.get();//获取当前用户信息
        Integer id = (Integer) map.get("id");//获取用户id
        userMapper.updateAvatar(avatarUrl,id,LocalDateTime.now());
    }

    /**
     * 用户修改密码
     * @param newPwd
     */
    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map= ThreadLocalUtil.get();//获取当前用户信息
        Integer id = (Integer) map.get("id");//获取用户id
        String md5Pwd = Md5Util.getMD5String(newPwd);//对密码进行MD5加密
        userMapper.updatePwd(md5Pwd,id,LocalDateTime.now());
    }
}
