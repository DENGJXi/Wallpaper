package com.elves.wallpaper.service.impl;

import com.elves.wallpaper.common.Result;
import com.elves.wallpaper.dto.UserLoginDto;
import com.elves.wallpaper.dto.UserRegisterDto;
import com.elves.wallpaper.mapper.AuthMapper;
import com.elves.wallpaper.model.User;
import com.elves.wallpaper.service.AuthService;
import com.elves.wallpaper.utils.JwtUtils;
import com.elves.wallpaper.vo.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.elves.wallpaper.constant.MessageConstant.*;


@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${elves.jwt.SIGN_KEY}")
    private String secretKey;

    /**
     * 用户登录功能
     * @param userLoginDto
     * @return  Result
     */
    @Override
    public UserLoginVo login(UserLoginDto userLoginDto) {
        //  1.获取当前用户输入的用户名和密码
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
        if(!StringUtils.hasText(username) || !StringUtils.hasText(password)){
            throw new RuntimeException(USERNAME_OR_PASSWORD_IS_NULL);
        }
        //  根据用户名查询数据库
        User user = authMapper.getByUsername(username);
        //  用户不存在
        if (user == null) {
            throw new RuntimeException(USERNAME_NOT_FOUND);
        }
        //  密码错误
        if(!passwordEncoder.matches(password,user.getPassword())) {
            throw new RuntimeException(PASSWORD_ERROR);
        }
        //  使用自定义工具类为用户创建token
        String token = JwtUtils.createToken(secretKey, user.getId());
        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setToken(token);
        userLoginVo.setUsername(username);
        userLoginVo.setAvatar(user.getAvatar());
        userLoginVo.setNickname(user.getNickname());

        log.info("用户 {} 登录成功",username);
        return userLoginVo;
    }

    /**
     *  用户登出功能
     * @return
     */
    @Override
    public void logout() {
        log.info("用户注销逻辑执行完毕");
    }

    /**
     * 用户注册功能
     * @param userRegisterDto
     */
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        String username = userRegisterDto.getUsername();
        //  验证用户名是否已存在
        if(authMapper.getByUsername(username) != null){
            throw new RuntimeException(USERNAME_ALREADY_EXIST);
        }
        //  验证密码长度是否大于等于六位
        if(userRegisterDto.getPassword().length() < 6){
            throw new RuntimeException(PASSWORD_NOT_LONG_ENOUGH);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setAvatar(user.getAvatar());
        user.setNickname("新用户_" + userRegisterDto.getUsername());   //  设置默认昵称
        user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");

        authMapper.insert(user);
    }
}
