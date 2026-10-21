package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.entity.User;

public interface UserService extends IService<User> {
    /**
     * 用户注册
     */
    void register(UserRegisterDTO dto);
}
