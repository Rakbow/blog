package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.common.SpringUtils;
import com.rakbow.website.util.common.RedisUtil;

import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-07 20:38
 * @Description:
 */
public class FranchiseUtils {

    /**
     * 将数据库实体类franchise的json字符串转为JSONArray
     *
     * @param franchisesJson franchise的json字符串
     * @return JSONArray
     * @author rakbow
     */
    public static JSONArray getFranchiseList (String franchisesJson) {

        RedisUtil redisUtil = SpringUtils.getBean("redisUtil");

        List<JSONObject> allFranchises = (List<JSONObject>) redisUtil.get("franchiseSet");

        JSONArray franchises = new JSONArray();

        CommonUtils.ids2List(franchisesJson)
                .forEach(id -> {
                    JSONObject franchise = DataFinder.findJsonByIdInSet(id, allFranchises);
                    if (franchise != null) {
                        franchises.add(franchise);
                    }
                });

        return franchises;
    }

    /**
     * 将数据库实体类franchise的int转为JSONObject
     *
     * @param franchiseId franchise的id
     * @return JSONObject
     * @author rakbow
     */
    public static JSONObject getFranchise(int franchiseId) {

        RedisUtil redisUtil = SpringUtils.getBean("redisUtil");
        List<JSONObject> allFranchises = (List<JSONObject>) redisUtil.get("franchiseSet");

        return DataFinder.findJsonByIdInSet(franchiseId, allFranchises);
    }

}
