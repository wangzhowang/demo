package com.example.demo.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {
    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx

    PARAM_ERROR(400, "参数校验失败"),
    UNAUTHORIZED(401, "未登录或token失效"),
    FORBIDDEN(403, "权限不足，禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    // 服务端错误 5xx
    SERVER_ERROR(500, "服务器内部异常"),


    //登录校验枚举类
    USER_NOT_EXIST(404,"用户名不存在"),
    PASSWORD_ERROR(405, "用户名密码不匹配"),
    ACCOUNT_DISABLE(409,"账号启用异常" );

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}