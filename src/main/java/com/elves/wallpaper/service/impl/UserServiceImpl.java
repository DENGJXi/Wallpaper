package com.elves.wallpaper.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.elves.wallpaper.dto.UserPwdResetReq;
import com.elves.wallpaper.dto.UserPwdChangeReq;
import com.elves.wallpaper.dto.UserUpdateReq;
import com.elves.wallpaper.mapper.UserMapper;
import com.elves.wallpaper.model.User;
import com.elves.wallpaper.model.Wallpaper;
import com.elves.wallpaper.service.AuthService;
import com.elves.wallpaper.service.UserService;
import com.elves.wallpaper.utils.OssUtil;
import com.elves.wallpaper.utils.SecurityUtils;
import com.elves.wallpaper.vo.UserResp;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

import static com.elves.wallpaper.constant.MessageConstant.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OssUtil ossUtil;
    /**
     * 更新用户资料
     * @param userUpdateReq 用户详情DTO
     * @return  UserVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResp uploadProfile(UserUpdateReq userUpdateReq) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = new User();
        user.setId(userId);
        BeanUtil.copyProperties(userUpdateReq, user, CopyOptions.create().setIgnoreNullValue(true));
        int updateRows = userMapper.updateUser(user);
        if (updateRows != 1) {
            throw new RuntimeException(UPDATE_FILED);
        }
        UserResp userResp = new UserResp();
        BeanUtil.copyProperties(user, userResp);
        return userResp;
    }

    /**
     * 修改用户密码（登录态）
     * @param userPwdChangeReq 用户密码重置DTO
     */
    @Override
    public void changePwd(UserPwdChangeReq userPwdChangeReq) {
        // 获取当前用户信息
        Long userId = SecurityUtils.getCurrentUserId();
        User currentUser = userMapper.getUserById(userId);
        String currentEmail = currentUser.getEmail();
        //  验证验证码是否正确
        if (!authService.verifyCode(currentEmail, userPwdChangeReq.getVerificationCode()))
            throw new RuntimeException(CODE_ERROR);

        // 验证新密码格式
        validatePassword(userPwdChangeReq.getNewPassword(), userPwdChangeReq.getConfirmNewPassword());

        // 执行密码更新
        User user = new User();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(userPwdChangeReq.getNewPassword()));
        int updateRows = userMapper.updateUser(user);

        if (updateRows != 1) {
            throw new RuntimeException(UPDATE_FILED);
        }
    }

    @Override
    public void resetPwd(UserPwdResetReq userPwdResetReq) {
        // 判断令牌是否有效
        if(!authService.verifyResetToken(userPwdResetReq.getEmail(), userPwdResetReq.getResetToken())) {
            throw new RuntimeException(TOKEN_INVALID_OR_EXPIRED);
        }
        // 验证新密码格式
        validatePassword(userPwdResetReq.getNewPassword(), userPwdResetReq.getConfirmNewPassword());

        // 执行密码更新
        User user = new User();
        user.setEmail(userPwdResetReq.getEmail());
        user.setPassword(passwordEncoder.encode(userPwdResetReq.getNewPassword()));
        int updateRows = userMapper.updateUser(user);

        if (updateRows != 1) {
            throw new RuntimeException(UPDATE_FILED);
        }
        //  在修改密码成功后立刻删除短期令牌和验证码
        String tokenKey = PWD_RESET_TOKEN_PREFIX + userPwdResetReq.getResetToken();
        stringRedisTemplate.delete(tokenKey);
        String codeKey = VERIFY_CODE_RESET_PREFIX + userPwdResetReq.getEmail();
        stringRedisTemplate.delete(codeKey);
    }

    @Override
    public UserResp getCurrentUserInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userMapper.getUserById(userId);
        UserResp userResp = new UserResp();
        BeanUtil.copyProperties(user,userResp);
        return userResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(MultipartFile file) {
        String fileUrl = ossUtil.uploadFile(file,"avatar");
        Long userId = SecurityUtils.getCurrentUserId();

        User user = new User();
        user.setAvatar(fileUrl);
        user.setId(userId);

        int updateRows = userMapper.updateUser(user);
        if (updateRows != 1) {
            throw new RuntimeException(UPDATE_FILED);
        }
        return fileUrl;
    }

    /**
     * 验证密码格式
     * @param newPassword           新密码
     * @param confirmNewPassword    确认新密码
     */
    private void validatePassword(String newPassword, String confirmNewPassword) {
        // 参数校验
        if (newPassword.isEmpty() || confirmNewPassword.isEmpty())
            throw new RuntimeException(PASSWORD_IS_EMPTY);

        // 比较新密码长度
        if (newPassword.length() < 6)
            throw new RuntimeException(PASSWORD_NOT_LONG_ENOUGH);

        // 比较两次输入的新密码是否一致
        if (!newPassword.equals(confirmNewPassword))
            throw new RuntimeException(PASSWORD_DO_NOT_MATCH);
    }

}
