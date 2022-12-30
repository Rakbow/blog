package com.rakbow.website.util.Image;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.common.ImageType;
import com.rakbow.website.data.clazz.ImageProperty;
import com.rakbow.website.util.common.ApiInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-01 23:02
 * @Description:
 */
public class CommonImageHandleUtils {

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
     * @param
     * @return
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

}
