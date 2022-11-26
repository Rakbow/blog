package com.rakbow.website.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ImageType;
import com.rakbow.website.data.ProductClass;
import com.rakbow.website.entity.Product;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-26 14:28
 * @Description:
 */
public class ProductUtils {

    /**
     * 获取作品分类数组
     *
     * @return list 作品分类数组
     * @author rakbow
     */
    public static List<JSONObject> getProductClassSet() {
        List<JSONObject> list = new ArrayList<>();
        for (ProductClass productClassSet : ProductClass.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", productClassSet.getNameZh());
            jsonObject.put("labelEn", productClassSet.getNameEn());
            jsonObject.put("value", productClassSet.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 获取封面图片
     *
     * @param product 作品
     * @return coverUrl 作品封面图片url
     * @author rakbow
     */
    public static String getProductCoverUrl(Product product) {
        //先赋值为404图片
        String coverUrl = CommonConstant.EMPTY_IMAGE_URL;

        JSONArray images = JSON.parseArray(product.getImages());

        for (int i = 0; i < images.size(); i++) {
            JSONObject image = images.getJSONObject(i);
            if (image.getIntValue("type") == ImageType.COVER.getIndex()) {
                coverUrl = image.getString("url");
            }
        }
        return coverUrl;
    }

}
