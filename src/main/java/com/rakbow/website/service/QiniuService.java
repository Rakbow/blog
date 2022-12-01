package com.rakbow.website.service;

import com.alibaba.fastjson2.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.rakbow.website.util.FileUtil;
import com.rakbow.website.util.common.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-30 22:13
 * @Description:
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    @Value("${website.qiniu.access-key}")
    private String ACCESS_KEY;
    @Value("${website.qiniu.secret-key}")
    private String SECRET_KEY;
    @Value("${website.qiniu.image.domain}")
    private String IMAGE_DOMAIN;
    @Value("${website.qiniu.bucketName}")
    private String BUCKET_NAME;

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        // 密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken(BUCKET_NAME);
        return token;
    }

    public String saveImageTest(MultipartFile file) throws IOException {
        try {
            // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
            // 华东-浙江2 CnEast2
            Configuration cfg = new Configuration(Region.regionCnEast2());
            // 创建上传对象
            UploadManager uploadManager = new UploadManager(cfg);

            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }

            // 获取图片文件后缀名
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            // 判断是否是合法的文件后缀
            if (!FileUtil.isFileAllowed(fileExt)) {
                return null;
            }

            //通过随机UUID生成唯一文件名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            // 调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回这张存储照片的地址
                return IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return "七牛异常: " + res.bodyString();
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常:" + e.getMessage());
            return "七牛异常:" + e.getMessage();
        }
    }

    public ActionResult saveImage(MultipartFile file, String filePath) throws IOException {
        ActionResult ar = new ActionResult();
        try {
            // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
            // 华东-浙江2 CnEast2
            Configuration cfg = new Configuration(Region.regionCnEast2());
            // 创建上传对象
            UploadManager uploadManager = new UploadManager(cfg);

            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }

            // 获取图片文件后缀名
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            // 判断是否是合法的文件后缀
            if (!FileUtil.isFileAllowed(fileExt)) {
                ar.setErrorMessage("图片格式错误");
                return ar;
            }

            //通过随机UUID生成唯一文件名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;

            filePath = filePath + fileName;

            // 调用put方法上传
            Response res = uploadManager.put(filePath.getBytes(), filePath, getUpToken());
            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回这张存储照片的地址
                ar.data = IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + res.bodyString());
                ar.setErrorMessage("七牛异常: " + res.bodyString());
            }
            return ar;
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常:" + e.getMessage());
            ar.setErrorMessage("七牛异常:" + e.getMessage());
            return ar;
        }
    }

    public ActionResult deleteFileFromQiniu(String fileName){

        ActionResult ar = new ActionResult();
        try{
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Region.regionCnEast2());
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth, cfg);
            bucketManager.delete(BUCKET_NAME, fileName);
            ar.message = "删除成功";
        }catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            ar.setErrorMessage(ex.response.toString());
        }
        return ar;
    }

}

