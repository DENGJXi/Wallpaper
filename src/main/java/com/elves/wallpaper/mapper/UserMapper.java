package com.elves.wallpaper.mapper;

import com.elves.wallpaper.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据用户ID查询用户信息
     * @param id    用户ID
     * @return      用户信息
     */
    @Select("select * from user where id = #{id}")
    User getUserById(Long id);


    int updateUser(User user);

}
