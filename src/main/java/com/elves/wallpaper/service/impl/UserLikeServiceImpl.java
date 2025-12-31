package com.elves.wallpaper.service.impl;

import com.elves.wallpaper.dto.UserLikeReq;
import com.elves.wallpaper.mapper.UserLikeMapper;
import com.elves.wallpaper.mapper.WallpaperMapper;
import com.elves.wallpaper.service.UserLikeService;
import com.elves.wallpaper.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLikeServiceImpl implements UserLikeService {
    @Autowired
    private UserLikeMapper userLikeMapper;
    @Autowired
    private WallpaperMapper wallpaperMapper;

    /**
     * 点赞或取消点赞
     * @param wallpaperId   壁纸UID
     * @return true代表点赞，false代表取消点赞
     */
    @Override
    @Transactional  //  事务注解
    public boolean toggleLike(Long wallpaperId) {
        Long userId = SecurityUtils.getCurrentUserId();
        UserLikeReq userLikeReq = new UserLikeReq();
        userLikeReq.setUserId(userId);
        userLikeReq.setWallpaperId(wallpaperId);
        try{
            boolean isLiked = userLikeMapper.checkExists(userLikeReq);
            if (!isLiked) {
                //  插入当前数据
                userLikeMapper.insert(userLikeReq);
                //  更新壁纸表中的点赞数
                wallpaperMapper.updateHitCount(wallpaperId,1);
                return true;
            } else {
                //  删除当前数据
                userLikeMapper.delete(userLikeReq);
                //  更新壁纸表中的点赞数
                wallpaperMapper.updateHitCount(wallpaperId,-1);
                return false;
            }
        }catch (DuplicateKeyException e){
            // 如果唯一索引冲突，说明已经点过赞了，直接返回或报错
            throw new RuntimeException("请勿重复点赞");
        }
    }
}
