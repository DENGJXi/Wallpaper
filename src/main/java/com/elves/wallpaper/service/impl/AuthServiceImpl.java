package com.elves.wallpaper.service.impl;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.elves.wallpaper.constant.MessageConstant.EMAIL_NOT_FOUND;
import static com.elves.wallpaper.constant.MessageConstant.EMAIL_OR_CODE_IS_NULL;
import static com.elves.wallpaper.constant.MessageConstant.PASSWORD_ERROR;
import static com.elves.wallpaper.constant.MessageConstant.PASSWORD_NOT_LONG_ENOUGH;
import static com.elves.wallpaper.constant.MessageConstant.PWD_RESET_TOKEN_PREFIX;
import static com.elves.wallpaper.constant.MessageConstant.USERNAME_ALREADY_EXIST;
import static com.elves.wallpaper.constant.MessageConstant.USERNAME_NOT_FOUND;
import static com.elves.wallpaper.constant.MessageConstant.USERNAME_OR_PASSWORD_IS_NULL;
import static com.elves.wallpaper.constant.MessageConstant.VERIFY_CODE_CHANGE_PREFIX;
import static com.elves.wallpaper.constant.MessageConstant.VERIFY_CODE_RESET_PREFIX;
import com.elves.wallpaper.dto.UserLoginReq;
import com.elves.wallpaper.dto.UserRegisterReq;
import com.elves.wallpaper.mapper.AuthMapper;
import com.elves.wallpaper.mapper.UserMapper;
import com.elves.wallpaper.model.User;
import com.elves.wallpaper.service.AuthService;
import com.elves.wallpaper.utils.JwtUtils;
import com.elves.wallpaper.utils.SecurityUtils;
import com.elves.wallpaper.vo.UserLoginResp;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 用户登录功能
     *
     * @param userLoginReq
     * @return Result
     */
    @Override
    public UserLoginResp login(UserLoginReq userLoginReq) {
        //  1.获取当前用户输入的用户名和密码
        String username = userLoginReq.getUsername();
        String password = userLoginReq.getPassword();
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new RuntimeException(USERNAME_OR_PASSWORD_IS_NULL);
        }
        //  根据用户名查询数据库
        User user = authMapper.getByUsername(username);
        //  用户不存在
        if (user == null) {
            throw new RuntimeException(USERNAME_NOT_FOUND);
        }
        //  密码错误
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException(PASSWORD_ERROR);
        }
        //  使用自定义工具类为用户创建token
        String token = JwtUtils.createToken(user.getId());
        UserLoginResp userLoginResp = new UserLoginResp();
        userLoginResp.setToken(token);
        userLoginResp.setUsername(username);
        userLoginResp.setAvatar(user.getAvatar());
        userLoginResp.setNickname(user.getNickname());

        log.info("用户 {} 登录成功", username);
        return userLoginResp;
    }

    /**
     * 用户登出功能
     *
     * @return
     */
    @Override
    public void logout() {
        log.info("用户注销逻辑执行完毕");
    }

    /**
     * 用户注册功能
     *
     * @param userRegisterReq
     */
    @Override
    public void register(UserRegisterReq userRegisterReq) {
        String username = userRegisterReq.getUsername();
        //  验证用户名是否已存在
        if (authMapper.getByUsername(username) != null) {
            throw new RuntimeException(USERNAME_ALREADY_EXIST);
        }
        //  验证密码长度是否大于等于六位
        if (userRegisterReq.getPassword().length() < 6) {
            throw new RuntimeException(PASSWORD_NOT_LONG_ENOUGH);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(userRegisterReq.getPassword()));
        user.setAvatar(user.getAvatar());
        user.setNickname("新用户_" + userRegisterReq.getUsername());   //  设置默认昵称
        user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");

        authMapper.insert(user);
    }

    /**
     * 发送普通文本邮件
     *
     * @param to      接收人
     * @param subject 主题
     * @param content 内容
     */
    @Async // 异步执行，不阻塞主线程
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail); // 发送者
        message.setTo(to);          // 接收者
        message.setSubject(subject); // 邮件标题
        message.setText(content);    // 邮件内容

        mailSender.send(message);
    }

    @Override
    public void sendVerificationCode() {
        //  验证邮箱是否匹配
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userMapper.getUserById(userId);
        String currentEmail = user.getEmail();
        String code = RandomCode(VERIFY_CODE_CHANGE_PREFIX, currentEmail);
        // 4. 发送邮件
        String content = "您正在尝试修改密码，验证码为：" + code + "，5分钟内有效。";
        sendSimpleMail(currentEmail, "密码重置验证码", content);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        return internalVerify(email, code, VERIFY_CODE_CHANGE_PREFIX);
    }

    @Override
    public boolean sendResetPwdCode(String email) {
        
        User user = authMapper.getByEmail(email);
        if (user == null) {
            log.error(EMAIL_NOT_FOUND);
            return false;
        }
        String code = RandomCode(VERIFY_CODE_RESET_PREFIX, email);
        // 4. 发送邮件
        String content = "您正在尝试修改密码，验证码为：" + code + "，5分钟内有效。";
        sendSimpleMail(email, "密码重置验证码", content);
        return true;
    }

    @Override
    public String issueResetToken(String email) {
        // 通过验证码校验后，签发短期一次性重置令牌（非登录用JWT）
        String resetToken = UUID.randomUUID().toString();

        String key = PWD_RESET_TOKEN_PREFIX + resetToken;
        stringRedisTemplate.opsForValue().set(key, email, 5, TimeUnit.MINUTES);

        return resetToken;
    }

    @Override
    public boolean verifyResetToken(String email, String resetToken) {
        String key = PWD_RESET_TOKEN_PREFIX + resetToken;
        String storedEmail = stringRedisTemplate.opsForValue().get(key);

        if (storedEmail == null || !storedEmail.equals(email)) {
            log.error("重置令牌验证失败: email={}", email);
            return false;
        }
        return true;
    }

    @Override
    public String verifyForgetPwdCode(String email, String code) {
        //  如果验证码正确，则签发短期令牌
        log.info("开始验证忘记密码验证码 - email: {}, code: {}", email, code);
        if(internalVerify(email, code, VERIFY_CODE_RESET_PREFIX)){
            String resetToken = issueResetToken(email);
            log.info("验证码验证成功，已签发重置令牌: {}", resetToken);
            return resetToken;
        }
        log.error("验证码验证失败，返回 null");
        return null;
    }

    private boolean internalVerify(String email, String code, String prefix) {

        if (!StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            throw new RuntimeException(EMAIL_OR_CODE_IS_NULL);
        }
        String key = prefix + email;
        String redisCode = (String) stringRedisTemplate.opsForValue().get(key);

        // 防御性编程：Redis 中的数据可能包含 \x00 填充字符，使用正则表达式提取数字部分
        if (redisCode != null) {
            redisCode = redisCode.replaceAll("[^0-9]", "");
        }

        String cleanCode = code.trim().replaceAll("[^0-9]", "");
        log.info("验证码验证 - email: {}, 前端code: '{}', Redis中code: '{}', key: {}", email, cleanCode, redisCode, key);

        if (redisCode == null || !redisCode.equals(cleanCode)) {
            log.error("验证码验证失败: email={}, type={}", email, prefix);
            return false;
        }
        return true;
    }

    private String RandomCode(String prefix, String email) {
        //  生成六位随机验证码
        Random random = new Random();
        String key = prefix + email;
        String code = String.valueOf(100000 + random.nextInt(900000));
        // 存入Redis，设置五分钟过期时间
        stringRedisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        return code;
    }
}
