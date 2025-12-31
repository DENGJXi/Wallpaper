package com.elves.wallpaper.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.elves.wallpaper.service.UnsplashService;

import lombok.extern.slf4j.Slf4j;

/**
 * Unsplash定时导入任务
 */
@Component
@Slf4j
public class UnsplashScheduledTask {
    
    @Autowired
    private UnsplashService unsplashService;
    
    /**
     * 每天上午11点执行
     * cron表达式: "0 0 11 * * ?" 表示每天11:00:00执行
     * 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void importRandomWallpapersDaily() {
        log.info("========== 定时任务开始：导入随机壁纸 ==========");
        try {
            int count = unsplashService.importRandomWallpapers(15);
            log.info("每日定时导入完成，本次导入 {} 张壁纸", count);
        } catch (Exception e) {
            log.error("每日定时导入失败", e);
        }
        log.info("========== 定时任务结束 ==========");
    }
    
    /**
     * 每天下午2点执行（搜索热门关键词）
     * cron表达式: "0 0 14 * * ?" 表示每天14:00:00执行
     */
    @Scheduled(cron = "0 0 14 * * ?")
    public void importPopularWallpapersDaily() {
        log.info("========== 定时任务开始：导入热门壁纸 ==========");
        try {
            // 导入热门关键词的壁纸
            String[] keywords = {"landscape", "nature", "city", "abstract"};
            int totalCount = 0;
            
            for (String keyword : keywords) {
                try {
                    int count = unsplashService.importWallpapersFromUnsplash(keyword, 1, 5);
                    totalCount += count;
                    log.info("导入关键词 '{}' 完成，本次导入 {} 张", keyword, count);
                    
                    // 避免频繁请求API，延迟1秒
                    Thread.sleep(1000);
                } catch (Exception e) {
                    log.error("导入关键词 '{}' 失败", keyword, e);
                }
            }
            
            log.info("热门壁纸定时导入完成，总计导入 {} 张壁纸", totalCount);
        } catch (Exception e) {
            log.error("热门壁纸定时导入失败", e);
        }
        log.info("========== 定时任务结束 ==========");
    }
}
