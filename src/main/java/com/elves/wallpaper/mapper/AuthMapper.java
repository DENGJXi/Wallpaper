package com.elves.wallpaper.mapper;

import com.elves.wallpaper.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    User getByUsername(String username);

    /**
     * 插入数据
     * @param user
     * @return
     */
    int insert(User user);
}
