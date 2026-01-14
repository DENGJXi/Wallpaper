package com.elves.wallpaper.service.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.elves.wallpaper.common.PageResult;
import com.elves.wallpaper.dto.WallpaperUploadReq;
import com.elves.wallpaper.mapper.UserLikeMapper;
import com.elves.wallpaper.mapper.WallpaperMapper;
import com.elves.wallpaper.model.Wallpaper;
import com.elves.wallpaper.service.WallpaperService;
import com.elves.wallpaper.utils.OssUtil;
import com.elves.wallpaper.utils.SecurityUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class WallpaperServiceImpl implements WallpaperService {
    @Autowired
    private WallpaperMapper wallpaperMapper;
    @Autowired
    private UserLikeMapper userLikeMapper;

    @Value("${unsplash.access-key}")
    private String accessKey;

    @Value("${unsplash.secret-key}")
    private String secretKey;
    @Autowired
    private OssUtil ossUtil;

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

    @Override
    public PageResult<Wallpaper> search(int page, int size, String keyword) {
        //  开启分页
        PageHelper.startPage(page, size);
        //  查询数据
        List<Wallpaper> result = wallpaperMapper.searchByKey(keyword);
        //  封装返回
        return PageResult.of(new PageInfo<>(result));
    }

    @Override
    public void wallpaperUpload(WallpaperUploadReq wallpaperUploadReq) {
        String fileUrl = ossUtil.uploadFile(wallpaperUploadReq.getFile());

        Wallpaper wallpaper = new Wallpaper();
        wallpaper.setFileUrl(fileUrl);
        // m_lfit: 等比缩放，限定在指定w与h的矩形内的最大图片，不会裁剪
        wallpaper.setThumbnailUrl(fileUrl+"?x-oss-process=image/resize,m_lfit,w_400");
        wallpaper.setTitle(wallpaperUploadReq.getTitle());
        wallpaper.setDescription(wallpaperUploadReq.getDescription());
        wallpaper.setCategory((wallpaperUploadReq.getCategory()));

        wallpaperMapper.insertWallpaper(wallpaper);
    }

    @Override
    public PageResult<Wallpaper> getRankList(int page, int size) {
        PageHelper.startPage(page, size);
        List<Wallpaper> list = wallpaperMapper.rankListByHits();
        PageInfo<Wallpaper> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo);
    }


}
