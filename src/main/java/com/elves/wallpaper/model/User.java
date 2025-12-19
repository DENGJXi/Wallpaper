package com.elves.wallpaper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;                    //用户UID
    private String username;            //账号
    private String password;            //密码
    private String nickname;            //用户昵称
    private String avatar;              //用户头像地址
    private LocalDateTime createTime;   //创建时间
}
