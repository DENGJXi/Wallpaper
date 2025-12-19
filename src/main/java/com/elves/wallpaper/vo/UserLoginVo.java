package com.elves.wallpaper.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVo {
    private String username;    //  用户名
    private String avatar;      //  头像
    private String nickname;    //  昵称
    private String token;       //  token
}
