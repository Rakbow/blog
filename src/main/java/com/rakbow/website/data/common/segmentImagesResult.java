package com.rakbow.website.data.common;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-31 1:18
 * @Description:
 */
public class segmentImagesResult {

    //添加了缩略图的images
    public JSONArray images;
    //封面图片
    public JSONObject cover;
    //展示图片
    public List<JSONObject> displayImages;
    //其他图片
    public List<JSONObject> otherImages;

    public segmentImagesResult () {
        this.images = new JSONArray();
        this.cover = new JSONObject();
        this.displayImages = new ArrayList<>();
        this.otherImages = new ArrayList<>();
    }

}
