package com.elves.wallpaper.controller;

import com.elves.wallpaper.common.PageResult;
import com.elves.wallpaper.common.Result;
import com.elves.wallpaper.model.Wallpaper;
import com.elves.wallpaper.service.UserLikeService;
import com.elves.wallpaper.service.WallpaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/wallpapers")
@CrossOrigin //处理前端跨域访问接口
public class WallpaperController {
    @Autowired
    private WallpaperService wallpaperService;
    @Autowired
    private UserLikeService userLikeService;

    /**
     * 分页查询壁纸列表
     * @param page  当前页码，默认为1
     * @param size  每页页码，默认是10
     * @return  包含分页数据（总页数，当前页列表等）的统一响应结果
     */
    @GetMapping
    public Result<PageResult<Wallpaper>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        PageResult<Wallpaper> pageResult = wallpaperService.findAll(page, size);
        return Result.success(pageResult);
    }

    /**
     * 查询我的喜欢
     * @param page  当前页码，默认为1
     * @param size  每页页码，默认是10
     * @return  包含分页数据（总页数，当前页列表等）的统一响应结果
     */
    @GetMapping("/mylike")
    public Result<PageResult<Wallpaper>> getMyLikeWallpapers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        PageResult<Wallpaper> pageResult = wallpaperService.getMyLikeWallpapers(page, size);
        return Result.success(pageResult);
    }

    /**
     * 点赞与取消点赞
     * @param wallpaperId   壁纸id
     * @return  固定返回格式，包含code，data，message
     */
    @PostMapping("/togglelike/{wallpaperId}") // 添加路径占位符
    public Result toggleLike(@PathVariable("wallpaperId") Long wallpaperId) { // 改用 @PathVariable
        boolean isLiked = userLikeService.toggleLike(wallpaperId);
        return Result.success(isLiked);
    }

}
