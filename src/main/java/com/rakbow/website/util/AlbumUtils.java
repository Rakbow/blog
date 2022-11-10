package com.rakbow.website.util;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.album.AlbumFormat;
import com.rakbow.website.data.album.MediaFormat;
import com.rakbow.website.data.album.PublishFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 23:44
 * @Description:
 */
public class AlbumUtils {

    //获取专辑分类数组
    public static List<JSONObject> getAlbumFormatSet(){
        List<JSONObject> list = new ArrayList<>();
        for(AlbumFormat albumFormat : AlbumFormat.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label",albumFormat.getName());
            jsonObject.put("labelEn",albumFormat.getNameEn());
            jsonObject.put("value",albumFormat.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

    //获取媒体类型数组
    public static List<JSONObject> getMediaFormatSet(){
        List<JSONObject> list = new ArrayList<>();
        for(MediaFormat mediaFormat : MediaFormat.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label",mediaFormat.getName());
            jsonObject.put("labelEn",mediaFormat.getNameEn());
            jsonObject.put("value",mediaFormat.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

    //获取出版形式数组
    public static List<JSONObject> getPublishFormatSet(){
        List<JSONObject> list = new ArrayList<>();
        for(PublishFormat publishFormat : PublishFormat.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label",publishFormat.getName());
            jsonObject.put("labelEn",publishFormat.getNameEn());
            jsonObject.put("value",publishFormat.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

}
