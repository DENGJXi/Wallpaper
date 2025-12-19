package com.elves.wallpaper.service;

import com.elves.wallpaper.common.Result;
import com.elves.wallpaper.dto.UserLoginDto;
import com.elves.wallpaper.dto.UserRegisterDto;
import com.elves.wallpaper.vo.UserLoginVo;

public interface AuthService {
    /**
     * 用户登录
     * @param userLoginDto
     * @return
     */
    UserLoginVo login(UserLoginDto userLoginDto);

    void logout();

    void register(UserRegisterDto userRegisterDto);
}
