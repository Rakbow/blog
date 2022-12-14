package com.rakbow.website.dao;

import com.rakbow.website.entity.Disc;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-27 19:43
 * @Description:
 */
@Mapper
public interface DiscMapper {

    //通过id查询disc
    Disc getDiscById(int id);

    //获取所有Disc
    List<Disc> getAllDisc();

    //根据过滤条件搜索disc
    List<Disc> getDiscsByFilterList(String catalogNo, String name, int seriesId, int productId, int media_format,
                                String is_limited, String has_bonus, String sortField, int sortOrder,
                                int first, int row);

    //超详细查询条数
    int getDiscsRowsByFilterList(String catalogNo, String name, int seriesId, int productId, int media_format,
                                 String is_limited, String has_bonus);

    //新增disc
    int addDisc (Disc disc);

    //更新disc基础信息
    int updateDisc (int id, Disc disc);

    //删除单个disc
    int deleteDiscById(int id);

    //更新图片
    int updateDiscImages(int id, String images, Timestamp editedTime);

    //更新描述信息
    int updateDiscDescription(int id, String description, Timestamp editedTime);

    //更新特典信息
    int updateDiscBonus(int id, String bonus, Timestamp editedTime);

    int updateStatusById(int id);

}
