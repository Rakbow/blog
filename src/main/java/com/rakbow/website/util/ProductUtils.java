package com.rakbow.website.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.common.ImageType;
import com.rakbow.website.data.product.ProductClass;
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
    public List<JSONObject> getProductClassSet() {
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
                    jo.put("id", id);
                    jo.put("name", productService.getProductById(id).getNameZh() + "(" +
                            ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")");
                    products.add(jo);
                });
        return products;
    }

}
