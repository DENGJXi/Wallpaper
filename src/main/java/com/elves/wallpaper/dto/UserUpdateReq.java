package com.elves.wallpaper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateReq {
    private String nickname;    // 用户昵称
    private String bio;         // 用户简介
}
