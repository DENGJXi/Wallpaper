package com.elves.wallpaper.mapper;

import com.elves.wallpaper.model.Wallpaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WallpaperMapper {

    List<Wallpaper> findAll();
}
