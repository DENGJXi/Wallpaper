package com.elves.wallpaper.service;

import com.elves.wallpaper.dto.UserLoginReq;
import com.elves.wallpaper.dto.UserRegisterReq;
import com.elves.wallpaper.vo.UserLoginResp;

public interface AuthService {
    /**
     * 用户登录
     * @param userLoginReq  登录请求体
     * @return  返回前端所需数据
     */
    UserLoginResp login(UserLoginReq userLoginReq);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 用户注册
     * @param userRegisterReq 注册请求体
     */
    void register(UserRegisterReq userRegisterReq);
    /**
     * 发送普通文本邮件
     * @param to 接收人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);
    /**
     * 发送验证码邮件(固定模板)
     */
    void sendVerificationCode();
    /**
     * 验证验证码是否正确
     * @param email  接收人邮箱
     * @param code  验证码
     * @return  boolean值
     */
    boolean verifyCode(String email, String code);
    /**
     * 发送重置密码验证码邮件
     * @param email 接收人邮箱
     * @return  boolean值
     */
    boolean sendResetPwdCode(String email);
    /**
     * 通过验证码校验后签发一次性重置令牌
     * @param email 接收人邮箱
     * @return  重置令牌
     */
    String issueResetToken(String email); // 通过验证码校验后签发一次性重置令牌
    /**
     * 验证重置令牌是否有效
     * @param email 接收人邮箱
     * @param resetToken 重置令牌
     * @return  boolean值
     */
    boolean verifyResetToken(String email, String resetToken);
    /**
     * 验证忘记密码验证码是否正确
     * @param email 接收人邮箱
     * @param code  验证码
     * @return  重置令牌，验证失败返回 null
     */
    String verifyForgetPwdCode(String email, String code);



}
