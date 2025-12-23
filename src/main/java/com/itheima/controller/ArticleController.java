package com.itheima.controller;

import com.itheima.pojo.Message;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/article")
@Tag(name = "文章相关接口")
public class ArticleController {


    @GetMapping("/list")
    @Operation(summary = "获取文章列表")
    public Result<String> list(/*@RequestHeader(name = "Authorization") String token, HttpServletResponse response*/){
        /*try {
            Map<String,Object> claims= JwtUtil.parseToken(token);
            return Result.success(Message.ALL_ARTICLE);
        } catch (Exception e) {
            *//**
             * 状态码相应为401
             *//*
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);*/
            return Result.error(Message.NOT_LOGIN);
    }
}
