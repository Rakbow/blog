package com.rakbow.website.util;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.merch.MerchCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 17:00
 * @Description:
 */
public class MerchUtils {

    /**
     * 获取周边商品分类数组
     *
     * @return list 周边商品分类数组
     * @author rakbow
     */
    public static List<JSONObject> getMerchCategorySet() {
        List<JSONObject> list = new ArrayList<>();
        for (MerchCategory merchCategory : MerchCategory.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", merchCategory.getNameZh());
            jsonObject.put("labelEn", merchCategory.getNameEn());
            jsonObject.put("value", merchCategory.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

}
