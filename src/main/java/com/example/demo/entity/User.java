package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    //主键唯一标识
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户名
    private String username;
    //密码
    @JsonIgnore
    @ToString.Exclude
    private String password;
    //昵称
    private String nickname;
    //用户手机号
    private String phone;
    //邮箱
    private String email;
    //角色
    private String role;
    //状态
    private Integer status;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
}