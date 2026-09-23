package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    //唯一标识
    @TableId
    private Long userId;
    //用户名（登录账号）
    private String username;
    //密码（BCrypt加密存储）
    private String password;
    //真实姓名
    private String realName;

    //收银员工号
    private String cashierNo;
    //门店id
    private Long shopId;
    //收银员状态：1正常 0禁用
    private Integer status;

    // ⭐ 新增：存储 Token
    private String token;
    

    // ⭐ 新增：存储 Token 过期时间（可选，但建议加上）
    private LocalDateTime tokenExpireTime;

}