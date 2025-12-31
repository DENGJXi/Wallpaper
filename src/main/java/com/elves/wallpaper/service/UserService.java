package com.elves.wallpaper.service;

import com.elves.wallpaper.dto.UserPwdResetReq;
import com.elves.wallpaper.dto.UserPwdChangeReq;
import com.elves.wallpaper.dto.UserUpdateReq;
import com.elves.wallpaper.vo.UserResp;

public interface UserService {
    /**
     * 更新用户资料
     * @param userUpdateReq 用户详情DTO
     * @return  UserVo
     */
    UserResp uploadProfile(UserUpdateReq userUpdateReq);

    /**
     * 修改密码（登录态，可从本地获取用户ID）
     * @param userPwdChangeReq 用户密码重置DTO
     */
    void changePwd(UserPwdChangeReq userPwdChangeReq);

    /**
     * 重置密码（未登录态）
     * @param userPwdResetReq 用户忘记密码DTO
     */
    void resetPwd(UserPwdResetReq userPwdResetReq);
}
