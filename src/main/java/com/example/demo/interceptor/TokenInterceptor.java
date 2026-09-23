package com.example.demo.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.SysUser;
import com.example.demo.mapper.SysUserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.time.LocalDateTime;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final SysUserMapper sysUserMapper;

    public TokenInterceptor(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头获取 Token
        String token = request.getHeader("Authorization");

        // 2. 如果没有 Token，拒绝访问
        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先登录\"}");
            return false;
        }

        // 3. 根据 Token 查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getToken, token);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);

        // 4. 用户不存在
        if (sysUser == null) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"Token 无效，请重新登录\"}");
            return false;
        }

        // 5. Token 是否过期
        if (sysUser.getTokenExpireTime() != null &&
                sysUser.getTokenExpireTime().isBefore(LocalDateTime.now())) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"Token 已过期，请重新登录\"}");
            return false;
        }

        // 6. 把用户信息存到 request 里，方便后续接口使用
        request.setAttribute("currentUser", sysUser);

        return true;  // 放行
    }
}