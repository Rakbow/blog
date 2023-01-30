package com.rakbow.website.data.emun.product;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 2:54
 * @Description:
 */
@AllArgsConstructor
public enum ProductCategory {
    UNCLASSIFIED(0,"未分类", "unclassified"),
    GAME(1, "游戏", "Game"),
    ANIMATION(2, "动画", "Animation"),
    LIVE_ACTION_MOVIE(3, "真人电影", "Live Action Movie"),
    BOOK(4, "图书", "Book"),
    MISC(5, "杂项", "Misc");

    @Getter
    private final int index;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String getNameZhByIndex (int index){
        for (ProductCategory productCategory : ProductCategory.values()) {
            if (productCategory.getIndex() == index) {
                return productCategory.getNameZh();
            }
        }
        return "未分类";
    }

    public static String getNameEnByIndex (int index){
        for (ProductCategory productCategory : ProductCategory.values()) {
            if (productCategory.getIndex() == index) {
                return productCategory.getNameEn();
            }
        }
        return "unclassified";
    }

    public static int getIndexByNameZh(String nameZh){
        for (ProductCategory productCategory : ProductCategory.values()) {
            if (productCategory.getNameZh().equals(nameZh)) {
                return productCategory.index;
            }
        }
        return 0;
    }

    public static int getIndexByNameEn(String nameEn){
        for (ProductCategory productCategory : ProductCategory.values()) {
            if (productCategory.getNameEn().equals(nameEn)) {
                return productCategory.index;
            }
        }
        return 0;
    }

    public static JSONObject getProductCategory(int categoryId) {
        JSONObject category = new JSONObject();
        category.put("id", categoryId);
        category.put("nameZh", ProductCategory.getNameZhByIndex(categoryId));
        return category;
    }

    /**
     * 获取作品分类数组
     *
     * @return list 作品分类数组
     * @author rakbow
     */
    public static JSONArray getProductCategorySet() {
        JSONArray list = new JSONArray();
        for (ProductCategory productCategorySet : ProductCategory.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", productCategorySet.getNameZh());
            jsonObject.put("labelEn", productCategorySet.getNameEn());
            jsonObject.put("value", productCategorySet.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

}
