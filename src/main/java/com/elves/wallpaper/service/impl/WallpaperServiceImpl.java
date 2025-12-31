package com.elves.wallpaper.service.impl;

import com.elves.wallpaper.common.PageResult;
import com.elves.wallpaper.mapper.UserLikeMapper;
import com.elves.wallpaper.mapper.WallpaperMapper;
import com.elves.wallpaper.model.Wallpaper;
import com.elves.wallpaper.service.WallpaperService;
import com.elves.wallpaper.utils.SecurityUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WallpaperServiceImpl implements WallpaperService {
    @Autowired
    private WallpaperMapper wallpaperMapper;
    @Autowired
    private UserLikeMapper userLikeMapper;

    /**
     * 获取所有壁纸
     * @param page  当前页数
     * @param size  每页大小
     * @return      分页查询固定格式（总记录数+页码+每页大小+当前页的数据列表）
     */
    @Override
    public PageResult<Wallpaper> findAll(int page, int size) {
        //  开启分页
        PageHelper.startPage(page, size);
        //  查询数据
        List<Wallpaper> list = wallpaperMapper.findAll();
        //  将查询的数据存储到list中
        PageInfo<Wallpaper> pageInfo = new PageInfo<>(list);

        return PageResult.of(pageInfo);
    }

    /**
     * 查询当前用户点赞的壁纸
     * @param page      当前页数
     * @param size      每页大小
     * @return          分页查询固定格式（总记录数+页码+每页大小+当前页的数据列表）
     */
    @Override
    public PageResult<Wallpaper> getMyLikeWallpapers(int page, int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        //  开启分页
        PageHelper.startPage(page, size);
        //  查询数据
        List<Wallpaper> myLikes = userLikeMapper.findMyLikes(userId);
        //  封装返回
        return PageResult.of(new PageInfo<>(myLikes));
    }


}
