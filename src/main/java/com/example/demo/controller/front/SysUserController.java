package com.example.demo.controller.front;

import com.example.demo.common.result.Result;
import com.example.demo.common.result.ResultCode;
import com.example.demo.dto.LoginDTO;
import com.example.demo.entity.SysUser;
import com.example.demo.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {

        this.sysUserService = sysUserService;
    }

    /**
     * 简易登录接口
     * post请求，json传参
     */
    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody LoginDTO loginDTO) {

        return sysUserService.login(loginDTO);
    }

    // 在 SysUserController 里加一个测试接口
    @GetMapping("/user/info")
    public Result<SysUser> getUserInfo(HttpServletRequest request) {
        // 从 request 里获取之前拦截器存进去的用户信息
        SysUser currentUser = (SysUser) request.getAttribute("currentUser");
        if (currentUser == null) {
            return Result.fail(ResultCode.UNAUTHORIZED);
        }
        // 返回用户信息（记得把密码等敏感字段置空）
        currentUser.setPassword(null);
        return Result.success(currentUser);
    }
}
