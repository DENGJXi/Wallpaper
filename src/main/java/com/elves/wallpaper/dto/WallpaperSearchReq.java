package com.elves.wallpaper.dto;

import lombok.Data;

@Data
public class WallpaperSearchReq {
    private Integer page = 1;
    private Integer size = 10;
    private String keyword;     //关键字
}
