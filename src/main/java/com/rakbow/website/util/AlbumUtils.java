package com.rakbow.website.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ImageType;
import com.rakbow.website.data.album.AlbumFormat;
import com.rakbow.website.data.album.MediaFormat;
import com.rakbow.website.data.album.PublishFormat;
import com.rakbow.website.entity.Album;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonConstant;
import com.rakbow.website.util.common.CommonUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 23:44
 * @Description:
 */
public class AlbumUtils {

    /**
     * 获取专辑分类数组
     *
     * @return list 专辑分类数组
     * @author rakbow
     */
    public static List<JSONObject> getAlbumFormatSet() {
        List<JSONObject> list = new ArrayList<>();
        for (AlbumFormat albumFormat : AlbumFormat.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", albumFormat.getName());
            jsonObject.put("labelEn", albumFormat.getNameEn());
            jsonObject.put("value", albumFormat.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 获取媒体类型数组
     *
     * @return list 媒体类型数组
     * @author rakbow
     */
    public static List<JSONObject> getMediaFormatSet() {
        List<JSONObject> list = new ArrayList<>();
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", mediaFormat.getName());
            jsonObject.put("labelEn", mediaFormat.getNameEn());
            jsonObject.put("value", mediaFormat.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 获取出版形式数组
     *
     * @return list 出版形式数组
     * @author rakbow
     */
    public static List<JSONObject> getPublishFormatSet() {
        List<JSONObject> list = new ArrayList<>();
        for (PublishFormat publishFormat : PublishFormat.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", publishFormat.getName());
            jsonObject.put("labelEn", publishFormat.getNameEn());
            jsonObject.put("value", publishFormat.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 获取专辑封面图片
     *
     * @param album 专辑
     * @return coverUrl 专辑封面图片url
     * @author rakbow
     */
    public static String getAlbumCoverUrl(Album album) {
        //先赋值为404图片
        String coverUrl = CommonConstant.EMPTY_IMAGE_URL;

        JSONArray images = JSON.parseArray(album.getImages());

        for (int i = 0; i < images.size(); i++) {
            JSONObject image = images.getJSONObject(i);
            if (image.getIntValue("type") == ImageType.COVER.getIndex()) {
                coverUrl = image.getString("url");
            }
        }
        return coverUrl;
    }

    /**
     * 对更新图片信息合法性进行检测，图片英文名和图片类型
     *
     * @param
     * @return
     * @author rakbow
     */
    public static String checkAlbumUpdateImages(JSONArray images) {
        //封面类型的图片个数
        int coverCount = 0;
        for (int i = 0; i < images.size(); i++) {
            if (images.getJSONObject(i).getIntValue("type") == ImageType.COVER.getIndex()) {
                coverCount++;
            }
        }
        if (coverCount > 1) {
            return ApiInfo.ALBUM_COVER_COUNT_EXCEPTION;
        }

        //检测是否存在重复英文名
        List<String> originImageUrlNameEns = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String imageUrl = images.getJSONObject(i).getString("url");
            originImageUrlNameEns.add(imageUrl.substring(
                    imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")));
        }
        if (originImageUrlNameEns.stream().distinct().count() != images.size()) {
            return ApiInfo.ALBUM_IMAGE_NAME_EN_REPEAT_EXCEPTION;
        }

        return "";
    }

    /**
     * 对新增图片信息合法性进行检测，图片英文名和图片类型
     *
     * @param imageInfos,images 新增图片信息，专辑原图片集合
     * @return boolean
     * @author rakbow
     */
    public static String checkAlbumAddImages(JSONArray imageInfos, JSONArray images) {

        List<String> imageUrlNameEns = new ArrayList<>();
        int coverCount = 0;

        if (images.size() != 0) {

            for (int i = 0; i < images.size(); i++) {
                String imageUrl = images.getJSONObject(i).getString("url");
                imageUrlNameEns.add(imageUrl.substring(
                        imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")));
            }
            for (int i = 0; i < imageInfos.size(); i++) {
                String nameEn = imageInfos.getJSONObject(i).getString("nameEn");
                imageUrlNameEns.add(nameEn.replace(" ", ""));
            }

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
                String nameEn = imageInfos.getJSONObject(i).getString("nameEn");
                imageUrlNameEns.add(nameEn.replace(" ", ""));
            }

            for (int i = 0; i < imageInfos.size(); i++) {
                if (imageInfos.getJSONObject(i).getIntValue("type") == ImageType.COVER.getIndex()) {
                    coverCount++;
                }
            }

        }

        //检测是否存在重复英文名
        int originNameUrlCount = imageUrlNameEns.size();
        if (imageUrlNameEns.stream().distinct().count() != originNameUrlCount) {
            return ApiInfo.ALBUM_IMAGE_NAME_EN_REPEAT_EXCEPTION;
        }

        //检测图片类型为封面的个数是否大于1
        if (coverCount > 1) {
            return ApiInfo.ALBUM_COVER_COUNT_EXCEPTION;
        }

        return "";
    }

    /**
     * 根据图片url获取图片文件全名
     *
     * @author rakbow
     * @param url 图片url
     * @return fileName
     * */
    public static String getImageFileNameByUrl (String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}
