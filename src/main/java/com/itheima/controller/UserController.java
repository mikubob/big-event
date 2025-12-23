package com.itheima.controller;

import com.itheima.pojo.Message;
import com.itheima.pojo.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping( "/user")
@Tag(name = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @Validated
    //用户名规则：4-16位，只能是数字、字母、下划线、中划线，密码规则：6-16位，只能是数字、字母、下划线、中划线
    public Result register(@Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = Message.USERNAME_FORMAT_ERROR) String username,
                           @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$", message = Message.PASSWORD_FORMAT_ERROR) String password){
        User user = userService.findByUserName(username);
        if(user == null){
            password = Md5Util.getMD5String(password);//密码进行MD5加密
            log.info("用户注册，用户名：{},密码：{}",username,password);
            userService.register(username,password);
            return Result.success();
        }else {
            return Result.error(Message.USERNAME_IS_USED);
        }
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @Validated
    public Result login(@Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = Message.USERNAME_FORMAT_ERROR) String username,
                        @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$", message = Message.PASSWORD_FORMAT_ERROR) String password){
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断该用户是否存在
        if (loginUser==null){
            return Result.error(Message.USER_NOT_EXIST);
        }
        //判断密码是否正确
        if (Md5Util.checkPassword(password,loginUser.getPassword())){
            //登录成功
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token = JwtUtil.getToken(claims);
            //输出日志
            log.info("用户登录成功，用户名：{},token：{}",username,token);
            //把token储存到redis中
            ValueOperations<String, String> operation = stringRedisTemplate.opsForValue();
            operation.set(token,token,1, TimeUnit.HOURS);//1小时有效期
            return Result.success(token);
        }
        return Result.error(Message.PASSWORD_ERROR);
    }

    /**
     * 获取用户的详细信息
     * @return
     */
    @GetMapping("/userInfo")
    @Operation(summary = "获取用户的详细信息")
    public Result<User> userInfo(){
        log.info("获取用户详细信息");
        Map<String,Object> map = ThreadLocalUtil.get();//获取当前用户信息
        String username =(String) map.get("username");//获取用户名
        User user=userService.findByUserName(username);//根据用户名查询用户
        return Result.success(user);
    }

    /**
     * 修改用户信息
     * @return
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户信息")
    public Result update(@RequestBody @Validated User user){
        log.info("修改用户信息，用户信息：{}",user);
        userService.update(user);
        return Result.success();
    }
}
