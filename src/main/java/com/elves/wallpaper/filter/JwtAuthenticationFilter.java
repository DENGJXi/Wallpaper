package com.elves.wallpaper.filter;

import com.elves.wallpaper.model.User;
import com.elves.wallpaper.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 请求概览（仅在 debug 级别输出）
        if (log.isDebugEnabled()) {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            log.debug("Incoming request: {} {}", method, uri);

            // 打印全部请求头，便于诊断 Authorization 是否被代理/浏览器移除或被改写
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    log.debug("Header: {} = {}", name, request.getHeader(name));
                }
            }
        }

        // 1. 获取请求头中的 Token
        String token = request.getHeader("Authorization");

        // 调试：记录 Authorization 头（仅在 debug 级别）
        if (log.isDebugEnabled()) {
            log.debug("Authorization header: {}", token);
        }

        // 2. 逻辑判断：处理不同格式的 token（如 "Bearer xxx"、"bearer xxx" 或 直接为 token）
        if (StringUtils.hasText(token)) {
            String jwt;
            String trimmed = token.trim();
            if (trimmed.length() >= 7 && trimmed.substring(0, 7).equalsIgnoreCase("Bearer ")) {
                jwt = trimmed.substring(7).trim();
            } else {
                // 如果没有 Bearer 前缀，也尝试把整个头当作 token 使用（以兼容某些客户端/代理）
                jwt = trimmed;
                if (log.isDebugEnabled()) {
                    log.debug("Authorization header does not start with 'Bearer '. Using header value as raw token.");
                }
            }

            try{
                Claims claims = JwtUtils.parseToken(jwt);
                User user = new User();
                user.setId(Long.valueOf(claims.get("userId").toString()));
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch (Exception e){
                log.error("jwt身份解析失败:{}",e.getMessage());
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("No Authorization header present on request");
            }
        }
        // 4. 放行给下一个过滤器
        filterChain.doFilter(request, response);
    }
}
