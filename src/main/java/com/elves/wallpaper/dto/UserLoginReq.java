package com.elves.wallpaper.dto;

import lombok.Data;

@Data
public class UserLoginReq {
    private String username;            //账号
    private String password;            //密码
}
