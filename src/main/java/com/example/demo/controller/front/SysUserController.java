package com.example.demo.controller.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.common.result.Result;
import com.example.demo.common.result.ResultCode;
import com.example.demo.dto.LoginDTO;
import com.example.demo.entity.SysUser;
import com.example.demo.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SysUserController {

    private final SysUserService sysUserService;
    private final BaseMapper<SysUser> baseMapper;

    public SysUserController(SysUserService sysUserService, @Qualifier("sysUserMapper") BaseMapper<SysUser> baseMapper) {

        this.sysUserService = sysUserService;
        this.baseMapper = baseMapper;
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

    //测试接口方法获取所有用户信息
    //后续应修改后优化接口
    @GetMapping("/getalluserdata")
    public Result<List<SysUser>> getUserListdata() {
        List<SysUser> allsysuserlist = baseMapper.selectList(null);

        return  Result.success(allsysuserlist);
    }
}
