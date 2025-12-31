# Unsplash 数据导入功能实现指南

## 一、已完成的修改

### 1. 实体类更新 - Wallpaper.java
添加了以下新字段以存储Unsplash数据：
```java
private String unsplashId;          // Unsplash API中的图片ID
private String photographer;        // 摄影师名称
private String photographerUrl;     // 摄影师个人页面链接
private String downloadUrl;         // Unsplash提供的下载链接
private String sourceUrl;           // Unsplash原始链接
private Integer width;              // 图片宽度(像素)
private Integer height;             // 图片高度(像素)
private String color;               // 平均色值
```

### 2. DTO更新 - UnsplashImageDTO.java
更新了UnsplashImageDTO类，添加了对Unsplash API响应数据的完整支持：
- User信息（摄影师）
- Links信息（下载链接、Unsplash页面）
- 图片尺寸和色值
- 多种URL选项（原图、高清、常规、小图、缩略图）

### 3. 服务层

#### 创建 UnsplashService 接口
两个主要方法：
1. `importWallpapersFromUnsplash(keyword, page, perPage)` - 根据关键词搜索并导入壁纸
2. `importRandomWallpapers(count)` - 导入随机壁纸

#### 创建 UnsplashServiceImpl 实现类
完整实现了数据导入逻辑：
- 调用Unsplash API获取数据
- 自动转换Unsplash DTO到Wallpaper实体
- 异常处理和日志记录
- 支持搜索和随机导入两种模式

### 4. 数据库层

#### WallpaperMapper.java
添加了：
```java
void insertWallpaper(Wallpaper wallpaper);
```

#### WallpaperMapper.xml
- 更新了resultMap以包含新字段的映射
- 添加了insertWallpaper的INSERT语句

### 5. 控制器 - WallpaperController.java
添加了两个新的HTTP接口：

1. **按关键词导入壁纸**
   ```
   POST /api/wallpapers/import/search?keyword=xxx&page=1&perPage=30
   ```
   
2. **导入随机壁纸**
   ```
   POST /api/wallpapers/import/random?count=10
   ```

### 6. 配置更新 - WebConfig.java
添加了RestTemplate的Bean配置，用于调用Unsplash API

---

## 二、数据库修改

需要执行以下SQL语句来更新数据库表结构（见schema_update.sql）：

```sql
ALTER TABLE wallpaper ADD COLUMN unsplash_id VARCHAR(255) UNIQUE;
ALTER TABLE wallpaper ADD COLUMN photographer VARCHAR(255);
ALTER TABLE wallpaper ADD COLUMN photographer_url VARCHAR(255);
ALTER TABLE wallpaper ADD COLUMN download_url VARCHAR(255);
ALTER TABLE wallpaper ADD COLUMN source_url VARCHAR(255);
ALTER TABLE wallpaper ADD COLUMN width INT;
ALTER TABLE wallpaper ADD COLUMN height INT;
ALTER TABLE wallpaper ADD COLUMN color VARCHAR(20);
```

---

## 三、使用方法

### 1. 按关键词导入壁纸
```bash
curl -X POST "http://localhost:8080/api/wallpapers/import/search?keyword=landscape&page=1&perPage=30"
```

### 2. 导入随机壁纸
```bash
curl -X POST "http://localhost:8080/api/wallpapers/import/random?count=20"
```

### 3. 在代码中调用（Java）
```java
@Autowired
private UnsplashService unsplashService;

// 导入风景类图片
int count1 = unsplashService.importWallpapersFromUnsplash("landscape", 1, 30);

// 导入10张随机壁纸
int count2 = unsplashService.importRandomWallpapers(10);
```

---

## 四、工作流程

1. **API调用**: 通过HTTP接口或直接调用Service方法
2. **Unsplash API请求**: 使用配置的Access Key调用Unsplash API
3. **数据转换**: 将Unsplash返回的JSON转换为Wallpaper实体
4. **数据存储**: 使用MyBatis将数据插入数据库
5. **错误处理**: 单个图片导入失败不会影响整个批次的导入

---

## 五、配置信息

Unsplash API凭据已在application.yml中配置：
```yaml
unsplash:
  access-key: '_z_Lx0kaVv0_Qr454c98kKLTJqrdP-D8UuW3-pe_s8A'
  secret-key: 'cfc1ZcsRGMJE9iUP2N-CAz6MWy05yCmKlp74W5u1ppg'
```

---

## 六、API文档

### Unsplash API Endpoints Used:
- **Search**: `https://api.unsplash.com/search/photos`
- **Random**: `https://api.unsplash.com/photos/random`

### 数据字段映射关系

| Unsplash字段 | Wallpaper字段 | 说明 |
|------------|------------|------|
| id | unsplashId | 图片唯一标识 |
| alt_description | title | 图片标题（优先使用） |
| description | description | 图片描述 |
| urls.small/thumb | thumbnailUrl | 缩略图 |
| urls.full/regular | fileUrl | 下载地址 |
| user.name | photographer | 摄影师名称 |
| user.links.html | photographerUrl | 摄影师页面 |
| links.download | downloadUrl | Unsplash下载链接 |
| links.html | sourceUrl | Unsplash源页面 |
| width | width | 原始宽度 |
| height | height | 原始高度 |
| color | color | 平均色值 |

---

## 七、注意事项

1. ⚠️ **需要先执行数据库SQL脚本** 来添加新的表列
2. ⚠️ **Unsplash API有速率限制**（免费用户50请求/小时）
3. ✅ **单个导入失败不会中断其他记录** 的插入
4. ✅ **自动去重**：unsplash_id有唯一约束，重复导入的图片会被忽略
5. ✅ **完整的错误日志**：所有操作都有详细的日志记录

---

## 八、测试建议

1. 执行SQL脚本更新数据库
2. 启动Spring Boot应用
3. 调用导入接口测试：
   ```bash
   POST http://localhost:8080/api/wallpapers/import/random?count=5
   ```
4. 查看数据库是否成功插入数据
5. 查看应用日志输出的导入过程

---

## 九、后续优化建议

1. 添加导入任务的异步处理（使用@Async）
2. 实现导入进度跟踪（可以存储到Redis）
3. 添加导入历史记录表
4. 考虑实现智能去重（基于摄影师+图片相似度）
5. 添加导入任务的定时调度

