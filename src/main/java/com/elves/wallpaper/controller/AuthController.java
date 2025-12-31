package com.elves.wallpaper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elves.wallpaper.common.Result;
import com.elves.wallpaper.dto.ResetPwdCodeReq;
import com.elves.wallpaper.dto.ResetPwdEmailReq;
import com.elves.wallpaper.dto.UserLoginReq;
import com.elves.wallpaper.dto.UserRegisterReq;
import com.elves.wallpaper.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * @param userLoginReq
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginReq userLoginReq) {
        return Result.success(authService.login(userLoginReq));
    }

    /**
     * 用户登出
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        authService.logout();
        return Result.success();
    }
    /**
     * 用户注册
     * @param userRegisterReq
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterReq userRegisterReq) {
        authService.register(userRegisterReq);
        return Result.success();
    }

    /**
     * 发送验证码邮件(登陆状态)
     * @return
     */
    @PostMapping("/verification/send")
    public Result sendVerificationCode() {
        authService.sendVerificationCode();
        return Result.success();
    }
    /**
     * 发送忘记密码验证码邮件
     * @param resetPwdEmailReq 重置密码邮箱
     * @return
     */
    @PostMapping("/reset/send")
    public Result sendResetPwdCode(@RequestBody ResetPwdEmailReq resetPwdEmailReq) {
        boolean isSent = authService.sendResetPwdCode(resetPwdEmailReq.getEmail());
        if (isSent) {
            return Result.success();
        } else {
            return Result.error("Failed to send verification code");
        }
    }
    /**
     * 验证忘记密码验证码并签发重置令牌
     * @param resetPwdCodeReq 重置密码验证码请求体
     * @return
     */
    @PostMapping("/reset/verify")
    public Result verifyResetPwdCode(@RequestBody ResetPwdCodeReq resetPwdCodeReq) {
        String resetToken = authService.verifyForgetPwdCode(resetPwdCodeReq.getEmail(), resetPwdCodeReq.getCode());
        return Result.success(resetToken);
    }

}
