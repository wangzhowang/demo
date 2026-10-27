package com.example.demo.exception;

import com.example.demo.common.result.Result;
import com.example.demo.common.result.ResultCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 *
 * 作用：拦截所有 Controller 抛出的异常，统一转成 Result 格式返回给前端。
 * 好处：业务代码里只管 throw，不用每个接口自己 try-catch、自己拼错误响应。
 *
 * 匹配规则（重要）：
 *   Spring 会按“异常类型精确度”匹配，越具体的 @ExceptionHandler 优先级越高。
 *   比如抛 ExpiredJwtException，会先匹配 handleExpired，不会落到 handleException 兜底。
 *   所以下面 1~6 是“具体异常”，最后一个 Exception 是“兜底”，顺序不影响匹配，但建议把兜底放最后，便于阅读。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 业务异常
     * 触发时机：Service 里主动 throw new BusinessException(ResultCode.XXX)
     * 典型场景：用户名已存在、密码错误、账号被禁用、未登录等。
     * 处理方式：直接取异常里携带的 code 和 message，原样返回。
     * 设计意义：业务错误是“预期内”的，不需要打堆栈，返回给前端提示即可。
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 2. 参数校验失败
     * 触发时机：Controller 参数上加了 @Valid，且 DTO 上的 @NotBlank/@Size/@Pattern 等校验不通过。
     * 典型场景：注册时用户名为空、密码太短、手机号格式错误。
     * 处理方式：从 BindingResult 里取第一条字段错误信息返回，前端直接展示。
     * 注意：只取第一条，避免一次性返回一堆错误让前端展示混乱。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(f -> f.getDefaultMessage())
                .orElse("参数校验失败");
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * 3. 资源不存在（404）
     * 触发时机：请求了一个 Controller 里没有映射的路径，Spring 6 抛 NoResourceFoundException。
     * 典型场景：接口还没写、前端路径拼错。
     * 为什么单独处理：如果不单独接住，会落到下面的 Exception 兜底，返回 1500“服务器内部异常”，
     *   把“路径写错”误报成“服务器崩了”，排查时误导人。单独处理返回 1404，语义正确。
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNotFound(NoResourceFoundException e) {
        return Result.fail(ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMsg());
    }

    /**
     * 4. JWT 过期
     * 触发时机：请求头里的 token 已过 exp 时间，JwtUtil.parseToken 抛 ExpiredJwtException。
     * 典型场景：用户登录超过 24 小时没操作，token 自然过期。
     * 处理方式：返回 1201 未登录，前端响应拦截器收到后清 token、跳登录页。
     * 注意：必须放在 JwtException 之前？不需要，Spring 按精确度匹配，ExpiredJwtException 是 JwtException 的子类，
     *   有专门的处理器就会优先匹配它，不会落到 handleJwt。
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public Result<Void> handleExpired(ExpiredJwtException e) {
        return Result.fail(ResultCode.UNAUTHORIZED);
    }

    /**
     * 5. JWT 其他异常（签名错误、格式错误、token 被篡改等）
     * 触发时机：token 不是本服务签发的、被改过、格式不是 xxx.yyy.zzz。
     * 典型场景：有人拿别的系统的 token 来请求、手动改了 payload 导致签名对不上。
     * 处理方式：同样返回 1201 未登录。
     * 注意：ExpiredJwtException 是 JwtException 的子类，但因为有专门的处理器（第 4 个），
     *   过期时不会走到这里；这里只接“非过期”的 JWT 异常。
     */
    @ExceptionHandler(JwtException.class)
    public Result<Void> handleJwt(JwtException e) {
        return Result.fail(ResultCode.UNAUTHORIZED);
    }

    /**
     * 6. 兜底异常（最后防线）
     * 触发时机：上面 1~5 都没匹配到的所有异常，比如空指针、数据库连接失败、SQL 异常等。
     * 处理方式：返回 1500“服务器内部异常”，不把真实异常信息暴露给前端（防止泄露内部细节）。
     * 注意：
     *   - 生产环境这里必须 log.error("...", e) 记录完整堆栈，否则线上出问题无从排查。
     *   - 学习阶段用 e.printStackTrace() 方便看控制台，上线前换成日志框架。
     *   - 这个处理器必须存在，否则未捕获异常会返回 Spring 默认的一大坨错误 JSON。
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.fail(ResultCode.SERVER_ERROR.getCode(), "服务器内部异常");
    }
}