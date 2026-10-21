package com.example.demo.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {
    // 成功
    SUCCESS(1200, "操作成功"),

    // 客户端错误 4xx

    PARAM_ERROR(1400, "参数校验失败"),
    UNAUTHORIZED(1401, "未登录或token失效"),
    FORBIDDEN(1403, "权限不足，禁止访问"),
    NOT_FOUND(1404, "资源不存在"),

    // 服务端错误 5xx
    SERVER_ERROR(1500, "服务器内部异常"),

    //注册校验
    USERNAME_EXISTS(1201,"账号已存在"),

    //登录校验枚举类
    USER_NOT_EXIST(1404,"用户名不存在"),
    PASSWORD_ERROR(1405, "用户名密码不匹配"),
    ACCOUNT_DISABLE(1409,"账号启用异常" );

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}