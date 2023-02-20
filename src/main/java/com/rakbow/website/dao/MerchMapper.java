package com.rakbow.website.dao;

import com.rakbow.website.entity.Merch;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 14:12
 * @Description:
 */

@Mapper
public interface MerchMapper {

    //通过id查询Merch
    Merch getMerch(int id, boolean status);

    List<Merch> getMerchs(List<Integer> ids);

    List<Merch> getAll();

    //根据过滤条件搜索Merch
    List<Merch> getMerchsByFilter(String name, String barcode, List<Integer> franchises, List<Integer> products,
                                  int category, String region, String notForSale, boolean status,
                                  String sortField, int sortOrder, int first, int row);

    //超详细查询条数
    int getMerchsRowsByFilter(String name, String barcode, List<Integer> franchises, List<Integer> products,
                              int category, String region, String notForSale, boolean status);

    //新增Merch
    int addMerch (Merch merch);

    //更新Merch基础信息
    void updateMerch (int id, Merch merch);

    //删除单个Merch
    void deleteMerch(int id);

    //更新规格信息
    void updateMerchSpec(int id, String spec, Timestamp editedTime);

    int updateStatusById(int id);

    //获取最新添加Merch, limit
    List<Merch> getMerchsOrderByAddedTime(int limit);

}
