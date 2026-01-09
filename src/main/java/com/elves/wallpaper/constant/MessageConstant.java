package com.elves.wallpaper.constant;

public class MessageConstant {
    public static final String USERNAME_OR_PASSWORD_IS_NULL = "用户名或密码不能为空";
    public static final String USERNAME_NOT_FOUND = "用户名不存在";
    public static final String USERNAME_ALREADY_EXIST = "用户名已存在";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String PASSWORD_IS_EMPTY = "密码不能为空";
    public static final String PASSWORD_NOT_LONG_ENOUGH = "密码长度不足6位";
    public static final String PASSWORD_DO_NOT_MATCH = "两次输入的密码不一致";
    public static final String UPDATE_FILED = "更新用户信息失败";
    public static final String EMAIL_NOT_FOUND = "邮箱不存在";
    public static final String WRONG_EMAIL = "邮箱与用户名不匹配";
    public static final String EMAIL_OR_CODE_IS_NULL = "邮箱或验证码不能为空";
    public static final String CODE_ERROR = "验证码错误";
    public static final String VERIFY_CODE_RESET_PREFIX = "auth:verify_code_reset:";        // 用于存储重置密码的验证码
    public static final String VERIFY_CODE_CHANGE_PREFIX = "auth:verify_code_change:";     // 用于存储修改密码的验证码
    public static final String PWD_RESET_TOKEN_PREFIX = "auth:reset_token_value:";           // 用于存储重置令牌值
    public static final String TOKEN_INVALID_OR_EXPIRED = "重置令牌无效或已过期";
    public static final String EMPTY_FILE = "上传文件不能为空";

}
