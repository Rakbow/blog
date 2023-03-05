package com.rakbow.website.util.file;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ActionResult;
import com.rakbow.website.data.CommonConstant;
import com.rakbow.website.data.ImageInfo;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.system.FileType;
import com.rakbow.website.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-01 21:25
 * @Description:
 */
@Component
public class QiniuImageUtil {

    private final QiniuBaseUtil qiniuBaseUtil;

    public QiniuImageUtil(QiniuBaseUtil qiniuBaseUtil) {
        this.qiniuBaseUtil = qiniuBaseUtil;
    }

    /**
     * 通用新增图片
     *
     * @param entityId                 实体id
     * @param entityName               实体表名
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @param user         上传用户
     * @return finalImageJson 最终保存到数据库的json信息
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public JSONArray commonAddImages(int entityId, String entityName, MultipartFile[] images,
                                     JSONArray originalImagesJson, JSONArray imageInfos, User user) throws IOException {
        //最终保存到数据库的json信息
        JSONArray finalImageJson = new JSONArray();

        //新增图片信息json
        JSONArray addImageJson = new JSONArray();

        //创建存储链接前缀
        String filePath = entityName + "/" + entityId + "/";

        for (int i = 0; i < images.length; i++) {
            //上传图片
            ActionResult ar = qiniuBaseUtil.uploadFileToQiniu(images[i], filePath, FileType.IMAGE);
            if (ar.state) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setUrl(ar.data.toString());
                imageInfo.setNameEn(imageInfos.getJSONObject(i).getString("nameEn"));
                imageInfo.setNameZh(imageInfos.getJSONObject(i).getString("nameZh"));
                imageInfo.setType(imageInfos.getJSONObject(i).getString("type"));
                if(!imageInfos.getJSONObject(i).getString("description").isEmpty()) {
                    imageInfo.setDescription(imageInfos.getJSONObject(i).getString("description"));
                }
                imageInfo.setUploadUser(user.getUsername());
                addImageJson.add(JSON.toJSON(imageInfo));
            }
        }

        //汇总
        finalImageJson.addAll(originalImagesJson);
        finalImageJson.addAll(addImageJson);

        return finalImageJson;
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
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + 200 + "x" + size;
    }

    public static String getThumbBackgroundUrl(String imageUrl, int size) {
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + size + "x" + size
                + "/extent/" + size + "x" + size + "/background/IzJmMzY0Zg==";
    }

    public static String getBookThumbBackgroundUrl(String imageUrl, double width, double height) {

        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + width + "x" + height
                + "/extent/" + width + "x" + height + "/background/IzJmMzY0Zg==";
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

    public static String getThumb70Url(String imagesJson) {
        JSONArray images = JSON.parseArray(imagesJson);
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    return getThumbBackgroundUrl(image.getString("url"), 70);
                }
            }
        }
        return getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 70);
    }

}
