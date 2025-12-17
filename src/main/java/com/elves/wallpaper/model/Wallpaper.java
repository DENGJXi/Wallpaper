package com.elves.wallpaper.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Wallpaper {
    private Long id;                    //壁纸id
    private String title;               //壁纸标题
    private String description;         //壁纸描述
    private String category;            //壁纸分类
    private String thumbnailUrl;        //缩略图
    private String fileUrl;             //下载地址
    private Integer hits;               //点击数
    private LocalDateTime createTime;   //创建时间

}
