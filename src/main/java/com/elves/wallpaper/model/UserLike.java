package com.elves.wallpaper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLike {
    private Long id;                    //uid
    private Long wallpaperId;           //壁纸uid
    private Long userId;                //用户uid
    private LocalDateTime createTime;   //创建时间
}
