package com.rakbow.website.dao;

import com.rakbow.website.entity.Franchise;
import org.apache.ibatis.annotations.Mapper;

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
    int addFranchise(Franchise franchise);

    //通过id查找系列
    Franchise getFranchise(int id);

    //修改系列信息
    int updateFranchise(int id, Franchise franchise);

    //删除系列
    int deleteFranchise(int id);

    //获取所有系列
    List<Franchise> getAll();

}
