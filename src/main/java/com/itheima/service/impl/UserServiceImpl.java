package com.itheima.service.impl;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.AliyunOSSOperator;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

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
     * 用户修改头像（上传新文件并删除旧头像）
     * @param file 上传的头像文件
     */
    @Override
    public void updateAvatar(MultipartFile file) throws Exception {
        // 获取当前用户信息
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        String username = (String) map.get("username");
        
        // 获取当前用户的旧头像URL
        User currentUser = userMapper.findByUserName(username);
        String oldAvatarUrl = currentUser.getUserPic();
        
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 上传新头像到OSS
        String newAvatarUrl = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        
        // 更新数据库中的头像URL
        userMapper.updateAvatar(newAvatarUrl, id, LocalDateTime.now());
        
        // 如果有旧头像，删除旧头像文件
        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
            try {
                aliyunOSSOperator.deleteBatch(List.of(oldAvatarUrl));
            } catch (Exception e) {
                // 记录删除旧头像失败的日志，但不影响上传新头像的流程
                System.err.println("删除旧头像失败: " + e.getMessage());
            }
        }
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