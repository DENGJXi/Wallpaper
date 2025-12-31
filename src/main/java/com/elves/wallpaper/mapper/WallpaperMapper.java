package com.elves.wallpaper.mapper;

import com.elves.wallpaper.model.Wallpaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
