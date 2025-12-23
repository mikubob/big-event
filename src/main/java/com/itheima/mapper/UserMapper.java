package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 修改用户信息
     * @param user
     */
    void update(User user);

    /**
     * 用户修改头像
     * @param avatarUrl
     * @param id
     */
    void updateAvatar(String avatarUrl, Integer id, LocalDateTime updateTime);

    /**
     * 修改用户密码
     * @param md5Pwd
     * @param id
     * @param now
     */
    void updatePwd(String md5Pwd, Integer id, LocalDateTime now);
}