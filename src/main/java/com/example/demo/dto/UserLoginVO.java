package com.example.demo.dto;

import lombok.Data;

@Data
public class UserLoginVO {
    private String token;
    private Long userId;
    private String username;
    private String nickname;
    private String role;
}