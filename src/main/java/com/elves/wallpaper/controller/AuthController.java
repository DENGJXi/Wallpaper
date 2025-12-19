package com.elves.wallpaper.controller;

import com.elves.wallpaper.common.Result;
import com.elves.wallpaper.dto.UserLoginDto;
import com.elves.wallpaper.dto.UserRegisterDto;
import com.elves.wallpaper.mapper.AuthMapper;
import com.elves.wallpaper.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDto userLoginDto) {
        return Result.success(authService.login(userLoginDto));
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
     * @param userRegisterDto
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDto  userRegisterDto) {
        authService.register(userRegisterDto);
        return Result.success();
    }
}
