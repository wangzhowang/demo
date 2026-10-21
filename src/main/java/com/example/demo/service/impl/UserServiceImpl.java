package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.result.ResultCode;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

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
}