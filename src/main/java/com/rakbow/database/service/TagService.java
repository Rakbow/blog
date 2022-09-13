package com.rakbow.database.service;

import com.rakbow.database.dao.TagMapper;
import com.rakbow.database.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-07 21:35
 * @Description:
 */
@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    //新增专辑标签
    public void insertTag(Tag tag){
        tagMapper.insertTag(tag);
    }

    //获取所有标签
    public List<Tag> getAll(){
        return tagMapper.getAll();
    }

    //获取标签总数
    public int selectTagRows(){
        return tagMapper.selectTagRows();
    }

    //获取标签
    public Tag selectTagById(int id){
        return tagMapper.selectTagById(id);
    }

    //删除标签
    public int deleteTag(int id){
        return tagMapper.deleteTag(id);
    }

    //更新标签
    public int updateTag(int id, Tag tag){
        return tagMapper.updateTag(id, tag);
    }

    //根据商品类型id和商品对应id新增标签(单个标签)
    public int insertTagByItemTypeIdAndItemId(int itemTypeId, int itemId, int tagId){
        return tagMapper.insertTagByItemTypeIdAndItemId(itemTypeId, itemId, tagId);
    };

    //根据商品类型id和商品对应id删除标签(单个标签)
    public int deleteTagByItemTypeIdAndItemId(int itemTypeId, int itemId, int tagId){
        return tagMapper.deleteTagByItemTypeIdAndItemId(itemTypeId, itemId, tagId);
    };

    //根据商品类型id和商品对应id获取该商品的所有标签(单个商品的所有标签)
    public List<Integer> getAllTagsByItemTypeIdAndItemId(int itemTypeId, int itemId){
        return tagMapper.getAllTagsByItemTypeIdAndItemId(itemTypeId, itemId);
    };

    //根据商品类型id和商品对应id删除该商品的所有标签(单个商品的所有标签)
    public int deleteAllTagsByItemTypeIdAndItemId(int itemTypeId, int itemId){
        return tagMapper.deleteAllTagsByItemTypeIdAndItemId(itemTypeId, itemId);
    };

}
