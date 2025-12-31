package com.elves.wallpaper.service;

/**
 * Unsplash数据导入服务
 */
public interface UnsplashService {
    
    /**
     * 从Unsplash导入壁纸数据
     * @param keyword   搜索关键词
     * @param page      页码
     * @param perPage   每页数量
     * @return          导入成功的数量
     */
    int importWallpapersFromUnsplash(String keyword, int page, int perPage);
    
    /**
     * 从Unsplash导入随机壁纸
     * @param count     导入数量
     * @return          导入成功的数量
     */
    int importRandomWallpapers(int count);
}
