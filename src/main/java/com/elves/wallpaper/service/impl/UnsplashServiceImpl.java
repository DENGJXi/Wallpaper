package com.elves.wallpaper.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.elves.wallpaper.dto.UnsplashImageDTO;
import com.elves.wallpaper.mapper.WallpaperMapper;
import com.elves.wallpaper.model.Wallpaper;
import com.elves.wallpaper.service.UnsplashService;

import lombok.extern.slf4j.Slf4j;

/**
 * Unsplash数据导入服务实现
 */
@Service
@Slf4j
public class UnsplashServiceImpl implements UnsplashService {
    
    @Autowired
    private WallpaperMapper wallpaperMapper;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${unsplash.access-key}")
    private String accessKey;
    
    private static final String UNSPLASH_API_BASE = "https://api.unsplash.com";

    @Override
    public int importWallpapersFromUnsplash(String keyword, int page, int perPage) {
        try {
            // 构建API URL
            String url = String.format(
                    "%s/search/photos?query=%s&page=%d&per_page=%d&client_id=%s",
                    UNSPLASH_API_BASE, keyword, page, perPage, accessKey
            );
            
            log.info("开始从Unsplash导入数据: {}", url);
            
            // 调用Unsplash API
            UnsplashSearchResponse response = restTemplate.getForObject(url, UnsplashSearchResponse.class);
            
            if (response == null || response.getResults() == null || response.getResults().length == 0) {
                log.warn("Unsplash API返回为空");
                return 0;
            }
            
            log.info("Unsplash API返回了 {} 张图片", response.getResults().length);
            
            int successCount = 0;
            int duplicateCount = 0;
            for (UnsplashImageDTO imageDto : response.getResults()) {
                try {
                    Wallpaper wallpaper = convertDtoToWallpaper(imageDto, keyword);
                    wallpaperMapper.insertWallpaper(wallpaper);
                    successCount++;
                    log.info("成功导入壁纸: {}", wallpaper.getTitle());
                } catch (org.springframework.dao.DuplicateKeyException e) {
                    // 数据库唯一约束冲突（重复数据）
                    log.debug("壁纸已存在，跳过: {}", imageDto.getId());
                    duplicateCount++;
                } catch (Exception e) {
                    log.error("导入单个壁纸失败 [ID: {}]: {}", imageDto.getId(), e.getMessage(), e);
                }
            }
            
            log.info("本次导入完成，成功导入 {} 张壁纸，跳过重复 {} 张", successCount, duplicateCount);
            return successCount;
            
        } catch (Exception e) {
            log.error("从Unsplash导入壁纸失败", e);
            return 0;
        }
    }

    @Override
    public int importRandomWallpapers(int count) {
        try {
            // 构建API URL - 获取随机图片
            String url = String.format(
                    "%s/photos/random?count=%d&client_id=%s",
                    UNSPLASH_API_BASE, count, accessKey
            );
            
            log.info("开始导入随机壁纸: {}", url);
            
            // 调用Unsplash API (返回数组)
            UnsplashImageDTO[] imageDtos = restTemplate.getForObject(url, UnsplashImageDTO[].class);
            
            if (imageDtos == null || imageDtos.length == 0) {
                log.warn("Unsplash API返回为空");
                return 0;
            }
            
            int successCount = 0;
            for (UnsplashImageDTO imageDto : imageDtos) {
                try {
                    Wallpaper wallpaper = convertDtoToWallpaper(imageDto, "随机");
                    wallpaperMapper.insertWallpaper(wallpaper);
                    successCount++;
                    log.info("成功导入随机壁纸: {}", wallpaper.getTitle());
                } catch (Exception e) {
                    log.error("导入单个壁纸失败: {}", imageDto.getId(), e);
                }
            }
            
            log.info("随机导入完成，成功导入 {} 张壁纸", successCount);
            return successCount;
            
        } catch (Exception e) {
            log.error("导入随机壁纸失败", e);
            return 0;
        }
    }

    /**
     * 将Unsplash DTO转换为Wallpaper实体
     */
    private Wallpaper convertDtoToWallpaper(UnsplashImageDTO dto, String category) {
        Wallpaper wallpaper = new Wallpaper();
        
        // 基本信息
        wallpaper.setUnsplashId(dto.getId());
        wallpaper.setTitle(dto.getAlt_description() != null ? dto.getAlt_description() : dto.getDescription());
        wallpaper.setDescription(dto.getDescription() != null ? dto.getDescription() : "来自Unsplash的精美壁纸");
        wallpaper.setCategory(category);
        
        // 图片URL
        if (dto.getUrls() != null) {
            wallpaper.setThumbnailUrl(dto.getUrls().getSmall() != null ? dto.getUrls().getSmall() : dto.getUrls().getThumb());
            wallpaper.setFileUrl(dto.getUrls().getFull() != null ? dto.getUrls().getFull() : dto.getUrls().getRegular());
        }
        
        // 摄影师信息
        if (dto.getUser() != null) {
            wallpaper.setPhotographer(dto.getUser().getName());
            if (dto.getUser().getLinks() != null) {
                wallpaper.setPhotographerUrl(dto.getUser().getLinks().getHtml());
            }
        }
        
        // Unsplash链接
        if (dto.getLinks() != null) {
            wallpaper.setSourceUrl(dto.getLinks().getHtml());
            wallpaper.setDownloadUrl(dto.getLinks().getDownload());
        }
        
        // 图片尺寸和颜色
        wallpaper.setWidth(dto.getWidth());
        wallpaper.setHeight(dto.getHeight());
        wallpaper.setColor(dto.getColor());
        
        // 其他信息
        wallpaper.setHits(0);
        wallpaper.setCreateTime(LocalDateTime.now());
        
        return wallpaper;
    }
    
    /**
     * Unsplash搜索API响应类
     */
    public static class UnsplashSearchResponse {
        private UnsplashImageDTO[] results;
        private Integer total;
        private Integer total_pages;
        
        public UnsplashImageDTO[] getResults() {
            return results;
        }
        
        public void setResults(UnsplashImageDTO[] results) {
            this.results = results;
        }
        
        public Integer getTotal() {
            return total;
        }
        
        public void setTotal(Integer total) {
            this.total = total;
        }
        
        public Integer getTotal_pages() {
            return total_pages;
        }
        
        public void setTotal_pages(Integer total_pages) {
            this.total_pages = total_pages;
        }
    }
}
