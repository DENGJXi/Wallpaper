package com.elves.wallpaper.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component // 必须加这个，Spring 才会扫描并注入
public class JwtUtils {

    private static String SIGN_KEY;

    @Value("${elves.jwt.SIGN_KEY}")
    public void setSignKey(String signKey) {
        SIGN_KEY = signKey;
    }

    // 过期时间：7天
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    /**
     * 生成 Token
     */
    public static String createToken(Long userId) {
        Date expTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        SecretKey key = Keys.hmacShaKeyFor(SIGN_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claim("userId", userId)
                .setExpiration(expTime)
                .signWith(key) // 现代版本的 jjwt 推荐直接传 Key 对象
                .compact();
    }

    /**
     * 解析 Token
     */
    public static Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SIGN_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
