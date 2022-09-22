package com.rakbow.website.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-13 23:11
 * @Description:
 */
@Mapper
public interface ItemInTagMapper {

    //根据商品类型id和商品对应id新增标签
    int insertTagByItemTypeIdAndItemId(int itemTypeId, int itemId, int tagId);

    //根据商品类型id和商品对应id删除标签
    int deleteTagByItemTypeIdAndItemId(int itemTypeId, int itemId, int tagId);

    //根据商品类型id和商品对应id获取该商品的所有标签
    List<String> getAllTagsByItemTypeIdAndItemId(int itemTypeId, int itemId);

    //根据商品类型id和商品对应id删除该商品的所有标签
    List<String> deleteAllTagsByItemTypeIdAndItemId(int itemTypeId, int itemId);
}
