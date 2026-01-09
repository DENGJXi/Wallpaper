package com.elves.wallpaper.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.elves.wallpaper.config.OssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class OssUtil {

    @Autowired
    private OssProperties ossProperties;

    public String uploadFile(MultipartFile file){
        // 获取属性值
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try{
            String originalFilename = file.getOriginalFilename();
            //  获取文件后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;
            //  上传文件
            ossClient.putObject(bucketName, fileName, file.getInputStream());
            //  返回图片url
            return "https://" +  bucketName + "." + endpoint + "/" + fileName;
        }catch (IOException e){
            throw new RuntimeException("文件流读取失败", e);
        } finally {
            if(ossClient!=null){
                ossClient.shutdown();
            }
        }
    }
    public String uploadFile(MultipartFile file, String folder) { // 增加目录参数
        // 获取属性值
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 【关键改动】：在文件名前拼接目录名
            // 格式如：avatar/458e-xxxx-xxxx.jpg
            String fileName = folder + "/" + UUID.randomUUID() + suffix;

            // 上传文件 (OSS会自动根据 fileName 中的 "/" 创建虚拟目录)
            ossClient.putObject(bucketName, fileName, file.getInputStream());

            // 返回图片url
            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("文件流读取失败", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
