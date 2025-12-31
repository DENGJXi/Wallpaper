package com.elves.wallpaper.dto;

import lombok.Data;

@Data
public class UserPwdResetReq {
    private String resetToken;         //重置令牌
    private String email;               //邮箱
    private String newPassword;         //新密码
    private String confirmNewPassword;  //确认新密码
}
