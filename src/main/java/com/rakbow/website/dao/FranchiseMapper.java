package com.rakbow.website.dao;

import com.rakbow.website.entity.Franchise;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 1:06
 * @Description: 系列CRUD
 */
@Mapper
public interface FranchiseMapper {

    //新增系列
    void addFranchise(Franchise franchise);

    //通过id查找系列
    Franchise getFranchise(int id);

    //根据过滤条件搜索Book
    List<Franchise> getFranchisesByFilter(String name, String nameZh, String sortField, int sortOrder, int first, int row);

    //超详细查询条数
    int getFranchisesRowsByFilter(String name, String nameZh);

    //修改系列信息
    void updateFranchise(int id, Franchise franchise);

    //删除系列
    int deleteFranchise(int id);

    //获取所有系列
    List<Franchise> getAll();

    //更新描述信息
    void updateFranchiseDescription(int id, String description, Timestamp editedTime);

    //更新图片
    void updateFranchiseImages(int id, String images, Timestamp editedTime);

}
