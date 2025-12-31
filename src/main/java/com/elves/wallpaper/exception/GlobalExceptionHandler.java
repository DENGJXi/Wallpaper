package com.elves.wallpaper.exception;

import com.elves.wallpaper.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常 RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error("检测到业务异常: {}", e.getMessage());
        // 返回 Result.error，这样前端就能收到 code: 500 和具体的错误消息了
        return Result.error(e.getMessage());
    }

    /**
     * 捕获其他未知异常 (防止程序直接崩溃)
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统未知错误: ", e);
        return Result.error("系统繁忙，请稍后再试");
    }
}
