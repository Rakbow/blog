package com.rakbow.website.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.FranchiseMapper;
import com.rakbow.website.entity.Franchise;
import com.rakbow.website.util.system.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 1:17
 * @Description:
 */
@Service
public class FranchiseService {

    @Autowired
    private FranchiseMapper franchiseMapper;
    @Autowired
    private RedisUtil redisUtil;

    //新增系列
    public int addFranchise(Franchise franchise){
        return franchiseMapper.addFranchise(franchise);
    };

    //通过id查找系列
    public Franchise getFranchise(int id){
        return franchiseMapper.getFranchise(id);
    };

    //修改系列信息
    public int updateFranchise(int id, Franchise franchise){
        return franchiseMapper.updateFranchise(id, franchise);
    };

    //删除系列
    public int deleteFranchise(int id){
        return franchiseMapper.deleteFranchise(id);
    };

    /**
     * 刷新Redis缓存中的franchises数据
     *
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public void refreshRedisFranchises () {

        JSONArray franchiseSet = new JSONArray();
        List<Franchise> franchises = franchiseMapper.getAll();
        for (Franchise franchise : franchises) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", franchise.getId());
            jsonObject.put("label", franchise.getNameZh());
            franchiseSet.add(jsonObject);
        }

        redisUtil.set("franchiseSet", franchiseSet);
        //缓存时间1个月
        redisUtil.expire("franchiseSet", 2592000);

    }

}
