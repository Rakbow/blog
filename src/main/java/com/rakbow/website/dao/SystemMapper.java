package com.rakbow.website.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-12 21:36
 * @Description:
 */
@Mapper
public interface SystemMapper {

    void updateItemStatus(String entityName, int entityId, int status);

}
