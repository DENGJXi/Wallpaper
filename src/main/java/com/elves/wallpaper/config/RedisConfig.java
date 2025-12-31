//package com.elves.wallpaper.config;
//
//
//import java.nio.charset.Charset;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//
//        // 使用 StringRedisSerializer，明确指定 UTF-8 编码
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(Charset.forName("UTF-8"));
//
//        // Key 序列化
//        template.setKeySerializer(stringRedisSerializer);
//        template.setHashKeySerializer(stringRedisSerializer);
//
//        // Value 序列化
//        template.setValueSerializer(stringRedisSerializer);
//        template.setHashValueSerializer(stringRedisSerializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }
//
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
//        StringRedisTemplate template = new StringRedisTemplate(factory);
//        // 使用基础的 String 序列化器
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(Charset.forName("UTF-8"));
//        template.setKeySerializer(stringRedisSerializer);
//        template.setValueSerializer(stringRedisSerializer);
//        template.setHashKeySerializer(stringRedisSerializer);
//        template.setHashValueSerializer(stringRedisSerializer);
//        return template;
//    }
//}
