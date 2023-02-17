package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.VisitMapper;
import com.rakbow.website.data.pageInfo;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.common.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-07 8:18
 * @Description:
 */
@Service
public class VisitService {

    @Autowired
    private VisitMapper visitMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Visit getVisit(int entityType, int entityId){
        return visitMapper.getVisit(entityType, entityId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<Visit> getAll(){
        return visitMapper.getAll();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void insertVisit(Visit visit){
        visitMapper.insertVisit(visit);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateVisit(int entityType, int entityId, long visitNum){
        visitMapper.updateVisit(entityType, entityId, visitNum);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteVisit(int entityType, int entityId){
        visitMapper.deleteVisit(entityType, entityId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void increaseVisit(int entityType, int entityId) {
        Visit visit = visitMapper.getVisit(entityType, entityId);
        visit.increaseVisitNum();
        visitMapper.updateVisit(entityType, entityId, visit.getVisitNum());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<Visit> selectVisitOrderByVisitNum(int entityType, int limit) {
        return visitMapper.selectVisitOrderByVisitNum(entityType, limit);
    }

}
