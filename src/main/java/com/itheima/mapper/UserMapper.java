package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    /**
     * 用户注册
     * @param username
     * @param password
     */
    void register(String username, String password, LocalDateTime createTime, LocalDateTime updateTime);

    /**
     * 根据用户名查询用户
     * @param username
     * @return User
     */
    User findByUserName(String username);
}