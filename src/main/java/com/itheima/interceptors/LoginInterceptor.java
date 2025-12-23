package com.itheima.interceptors;

import com.itheima.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        try {
            //验证token
            Map<String,Object> claims= JwtUtil.parseToken(token);
            //放行
            return true;
        } catch (Exception e) {
            //http响应状态码设为401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //不放行
            return false;
        }
    }
}
