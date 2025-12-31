package com.elves.wallpaper.controller;

import com.elves.wallpaper.common.Result;
import com.elves.wallpaper.dto.UserPwdResetReq;
import com.elves.wallpaper.dto.UserPwdChangeReq;
import com.elves.wallpaper.dto.UserUpdateReq;
import com.elves.wallpaper.service.UserService;
import com.elves.wallpaper.vo.UserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping
    public Result<UserResp> uploadProfile(@RequestBody UserUpdateReq userUpdateReq) {
        UserResp userResp = userService.uploadProfile(userUpdateReq);
        return Result.success(userResp);
    }

    /**
     * 修改密码（登录态）
     * @param userPwdChangeReq  用户密码重置请求DTO
     */
    @PutMapping("/password/change")
    public Result changePwd(@RequestBody UserPwdChangeReq userPwdChangeReq) {
        userService.changePwd(userPwdChangeReq);
        return Result.success();
    }
    /**
     * 重置密码（未登录态）
     * @param userPwdResetReq  用户密码重置请求DTO
     */
    @PutMapping("/password/reset")
    public Result resetPwd(@RequestBody UserPwdResetReq userPwdResetReq) {
        userService.resetPwd(userPwdResetReq);
        return Result.success();
    }
}
