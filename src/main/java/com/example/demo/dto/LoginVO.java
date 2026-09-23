package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginVO {
    private Long userId;
    private String username;
    private String realName;
    private String cashierNo;
    private Long shopId;
    // 后面可以加上token
    private String token;
}
