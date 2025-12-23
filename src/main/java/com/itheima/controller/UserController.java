package com.itheima.controller;

import com.itheima.pojo.ErrorMessage;
import com.itheima.pojo.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.Md5Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping( "/user")
@Tag(name = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册接口")
    @Validated
    //用户名规则：4-16位，只能是数字、字母、下划线、中划线，密码规则：6-16位，只能是数字、字母、下划线、中划线
    public Result register(@Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = ErrorMessage.USERNAME_FORMAT_ERROR) String username,
                           @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$", message = ErrorMessage.PASSWORD_FORMAT_ERROR) String password){
        User user = userService.findByUserName(username);
        if(user == null){
            password = Md5Util.getMD5String(password);//密码进行MD5加密
            log.info("用户注册，用户名：{},密码：{}",username,password);
            userService.register(username,password);
            return Result.success();
        }else {
            return Result.error(ErrorMessage.USERNAME_IS_USED);
        }
    }

}
