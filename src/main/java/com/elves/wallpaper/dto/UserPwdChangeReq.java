package com.elves.wallpaper.dto;

import lombok.Data;

// 用户密码重置请求DTO
@Data
public class UserPwdChangeReq {
    private String newPassword;         //新密码
    private String confirmNewPassword;  //确认新密码
    private String verificationCode;    // 邮箱收到的验证码
}
