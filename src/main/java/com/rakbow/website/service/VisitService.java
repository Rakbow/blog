package com.rakbow.website.service;

import com.rakbow.website.dao.VisitMapper;
import com.rakbow.website.entity.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Visit getVisit(int entityType, int entityId){
        return visitMapper.getVisit(entityType, entityId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Visit> getAll(){
        return visitMapper.getAll();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void insertVisit(Visit visit){
        visitMapper.insertVisit(visit);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateVisit(int entityType, int entityId, long visitNum){
        visitMapper.updateVisit(entityType, entityId, visitNum);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void deleteVisit(int entityType, int entityId){
        visitMapper.deleteVisit(entityType, entityId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void increaseVisit(int entityType, int entityId) {
        Visit visit = visitMapper.getVisit(entityType, entityId);
        visit.increaseVisitNum();
        visitMapper.updateVisit(entityType, entityId, visit.getVisitNum());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Visit> selectVisitOrderByVisitNum(int entityType, int limit) {
        return visitMapper.selectVisitOrderByVisitNum(entityType, limit);
    }

}
