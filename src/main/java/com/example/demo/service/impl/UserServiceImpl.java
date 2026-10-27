package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.result.ResultCode;
import com.example.demo.dto.UserLoginDTO;
import com.example.demo.dto.UserLoginVO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtUtil jwtUtil;

    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    //注册
    @Override
    public void register(UserRegisterDTO dto) {
        // 1. 查重
        if (lambdaQuery().eq(User::getUsername, dto.getUsername()).count() > 0) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        // 2. DTO -> Entity
        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setRole("USER");
        user.setStatus(1);

        // 3. 插入
        save(user);
    }


    //登录方法
    @Override
    public UserLoginVO login(UserLoginDTO dto) {
        // 1. 按用户名查用户
        User user = lambdaQuery()
                .eq(User::getUsername, dto.getUsername().trim())
                .one();

        // 2. 用户不存在 或 密码错误，统一提示（防用户名枚举）
        if (user == null || !BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // 3. 校验账号状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLE);
        }

        // 4. 签发 token
        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        // 5. 组装 VO 返回
        UserLoginVO vo = new UserLoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setRole(user.getRole());
        return vo;
    }
}