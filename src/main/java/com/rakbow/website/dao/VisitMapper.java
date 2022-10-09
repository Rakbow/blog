package com.rakbow.website.dao;

import com.rakbow.website.entity.Visit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-07 8:13
 * @Description:
 */
@Mapper
public interface VisitMapper {

    Visit getVisit(int entityType, int entityId);

    //获取所有访问量信息
    List<Visit> getAll();

    //新增新访问量实体
    int insertVisit(Visit visit);

    //访问数量增加
    int updateVisit(int entityType, int entityId, long visitNum);

    //删除访问量实体
    int deleteVisit(int entityType, int entityId);

}
