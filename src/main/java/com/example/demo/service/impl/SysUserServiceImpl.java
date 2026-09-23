package com.example.demo.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.result.Result;
import com.example.demo.common.result.ResultCode;
import com.example.demo.dto.LoginDTO;
import com.example.demo.entity.SysUser;
import com.example.demo.mapper.SysUserMapper;
import com.example.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public Result<SysUser> login(LoginDTO loginDTO) {
        //1. mybatis‑plus条件构造器，根据用户名查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, loginDTO.getUsername());
        SysUser sysUser = baseMapper.selectOne(wrapper);



        //2. 判断账号是否存在
        if(sysUser == null){
            return Result.fail(ResultCode.USER_NOT_EXIST);
        }

        //3. 直接明文对比密码（现在不加密！后面再改加密）
        if(!sysUser.getPassword().equals(loginDTO.getPassword())){
            return Result.fail(ResultCode.PASSWORD_ERROR);
        }

        //4. 可以再加：判断账号状态是否启用 status=1正常
        if(sysUser.getStatus() != 1){
            return Result.fail(ResultCode.ACCOUNT_DISABLE);
        }

        String token = UUID.randomUUID().toString().replaceAll("-","");
        sysUser.setToken(token);
        sysUser.setTokenExpireTime(LocalDateTime.now().plusDays(7));

        baseMapper.updateById(sysUser);

        //登录成功，返回用户信息给前端
        return Result.success(sysUser);
    }
}
