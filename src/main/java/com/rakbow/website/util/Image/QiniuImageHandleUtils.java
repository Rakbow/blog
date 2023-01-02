package com.rakbow.website.util.Image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-01 21:25
 * @Description:
 */
public class QiniuImageHandleUtils {

    /**
     * 获取等比固定高宽的缩略图URL
     *
     * @param imageUrl,size 原始图url，缩略图宽高
     * @return thumbImageUrl
     */
    public static String getThumbUrl(String imageUrl, int size) {
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + size + "x" + size;
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
