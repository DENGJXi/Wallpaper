package com.elves.wallpaper.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResp {
    private Long id;                    //用户UID
    private String nickname;            //用户昵称
    private String avatar;              //用户头像地址
    private String bio;                 //用户简介
}
