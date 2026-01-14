package com.elves.wallpaper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elves.wallpaper.common.PageResult;
import com.elves.wallpaper.common.Result;
import com.elves.wallpaper.dto.WallpaperSearchReq;
import com.elves.wallpaper.dto.WallpaperUploadReq;
import com.elves.wallpaper.model.Wallpaper;
import com.elves.wallpaper.service.UnsplashService;
import com.elves.wallpaper.service.UserLikeService;
import com.elves.wallpaper.service.WallpaperService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/wallpapers")
@CrossOrigin //处理前端跨域访问接口
public class WallpaperController {
    @Autowired
    private WallpaperService wallpaperService;
    @Autowired
    private UserLikeService userLikeService;
    @Autowired
    private UnsplashService unsplashService;

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
        log.info("分页查询壁纸 - page: {}, size: {}", page, size);
        PageResult<Wallpaper> pageResult = wallpaperService.findAll(page, size);
        log.info("分页查询结果 - total: {}, records size: {}", pageResult.getTotal(), pageResult.getRecords().size());
        return Result.success(pageResult);
    }
    @GetMapping("/search")
    public Result<PageResult<Wallpaper>> search(WallpaperSearchReq req) {
        PageResult<Wallpaper> pageResult = wallpaperService.search(
                req.getPage(),
                req.getSize(),
                req.getKeyword()
        );
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

    /**
     * 从Unsplash导入壁纸数据
     * @param keyword   搜索关键词
     * @param page      页码（默认1）
     * @param perPage   每页数量（默认30）
     * @return  导入成功的数量
     */
    @PostMapping("/import/search")
    public Result<Integer> importFromUnsplashSearch(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int perPage
    ) {
        int count = unsplashService.importWallpapersFromUnsplash(keyword, page, perPage);
        return Result.success(count);
    }

    /**
     * 从Unsplash导入随机壁纸
     * @param count 导入数量（默认10）
     * @return  导入成功的数量
     */
    @PostMapping("/import/random")
    public Result<Integer> importRandomWallpapers(
            @RequestParam(defaultValue = "10") int count
    ) {
        int successCount = unsplashService.importRandomWallpapers(count);
        return Result.success(successCount);
    }

    /**
     * 用户自行上传壁纸
     * @param wallpaperUploadReq  上传壁纸dto
     * @return  是否上传成功
     */
    @PostMapping("/upload")
    public Result wallpaperUpload(@ModelAttribute WallpaperUploadReq wallpaperUploadReq){
        wallpaperService.wallpaperUpload(wallpaperUploadReq);
        return Result.success();
    }
    /**
     * 壁纸排行榜
     * @return 排行榜壁纸列表
     */
    @GetMapping("/ranklist")
    public Result<PageResult<Wallpaper>> rankList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        PageResult<Wallpaper> pageResult = wallpaperService.getRankList(page,size);
        return Result.success(pageResult);
    }
}
