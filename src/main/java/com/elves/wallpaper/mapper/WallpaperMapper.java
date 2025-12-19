package com.elves.wallpaper.mapper;

import com.elves.wallpaper.model.Wallpaper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WallpaperMapper {
    /**
     * 查询壁纸表中所有壁纸
     * @return 壁纸列表
     */
    List<Wallpaper> findAll();
}
