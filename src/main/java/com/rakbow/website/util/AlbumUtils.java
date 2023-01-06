package com.rakbow.website.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.common.ImageType;
import com.rakbow.website.data.album.AlbumFormat;
import com.rakbow.website.data.MediaFormat;
import com.rakbow.website.data.album.PublishFormat;
import com.rakbow.website.entity.Album;
import com.rakbow.website.util.common.CommonConstant;

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

}
