package com.rakbow.website.data.vo.album;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-03 16:13
 * @Description: VO 存储到搜索引擎数据库
 */
@Data
public class AlbumVOGamma {

    //基础信息
    private int id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑名称（日语）
    private String nameEn;
    private String nameZh;
    private String releaseDate;//发行日期
    private boolean hasBonus;//是否包含特典内容

    //关联信息
    private JSONArray franchises;//所属系列
    private JSONArray products;//所属产品id 在mysql中以数组字符串形式存储

    private JSONArray albumFormat;//专辑分类 在mysql中以数组字符串形式存储

    //图片相关
    private String cover;

    private long visitCount;//浏览数
    private long likeCount;//点赞数

}
