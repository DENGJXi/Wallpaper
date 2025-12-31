package com.elves.wallpaper.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallpaper {
    private Long id;                    //壁纸UID
    private String title;               //壁纸标题
    private String description;         //壁纸描述
    private String category;            //壁纸分类
    private String thumbnailUrl;        //缩略图
    private String fileUrl;             //下载地址
    private Integer hits;               //点击数
    private LocalDateTime createTime;   //创建时间
    
    // 新增字段：用于存储Unsplash数据
    private String unsplashId;          //Unsplash API中的图片ID
    private String photographer;        //摄影师名称
    private String photographerUrl;     //摄影师个人页面链接
    private String downloadUrl;         //Unsplash提供的下载链接
    private String sourceUrl;           //Unsplash原始链接
    private Integer width;              //图片宽度(像素)
    private Integer height;             //图片高度(像素)
    private String color;               //平均色值

}
