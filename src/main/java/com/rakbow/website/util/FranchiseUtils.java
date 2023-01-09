package com.rakbow.website.util;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.system.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-07 20:38
 * @Description:
 */
@Component
public class FranchiseUtils {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 将数据库实体类franchise的json字符串转为List<JSONObject>
     *
     * @param franchisesJson franchise的json字符串
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> getFranchiseList (String franchisesJson) {

        List<JSONObject> allFranchises = (List<JSONObject>) redisUtil.get("franchises");

        List<JSONObject> franchises = new ArrayList<>();

        JSONObject.parseObject(franchisesJson).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject franchise = DataFinder.findJsonByIdInSet(id, allFranchises);
                    if (franchise != null) {
                        franchises.add(franchise);
                    }
                });

        return franchises;
    }

}
