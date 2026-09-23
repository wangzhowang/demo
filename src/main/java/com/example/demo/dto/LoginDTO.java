package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginDTO {
    /** 登录账号 username */
    private String username;
    /** 前端传过来的原始密码 */
    private String password;
}
