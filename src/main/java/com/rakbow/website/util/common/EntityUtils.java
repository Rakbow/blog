package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ItemDetailInfo;
import com.rakbow.website.util.entity.FranchiseUtils;
import com.rakbow.website.util.entity.ProductUtils;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-06 16:38
 * @Description:
 */
public class EntityUtils {

    public static ItemDetailInfo getItemDetailInfo(Object o, int entityType) {
        ItemDetailInfo detailInfo = new ItemDetailInfo();
        if(o == null) {
            return null;
        }
        JSONObject json = JSON.parseObject(JSON.toJSONString(o));
        detailInfo.setId(json.getInteger("id"));

        detailInfo.setDescription(json.getString("description"));
        detailInfo.setProducts(ProductUtils.getProductList(json.getString("products")));
        detailInfo.setFranchises(FranchiseUtils.getFranchiseList(json.getString("franchises")));

        detailInfo.setEntityType(entityType);
        detailInfo.setStatus(json.getIntValue("status") == 1);

        return detailInfo;
    }

    public static ItemDetailInfo getMetaDetailInfo(Object o, int entityType) {
        ItemDetailInfo detailInfo = new ItemDetailInfo();
        if(o == null) {
            return null;
        }
        JSONObject json = JSON.parseObject(JSON.toJSONString(o));
        detailInfo.setId(json.getInteger("id"));

        detailInfo.setDescription(json.getString("description"));

        detailInfo.setEntityType(entityType);
        detailInfo.setStatus(json.getIntValue("status") == 1);

        return detailInfo;
    }

}
