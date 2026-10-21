package com.example.demo.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动get、set、toString、equals、hashCode
@NoArgsConstructor // 无参构造，序列化必备
@AllArgsConstructor // 全参构造，静态方法new对象用
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    // ===================== 成功静态方法 =====================
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> success(String msg, T data) {

        return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> Result<T> success( int code,String msg,T data) {//自定义成功方法，错误代码，错误信息，返回数据自定义
        return new Result<>(code,msg,data);
    }

    // ===================== 失败静态方法 =====================
    public static <T> Result<T> fail(ResultCode codeEnum) {

        return new Result<>(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    public static <T> Result<T> fail(int code, String msg) {

        return new Result<>(code, msg, null);
    }

}