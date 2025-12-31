package com.elves.wallpaper.service;

public interface UserLikeService {

    /**
     * 点赞或取消点赞
     * @param wallpaperId   壁纸UID
     */
    boolean toggleLike(Long wallpaperId);
}
