package com.elves.wallpaper.service;

import com.elves.wallpaper.common.PageResult;
import com.elves.wallpaper.model.Wallpaper;

public interface WallpaperService {

    PageResult<Wallpaper> findAll(int page, int size);

    public PageResult<Wallpaper> getMyLikeWallpapers(int page, int size);
}
