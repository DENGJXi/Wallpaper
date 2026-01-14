package com.elves.wallpaper.service;

import java.util.List;

import com.elves.wallpaper.common.PageResult;
import com.elves.wallpaper.dto.WallpaperUploadReq;
import com.elves.wallpaper.model.Wallpaper;

public interface WallpaperService {
    /**
     * 查询数据库中所有壁纸
     * @param page  当前页数
     * @param size  页码大小
     * @return  分页结果
     */
    PageResult<Wallpaper> findAll(int page, int size);

    /**
     * 获取我喜欢的壁纸列表
     * @param page  当前页数
     * @param size  页码大小
     * @return  分页结果
     */
    PageResult<Wallpaper> getMyLikeWallpapers(int page, int size);

    /**
     * 根据关键字搜索壁纸
     * @param page      当前页数
     * @param size      页码大小
     * @param keyword   关键字
     * @return          分页结果
     */
    PageResult<Wallpaper> search(int page, int size, String keyword);

    /**
     * 用户上传壁纸
     * @param wallpaperUploadReq 上传壁纸dto
     * @return  上传成功或失败
     */
    void wallpaperUpload(WallpaperUploadReq wallpaperUploadReq);

    PageResult<Wallpaper> getRankList(int page, int size);

}
