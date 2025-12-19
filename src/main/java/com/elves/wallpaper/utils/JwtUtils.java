package com.elves.wallpaper.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JwtUtils {

    //过期时间
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    /**
     * 生成token
     * @param userId
     * @return
     */
    public static String createToken(String secretKey , Long userId){
        Date expTime =  new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder()
                .claim("userId",userId)
                .setExpiration(expTime)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    /**
     * 解析token并获取载荷信息
     * @param secretKey 密钥（必须与加密时一致）
     * @param token 前端传来的令牌字符串
     * @return Claims 载荷对象（可以从中取userId）
     */
    public static Claims parseToken(String secretKey, String token){
        return Jwts.parser().verifyWith(new SecretKeySpec(secretKey.getBytes(), "HmacSHA256")).build().parseSignedClaims(token).getPayload();

    }
}
