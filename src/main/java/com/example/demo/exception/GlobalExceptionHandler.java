package com.example.demo.exception;

import com.example.demo.common.result.Result;
import com.example.demo.common.result.ResultCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 业务异常：你主动 throw 的
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 2. 参数校验失败：@Valid 触发
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(f -> f.getDefaultMessage())
                .orElse("参数校验失败");
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), msg);
    }

    // 3. 兜底：其他所有未处理异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // 生产环境这里要 log.error 记录堆栈，方便排查
        e.printStackTrace();
        return Result.fail(ResultCode.SERVER_ERROR.getCode(), "服务器内部异常");
    }
}