package com.elves.wallpaper.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.elves.wallpaper.model.Wallpaper;

@Mapper
public interface WallpaperMapper {
    /**
     * 查询壁纸表中所有壁纸
     * @return 壁纸列表
     */
    List<Wallpaper> findAll();

    /**
     * 更新点赞数
     * @param wallpaperId   壁纸id
     * @param offset        增量值（1 或 -1）
     */
    void updateHitCount(@Param("wallpaperId") Long wallpaperId, @Param("offset") int offset);

    /**
     * 根据关键字查找壁纸
     * @param keyword   关键字
     * @return  壁纸列表
     */
    List<Wallpaper> searchByKey(String keyword);
    
    /**
     * 插入新的壁纸
     * @param wallpaper 壁纸对象
     */
    void insertWallpaper(Wallpaper wallpaper);

    /**
     * 根据点赞数排序壁纸
     * @return
     */
    List<Wallpaper> rankListByHits();
}
