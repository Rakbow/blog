package com.rakbow.database.dao;

import com.rakbow.database.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-08-07 21:37
 * @Description:
 */
@Mapper
public interface TagMapper {

    //根据id查找标签
    Tag selectTagById(int id);

    //获取所有标签
    List<Tag> getAll();

    //新增标签
    int insertTag(Tag tag);

    //更新标签
    int updateTag(int id, Tag tag);

    //删除标签
    int deleteTag(int id);

    //获取总标签数
    int selectTagRows();

    //根据商品类型id和商品对应id新增标签(单个标签)
    int insertTagByItemTypeIdAndItemId(int itemTypeId, int itemId, int tagId);

    //根据商品类型id和商品对应id删除标签(单个标签)
    int deleteTagByItemTypeIdAndItemId(int itemTypeId, int itemId, int tagId);

    //根据商品类型id和商品对应id获取该商品的所有标签(单个商品的所有标签)
    List<Integer> getAllTagsByItemTypeIdAndItemId(int itemTypeId, int itemId);

    //根据商品类型id和商品对应id删除该商品的所有标签(单个商品的所有标签)
    int deleteAllTagsByItemTypeIdAndItemId(int itemTypeId, int itemId);

}
