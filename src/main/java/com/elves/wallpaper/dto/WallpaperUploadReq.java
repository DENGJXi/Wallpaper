package com.elves.wallpaper.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class WallpaperUploadReq {
    // 上传的文件
    private MultipartFile file;

    // 必填字段
    private String title;  // 图片标题

    // 可选字段
    private String description;      // 详细描述

    // 分类
    private String category;         // 自定义分类
}
