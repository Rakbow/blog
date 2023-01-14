package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.image.ImageType;
import com.rakbow.website.data.emun.product.ProductCategory;
import com.rakbow.website.entity.Product;
import com.rakbow.website.data.CommonConstant;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.common.SpringUtils;
import com.rakbow.website.util.common.RedisUtil;

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

    /**
     * 将数据库实体类product的json字符串转为JSONArray
     *
     * @param productJson product的json字符串
     * @return JSONArray
     * @author rakbow
     */
    public static JSONArray getProductList (String productJson) {

        RedisUtil redisUtil = SpringUtils.getBean("redisUtil");

        List<JSONObject> allProducts = (List<JSONObject>) redisUtil.get("productSet");

        JSONArray products = new JSONArray();

        CommonUtils.ids2List(productJson)
                .forEach(id -> {
                    JSONObject product = DataFinder.findJsonByIdInSet(id, allProducts);
                    if (product != null) {
                        products.add(product);
                    }
                });

        return products;
    }

    /**
     * 通过实体类型id获取作品选项包含的作品类型
     *
     * @param entityType
     * @return JSONObject
     * @author rakbow
     */
    public static List<Integer> getCategoriesByEntityType (int entityType) {

        List<Integer> categories = new ArrayList<>();
        if (entityType == EntityType.PRODUCT.getId()) {
            categories.add(ProductCategory.UNCLASSIFIED.getIndex());
            categories.add(ProductCategory.GAME.getIndex());
            categories.add(ProductCategory.ANIMATION.getIndex());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getIndex());
            categories.add(ProductCategory.BOOK.getIndex());
            categories.add(ProductCategory.MISC.getIndex());
        }
        if (entityType == EntityType.ALBUM.getId()) {
            categories.add(ProductCategory.GAME.getIndex());
            categories.add(ProductCategory.ANIMATION.getIndex());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getIndex());
        }
        if (entityType == EntityType.BOOK.getId()) {
            categories.add(ProductCategory.BOOK.getIndex());
        }
        if (entityType == EntityType.DISC.getId()) {
            categories.add(ProductCategory.ANIMATION.getIndex());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getIndex());
        }
        if (entityType == EntityType.GAME.getId()) {
            categories.add(ProductCategory.GAME.getIndex());
        }
        if (entityType == EntityType.MERCH.getId()) {
            categories.add(ProductCategory.GAME.getIndex());
            categories.add(ProductCategory.ANIMATION.getIndex());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getIndex());
            categories.add(ProductCategory.BOOK.getIndex());
        }
        return categories;
    }

}
