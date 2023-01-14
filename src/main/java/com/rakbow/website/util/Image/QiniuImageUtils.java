package com.rakbow.website.util.image;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ActionResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.util.common.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-01 21:25
 * @Description:
 */
@Component
public class QiniuImageUtils {

    private final QiniuBaseUtils qiniuBaseUtils;

    public QiniuImageUtils(QiniuBaseUtils qiniuBaseUtils) {
        this.qiniuBaseUtils = qiniuBaseUtils;
    }

    /**
     * 通用新增图片
     *
     * @param entityId                 实体id
     * @param entityType               实体类型
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @return finalImageJson 最终保存到数据库的json信息
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public JSONArray commonAddImages(int entityId, EntityType entityType, MultipartFile[] images,
                                     JSONArray originalImagesJson, JSONArray imageInfos) throws IOException {
        //最终保存到数据库的json信息
        JSONArray finalImageJson = new JSONArray();

        //新增图片信息json
        JSONArray addImageJson = new JSONArray();

        //创建存储链接前缀
        String filePath = entityType.getNameEn().toLowerCase() + "/" + entityId + "/";

        for (int i = 0; i < images.length; i++) {
            //上传图片
            ActionResult ar = qiniuBaseUtils.uploadImageToQiniu(images[i], filePath);
            if (ar.state) {
                JSONObject jo = new JSONObject();
                jo.put("url", ar.data.toString());
                jo.put("nameEn", imageInfos.getJSONObject(i).getString("nameEn"));
                jo.put("nameZh", imageInfos.getJSONObject(i).getString("nameZh"));
                jo.put("type", imageInfos.getJSONObject(i).getString("type"));
                jo.put("uploadTime", CommonUtils.getCurrentTime());
                if (imageInfos.getJSONObject(i).getString("description") == null) {
                    jo.put("description", "");
                } else {
                    jo.put("description", imageInfos.getJSONObject(i).getString("description"));
                }
                addImageJson.add(jo);
            }
        }

        //汇总
        finalImageJson.addAll(originalImagesJson);
        finalImageJson.addAll(addImageJson);

        return finalImageJson;
    }

    /**
     * 通用删除图片
     *
     * @param entityId     实体id
     * @param images       原始图片json数组
     * @param deleteImages 需要删除的图片jsonArray
     * @return images 最终保存到数据库的json信息
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public JSONArray commonDeleteImages(int entityId, JSONArray images, JSONArray deleteImages) throws Exception {

        //从七牛云上删除
        //删除结果
        List<String> deleteResult = new ArrayList<>();
        //若删除的图片只有一张，调用单张删除方法
        if (deleteImages.size() == 1) {
            ActionResult ar = qiniuBaseUtils.deleteImageFromQiniu
                    (deleteImages.getJSONObject(0).getString("url"));
            if (!ar.state) {
                throw new Exception(ar.message);
            }
            deleteResult.add(deleteImages.getJSONObject(0).getString("url"));
        } else {
            String[] fullImageUrlList = new String[deleteImages.size()];
            for (int i = 0; i < deleteImages.size(); i++) {
                fullImageUrlList[i] = deleteImages.getJSONObject(i).getString("url");
            }
            ActionResult ar = qiniuBaseUtils.deleteImagesFromQiniu(fullImageUrlList);
            deleteResult = (List<String>) ar.data;
        }

        //根据删除结果循环删除图片信息json数组
        // 迭代器

        for (String s : deleteResult) {
            Iterator<Object> iterator = images.iterator();
            while (iterator.hasNext()) {
                JSONObject itJson = (JSONObject) iterator.next();
                if (StringUtils.equals(itJson.getString("url"), s)) {
                    // 删除数组元素
                    iterator.remove();
                }
            }
        }

        return images;
    }

    /**
     * 通用删除所有图片
     *
     * @param images 删除图片合集
     * @param entityType 实体类型
     * @author rakbow
     */
    public void commonDeleteAllImages(EntityType entityType, JSONArray images) {

        String[] deleteImageKeyList = new String[images.size()];
        //图片文件名
        String deleteImageUrl;
        for (int i = 0; i < images.size(); i++) {
            JSONObject image = images.getJSONObject(i);
            deleteImageUrl = image.getString("url");
            //删除七牛服务器上对应图片文件
            deleteImageKeyList[i] = deleteImageUrl;
        }
        qiniuBaseUtils.deleteImagesFromQiniu(deleteImageKeyList);
    }

    /**
     * 获取等比固定高宽的缩略图URL
     *
     * @param imageUrl,size 原始图url，缩略图宽高
     * @return thumbImageUrl
     */
    public static String getThumbUrl(String imageUrl, int size) {
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + size + "x" + size;
    }

    public static String getThumbUrlWidth(String imageUrl, int size) {
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + "x" + size;
    }

    public static String getThumbBlackBackgroundUrl(String imageUrl, int size) {
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + size + "x" + size
                + "/extent/" + size + "x" + size + "/background/YmxhY2s=";
    }

    /**
     * 通过外链获取图片key
     *
     * @param fullImageUrl 原始图url
     * @return thumbImageUrl
     */
    public static String getImageKeyByFullUrl(String fullImageUrl) {
        String IMAGE_DOMAIN = "https://img.rakbow.com/";
        return fullImageUrl.replace(IMAGE_DOMAIN, "");
    }

}
