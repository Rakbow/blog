package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.entity.franchise.MetaInfo;
import com.rakbow.website.entity.Franchise;
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

    /**
     * 判断该franchise是否为meta-franchise
     *
     * @param franchise franchise
     * @return boolean
     * @author rakbow
     */
    public static boolean isMetaFranchise(Franchise franchise) {
        MetaInfo metaInfo = JSON.to(MetaInfo.class, franchise.getMetaInfo());
        return metaInfo.isMeta == 1;
    }

    /**
     * 获取该meta-franchise的子系列
     *
     * @param originFranchise franchise
     * @return JSONArray
     * @author rakbow
     */
    public static JSONArray getChildFranchises(Franchise originFranchise) {
        MetaInfo metaInfo = JSON.to(MetaInfo.class, originFranchise.getMetaInfo());

        RedisUtil redisUtil = SpringUtils.getBean("redisUtil");

        List<JSONObject> allFranchises = (List<JSONObject>) redisUtil.get("franchiseSet");

        JSONArray childFranchises = new JSONArray();

        metaInfo.childFranchises.forEach(id -> {
                    JSONObject franchise = DataFinder.findJsonByIdInSet(id, allFranchises);
                    if (franchise != null) {
                        childFranchises.add(franchise);
                    }
                });

        return childFranchises;
    }

    /**
     * 获取该meta-franchise的子系列Ids
     *
     * @param originFranchise franchise
     * @return int[]
     * @author rakbow
     */
    public static List<Integer> getChildFranchiseIds(Franchise originFranchise) {
        MetaInfo metaInfo = JSON.to(MetaInfo.class, originFranchise.getMetaInfo());

        RedisUtil redisUtil = SpringUtils.getBean("redisUtil");

        List<JSONObject> allFranchises = (List<JSONObject>) redisUtil.get("franchiseSet");

        return metaInfo.childFranchises;
    }
}
