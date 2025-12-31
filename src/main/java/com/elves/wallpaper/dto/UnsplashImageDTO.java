package com.elves.wallpaper.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Unsplash API 返回的图片数据DTO
 */
@Data
public class UnsplashImageDTO {
    private String id;
    private Integer width;
    private Integer height;
    private String description;
    private String alt_description; // 图片标题/描述
    private String color;           // 平均色值
    private Urls urls;              // 包含不同尺寸的URL
    private User user;              // 摄影师信息
    private Links links;            // 相关链接
    private List<Tag> tags;         // 标签（搜索结果接口才会有）

    @Data
    public static class Urls {
        private String raw;         // 原图
        private String full;        // 高清
        private String regular;     // 常用（适合详情页）
        private String small;       // 小图（适合列表预览）
        private String thumb;       // 缩略图
    }
    
    @Data
    public static class User {
        private String id;
        private String username;
        private String name;
        @JsonProperty("portfolio_url")
        private String portfolio_url;
        private UserLinks links;
    }
    
    @Data
    public static class UserLinks {
        private String self;
        private String html;        // 用户个人页面
        private String photos;
        private String likes;
        private String portfolio;
    }
    
    @Data
    public static class Links {
        private String self;
        private String html;        // Unsplash上的图片页面
        private String download;    // 下载链接
        @JsonProperty("download_location")
        private String download_location;
    }
    
    @Data
    public static class Tag {
        private String type;
        private String title;
    }
}

