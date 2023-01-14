package com.rakbow.website.util.image;

import com.alibaba.fastjson2.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import com.rakbow.website.util.common.FileUtil;
import com.rakbow.website.data.ActionResult;
import com.rakbow.website.data.ApiInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-01 1:44
 * @Description: 通过七牛云api实现图片的增删改查
 */
@Component
public class QiniuBaseUtils {

    @Value("${website.qiniu.access-key}")
    private String ACCESS_KEY;
    @Value("${website.qiniu.secret-key}")
    private String SECRET_KEY;
    @Value("${website.qiniu.image.domain}")
    private String IMAGE_DOMAIN;
    @Value("${website.qiniu.bucketName}")
    private String BUCKET_NAME;

    /**
     * 获取上传文件的token值
     */
    public String getUpToken() {
        // 密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth.uploadToken(BUCKET_NAME);
    }

    /**
     * 统一上传图片方法，单张
     *
     * @param file,filePath file图片文件，filePath 图片路径
     * @return ActionResult
     */
    public ActionResult uploadImageToQiniu(MultipartFile file, String filePath) throws IOException {
        ActionResult ar = new ActionResult();
        try {
            // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
            // 华东-浙江2 CnEast2
            Configuration cfg = new Configuration(Region.regionCnEast2());
            // 创建上传对象
            UploadManager uploadManager = new UploadManager(cfg);

            // 检测图片是否为空
            if (file.isEmpty()) {
                ar.setErrorMessage(ApiInfo.INPUT_IMAGE_EMPTY);
                return ar;
            }

            // 检测图片文件格式是否合法
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                ar.setErrorMessage(ApiInfo.IMAGE_FORMAT_EXCEPTION);
                return ar;
            }

            // 获取图片文件后缀名
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            // 检测图片格式是否支持
            if (!FileUtil.isFileAllowed(fileExt)) {
                ar.setErrorMessage(ApiInfo.IMAGE_FORMAT_EXCEPTION);
                return ar;
            }

            // 通过随机UUID生成唯一文件名 长度：16
            String fileName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16) + "." + fileExt;

            // 生成完整文件名，例：album/11/xxx.jpg
            String fullFileName = filePath + fileName;

            // 调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fullFileName, getUpToken());

            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回这张存储照片的地址
                ar.data = IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                ar.setErrorMessage(String.format(ApiInfo.QINIU_EXCEPTION, res.bodyString()));
            }
            return ar;
        } catch (QiniuException ex) {
            // 请求失败时打印的异常的信息
            ar.setErrorMessage(String.format(ApiInfo.QINIU_EXCEPTION, ex.getMessage()));
            return ar;
        }
    }

    /**
     * 统一上传图片方法，多张，同一实体类型
     *
     * @param files,filePath file图片文件数组，filePath 图片路径
     * @return ActionResult
     */
    public ActionResult uploadImagesToQiniu(MultipartFile[] files, String filePath) throws IOException {
        ActionResult ar = new ActionResult();
        try {
            // 上传后存放文件全路径名的列表
            List<String> fullFileNames = new ArrayList<>();

            // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
            // 华东-浙江2 CnEast2
            Configuration cfg = new Configuration(Region.regionCnEast2());
            // 创建上传对象
            UploadManager uploadManager = new UploadManager(cfg);


            for (MultipartFile file : files) {

                // 检测图片文件格式是否合法
                int dotPos = file.getOriginalFilename().lastIndexOf(".");
                if (dotPos < 0) {
                    ar.setErrorMessage(ApiInfo.IMAGE_FORMAT_EXCEPTION);
                    return ar;
                }

                // 获取图片文件后缀名
                String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
                // 检测图片格式是否支持
                if (!FileUtil.isFileAllowed(fileExt)) {
                    ar.setErrorMessage(ApiInfo.IMAGE_FORMAT_EXCEPTION);
                    return ar;
                }

                // 通过随机UUID生成唯一文件名 长度：16
                String fileName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16) + "." + fileExt;

                // 生成完整文件名，例：album/11/xxx.jpg
                String fullFileName = filePath + fileName;

                // 调用put方法上传
                Response res = uploadManager.put(file.getBytes(), fullFileName, getUpToken());

                // 打印返回的信息
                if (res.isOK() && res.isJson()) {
                    // 返回这张存储照片的地址并存入fullFileNames
                    fullFileNames.add(IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key"));
                } else {
                    ar.setErrorMessage(String.format(ApiInfo.QINIU_EXCEPTION, res.bodyString()));
                }
            }
            ar.data = fullFileNames;
            return ar;
        } catch (QiniuException ex) {
            // 请求失败时打印的异常的信息
            ar.setErrorMessage(String.format(ApiInfo.QINIU_EXCEPTION, ex.getMessage()));
            return ar;
        }
    }

    /**
     * 统一删除图片方法，单张
     *
     * @param fullFileName 需要删除图片的全路径文件名
     * @return ActionResult
     */
    public ActionResult deleteImageFromQiniu(String fullFileName){

        ActionResult ar = new ActionResult();
        try{
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Region.regionCnEast2());
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth, cfg);

            //去除前缀，获得图片的key
            String key = fullFileName.replace(IMAGE_DOMAIN, "");

            bucketManager.delete(BUCKET_NAME, key);
        }catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            ar.setErrorMessage(String.format(ApiInfo.QINIU_EXCEPTION, ex.response.toString()));
        }
        return ar;
    }

    /**
     * 统一删除图片方法，多张，同一实体类型
     *
     * @param fullFileNames 需要删除图片的全路径文件名数组
     * @return ActionResult
     */
    public ActionResult deleteImagesFromQiniu(String[] fullFileNames){

        ActionResult ar = new ActionResult();
        try{
            // 删除结果map，key为文件名，value为删除状态，true为删除成功，false为删除失败
            List<String> deleteResults = new ArrayList<>();

            //对删除url数组进行处理，去除前缀获得key
            String[] keyList = new String[fullFileNames.length];
            for (int i = 0; i < fullFileNames.length; i++) {
                keyList[i] = fullFileNames[i].replace(IMAGE_DOMAIN, "");
            }

            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Region.regionCnEast2());
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth, cfg);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();

            batchOperations.addDeleteOp(BUCKET_NAME, keyList);

            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < fullFileNames.length; i++) {
                BatchStatus status = batchStatusList[i];
                if (status.code == 200) {
                    deleteResults.add(fullFileNames[i]);
                }
            }
            ar.data = deleteResults;
        }catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            ar.setErrorMessage(String.format(ApiInfo.QINIU_EXCEPTION, ex.response.toString()));
        }
        return ar;
    }

}
