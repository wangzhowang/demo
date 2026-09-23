package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.result.Result;
import com.example.demo.dto.LoginDTO;
import com.example.demo.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    /**
     * 收银员登录
     * @param loginDTO 账号密码
     */
    Result<SysUser> login(LoginDTO loginDTO);
}
