package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.image.ImageType;
import com.rakbow.website.data.emun.entity.product.ProductCategory;
import com.rakbow.website.data.emun.system.SystemLanguage;
import com.rakbow.website.entity.Product;
import com.rakbow.website.data.CommonConstant;
import com.rakbow.website.util.common.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-26 14:28
 * @Description:
 */

public class ProductUtil {

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
            if (image.getIntValue("type") == ImageType.COVER.getId()) {
                coverUrl = image.getString("url");
            }
        }
        return coverUrl;
    }

    /**
     * 将数据库实体类product的json字符串转为JSONArray
     *
     * @param json product的json字符串
     * @return JSONArray
     * @author rakbow
     */
    public static List<Attribute> getProducts(String json) {
        RedisUtil redisUtil = SpringUtil.getBean("redisUtil");
        String lang = LocaleContextHolder.getLocale().getLanguage();
        String key;
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            key = RedisCacheConstant.ORIGIN_SET_EN;
        }else {
            key = RedisCacheConstant.ORIGIN_SET_ZH;
        }
        List<Attribute> allProducts = JSON.parseArray(JSON.toJSONString(redisUtil.get(key))).toJavaList(Attribute.class);

        List<Attribute> products = new ArrayList<>();

        List<Integer> ids = CommonUtil.ids2List(json);

        ids.forEach(id -> {
            Attribute product = DataFinder.findAttributeByValue(id, allProducts);
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
            categories.add(ProductCategory.UNCATEGORIZED.getId());
            categories.add(ProductCategory.GAME.getId());
            categories.add(ProductCategory.ANIMATION.getId());
            categories.add(ProductCategory.OVA_OAD.getId());
            categories.add(ProductCategory.TV_SERIES.getId());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getId());
            categories.add(ProductCategory.NOVEL.getId());
            categories.add(ProductCategory.MANGA.getId());
            categories.add(ProductCategory.PUBLICATION.getId());
            categories.add(ProductCategory.MISC.getId());
        }
        if (entityType == EntityType.ALBUM.getId()) {
            categories.add(ProductCategory.GAME.getId());
            categories.add(ProductCategory.ANIMATION.getId());
            categories.add(ProductCategory.OVA_OAD.getId());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getId());
            categories.add(ProductCategory.TV_SERIES.getId());
        }
        if (entityType == EntityType.BOOK.getId()) {
            categories.add(ProductCategory.NOVEL.getId());
            categories.add(ProductCategory.MANGA.getId());
            categories.add(ProductCategory.PUBLICATION.getId());
        }
        if (entityType == EntityType.DISC.getId()) {
            categories.add(ProductCategory.ANIMATION.getId());
            categories.add(ProductCategory.OVA_OAD.getId());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getId());
            categories.add(ProductCategory.TV_SERIES.getId());
        }
        if (entityType == EntityType.GAME.getId()) {
            categories.add(ProductCategory.GAME.getId());
        }
        if (entityType == EntityType.MERCH.getId()) {
            categories.add(ProductCategory.GAME.getId());
            categories.add(ProductCategory.ANIMATION.getId());
            categories.add(ProductCategory.OVA_OAD.getId());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getId());
            categories.add(ProductCategory.TV_SERIES.getId());
            categories.add(ProductCategory.NOVEL.getId());
            categories.add(ProductCategory.MANGA.getId());
            categories.add(ProductCategory.PUBLICATION.getId());
        }
        return categories;
    }

}
