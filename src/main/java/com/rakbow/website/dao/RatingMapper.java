package com.rakbow.website.dao;

import com.rakbow.website.entity.Rating;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-13 21:17
 * @Description:
 */
@Mapper
public interface RatingMapper {

    //根据商品类型获取该商品所有评分信息
    List<RatingMapper> getAllAlbumRatings(int itemId);

    //根据商品类型和对应商品id获取单条商品评分信息
    Rating getRatingByCatalogNo(int itemId, int productionId);

}
