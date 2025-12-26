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
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 用户上传头像（上传新文件并删除旧头像）
     * @param file 上传的头像文件
     * @return
     */
    @PutMapping("/updateAvatar")
    @Operation(summary = "用户上传头像")
    public Result uploadAvatar(MultipartFile file) {
        log.info("用户上传头像，文件名：{}", file.getOriginalFilename());
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return Result.error(Message.FILE_NOT_EMPTY);
            }

            // 检查文件类型是否为图片
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error(Message.FILE_TYPE_ERROR);
            }

            // 调用服务上传头像并删除旧头像
            userService.updateAvatar(file);
            return Result.success(Message.FILE_UPLOAD_SUCCESS);
        } catch (Exception e) {
            log.error("头像上传失败", e);
            return Result.error("头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 用户修改密码
     * @param params
     * @param token
     * @return
     */
    @PatchMapping("/updatePwd")
    @Operation(summary = "用户修改密码")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        log.info("用户修改密码，参数：{},token：{}",params,token);
        //1.校验参数
        String oldPwd=params.get("old_pwd");//旧密码
        String newPwd=params.get("new_pwd");//新密码
        String rePwd = params.get("re_pwd");//重复密码
        if(!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||!StringUtils.hasLength(rePwd)){
            return Result.error(Message.PARAM_ERROR);
        }
        //原密码是否正确
        //根据用户名拿到原密码，再和oldPwd进行比较
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");//获取用户名
        User Loginuser = userService.findByUserName(username);//根据用户名查询用户
        if(!Md5Util.checkPassword(oldPwd,Loginuser.getPassword())){
            return Result.error(Message.OLD_PWD_ERROR);
        }
        //newPwd和rePwd是否一致
        if(!newPwd.equals(rePwd)){
            return Result.error(Message.RE_PWD_ERROR);
        }
        //2.调用service完成密码的更新
        userService.updatePwd(newPwd);
        //删除redis中的token
        stringRedisTemplate.delete(token);//删除token
        return Result.success();
    }
}