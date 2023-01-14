package com.rakbow.website.util.image;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.emun.image.ImageProperty;
import com.rakbow.website.data.emun.image.ImageType;
import com.rakbow.website.data.segmentImagesResult;
import com.rakbow.website.data.CommonConstant;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-31 1:18
 * @Description:
 */
public class CommonImageUtils {

    //region ------检测------

    /**
     * 对新增图片信息合法性进行检测，图片类型
     *
     * @param imageInfos,images 新增图片信息，专辑原图片集合
     * @return boolean
     * @author rakbow
     */
    public static String checkAddImages(JSONArray imageInfos, JSONArray images) {

        int coverCount = 0;

        if (images.size() != 0) {

            for (int i = 0; i < images.size(); i++) {
                if (images.getJSONObject(i).getIntValue("type") == ImageType.COVER.getIndex()) {
                    coverCount++;
                }
            }
            for (int i = 0; i < imageInfos.size(); i++) {
                if (imageInfos.getJSONObject(i).getIntValue("type") == ImageType.COVER.getIndex()) {
                    coverCount++;
                }
            }
        } else {

            for (int i = 0; i < imageInfos.size(); i++) {
                if (imageInfos.getJSONObject(i).getIntValue("type") == ImageType.COVER.getIndex()) {
                    coverCount++;
                }
            }

        }

        //检测图片类型为封面的个数是否大于1
        if (coverCount > 1) {
            return ApiInfo.COVER_COUNT_EXCEPTION;
        }

        return "";
    }

    /**
     * 对更新图片信息合法性进行检测，图片英文名和图片类型
     *
     * @param images 图片json数组
     * @return 报错信息
     * @author rakbow
     */
    public static String checkUpdateImages(JSONArray images) {
        //封面类型的图片个数
        int coverCount = 0;
        for (int i = 0; i < images.size(); i++) {
            if (images.getJSONObject(i).getIntValue("type") == ImageType.COVER.getIndex()) {
                coverCount++;
            }
        }
        if (coverCount > 1) {
            return ApiInfo.COVER_COUNT_EXCEPTION;
        }

        return "";
    }

    //endregion

    /**
     * 使用 通过图片url获取字节大小，长宽
     * @param imgUrl 图片URL
     */
    public static ImageProperty getImageProperty(String imgUrl) throws IOException {
        ImageProperty img = new ImageProperty();

        File file = new File(imgUrl);

        // 图片对象
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));

        img.setSize(file.length());
        img.setWidth(bufferedImage.getWidth());
        img.setHeight(bufferedImage.getHeight());

        return img;
    }

    /**
     * 通过遍历通用图片信息json数组获取封面url
     *
     * @param imageJson 图片信息
     * @return coverUrl
     * @author rakbow
     */
    public static String getCoverUrl (JSONArray imageJson) {
        for (int i = 0; i < imageJson.size(); i++) {
            JSONObject image = imageJson.getJSONObject(i);
            if (image.getIntValue("type") == ImageType.COVER.getIndex()) {
                return image.getString("url");
            }
        }
        return "";
    }

    /**
     * 将图片切分为封面、展示和其他图片
     *
     * @param imagesJson 数据库原图片合集的JSON字符串
     * @param coverSize 封面图尺寸
     * @return segmentImagesResult
     * @author rakbow
     */
    public static segmentImagesResult segmentImages (String imagesJson, int coverSize) {

        segmentImagesResult result = new segmentImagesResult();

        JSONArray images = JSON.parseArray(imagesJson);

        //添加缩略图
        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                image.put("thumbUrl", QiniuImageUtils.getThumbUrl(image.getString("url"), 100));
                image.put("thumbUrl50", QiniuImageUtils.getThumbUrl(image.getString("url"), 50));
            }
        }

        //对封面图片进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, coverSize));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageUtils.getThumbUrl(image.getString("url"), coverSize));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }

        //对展示图片进行封装
        JSONArray displayImages = new JSONArray();
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (image.getIntValue("type") == ImageType.DISPLAY.getIndex()
                        || image.getIntValue("type") == ImageType.COVER.getIndex()) {
                    displayImages.add(image);
                }
            }
        }

        //对其他图片进行封装
        JSONArray otherImages = new JSONArray();
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "2")) {
                    otherImages.add(image);
                }
            }
        }

        result.images = images;
        result.cover = cover;
        result.displayImages = displayImages;
        result.otherImages = otherImages;

        return result;

    }

    /**
     * 获取各尺寸封面图片
     *
     * @param imagesJson 数据库原图片合集的JSON字符串
     * @return JSONObject
     * @author rakbow
     */
    public static JSONObject generateCover(String imagesJson) {

        JSONArray images = JSONArray.parseArray(imagesJson);

        //对图片封面进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageUtils.getThumbBlackBackgroundUrl(CommonConstant.EMPTY_IMAGE_URL, 200));
        cover.put("thumbUrl", QiniuImageUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 50));
        cover.put("thumbUrl70", QiniuImageUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 70));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageUtils.getThumbBlackBackgroundUrl(image.getString("url"), 200));
                    cover.put("thumbUrl", QiniuImageUtils.getThumbUrl(image.getString("url"), 50));
                    cover.put("thumbUrl70", QiniuImageUtils.getThumbUrl(image.getString("url"), 70));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }
        return cover;
    }

    /**
     * 获取封面图片缩略图
     *
     * @param imagesJson 数据库原图片合集的JSON字符串
     * @return JSONObject
     * @author rakbow
     */
    public static JSONObject generateThumbCover(String imagesJson, int size) {
        JSONObject cover = new JSONObject();
        JSONArray images = JSONArray.parseArray(imagesJson);
        cover.put("url", QiniuImageUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, size));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageUtils.getThumbUrl(image.getString("url"), size));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }
        return cover;
    }

}
