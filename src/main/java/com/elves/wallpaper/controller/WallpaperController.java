package com.elves.wallpaper.controller;

import com.elves.wallpaper.common.Result;
import com.elves.wallpaper.mapper.WallpaperMapper;
import com.elves.wallpaper.model.Wallpaper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/wallpapers")
@CrossOrigin //处理前端跨域访问接口
public class WallpaperController {
    @Autowired
    private WallpaperMapper wallpaperMapper;

    @GetMapping
    public Result<List<Wallpaper>> list(){
        List<Wallpaper> list = wallpaperMapper.findAll();
        return Result.success(list);
    }
}
