package com.rakbow.website.util;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.entity.Franchise;
import com.rakbow.website.service.FranchiseService;
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
    private FranchiseService franchiseService;

    /**
     * 将数据库实体类franchise的json字符串转为List<JSONObject>
     *
     * @param franchiseJson franchise的json字符串
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> getFranchiseList (String franchiseJson) {
        List<JSONObject> franchiseSet = new ArrayList<>();
        JSONObject.parseObject(franchiseJson).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject jo = new JSONObject();
                    Franchise franchise = franchiseService.getFranchise(id);
                    jo.put("value", id);
                    jo.put("label", franchise.getNameZh());
                    franchiseSet.add(jo);
                });
        return franchiseSet;
    }

}
