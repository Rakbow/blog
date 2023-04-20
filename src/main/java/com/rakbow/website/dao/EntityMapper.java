package com.rakbow.website.dao;

import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-07 8:13
 * @Description:
 */
@Mapper
public interface EntityMapper {

    //修改状态(单个)
    void updateItemStatus(String entityName, int entityId, int status);

    //修改状态(批量)
    void updateItemsStatus(String entityName, List<Integer> ids, int status);

    //通用更新描述
    void updateItemDescription(String entityName, int entityId, String description, Timestamp editedTime);

    //通用更新特典信息
    void updateItemBonus(String entityName, int entityId, String bonus, Timestamp editedTime);

    //通用更新规格信息
    void updateItemSpec(String entityName, int entityId, String spec, Timestamp editedTime);

    String getItemImages(String entityName, int entityId);

    //更新图片
    void updateItemImages(String entityName, int entityId, String images, Timestamp editedTime);

    //获取数据数
    int getItemAmount(String entityName);

}
