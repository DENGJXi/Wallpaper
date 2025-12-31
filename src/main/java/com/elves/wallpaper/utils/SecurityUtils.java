package com.elves.wallpaper.utils;

import com.elves.wallpaper.model.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    /**
     * 获取当前用户id
     * @return  userId
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 判断用户是否已经登录
        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
            throw new RuntimeException("用户未登录");
        }
        Object principal = authentication.getPrincipal();
        return ((User)principal).getId();
    }

}
