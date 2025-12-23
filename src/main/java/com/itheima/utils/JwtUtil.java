package com.itheima.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String KEY="itheima";

    //接收业务数据，生成token并且返回
    public static String getToken(Map<String,Object> claims){
        return JWT.create()//创建JWTBuilder对象
                .withClaim("claims",claims)//添加自定义数据
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))//设置过期时间
                .sign(Algorithm.HMAC256(KEY));//设置密钥
    }

    //接收token，验证token，返回业务数据
    public static Map<String,Object> parseToken(String token){
        return JWT.require(Algorithm.HMAC256(KEY))//创建验证对象
                .build()//创建验证对象
                .verify(token)//验证token
                .getClaim("claims")//获取业务数据
                .asMap();//转为Map
    }
}
