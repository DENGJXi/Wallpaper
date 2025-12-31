package com.elves.wallpaper.mapper;

import com.elves.wallpaper.dto.UserLikeReq;
import com.elves.wallpaper.model.Wallpaper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserLikeMapper {
    /**
     * 检查该用户是否已经点赞过该壁纸
     * @param userLikeReq   用户喜爱dto
     * @return  boolean值
     */
    boolean checkExists(UserLikeReq userLikeReq);

    /**
     * 删除当前用户对壁纸的点赞记录
     * @param userLikeReq   用户喜爱dto
     */
    void delete(UserLikeReq userLikeReq);

    /**
     * 插入用户点赞记录
     * @param userLikeReq   用户喜爱dto
     */
    void insert(UserLikeReq userLikeReq);

    /**
     * 根据用户id查找出该用户点赞的壁纸
     * @param userId    用户id
     * @return          返回壁纸列表
     */
    List<Wallpaper> findMyLikes(Long userId);
}
