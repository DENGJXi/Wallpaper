package com.elves.wallpaper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeReq {
    private Long userId;       // 用户ID
    private Long wallpaperId;  // 壁纸ID
}
