package com.rakbow.website.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.common.EntityType;
import com.rakbow.website.data.common.ImageType;
import com.rakbow.website.data.product.ProductCategory;
import com.rakbow.website.entity.Product;
import com.rakbow.website.service.ProductService;
import com.rakbow.website.util.common.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-26 14:28
 * @Description:
 */
@Component
public class ProductUtils {

    @Autowired
    private ProductService productService;

    /**
     * 获取作品分类数组
     *
     * @return list 作品分类数组
     * @author rakbow
     */
    public List<JSONObject> getProductCategorySet() {
        List<JSONObject> list = new ArrayList<>();
        for (ProductCategory productCategorySet : ProductCategory.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", productCategorySet.getNameZh());
            jsonObject.put("labelEn", productCategorySet.getNameEn());
            jsonObject.put("value", productCategorySet.getIndex());
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
    public String getProductCoverUrl(Product product) {
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
     * 将数据库实体类product的json字符串转为List<JSONObject>
     *
     * @param productJson product的json字符串
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> getProductList (String productJson) {
        List<JSONObject> products = new ArrayList<>();
        JSONObject.parseObject(productJson).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject jo = new JSONObject();
                    Product product = productService.getProduct(id);
                    jo.put("value", id);
                    jo.put("label", product.getNameZh() + "(" +
                            ProductCategory.getNameZhByIndex(product.getCategory()) + ")");
                    products.add(jo);
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
